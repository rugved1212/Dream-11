package com.example.dream11.TeamsAndContest.CreateTeam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.dream11.PropertyClasses.Player;
import com.example.dream11.R;
import com.example.dream11.TeamsAndContest.BottomSheet.CricketBottomSheet;
import com.example.dream11.TeamsAndContest.BottomSheet.FootballBottomSheet;
import com.example.dream11.TeamsAndContest.BottomSheet.KabaddiBottomSheet;
import com.example.dream11.TeamsAndContest.CreateTeam.Cricket.CreateCricketTeam;
import com.example.dream11.TeamsAndContest.CreateTeam.Football.CreateFootballTeam;
import com.example.dream11.TeamsAndContest.CreateTeam.Kabaddi.CreateKabaddiTeam;
import com.example.dream11.TeamsAndContest.TeamAndContest;
import com.github.nikartm.button.FitButton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Select_C_VC extends AppCompatActivity {

    String team1, team2, date, status, series, game, jsonSelectedPlayers;
    Toolbar toolbar;
    private CountDownTimer countDown;
    private Date targetDate;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_cvc);

        mAuth = FirebaseAuth.getInstance();

        team1 = getIntent().getStringExtra("team1");
        team2 = getIntent().getStringExtra("team2");
        date = getIntent().getStringExtra("date");
        status = getIntent().getStringExtra("status");
        series = getIntent().getStringExtra("series");
        game = getIntent().getStringExtra("game");
        jsonSelectedPlayers = getIntent().getStringExtra("selectedPlayers");

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle(teamName(team1) + " VS " + teamName(team2));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        try {
            targetDate = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        startCountdownTimer();

        List<Player> selectedPlayers = new Gson().fromJson(jsonSelectedPlayers, new TypeToken<ArrayList<Player>>() {
        }.getType());

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        SelectedPlayerAdapter adapter = new SelectedPlayerAdapter(selectedPlayers);
        recyclerView.setAdapter(adapter);

        FitButton preview = findViewById(R.id.preview);
        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (game.equals("cricket")) {
                    List<Player> wk = new ArrayList<>();
                    List<Player> bat = new ArrayList<>();
                    List<Player> ar = new ArrayList<>();
                    List<Player> bowl = new ArrayList<>();
                    for (Player player : selectedPlayers) {
                        if (player.getPlayer_role().equals("WICKETKEEPER-BATSMAN")) {
                            wk.add(player);
                        } else if (player.getPlayer_role().equals("BATSMAN")) {
                            bat.add(player);
                        } else if (player.getPlayer_role().equals("ALL-ROUNDER")) {
                            ar.add(player);
                        } else if (player.getPlayer_role().equals("BOWLER")) {
                            bowl.add(player);
                        }
                    }
                    CricketBottomSheet cricketBottomSheet = new CricketBottomSheet(wk, bat, ar, bowl, "", "", "11/11", teamName(team1), teamName(team2));
                    cricketBottomSheet.show(getSupportFragmentManager(), cricketBottomSheet.getTag());
                } else if (game.equals("football")) {
                    List<Player> gk = new ArrayList<>();
                    List<Player> def = new ArrayList<>();
                    List<Player> mid = new ArrayList<>();
                    List<Player> forw = new ArrayList<>();
                    for (Player player : selectedPlayers) {
                        if (player.getPlayer_position().equals("GOAL-KEEPER")) {
                            gk.add(player);
                        } else if (player.getPlayer_position().equals("DEFENDER")) {
                            def.add(player);
                        } else if (player.getPlayer_position().equals("MIDFIELDER")) {
                            mid.add(player);
                        } else if (player.getPlayer_position().equals("FORWARD")) {
                            forw.add(player);
                        }
                    }
                    FootballBottomSheet footballBottomSheet = new FootballBottomSheet(gk, def, mid, forw, "", "", "11/11", teamName(team1), teamName(team2));
                    footballBottomSheet.show(getSupportFragmentManager(), footballBottomSheet.getTag());
                } else if (game.equals("kabaddi")) {
                    List<Player> def = new ArrayList<>();
                    List<Player> ar = new ArrayList<>();
                    List<Player> raid = new ArrayList<>();
                    for (Player player : selectedPlayers) {
                        if (player.getPlayer_position().equals("DEFENDER")) {
                            def.add(player);
                        } else if (player.getPlayer_position().equals("ALL-ROUNDER")) {
                            ar.add(player);
                        } else if (player.getPlayer_position().equals("RAIDER")) {
                            raid.add(player);
                        }
                    }
                    KabaddiBottomSheet kabaddiBottomSheet = new KabaddiBottomSheet(def, ar, raid, "", "", "7/7", teamName(team1), teamName(team2));
                    kabaddiBottomSheet.show(getSupportFragmentManager(), kabaddiBottomSheet.getTag());
                }

            }
        });

        FitButton save = findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int captainPosition = adapter.getCaptainPosition();
                int viceCaptainPosition = adapter.getViceCaptainPosition();

                if (captainPosition != RecyclerView.NO_POSITION && viceCaptainPosition != RecyclerView.NO_POSITION) {
                    DatabaseReference teamsRef = FirebaseDatabase.getInstance().getReference("users").child(mAuth.getUid()).child("teams/" + game + "/" + team1 + " VS " + team2 + " , " + series);
                    teamsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            long teamCount = dataSnapshot.getChildrenCount();
                            String teamId = "Team" + (teamCount + 1);
                            DatabaseReference userRef = teamsRef.child(teamId);
                            userRef.child("players").setValue(selectedPlayers);
                            userRef.child("captain_position").setValue(captainPosition);
                            userRef.child("vicecaptain_position").setValue(viceCaptainPosition);

                            if (game.equals("cricket")) {
                                Intent intent = new Intent(Select_C_VC.this, TeamAndContest.class);
                                intent.putExtra("team1", team1);
                                intent.putExtra("team2", team2);
                                intent.putExtra("series", series);
                                intent.putExtra("date", date);
                                intent.putExtra("status", status);
                                intent.putExtra("game", game);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            } else if (game.equals("football")) {
                                Intent intent = new Intent(Select_C_VC.this, TeamAndContest.class);
                                intent.putExtra("team1", team1);
                                intent.putExtra("team2", team2);
                                intent.putExtra("series", series);
                                intent.putExtra("date", date);
                                intent.putExtra("status", status);
                                intent.putExtra("game", game);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            } else if (game.equals("kabaddi")) {
                                Intent intent = new Intent(Select_C_VC.this, TeamAndContest.class);
                                intent.putExtra("team1", team1);
                                intent.putExtra("team2", team2);
                                intent.putExtra("series", series);
                                intent.putExtra("date", date);
                                intent.putExtra("status", status);
                                intent.putExtra("game", game);
                                startActivity(intent);
                                finish();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle onCancelled
                        }
                    });
                }
            }
        });

    }

    private String teamName(String name) {
        String[] words = name.split("\\s+");
        StringBuilder builder = new StringBuilder();
        if (words.length == 1) {
            for (String word : words) {
                int wordLength = Math.min(3, word.length());
                builder.append(word.substring(0, wordLength));
            }
        } else {
            for (String word : words) {
                builder.append(word.charAt(0));
            }
        }
        String result = builder.toString();
        return result;
    }

    private void startCountdownTimer() {
        countDown = new CountDownTimer(targetDate.getTime() - System.currentTimeMillis(), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long days = millisUntilFinished / (1000 * 60 * 60 * 24);
                long hours = (millisUntilFinished % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
                long minutes = (millisUntilFinished % (1000 * 60 * 60)) / (1000 * 60);
                long seconds = (millisUntilFinished % (1000 * 60)) / 1000;

                String countdown;
                if (days > 0) {
                    countdown = days + " day" + (days == 1 ? "" : "s") + " left";
                } else {
                    countdown = String.format(Locale.getDefault(), "%02d hrs %02d mins %02d secs left", hours, minutes, seconds);
                }
                toolbar.setSubtitle(countdown);
            }

            @Override
            public void onFinish() {
                toolbar.setSubtitle("Countdown Finished!");
            }
        }.start();
    }
}

class SelectedPlayerAdapter extends RecyclerView.Adapter<SelectedPlayerAdapter.ViewHolder> {

    private List<Player> players;
    private int captainPosition = RecyclerView.NO_POSITION;
    private int viceCaptainPosition = RecyclerView.NO_POSITION;

    public SelectedPlayerAdapter(List<Player> players) {
        this.players = players;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.c_vc_recyclerview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Player player = players.get(position);
        holder.playerNameTextView.setText(player.getPlayer_name());
        if (player.getPlayer_role() != null) {
            holder.playerRoleTextView.setText(player.getPlayer_role());
        } else {
            holder.playerRoleTextView.setText(player.getPlayer_position());
        }

        Glide.with(holder.playerImg.getContext())
                .load(player.getImageUrl())
                .placeholder(R.drawable.default_profile)
                .error(R.drawable.default_profile)
                .into(holder.playerImg);

        if (position == captainPosition) {
            holder.cimg.setBackgroundResource(R.drawable.circle_fill);
        } else {
            holder.cimg.setBackgroundResource(R.drawable.circle);
        }

        if (position == viceCaptainPosition) {
            holder.vcimg.setBackgroundResource(R.drawable.circle_fill);
        } else {
            holder.vcimg.setBackgroundResource(R.drawable.circle);
        }

        holder.ctxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int clickedPosition = holder.getAdapterPosition();
                if (viceCaptainPosition != clickedPosition) {
                    captainPosition = clickedPosition;
                    notifyDataSetChanged();
                }
            }
        });

        holder.vctxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int clickedPosition = holder.getAdapterPosition();
                if (captainPosition != clickedPosition) {
                    viceCaptainPosition = clickedPosition;
                    notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    public int getCaptainPosition() {
        return captainPosition;
    }

    public int getViceCaptainPosition() {
        return viceCaptainPosition;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView playerNameTextView;
        TextView playerRoleTextView;
        ImageView playerImg;

        TextView ctxt, vctxt;
        ImageView cimg, vcimg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            playerNameTextView = itemView.findViewById(R.id.name);
            playerRoleTextView = itemView.findViewById(R.id.role);
            playerImg = itemView.findViewById(R.id.img);

            ctxt = itemView.findViewById(R.id.ctxt);
            vctxt = itemView.findViewById(R.id.vctxt);
            cimg = itemView.findViewById(R.id.c);
            vcimg = itemView.findViewById(R.id.vc);
        }
    }
}
