package com.example.dream11.TeamsAndContest;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.dream11.PropertyClasses.Match;
import com.example.dream11.PropertyClasses.Player;
import com.example.dream11.R;
import com.example.dream11.TeamsAndContest.BottomSheet.CricketBottomSheet;
import com.example.dream11.TeamsAndContest.BottomSheet.FootballBottomSheet;
import com.example.dream11.TeamsAndContest.BottomSheet.KabaddiBottomSheet;
import com.example.dream11.TeamsAndContest.CreateTeam.Cricket.CreateCricketTeam;
import com.example.dream11.TeamsAndContest.CreateTeam.Football.CreateFootballTeam;
import com.example.dream11.TeamsAndContest.CreateTeam.Kabaddi.CreateKabaddiTeam;
import com.example.dream11.TeamsAndContest.LineUP.LineUP;
import com.github.nikartm.button.FitButton;
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
import java.util.List;
import java.util.Locale;


public class MyTeams extends Fragment implements TeamAdapter.OnItemClickListener {

    private String team1, team2, date, status, series, game;
    ImageView bg_img;
    TextView bg_text;

    public MyTeams() {

    }

    public MyTeams(String team1, String team2, String date, String status, String series) {
        this.team1 = team1;
        this.team2 = team2;
        this.date = date;
        this.status = status;
        this.series = series;
    }


    public static MyTeams newInstance(String param1, String param2) {
        MyTeams fragment = new MyTeams();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_teams, container, false);

        game = getActivity().getIntent().getStringExtra("game");

        LinearLayout create_team_btn_layout = view.findViewById(R.id.ll);
        Match match = new Match(series, "", "", team1, team2, date, status);

        if (isMatchInFuture(match)) {
            create_team_btn_layout.setVisibility(View.VISIBLE);
        } else {
            create_team_btn_layout.setVisibility(View.GONE);
        }

        bg_img = view.findViewById(R.id.no_team_img);
        bg_text = view.findViewById(R.id.no_team_text);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        List<String> teamNames = new ArrayList<>();
        List<String> team1list = new ArrayList<>();
        List<String> team2list = new ArrayList<>();
        List<String> team1player_selected = new ArrayList<>();
        List<String> team2player_selected = new ArrayList<>();
        List<String> captain_name = new ArrayList<>();
        List<String> vicecaptain_name = new ArrayList<>();
        List<String> captain_img = new ArrayList<>();
        List<String> vicecaptain_img = new ArrayList<>();
        TeamAdapter adapter = new TeamAdapter(teamNames, team1list, team2list, team1player_selected, team2player_selected, captain_name, captain_img, vicecaptain_name, vicecaptain_img, this);
        recyclerView.setAdapter(adapter);

        retreiveTeams(teamNames, adapter, team1list, team2list, team1player_selected, team2player_selected, captain_name, captain_img, vicecaptain_name, vicecaptain_img);

        FitButton create = view.findViewById(R.id.create);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (game.equals("cricket")) {
                    Intent intent = new Intent(getContext(), CreateCricketTeam.class);
                    intent.putExtra("team1", team1);
                    intent.putExtra("team2", team2);
                    intent.putExtra("date", date);
                    intent.putExtra("status", status);
                    intent.putExtra("series", series);
                    startActivity(intent);
                } else if (game.equals("football")) {
                    Intent intent = new Intent(getContext(), CreateFootballTeam.class);
                    intent.putExtra("team1", team1);
                    intent.putExtra("team2", team2);
                    intent.putExtra("date", date);
                    intent.putExtra("status", status);
                    intent.putExtra("series", series);
                    startActivity(intent);
                } else if (game.equals("kabaddi")) {
                    Intent intent = new Intent(getContext(), CreateKabaddiTeam.class);
                    intent.putExtra("team1", team1);
                    intent.putExtra("team2", team2);
                    intent.putExtra("date", date);
                    intent.putExtra("status", status);
                    intent.putExtra("series", series);
                    startActivity(intent);
                }

            }
        });

        FitButton lineup = view.findViewById(R.id.lineup);
        lineup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), LineUP.class);
                intent.putExtra("team1", team1);
                intent.putExtra("team2", team2);
                intent.putExtra("date", date);
                intent.putExtra("status", status);
                intent.putExtra("series", series);
                intent.putExtra("game", game);
                startActivity(intent);
            }
        });

        return view;
    }

    private void retreiveTeams(List<String> teamNames, TeamAdapter adapter, List<String> team1list, List<String> team2list, List<String> team1player_selected, List<String> team2player_selected, List<String> captain_name, List<String> captain_img, List<String> vicecaptain_name, List<String> vicecaptain_img) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference pathref = FirebaseDatabase.getInstance().getReference("users/"+ mAuth.getCurrentUser().getUid()+"/teams/"+game).child(team1 + " VS " + team2 + " , " + series);
        pathref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int team1PlayerCount = 0;
                int team2PlayerCount = 0;

                teamNames.clear();
                team1list.clear();
                team2list.clear();
                captain_name.clear();
                captain_img.clear();
                vicecaptain_name.clear();
                vicecaptain_img.clear();
                team1player_selected.clear();
                team2player_selected.clear();

                for (DataSnapshot teamSnapshot : snapshot.getChildren()) {
                    String teamName = teamSnapshot.getKey();

                    Integer captainPosition = teamSnapshot.child("captain_position").getValue(Integer.class);
                    Integer viceCaptainPosition = teamSnapshot.child("vicecaptain_position").getValue(Integer.class);

                    if (captainPosition != null && viceCaptainPosition != null) {
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
                    }

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

                if (snapshot.exists()) {
                    bg_text.setVisibility(View.GONE);
                    bg_img.setVisibility(View.GONE);
                } else {
                    bg_text.setVisibility(View.VISIBLE);
                    bg_img.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onItemClick(int position) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference pathref = FirebaseDatabase.getInstance().getReference("users/"+ mAuth.getCurrentUser().getUid()+"/teams/"+game).child(team1 + " VS " + team2 + " , " + series);

        pathref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int pos = position + 1;
                DataSnapshot selectedTeamSnapshot = snapshot.child("Team"+pos);
                List<Player> selectedTeamPlayers = new ArrayList<>();
                for (DataSnapshot playerSnapshot : selectedTeamSnapshot.child("players").getChildren()) {
                    Player player = playerSnapshot.getValue(Player.class);
                    if (player != null) {
                        selectedTeamPlayers.add(player);
                    }
                }

                Integer captainPosition = selectedTeamSnapshot.child("captain_position").getValue(Integer.class);
                Integer viceCaptainPosition = selectedTeamSnapshot.child("vicecaptain_position").getValue(Integer.class);


                String captainName= "";
                String viceCaptainName = "";

                if (captainPosition != null && viceCaptainPosition != null) {
                    captainName = selectedTeamSnapshot.child("players").child(String.valueOf(captainPosition)).child("player_name").getValue(String.class);
                    viceCaptainName = selectedTeamSnapshot.child("players").child(String.valueOf(viceCaptainPosition)).child("player_name").getValue(String.class);
                }

                if (game.equals("cricket")) {
                    List<Player> wk = new ArrayList<>();
                    List<Player> bat = new ArrayList<>();
                    List<Player> ar = new ArrayList<>();
                    List<Player> bowl = new ArrayList<>();
                    for (Player player : selectedTeamPlayers) {
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
                    CricketBottomSheet cricketBottomSheet = new CricketBottomSheet(wk, bat, ar, bowl, captainName, viceCaptainName, "11/11", teamShort(team1), teamShort(team2));
                    cricketBottomSheet.show(getParentFragmentManager(), cricketBottomSheet.getTag());
                } else if (game.equals("football")) {
                    List<Player> gk = new ArrayList<>();
                    List<Player> def = new ArrayList<>();
                    List<Player> mid = new ArrayList<>();
                    List<Player> st = new ArrayList<>();
                    for (Player player : selectedTeamPlayers) {
                        if (player.getPlayer_position().equals("GOAL-KEEPER")) {
                            gk.add(player);
                        } else if (player.getPlayer_position().equals("DEFENDER")) {
                            def.add(player);
                        } else if (player.getPlayer_position().equals("MIDFIELDER")) {
                            mid.add(player);
                        } else if (player.getPlayer_position().equals("FORWARD")) {
                            st.add(player);
                        }
                    }

                    FootballBottomSheet footballBottomSheet = new FootballBottomSheet(gk, def, mid, st,  captainName, viceCaptainName, "11/11", teamShort(team1), teamShort(team2));
                    footballBottomSheet.show(getParentFragmentManager(), footballBottomSheet.getTag());
                } else if (game.equals("kabaddi")) {
                    List<Player> def = new ArrayList<>();
                    List<Player> ar = new ArrayList<>();
                    List<Player> ra = new ArrayList<>();
                    for (Player player : selectedTeamPlayers) {
                        if (player.getPlayer_position().equals("DEFENDER")) {
                            def.add(player);
                        } else if (player.getPlayer_position().equals("ALL-ROUNDER")) {
                            ar.add(player);
                        } else if (player.getPlayer_position().equals("RAIDER")) {
                            ra.add(player);
                        }
                    }

                    KabaddiBottomSheet kabaddiBottomSheet = new KabaddiBottomSheet(def, ar, ra,  captainName, viceCaptainName, "7/7", teamShort(team1), teamShort(team2));
                    kabaddiBottomSheet.show(getParentFragmentManager(), kabaddiBottomSheet.getTag());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private boolean isMatchInFuture(Match match) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        try {
            Date matchDate = sdf.parse(match.getDate());
            long matchTimeMillis = matchDate.getTime();
            return matchTimeMillis > System.currentTimeMillis();
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
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
}

class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.TeamViewHolder> {

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

    public TeamAdapter(List<String> teamNames, List<String> team1, List<String> team2, List<String> team1_player, List<String> team2_player, List<String> captain_name, List<String> captain_img, List<String> vicecaptain_name, List<String> vicecaptain_img, OnItemClickListener listener) {
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
    public TeamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_team_recyclerview, parent, false);
        return new TeamViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamViewHolder holder, int position) {
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

    public static class TeamViewHolder extends RecyclerView.ViewHolder {
        public TextView teamid;
        public TextView team1name;
        public TextView team2name;
        public TextView team1_selected;
        public TextView team2_selected;
        public TextView captain_NAME;
        public ImageView captain_IMG;
        public TextView vicecaptain_NAME;
        public ImageView vicecaptain_IMG;

        public TeamViewHolder(View itemView) {
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