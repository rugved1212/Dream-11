package com.example.dream11.TeamsAndContest;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.dream11.PropertyClasses.ContestModelClass;
import com.example.dream11.PropertyClasses.Player;
import com.example.dream11.PropertyClasses.Rule;
import com.example.dream11.R;
import com.example.dream11.TeamsAndContest.Contest_Detail.Contest_Details;
import com.example.dream11.TeamsAndContest.SelectTeamForContest.SelectTeam;
import com.github.nikartm.button.FitButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;


public class Contest extends Fragment {


    private String team1, team2, date, status, series, game;


    public Contest(String team1, String team2, String date, String status, String series) {
        this.team1 = team1;
        this.team2 = team2;
        this.date = date;
        this.status = status;
        this.series = series;
    }

    public Contest() {

    }


    public static Contest newInstance(String param1, String param2) {
        Contest fragment = new Contest();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        game = getActivity().getIntent().getStringExtra("game");
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contest, container, false);

        FitButton mg1 = view.findViewById(R.id.mg1);
        mg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                join_mega_contest("Mega Contest with Prize-Pool: 2.5 Lakhs and Entry: 49", 49, 250000, 6000);
            }
        });

        FitButton mg2 = view.findViewById(R.id.mg2);
        mg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                join_mega_contest("Mega Contest with Prize-Pool: 1 Lakhs and Entry: 25", 25, 100000, 5000);
            }
        });

        FitButton mc1 = view.findViewById(R.id.mc1);
        mc1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                join_normal_contest("Multiplier Contest with Prize-Pool: 20k and Entry: 19", 19, 20000, 1250);
            }
        });

        FitButton mc2 = view.findViewById(R.id.mc2);
        mc2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                join_normal_contest("Multiplier Contest with Prize-Pool: 10k and Entry: 15", 15, 10000, 1000);
            }
        });

        FitButton mum1 = view.findViewById(R.id.mum1);
        mum1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                join_normal_contest("Meet Your Match Contest with Prize-Pool: 100 and Entry: 27", 27, 100, 4);
            }
        });

        FitButton mum2 = view.findViewById(R.id.mum2);
        mum2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                join_normal_contest("Meet Your Match Contest with Prize-Pool: 50 and Entry: 19", 19, 50, 4);
            }
        });

        FitButton hth1 = view.findViewById(R.id.hth1);
        hth1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                join_normal_contest("Head-To-Head Contest with Prize-Pool: 66 and Entry: 35", 35, 66, 2);
            }
        });

        FitButton hth2 = view.findViewById(R.id.hth2);
        hth2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                join_normal_contest("Head-To-Head Contest with Prize-Pool: 35 and Entry: 20", 20, 35, 2);
            }
        });


        CardView cardView1 = view.findViewById(R.id.cardview1);
        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Contest_Details.class);
                intent.putExtra("team1", team1);
                intent.putExtra("team2", team2);
                intent.putExtra("date", date);
                intent.putExtra("status", status);
                intent.putExtra("series", series);
                intent.putExtra("game", game);
                intent.putExtra("contest_name", "Mega Contest with Prize-Pool: 2.5 Lakhs and Entry: 49");
                startActivity(intent);
            }
        });
        CardView cardView2 = view.findViewById(R.id.cardview2);
        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Contest_Details.class);
                intent.putExtra("team1", team1);
                intent.putExtra("team2", team2);
                intent.putExtra("date", date);
                intent.putExtra("status", status);
                intent.putExtra("series", series);
                intent.putExtra("game", game);
                intent.putExtra("contest_name", "Mega Contest with Prize-Pool: 1 Lakhs and Entry: 25");
                startActivity(intent);
            }
        });
        CardView cardView3 = view.findViewById(R.id.cardview3);
        cardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Contest_Details.class);
                intent.putExtra("team1", team1);
                intent.putExtra("team2", team2);
                intent.putExtra("date", date);
                intent.putExtra("status", status);
                intent.putExtra("series", series);
                intent.putExtra("game", game);
                intent.putExtra("contest_name", "Multiplier Contest with Prize-Pool: 20k and Entry: 19");
                startActivity(intent);
            }
        });
        CardView cardView4 = view.findViewById(R.id.cardview4);
        cardView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Contest_Details.class);
                intent.putExtra("team1", team1);
                intent.putExtra("team2", team2);
                intent.putExtra("date", date);
                intent.putExtra("status", status);
                intent.putExtra("series", series);
                intent.putExtra("game", game);
                intent.putExtra("contest_name", "Multiplier Contest with Prize-Pool: 10k and Entry: 15");
                startActivity(intent);
            }
        });
        CardView cardView5 = view.findViewById(R.id.cardview5);
        cardView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Contest_Details.class);
                intent.putExtra("team1", team1);
                intent.putExtra("team2", team2);
                intent.putExtra("date", date);
                intent.putExtra("status", status);
                intent.putExtra("series", series);
                intent.putExtra("game", game);
                intent.putExtra("contest_name", "Meet Your Match Contest with Prize-Pool: 100 and Entry: 27");
                startActivity(intent);
            }
        });
        CardView cardView6 = view.findViewById(R.id.cardview6);
        cardView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Contest_Details.class);
                intent.putExtra("team1", team1);
                intent.putExtra("team2", team2);
                intent.putExtra("date", date);
                intent.putExtra("status", status);
                intent.putExtra("series", series);
                intent.putExtra("game", game);
                intent.putExtra("contest_name", "Meet Your Match Contest with Prize-Pool: 50 and Entry: 19");
                startActivity(intent);
            }
        });
        CardView cardView7 = view.findViewById(R.id.cardview7);
        cardView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Contest_Details.class);
                intent.putExtra("team1", team1);
                intent.putExtra("team2", team2);
                intent.putExtra("date", date);
                intent.putExtra("status", status);
                intent.putExtra("series", series);
                intent.putExtra("game", game);
                intent.putExtra("contest_name", "Head-To-Head Contest with Prize-Pool: 66 and Entry: 35");
                startActivity(intent);
            }
        });
        CardView cardView8 = view.findViewById(R.id.cardview8);
        cardView8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Contest_Details.class);
                intent.putExtra("team1", team1);
                intent.putExtra("team2", team2);
                intent.putExtra("date", date);
                intent.putExtra("status", status);
                intent.putExtra("series", series);
                intent.putExtra("game", game);
                intent.putExtra("contest_name", "Head-To-Head Contest with Prize-Pool: 35 and Entry: 20");
                startActivity(intent);
            }
        });


        return view;
    }

    private void create_new_contest (int entry, int prize_pool, int limit, String contest_name) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference dref = FirebaseDatabase.getInstance().getReference("cricket/matches/" + team1 + " VS " + team2 + " , " + series + "/contest");
        DatabaseReference userteam = FirebaseDatabase.getInstance().getReference("users/"+ auth.getCurrentUser().getUid()+"/teams/"+game).child(team1 + " VS " + team2 + " , " + series);
        String contestId = dref.push().getKey();
        Rule rule = new Rule(limit, contest_name);
        ContestModelClass modelClass = new ContestModelClass(entry, prize_pool);
        dref.child(contestId).setValue(modelClass);
        dref.child(contestId).child("rules").setValue(rule);
        dref.child(contestId).child("participant").child(dref.child(contestId).child("participant").push().getKey()).setValue(auth.getCurrentUser().getUid());
    }

    private void create_normal_contest (int entry, int prize_pool, int limit, String contest_name) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference dref = FirebaseDatabase.getInstance().getReference(game + "/matches/" + team1 + " VS " + team2 + " , " + series + "/contest");
        DatabaseReference userteam = FirebaseDatabase.getInstance().getReference("users/"+ auth.getCurrentUser().getUid()+"/teams/"+game).child(team1 + " VS " + team2 + " , " + series);

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users/"+ FirebaseAuth.getInstance().getCurrentUser().getUid());

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Float money = snapshot.child("wallet").getValue(Float.class);
                if (money!=null && money >= limit) {
                    String contestId = dref.push().getKey();
                    Rule rule = new Rule(limit, contest_name);
                    ContestModelClass modelClass = new ContestModelClass(entry, prize_pool);
                    dref.child(contestId).setValue(modelClass);
                    dref.child(contestId).child("rules").setValue(rule);

                    userteam.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            long teamCount = snapshot.getChildrenCount();
                            Log.w("TEAM", "NO: "+ teamCount);
                            if (teamCount > 1) {
                                Intent intent = new Intent(getContext(), SelectTeam.class);
                                intent.putExtra("team1", team1);
                                intent.putExtra("team2", team2);
                                intent.putExtra("date", date);
                                intent.putExtra("status", status);
                                intent.putExtra("series", series);
                                intent.putExtra("game", game);
                                intent.putExtra("entry", entry);
                                intent.putExtra("contestId", contestId);
                                startActivity(intent);
                            } else if (teamCount == 1){
                                String key = dref.child(contestId).child("participant").push().getKey();
                                HashMap<String, Object> participantMap = new HashMap<>();
                                participantMap.put("uid", auth.getCurrentUser().getUid());
                                participantMap.put("user_team", snapshot.child("Team1").getValue());
                                dref.child(contestId).child("participant").child(key).setValue(participantMap);

                                float remain_amount = money - entry;
                                userRef.child("wallet").setValue(remain_amount);
                            } else if (teamCount == 0) {
                                Toast.makeText(getContext(), "PLEASE MAKE TEAM FIRST", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else {
                    Toast.makeText(getContext(), "ADD MONEY TO PLAY FURTHER", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void join_mega_contest(String contest_name, int entry, int prize_pool, int limit) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference userteam = FirebaseDatabase.getInstance().getReference("users/"+ auth.getCurrentUser().getUid()+"/teams/"+game).child(team1 + " VS " + team2 + " , " + series);
        DatabaseReference dref = FirebaseDatabase.getInstance().getReference(game + "/matches/" + team1 + " VS " + team2 + " , " + series + "/contest");
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users/"+ FirebaseAuth.getInstance().getCurrentUser().getUid());

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Float money = snapshot.child("wallet").getValue(Float.class);
                if (money!=null && money >= limit) {
                    dref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            List<String> contestIds = new ArrayList<>();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                String contestNAME = dataSnapshot.child("rules").child("contest_name").getValue(String.class);
                                Integer limitValue = dataSnapshot.child("rules").child("limit").getValue(Integer.class);
                                long participantCount = dataSnapshot.child("participant").getChildrenCount();

                                if (contestNAME != null && contestNAME.equals(contest_name)) {
                                    Log.w("CONTEST", "CONTEST EQUALS");

                                    if (participantCount < limitValue) {
                                        boolean isUserParticipated = false;
                                        int participationCount = 0;

                                        for (DataSnapshot participantSnapshot : dataSnapshot.child("participant").getChildren()) {
                                            String uid = participantSnapshot.child("uid").getValue(String.class);
                                            if (uid != null && uid.equals(auth.getCurrentUser().getUid())) {
                                                isUserParticipated = true;
                                                participationCount++;
                                                if (participationCount >= 10) {
                                                    break;
                                                }
                                            }
                                        }

                                        if (!isUserParticipated || participationCount < 10) {
                                            String contestId = dataSnapshot.getKey();
                                            if (contestId != null) {
                                                contestIds.add(contestId);
                                                Log.w("CONTEST ID", contestId);
                                            } else {
                                                Log.w("INFO", "Contest ID is null");
                                            }
                                        } else {
                                            Log.w("INFO", "User already participated in this contest");
                                        }
                                    } else {
                                        Log.w("INFO", "Contest reached participant limit");
                                    }
                                } else {
//                        create_normal_contest(entry, prize_pool, limit, contest_name);
                                }
                            }

                            if (!contestIds.isEmpty()) {
                                Log.w("CONTEST IDs SELECTED", contestIds.toString());
                                String id = getRandomItem(contestIds);
                                Log.d("RANDOM CONTEST ", "RANDOM: " + id);
                                userteam.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        long teamCount = snapshot.getChildrenCount();
                                        Log.w("TEAM", "NO: "+ teamCount);
                                        if (teamCount > 1) {
                                            Intent intent = new Intent(getContext(), SelectTeam.class);
                                            intent.putExtra("team1", team1);
                                            intent.putExtra("team2", team2);
                                            intent.putExtra("date", date);
                                            intent.putExtra("status", status);
                                            intent.putExtra("series", series);
                                            intent.putExtra("game", game);
                                            intent.putExtra("entry", entry);
                                            intent.putStringArrayListExtra("contest_list", (ArrayList<String>) contestIds);
                                            startActivity(intent);
                                        } else if (teamCount == 1){
                                            String key = dref.child(id).child("participant").push().getKey();
                                            HashMap<String, Object> participantMap = new HashMap<>();
                                            participantMap.put("uid", auth.getCurrentUser().getUid());
                                            participantMap.put("user_team", snapshot.getValue());
                                            dref.child(id).child("participant").child(key).setValue(participantMap);

                                            float remain_amount = money - entry;
                                            userRef.child("wallet").setValue(remain_amount);
                                        } else if (teamCount == 0) {
                                            Toast.makeText(getContext(), "PLEASE MAKE TEAM FIRST", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            } else {
                                Log.w("INFO", "No matching contest found");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else {
                    Toast.makeText(getContext(), "ADD MONEY TO PLAY FURTHER", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void join_normal_contest(String contest_name, int entry, int prize_pool, int limit) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference userteam = FirebaseDatabase.getInstance().getReference("users/"+ auth.getCurrentUser().getUid()+"/teams/"+game).child(team1 + " VS " + team2 + " , " + series);
        DatabaseReference dref = FirebaseDatabase.getInstance().getReference(game + "/matches/" + team1 + " VS " + team2 + " , " + series + "/contest");
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users/"+ FirebaseAuth.getInstance().getCurrentUser().getUid());

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Float money = snapshot.child("wallet").getValue(Float.class);
                if (money!=null && money >= limit) {
                    dref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            List<String> contestIds = new ArrayList<>();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                String contestNAME = dataSnapshot.child("rules").child("contest_name").getValue(String.class);
                                Integer limitValue = dataSnapshot.child("rules").child("limit").getValue(Integer.class);
                                long participantCount = dataSnapshot.child("participant").getChildrenCount();

                                if (contestNAME != null && contestNAME.equals(contest_name)) {

                                    if (participantCount < limitValue) {
                                        boolean isUserParticipated = false;

                                        for (DataSnapshot participantSnapshot : dataSnapshot.child("participant").getChildren()) {
                                            String uid = participantSnapshot.child("uid").getValue(String.class);
                                            if (uid != null && uid.equals(auth.getCurrentUser().getUid())) {
                                                isUserParticipated = true;
                                                break;
                                            }
                                        }

                                        if (!isUserParticipated) {
                                            String contestId = dataSnapshot.getKey();
                                            if (contestId != null) {
                                                contestIds.add(contestId);
                                                Log.w("CONTEST ID", contestId);
                                            } else {
                                                Log.w("INFO", "Contest ID is null");
                                            }
                                        } else {
                                            Log.w("INFO", "User already participated in this contest");
                                        }
                                    } else {
                                        Log.w("INFO", "Contest reached participant limit");
                                    }
                                }
                            }

                            if (!contestIds.isEmpty()) {
                                Log.w("CONTEST IDs SELECTED", contestIds.toString());
                                String id = getRandomItem(contestIds);
                                Log.d("RANDOM CONTEST ", "RANDOM: " + id);
                                userteam.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        long teamCount = snapshot.getChildrenCount();
                                        Log.w("TEAM", "NO: "+ teamCount);
                                        if (teamCount > 1) {
                                            Intent intent = new Intent(getContext(), SelectTeam.class);
                                            intent.putExtra("team1", team1);
                                            intent.putExtra("team2", team2);
                                            intent.putExtra("date", date);
                                            intent.putExtra("status", status);
                                            intent.putExtra("series", series);
                                            intent.putExtra("game", game);
                                            intent.putExtra("entry", entry);
                                            intent.putStringArrayListExtra("contest_list", (ArrayList<String>) contestIds);
                                            startActivity(intent);
                                        } else if (teamCount == 1){
                                            String key = dref.child(id).child("participant").push().getKey();
                                            HashMap<String, Object> participantMap = new HashMap<>();
                                            participantMap.put("uid", auth.getCurrentUser().getUid());
                                            participantMap.put("user_team", snapshot.getValue());
                                            dref.child(id).child("participant").child(key).setValue(participantMap);

                                            float remain_amount = money - entry;
                                            userRef.child("wallet").setValue(remain_amount);
                                        } else if (teamCount == 0) {
                                            Toast.makeText(getContext(), "PLEASE MAKE TEAM FIRST", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            } else {
                                create_normal_contest(entry, prize_pool, limit, contest_name);
                                Log.w("INFO", "No matching contest found");
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else {
                    Toast.makeText(getContext(), "ADD MONEY TO PLAY FURTHER", Toast.LENGTH_SHORT).show();
                }
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
}