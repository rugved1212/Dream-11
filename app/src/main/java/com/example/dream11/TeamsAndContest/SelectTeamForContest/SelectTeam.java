package com.example.dream11.TeamsAndContest.SelectTeamForContest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.dream11.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class SelectTeam extends AppCompatActivity implements SelectTeamAdapter.OnItemClickListener{

    private String team1, team2, date, status, series, game, contest, entry;
    List<String> id;
    private Date targetDate;
    private CountDownTimer countDown;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_team);

        team1 = getIntent().getStringExtra("team1");
        team2 = getIntent().getStringExtra("team2");
        date = getIntent().getStringExtra("date");
        status = getIntent().getStringExtra("status");
        series = getIntent().getStringExtra("series");
        game = getIntent().getStringExtra("game");
        entry = getIntent().getStringExtra("entry");
        contest = getIntent().getStringExtra("contestId");
        id = getIntent().getStringArrayListExtra("contest_list");

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (team1!=null && team2!=null){
            toolbar.setTitle(teamName(team1) + " VS " + teamName(team2));
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<String> teamNames = new ArrayList<>();
        List<String> team1list = new ArrayList<>();
        List<String> team2list = new ArrayList<>();
        List<String> team1player_selected = new ArrayList<>();
        List<String> team2player_selected = new ArrayList<>();
        List<String> captain_name = new ArrayList<>();
        List<String> vicecaptain_name = new ArrayList<>();
        List<String> captain_img = new ArrayList<>();
        List<String> vicecaptain_img = new ArrayList<>();
        SelectTeamAdapter adapter = new SelectTeamAdapter(teamNames, team1list, team2list, team1player_selected, team2player_selected, captain_name, captain_img, vicecaptain_name, vicecaptain_img, this);
        recyclerView.setAdapter(adapter);

        retreiveTeams(teamNames, adapter, team1list, team2list, team1player_selected, team2player_selected, captain_name, captain_img, vicecaptain_name, vicecaptain_img);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        try {
            targetDate = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        startCountdownTimer();
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

    private void retreiveTeams(List<String> teamNames, SelectTeamAdapter adapter, List<String> team1list, List<String> team2list, List<String> team1player_selected, List<String> team2player_selected, List<String> captain_name, List<String> captain_img, List<String> vicecaptain_name, List<String> vicecaptain_img) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference pathref = FirebaseDatabase.getInstance().getReference("users/"+ mAuth.getCurrentUser().getUid()+"/teams/"+game).child(team1 + " VS " + team2 + " , " + series);
        pathref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int team1PlayerCount = 0;
                int team2PlayerCount = 0;
                for (DataSnapshot teamSnapshot : snapshot.getChildren()) {
                    String teamName = teamSnapshot.getKey();

                    int captainPosition = teamSnapshot.child("captain_position").getValue(Integer.class);
                    int viceCaptainPosition = teamSnapshot.child("vicecaptain_position").getValue(Integer.class);

                    String captainName = teamSnapshot.child("players").child(String.valueOf(captainPosition)).child("player_name").getValue(String.class);
                    String captainImg = teamSnapshot.child("players").child(String.valueOf(captainPosition)).child("imageUrl").getValue(String.class);
                    String viceCaptainName = teamSnapshot.child("players").child(String.valueOf(viceCaptainPosition)).child("player_name").getValue(String.class);
                    String viceCaptainImg = teamSnapshot.child("players").child(String.valueOf(viceCaptainPosition)).child("imageUrl").getValue(String.class);

                    teamNames.add(teamName);
                    team1list.add(team1);
                    team2list.add(team2);
                    captain_name.add(captainName);
                    captain_img.add(captainImg);
                    vicecaptain_name.add(viceCaptainName);
                    vicecaptain_img.add(viceCaptainImg);

                    for (DataSnapshot playerSnapshot : teamSnapshot.child("players").getChildren()) {
                        String name = playerSnapshot.child("teamName").getValue(String.class);
                        if (name.equals(team1)) {
                            team1PlayerCount++;
                        } else if (name.equals(team2)) {
                            team2PlayerCount++;
                        }
                    }
                    team1player_selected.add(String.valueOf(team1PlayerCount));
                    team2player_selected.add(String.valueOf(team2PlayerCount));
                    team1PlayerCount = 0;
                    team2PlayerCount = 0;

                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private String getRandomItem(List<String> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        Random rand = new Random();
        int randomIndex = rand.nextInt(list.size());
        return list.get(randomIndex);
    }

    @Override
    public void onItemClick(int position) {
        String contest_id;
        if (id!=null) {
            contest_id = getRandomItem(id);
        } else {
            contest_id = contest;
        }
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference pathref = FirebaseDatabase.getInstance().getReference("users/"+ mAuth.getCurrentUser().getUid()+"/teams/"+game).child(team1 + " VS " + team2 + " , " + series);
        DatabaseReference dref = FirebaseDatabase.getInstance().getReference("cricket/matches/" + team1 + " VS " + team2 + " , " + series + "/contest");

        pathref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int pos = position + 1;

                String key = dref.child(contest_id).child("participant").push().getKey();
                HashMap<String, Object> participantMap = new HashMap<>();
                participantMap.put("uid", mAuth.getCurrentUser().getUid());
                participantMap.put("user_team", snapshot.child("Team"+pos).getValue());
                dref.child(contest_id).child("participant").child(key).setValue(participantMap);

                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users/"+ mAuth.getCurrentUser().getUid());
                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Float money = snapshot.child("wallet").getValue(Float.class);
                        if (money!=null) {
                            float remain_amount = money - Integer.parseInt(entry);
                            userRef.child("wallet").setValue(remain_amount);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

class SelectTeamAdapter extends RecyclerView.Adapter<SelectTeamAdapter.ViewHolder> {

    private List<String> teamNames;
    private List<String> team1;
    private List<String> team2;
    private List<String> team1_player;
    private List<String> team2_player;
    private List<String> captain_name;
    private List<String> vicecaptain_name;
    private List<String> captain_img;
    private List<String> vicecaptain_img;
    private OnItemClickListener listener;

    public SelectTeamAdapter(List<String> teamNames, List<String> team1, List<String> team2, List<String> team1_player, List<String> team2_player, List<String> captain_name, List<String> captain_img, List<String> vicecaptain_name, List<String> vicecaptain_img, OnItemClickListener listener) {
        this.teamNames = teamNames;
        this.team1 = team1;
        this.team2 = team2;
        this.team1_player = team1_player;
        this.team2_player = team2_player;
        this.captain_name = captain_name;
        this.captain_img = captain_img;
        this.vicecaptain_name = vicecaptain_name;
        this.vicecaptain_img = vicecaptain_img;
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }


    @NonNull
    @Override
    public SelectTeamAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_team_recyclerview, parent, false);
        return new SelectTeamAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectTeamAdapter.ViewHolder holder, int position) {
        String teamName = teamNames.get(position);
        String team1country = team1.get(position);
        String team2country = team2.get(position);
        String team1_sel_player = team1_player.get(position);
        String team2_sel_player = team2_player.get(position);
        String captain = captain_name.get(position);
        String captainimg = captain_img.get(position);
        String vicecaptain = vicecaptain_name.get(position);
        String vicecaptainimg = vicecaptain_img.get(position);

        holder.teamid.setText(teamName);
        holder.team1name.setText(teamShort(team1country));
        holder.team2name.setText(teamShort(team2country));
        holder.team1_selected.setText(team1_sel_player);
        holder.team2_selected.setText(team2_sel_player);
        holder.captain_NAME.setText(nameSurname(captain));
        holder.vicecaptain_NAME.setText(nameSurname(vicecaptain));

        Glide.with(holder.captain_IMG.getContext())
                .load(captainimg)
                .placeholder(R.drawable.default_profile)
                .error(R.drawable.default_profile)
                .into(holder.captain_IMG);

        Glide.with(holder.vicecaptain_IMG.getContext())
                .load(vicecaptainimg)
                .placeholder(R.drawable.default_profile)
                .error(R.drawable.default_profile)
                .into(holder.vicecaptain_IMG);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                if (listener != null && adapterPosition != RecyclerView.NO_POSITION) {
                    listener.onItemClick(adapterPosition);
                }
            }
        });
    }

    private String teamShort(String name) {
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

    public String nameSurname (String name) {
        char firstNameLetter = name.charAt(0);
        String[] parts = name.split(" ");
        String surname = "";
        if (parts.length > 1) {
            for (int i = 1; i < parts.length; i++) {
                surname += parts[i];
            }
        }
        String result = firstNameLetter+". " + surname;
        return result;
    }

    @Override
    public int getItemCount() {
        return teamNames.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView teamid;
        public TextView team1name;
        public TextView team2name;
        public TextView team1_selected;
        public TextView team2_selected;
        public TextView captain_NAME;
        public ImageView captain_IMG;
        public TextView vicecaptain_NAME;
        public ImageView vicecaptain_IMG;

        public ViewHolder(View itemView) {
            super(itemView);
            teamid = itemView.findViewById(R.id.teamid);
            team1name = itemView.findViewById(R.id.team1name);
            team2name = itemView.findViewById(R.id.team2name);
            team1_selected = itemView.findViewById(R.id.team1player_selected);
            team2_selected = itemView.findViewById(R.id.team2player_selected);
            captain_NAME = itemView.findViewById(R.id.captain_name);
            captain_IMG = itemView.findViewById(R.id.captain_img);
            vicecaptain_NAME = itemView.findViewById(R.id.vicecaptain_name);
            vicecaptain_IMG = itemView.findViewById(R.id.vicecaptain_img);
        }
    }
}