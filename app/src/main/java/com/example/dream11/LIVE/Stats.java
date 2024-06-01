package com.example.dream11.LIVE;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dream11.Adapters.Stats_Adapter;
import com.example.dream11.PropertyClasses.Player;
import com.example.dream11.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class Stats extends Fragment {

    String team1, team2, date, status, series, game;
    ArrayList<Player> playerArrayList = new ArrayList<>();
    ArrayList<Integer> playerPointList = new ArrayList<>();
    Stats_Adapter statsAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    public Stats() {
        // Required empty public constructor
    }


    public static Stats newInstance(String param1, String param2) {
        Stats fragment = new Stats();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        team1 = getActivity().getIntent().getStringExtra("team1");
        team2 = getActivity().getIntent().getStringExtra("team2");
        date = getActivity().getIntent().getStringExtra("date");
        status = getActivity().getIntent().getStringExtra("status");
        series = getActivity().getIntent().getStringExtra("series");
        game = getActivity().getIntent().getStringExtra("game");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stats, container, false);

        RecyclerView recyclerview = view.findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        statsAdapter = new Stats_Adapter(playerArrayList, playerPointList);
        recyclerview.setAdapter(statsAdapter);

        retrive_points();
        update_adapter();

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                retrive_points();
                update_adapter();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return view;
    }

    private void retrive_points() {
        DatabaseReference dref = FirebaseDatabase.getInstance().getReference(game + "/matches/" + team1 + " VS " + team2 + " , " + series + "/score");
        if (game.equals("cricket")) {
            dref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    dref.child("inning1/batsman_stats").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot playerstats : snapshot.getChildren()) {
                                String playerName = playerstats.child("playerName").getValue(String.class);
                                Integer ballsPlayed = playerstats.child("ballsPlayed").getValue(Integer.class);
                                Integer foursHit = playerstats.child("foursHit").getValue(Integer.class);
                                Integer runsScored = playerstats.child("runsScored").getValue(Integer.class);
                                Integer sixesHit = playerstats.child("sixesHit").getValue(Integer.class);
                                Integer strikeRate = playerstats.child("strikeRate").getValue(Integer.class);

                                int inning1_batsman_point = 0;
                                if (foursHit!=null) {
                                    inning1_batsman_point = inning1_batsman_point + foursHit;
                                }
                                if (runsScored!=null) {
                                    inning1_batsman_point = inning1_batsman_point + runsScored;
                                }
                                if (sixesHit!=null) {
                                    inning1_batsman_point = inning1_batsman_point + (sixesHit * 2);
                                }
                                if (strikeRate!=null) {
                                    if (strikeRate > 170f) {
                                        inning1_batsman_point = inning1_batsman_point + 6;
                                    } else if (150.01f < strikeRate && strikeRate < 170f) {
                                        inning1_batsman_point = inning1_batsman_point + 4;
                                    } else if (130f < strikeRate && strikeRate < 150f) {
                                        inning1_batsman_point = inning1_batsman_point + 2;
                                    } else if (60f < strikeRate && strikeRate < 70f) {
                                        inning1_batsman_point = inning1_batsman_point - 2;
                                    } else if (50f < strikeRate && strikeRate < 59.99f) {
                                        inning1_batsman_point = inning1_batsman_point - 4;
                                    } else if (strikeRate < 50f) {
                                        inning1_batsman_point = inning1_batsman_point - 6;
                                    }
                                }

                                DatabaseReference player = FirebaseDatabase.getInstance().getReference(game + "/matches/" + team1 + " VS " + team2 + " , " + series);
                                int finalPoints = inning1_batsman_point;
                                player.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        player.child("Team1").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                for (DataSnapshot player : snapshot.getChildren()) {
                                                    if (playerName.equals(player.child("player_name").getValue())) {
                                                        player.getRef().child("inning1_batsman_point").setValue(finalPoints);
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                        player.child("Team2").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                for (DataSnapshot player : snapshot.getChildren()) {
                                                    if (playerName.equals(player.child("player_name").getValue())) {
                                                        player.getRef().child("inning1_batsman_point").setValue(finalPoints);
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    dref.child("inning1/bowler_stats").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot playerstats : snapshot.getChildren()) {
                                String playerName = playerstats.child("playerName").getValue(String.class);
                                Integer economy = playerstats.child("economy").getValue(Integer.class);
                                Integer maindens = playerstats.child("maindens").getValue(Integer.class);
                                Integer overBowled = playerstats.child("overBowled").getValue(Integer.class);
                                Integer runsGiven = playerstats.child("runsGiven").getValue(Integer.class);
                                Integer wicketsTaken = playerstats.child("wicketsTaken").getValue(Integer.class);

                                int inning1_bowler_point = 0;
                                if (maindens!=null) {
                                    inning1_bowler_point = inning1_bowler_point + (maindens * 12);
                                }
                                if (runsGiven!=null) {
                                    inning1_bowler_point = inning1_bowler_point + runsGiven;
                                }
                                if (wicketsTaken!=null) {
                                    inning1_bowler_point = inning1_bowler_point + (wicketsTaken * 25);
                                    if (wicketsTaken > 3) {
                                        inning1_bowler_point = inning1_bowler_point + 4;
                                    } else if (wicketsTaken > 4) {
                                        inning1_bowler_point = inning1_bowler_point + 8;
                                    } else if (wicketsTaken > 5) {
                                        inning1_bowler_point = inning1_bowler_point + 16;
                                    }
                                }
                                if (economy!=null) {
                                    if (economy > 12) {
                                        inning1_bowler_point = inning1_bowler_point - 6;
                                    } else if (11.01f < economy && economy < 12f) {
                                        inning1_bowler_point = inning1_bowler_point - 4;
                                    } else if (10f < economy && economy < 11f) {
                                        inning1_bowler_point = inning1_bowler_point - 2;
                                    } else if (6f < economy && economy < 7f) {
                                        inning1_bowler_point = inning1_bowler_point + 2;
                                    } else if (5f < economy && economy < 5.99f) {
                                        inning1_bowler_point = inning1_bowler_point + 4;
                                    } else if (economy < 5f) {
                                        inning1_bowler_point = inning1_bowler_point + 6;
                                    }
                                }

                                DatabaseReference player = FirebaseDatabase.getInstance().getReference(game + "/matches/" + team1 + " VS " + team2 + " , " + series);
                                int finalPoints = inning1_bowler_point;
                                player.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        player.child("Team1").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                for (DataSnapshot player : snapshot.getChildren()) {
                                                    if (playerName.equals(player.child("player_name").getValue())) {
                                                        player.getRef().child("inning1_bowler_point").setValue(finalPoints);
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                        player.child("Team2").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                for (DataSnapshot player : snapshot.getChildren()) {
                                                    if (playerName.equals(player.child("player_name").getValue())) {
                                                        player.getRef().child("inning1_bowler_point").setValue(finalPoints);
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    dref.child("inning2/batsman_stats").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot playerstats : snapshot.getChildren()) {
                                String playerName = playerstats.child("playerName").getValue(String.class);
                                Integer ballsPlayed = playerstats.child("ballsPlayed").getValue(Integer.class);
                                Integer foursHit = playerstats.child("foursHit").getValue(Integer.class);
                                Integer runsScored = playerstats.child("runsScored").getValue(Integer.class);
                                Integer sixesHit = playerstats.child("sixesHit").getValue(Integer.class);
                                Integer strikeRate = playerstats.child("strikeRate").getValue(Integer.class);

                                int inning2_batsman_point = 0;
                                if (foursHit!=null) {
                                    inning2_batsman_point = inning2_batsman_point + (foursHit * 1);
                                }
                                if (runsScored!=null) {
                                    inning2_batsman_point = inning2_batsman_point + runsScored;
                                }
                                if (sixesHit!=null) {
                                    inning2_batsman_point = inning2_batsman_point + (sixesHit * 2);
                                }
                                if (strikeRate!=null) {
                                    if (strikeRate > 170) {
                                        inning2_batsman_point = inning2_batsman_point + 6;
                                    } else if (150.01f < strikeRate && strikeRate < 170f) {
                                        inning2_batsman_point = inning2_batsman_point + 4;
                                    } else if (130f < strikeRate && strikeRate < 150f) {
                                        inning2_batsman_point = inning2_batsman_point + 2;
                                    } else if (60f < strikeRate && strikeRate < 70f) {
                                        inning2_batsman_point = inning2_batsman_point - 2;
                                    } else if (50f < strikeRate && strikeRate < 59.99f) {
                                        inning2_batsman_point = inning2_batsman_point - 4;
                                    } else if (strikeRate < 50f) {
                                        inning2_batsman_point = inning2_batsman_point - 6;
                                    }
                                }

                                DatabaseReference player = FirebaseDatabase.getInstance().getReference(game + "/matches/" + team1 + " VS " + team2 + " , " + series);
                                int finalPoints = inning2_batsman_point;
                                player.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        player.child("Team1").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                for (DataSnapshot player : snapshot.getChildren()) {
                                                    if (playerName.equals(player.child("player_name").getValue())) {
                                                        player.getRef().child("inning2_batsman_point").setValue(finalPoints);
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                        player.child("Team2").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                for (DataSnapshot player : snapshot.getChildren()) {
                                                    if (playerName.equals(player.child("player_name").getValue())) {
                                                        player.getRef().child("inning2_batsman_point").setValue(finalPoints);
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    dref.child("inning2/bowler_stats").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot playerstats : snapshot.getChildren()) {
                                String playerName = playerstats.child("playerName").getValue(String.class);
                                Integer economy = playerstats.child("economy").getValue(Integer.class);
                                Integer maindens = playerstats.child("maindens").getValue(Integer.class);
                                Integer overBowled = playerstats.child("overBowled").getValue(Integer.class);
                                Integer runsGiven = playerstats.child("runsGiven").getValue(Integer.class);
                                Integer wicketsTaken = playerstats.child("wicketsTaken").getValue(Integer.class);

                                int inning2_bowler_point = 0;
                                if (maindens!=null) {
                                    inning2_bowler_point = inning2_bowler_point + (maindens * 12);
                                }
                                if (runsGiven!=null) {
                                    inning2_bowler_point = inning2_bowler_point + runsGiven;
                                }
                                if (wicketsTaken!=null) {
                                    inning2_bowler_point = inning2_bowler_point + (wicketsTaken * 25);
                                    if (wicketsTaken > 3) {
                                        inning2_bowler_point = inning2_bowler_point + 4;
                                    } else if (wicketsTaken > 4) {
                                        inning2_bowler_point = inning2_bowler_point + 8;
                                    } else if (wicketsTaken > 5) {
                                        inning2_bowler_point = inning2_bowler_point + 16;
                                    }
                                }
                                if (economy!=null) {
                                    if (economy > 12) {
                                        inning2_bowler_point = inning2_bowler_point - 6;
                                    } else if (11.01f < economy && economy < 12f) {
                                        inning2_bowler_point = inning2_bowler_point - 4;
                                    } else if (10f < economy && economy < 11f) {
                                        inning2_bowler_point = inning2_bowler_point - 2;
                                    } else if (6f < economy && economy < 7f) {
                                        inning2_bowler_point = inning2_bowler_point + 2;
                                    } else if (5f < economy && economy < 5.99f) {
                                        inning2_bowler_point = inning2_bowler_point + 4;
                                    } else if (economy < 5f) {
                                        inning2_bowler_point = inning2_bowler_point + 6;
                                    }
                                }

                                DatabaseReference player = FirebaseDatabase.getInstance().getReference(game + "/matches/" + team1 + " VS " + team2 + " , " + series);
                                int finalPoints = inning2_bowler_point;
                                player.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        player.child("Team1").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                for (DataSnapshot player : snapshot.getChildren()) {
                                                    if (playerName.equals(player.child("player_name").getValue())) {
                                                        player.getRef().child("inning2_bowler_point").setValue(finalPoints);
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                        player.child("Team2").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                for (DataSnapshot player : snapshot.getChildren()) {
                                                    if (playerName.equals(player.child("player_name").getValue())) {
                                                        player.getRef().child("inning2_bowler_point").setValue(finalPoints);
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    dref.child("inning3/batsman_stats").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot playerstats : snapshot.getChildren()) {
                                String playerName = playerstats.child("playerName").getValue(String.class);
                                Integer ballsPlayed = playerstats.child("ballsPlayed").getValue(Integer.class);
                                Integer foursHit = playerstats.child("foursHit").getValue(Integer.class);
                                Integer runsScored = playerstats.child("runsScored").getValue(Integer.class);
                                Integer sixesHit = playerstats.child("sixesHit").getValue(Integer.class);
                                Integer strikeRate = playerstats.child("strikeRate").getValue(Integer.class);

                                int inning3_batsman_point = 0;
                                if (foursHit!=null) {
                                    inning3_batsman_point = inning3_batsman_point + (foursHit * 1);
                                }
                                if (runsScored!=null) {
                                    inning3_batsman_point = inning3_batsman_point + runsScored;
                                }
                                if (sixesHit!=null) {
                                    inning3_batsman_point = inning3_batsman_point + (sixesHit * 2);
                                }
                                if (strikeRate!=null) {
                                    if (strikeRate > 170) {
                                        inning3_batsman_point = inning3_batsman_point + 6;
                                    } else if (150.01f < strikeRate && strikeRate < 170f) {
                                        inning3_batsman_point = inning3_batsman_point + 4;
                                    } else if (130f < strikeRate && strikeRate < 150f) {
                                        inning3_batsman_point = inning3_batsman_point + 2;
                                    } else if (60f < strikeRate && strikeRate < 70f) {
                                        inning3_batsman_point = inning3_batsman_point - 2;
                                    } else if (50f < strikeRate && strikeRate < 59.99f) {
                                        inning3_batsman_point = inning3_batsman_point - 4;
                                    } else if (strikeRate < 50f) {
                                        inning3_batsman_point = inning3_batsman_point - 6;
                                    }
                                }

                                DatabaseReference player = FirebaseDatabase.getInstance().getReference(game + "/matches/" + team1 + " VS " + team2 + " , " + series);
                                int finalPoints = inning3_batsman_point;
                                player.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        player.child("Team1").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                for (DataSnapshot player : snapshot.getChildren()) {
                                                    if (playerName.equals(player.child("player_name").getValue())) {
                                                        player.getRef().child("inning3_batsman_point").setValue(finalPoints);
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                        player.child("Team2").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                for (DataSnapshot player : snapshot.getChildren()) {
                                                    if (playerName.equals(player.child("player_name").getValue())) {
                                                        player.getRef().child("inning3_batsman_point").setValue(finalPoints);
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    dref.child("inning3/bowler_stats").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot playerstats : snapshot.getChildren()) {
                                String playerName = playerstats.child("playerName").getValue(String.class);
                                Integer economy = playerstats.child("economy").getValue(Integer.class);
                                Integer maindens = playerstats.child("maindens").getValue(Integer.class);
                                Integer overBowled = playerstats.child("overBowled").getValue(Integer.class);
                                Integer runsGiven = playerstats.child("runsGiven").getValue(Integer.class);
                                Integer wicketsTaken = playerstats.child("wicketsTaken").getValue(Integer.class);

                                int inning3_bowler_point = 0;
                                if (maindens!=null) {
                                    inning3_bowler_point = inning3_bowler_point + (maindens * 12);
                                }
                                if (runsGiven!=null) {
                                    inning3_bowler_point = inning3_bowler_point + runsGiven;
                                }
                                if (wicketsTaken!=null) {
                                    inning3_bowler_point = inning3_bowler_point + (wicketsTaken * 25);
                                    if (wicketsTaken > 3) {
                                        inning3_bowler_point = inning3_bowler_point + 4;
                                    } else if (wicketsTaken > 4) {
                                        inning3_bowler_point = inning3_bowler_point + 8;
                                    } else if (wicketsTaken > 5) {
                                        inning3_bowler_point = inning3_bowler_point + 16;
                                    }
                                }
                                if (economy!=null) {
                                    if (economy > 12) {
                                        inning3_bowler_point = inning3_bowler_point - 6;
                                    } else if (11.01f < economy && economy < 12f) {
                                        inning3_bowler_point = inning3_bowler_point - 4;
                                    } else if (10f < economy && economy < 11f) {
                                        inning3_bowler_point = inning3_bowler_point - 2;
                                    } else if (6f < economy && economy < 7f) {
                                        inning3_bowler_point = inning3_bowler_point + 2;
                                    } else if (5f < economy && economy < 5.99f) {
                                        inning3_bowler_point = inning3_bowler_point + 4;
                                    } else if (economy < 5f) {
                                        inning3_bowler_point = inning3_bowler_point + 6;
                                    }
                                }

                                DatabaseReference player = FirebaseDatabase.getInstance().getReference(game + "/matches/" + team1 + " VS " + team2 + " , " + series);
                                int finalPoints = inning3_bowler_point;
                                player.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        player.child("Team1").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                for (DataSnapshot player : snapshot.getChildren()) {
                                                    if (playerName.equals(player.child("player_name").getValue())) {
                                                        player.getRef().child("inning3_bowler_point").setValue(finalPoints);
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                        player.child("Team2").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                for (DataSnapshot player : snapshot.getChildren()) {
                                                    if (playerName.equals(player.child("player_name").getValue())) {
                                                        player.getRef().child("inning3_bowler_point").setValue(finalPoints);
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    dref.child("inning4/batsman_stats").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot playerstats : snapshot.getChildren()) {
                                String playerName = playerstats.child("playerName").getValue(String.class);
                                Integer ballsPlayed = playerstats.child("ballsPlayed").getValue(Integer.class);
                                Integer foursHit = playerstats.child("foursHit").getValue(Integer.class);
                                Integer runsScored = playerstats.child("runsScored").getValue(Integer.class);
                                Integer sixesHit = playerstats.child("sixesHit").getValue(Integer.class);
                                Integer strikeRate = playerstats.child("strikeRate").getValue(Integer.class);

                                int inning4_batsman_point = 0;
                                if (foursHit!=null) {
                                    inning4_batsman_point = inning4_batsman_point + (foursHit * 1);
                                }
                                if (runsScored!=null) {
                                    inning4_batsman_point = inning4_batsman_point + runsScored;
                                }
                                if (sixesHit!=null) {
                                    inning4_batsman_point = inning4_batsman_point + (sixesHit * 2);
                                }
                                if (strikeRate!=null) {
                                    if (strikeRate > 170) {
                                        inning4_batsman_point = inning4_batsman_point + 6;
                                    } else if (150.01f < strikeRate && strikeRate < 170f) {
                                        inning4_batsman_point = inning4_batsman_point + 4;
                                    } else if (130f < strikeRate && strikeRate < 150f) {
                                        inning4_batsman_point = inning4_batsman_point + 2;
                                    } else if (60f < strikeRate && strikeRate < 70f) {
                                        inning4_batsman_point = inning4_batsman_point - 2;
                                    } else if (50f < strikeRate && strikeRate < 59.99f) {
                                        inning4_batsman_point = inning4_batsman_point - 4;
                                    } else if (strikeRate < 50f) {
                                        inning4_batsman_point = inning4_batsman_point - 6;
                                    }
                                }

                                DatabaseReference player = FirebaseDatabase.getInstance().getReference(game + "/matches/" + team1 + " VS " + team2 + " , " + series);
                                int finalPoints = inning4_batsman_point;
                                player.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        player.child("Team1").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                for (DataSnapshot player : snapshot.getChildren()) {
                                                    if (playerName.equals(player.child("player_name").getValue())) {
                                                        player.getRef().child("inning4_batsman_point").setValue(finalPoints);
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                        player.child("Team2").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                for (DataSnapshot player : snapshot.getChildren()) {
                                                    if (playerName.equals(player.child("player_name").getValue())) {
                                                        player.getRef().child("inning4_batsman_point").setValue(finalPoints);
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    dref.child("inning4/bowler_stats").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot playerstats : snapshot.getChildren()) {
                                String playerName = playerstats.child("playerName").getValue(String.class);
                                Integer economy = playerstats.child("economy").getValue(Integer.class);
                                Integer maindens = playerstats.child("maindens").getValue(Integer.class);
                                Integer overBowled = playerstats.child("overBowled").getValue(Integer.class);
                                Integer runsGiven = playerstats.child("runsGiven").getValue(Integer.class);
                                Integer wicketsTaken = playerstats.child("wicketsTaken").getValue(Integer.class);

                                int inning4_bowler_point = 0;
                                if (maindens!=null) {
                                    inning4_bowler_point = inning4_bowler_point + (maindens * 12);
                                }
                                if (runsGiven!=null) {
                                    inning4_bowler_point = inning4_bowler_point + runsGiven;
                                }
                                if (wicketsTaken!=null) {
                                    inning4_bowler_point = inning4_bowler_point + (wicketsTaken * 25);
                                    if (wicketsTaken > 3) {
                                        inning4_bowler_point = inning4_bowler_point + 4;
                                    } else if (wicketsTaken > 4) {
                                        inning4_bowler_point = inning4_bowler_point + 8;
                                    } else if (wicketsTaken > 5) {
                                        inning4_bowler_point = inning4_bowler_point + 16;
                                    }
                                }
                                if (economy!=null) {
                                    if (economy > 12) {
                                        inning4_bowler_point = inning4_bowler_point - 6;
                                    } else if (11.01f < economy && economy < 12f) {
                                        inning4_bowler_point = inning4_bowler_point - 4;
                                    } else if (10f < economy && economy < 11f) {
                                        inning4_bowler_point = inning4_bowler_point - 2;
                                    } else if (6f < economy && economy < 7f) {
                                        inning4_bowler_point = inning4_bowler_point + 2;
                                    } else if (5f < economy && economy < 5.99f) {
                                        inning4_bowler_point = inning4_bowler_point + 4;
                                    } else if (economy < 5f) {
                                        inning4_bowler_point = inning4_bowler_point + 6;
                                    }
                                }

                                DatabaseReference player = FirebaseDatabase.getInstance().getReference(game + "/matches/" + team1 + " VS " + team2 + " , " + series);
                                int finalPoints = inning4_bowler_point;
                                player.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        player.child("Team1").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                for (DataSnapshot player : snapshot.getChildren()) {
                                                    if (playerName.equals(player.child("player_name").getValue())) {
                                                        player.getRef().child("inning4_bowler_point").setValue(finalPoints);
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                        player.child("Team2").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                for (DataSnapshot player : snapshot.getChildren()) {
                                                    if (playerName.equals(player.child("player_name").getValue())) {
                                                        player.getRef().child("inning4_bowler_point").setValue(finalPoints);
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            DatabaseReference pointref = FirebaseDatabase.getInstance().getReference(game + "/matches/" + team1 + " VS " + team2 + " , " + series);
            pointref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    pointref.child("Team1").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Integer inning1_batsman_point = dataSnapshot.child("inning1_batsman_point").getValue(Integer.class);
                                Integer inning2_batsman_point = dataSnapshot.child("inning2_batsman_point").getValue(Integer.class);
                                Integer inning3_batsman_point = dataSnapshot.child("inning3_batsman_point").getValue(Integer.class);
                                Integer inning4_batsman_point = dataSnapshot.child("inning4_batsman_point").getValue(Integer.class);
                                Integer inning1_bowler_point = dataSnapshot.child("inning1_bowler_point").getValue(Integer.class);
                                Integer inning2_bowler_point = dataSnapshot.child("inning2_bowler_point").getValue(Integer.class);
                                Integer inning3_bowler_point = dataSnapshot.child("inning3_bowler_point").getValue(Integer.class);
                                Integer inning4_bowler_point = dataSnapshot.child("inning4_bowler_point").getValue(Integer.class);

                                int points = 0;

                                if (inning1_batsman_point != null) {
                                    points += inning1_batsman_point;
                                }
                                if (inning2_batsman_point != null) {
                                    points += inning2_batsman_point;
                                }
                                if (inning3_batsman_point != null) {
                                    points += inning3_batsman_point;
                                }
                                if (inning4_batsman_point != null) {
                                    points += inning4_batsman_point;
                                }
                                if (inning1_bowler_point != null) {
                                    points += inning1_bowler_point;
                                }
                                if (inning2_bowler_point != null) {
                                    points += inning2_bowler_point;
                                }
                                if (inning3_bowler_point != null) {
                                    points += inning3_bowler_point;
                                }
                                if (inning4_bowler_point != null) {
                                    points += inning4_bowler_point;
                                }

                                dataSnapshot.getRef().child("points").setValue(points);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    pointref.child("Team2").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Integer inning1_batsman_point = dataSnapshot.child("inning1_batsman_point").getValue(Integer.class);
                                Integer inning2_batsman_point = dataSnapshot.child("inning2_batsman_point").getValue(Integer.class);
                                Integer inning3_batsman_point = dataSnapshot.child("inning3_batsman_point").getValue(Integer.class);
                                Integer inning4_batsman_point = dataSnapshot.child("inning4_batsman_point").getValue(Integer.class);
                                Integer inning1_bowler_point = dataSnapshot.child("inning1_bowler_point").getValue(Integer.class);
                                Integer inning2_bowler_point = dataSnapshot.child("inning2_bowler_point").getValue(Integer.class);
                                Integer inning3_bowler_point = dataSnapshot.child("inning3_bowler_point").getValue(Integer.class);
                                Integer inning4_bowler_point = dataSnapshot.child("inning4_bowler_point").getValue(Integer.class);

                                int points = 0;

                                if (inning1_batsman_point != null) {
                                    points += inning1_batsman_point;
                                }
                                if (inning2_batsman_point != null) {
                                    points += inning2_batsman_point;
                                }
                                if (inning3_batsman_point != null) {
                                    points += inning3_batsman_point;
                                }
                                if (inning4_batsman_point != null) {
                                    points += inning4_batsman_point;
                                }
                                if (inning1_bowler_point != null) {
                                    points += inning1_bowler_point;
                                }
                                if (inning2_bowler_point != null) {
                                    points += inning2_bowler_point;
                                }
                                if (inning3_bowler_point != null) {
                                    points += inning3_bowler_point;
                                }
                                if (inning4_bowler_point != null) {
                                    points += inning4_bowler_point;
                                }

                                dataSnapshot.getRef().child("points").setValue(points);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else if (game.equals("football")) {
            dref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    dref.child("stats").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot playerstats : snapshot.getChildren()) {
                                String playerName = playerstats.getKey();
                                Integer goals = playerstats.child("goals").getValue(Integer.class);
                                Integer own_goals = playerstats.child("own_goals").getValue(Integer.class);
                                Integer penalty_waste = playerstats.child("penalty_waste").getValue(Integer.class);
                                Integer penalty_saved = playerstats.child("penalty_saved").getValue(Integer.class);
                                Integer assists = playerstats.child("assists").getValue(Integer.class);
                                Integer yellowcards = playerstats.child("yellowcards").getValue(Integer.class);
                                Integer redcards = playerstats.child("redcards").getValue(Integer.class);
                                Integer passes = playerstats.child("passes").getValue(Integer.class);
                                Integer tackles = playerstats.child("tackles").getValue(Integer.class);

                                int points = 0;
                                if (goals!=null) {
                                    points = points + (goals * 40);
                                }
                                if (own_goals!=null) {
                                    points = points - (own_goals * 8);
                                }
                                if (penalty_waste!=null) {
                                    points = points - (penalty_waste * 20);
                                }
                                if (penalty_saved!=null) {
                                    points = points + (penalty_saved * 50);
                                }
                                if (assists!=null) {
                                    points = points + (assists * 20);
                                }
                                if (yellowcards!=null) {
                                    points = points - (yellowcards * 4);
                                }
                                if (redcards!=null) {
                                    points = points - (redcards * 10);
                                }
                                if (passes!=null) {
                                    int total_pass = passes / 5;
                                    points = points + total_pass;
                                }
                                if (tackles!=null) {
                                    points = points + (tackles * 4);
                                }


                                DatabaseReference player = FirebaseDatabase.getInstance().getReference(game + "/matches/" + team1 + " VS " + team2 + " , " + series);
                                int finalPoints = points;
                                player.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        player.child("Team1").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                for (DataSnapshot player : snapshot.getChildren()) {
                                                    if (playerName.equals(player.child("player_name").getValue())) {
                                                        player.getRef().child("points").setValue(finalPoints);
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                        player.child("Team2").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                for (DataSnapshot player : snapshot.getChildren()) {
                                                    if (playerName.equals(player.child("player_name").getValue())) {
                                                        player.getRef().child("points").setValue(finalPoints);
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else if (game.equals("kabaddi")) {
            dref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    dref.child("stats").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot playerstats : snapshot.getChildren()) {
                                String playerName = playerstats.child("playerName").getValue(String.class);
                                Integer bonus_point = playerstats.child("bonus_points").getValue(Integer.class);
                                Integer raid_points = playerstats.child("raid_points").getValue(Integer.class);
                                Integer super_tackles = playerstats.child("super_tackles").getValue(Integer.class);
                                Integer tackle_points = playerstats.child("tackle_points").getValue(Integer.class);
                                Integer yellowcards = playerstats.child("yellowcards").getValue(Integer.class);
                                Integer redcards = playerstats.child("redcards").getValue(Integer.class);
                                Integer greencards = playerstats.child("greencards").getValue(Integer.class);
                                int points = 0;
                                if (bonus_point!=null) {
                                    points = points + (bonus_point * 2);
                                }
                                if (raid_points!=null) {
                                    points = points + (raid_points * 8);
                                }
                                if (super_tackles!=null) {
                                    points = points + (super_tackles * 8);
                                }
                                if (tackle_points!=null) {
                                    points = points + (tackle_points * 20);
                                }
                                if (yellowcards!=null) {
                                    points = points - (yellowcards * 4);
                                }
                                if (redcards!=null) {
                                    points = points - (redcards * 6);
                                }
                                if (greencards!=null) {
                                    points = points - (greencards * 2);
                                }
                                DatabaseReference player = FirebaseDatabase.getInstance().getReference(game + "/matches/" + team1 + " VS " + team2 + " , " + series);
                                int finalPoints = points;
                                player.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        player.child("Team1").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                for (DataSnapshot player : snapshot.getChildren()) {
                                                    if (playerName.equals(player.child("player_name").getValue())) {
                                                        player.getRef().child("points").setValue(finalPoints);
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                        player.child("Team2").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                for (DataSnapshot player : snapshot.getChildren()) {
                                                    if (playerName.equals(player.child("player_name").getValue())) {
                                                        player.getRef().child("points").setValue(finalPoints);
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void update_adapter() {
        DatabaseReference dref = FirebaseDatabase.getInstance().getReference(game + "/matches/" + team1 + " VS " + team2 + " , " + series);
        dref.child("Team1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                playerArrayList.clear();
                playerPointList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Player player = dataSnapshot.getValue(Player.class);
                    Integer points = dataSnapshot.child("points").getValue(Integer.class);
                    if (points!=null) {
                        playerPointList.add(points);
                    } else {
                        points = 0;
                        playerPointList.add(points);
                    }
                    playerArrayList.add(player);
                }
                notifyandArrangeAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        dref.child("Team2").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Player player = dataSnapshot.getValue(Player.class);
                    Integer points = dataSnapshot.child("points").getValue(Integer.class);
                    if (points!=null) {
                        playerPointList.add(points);
                    } else {
                        points = 0;
                        playerPointList.add(points);
                    }
                    playerArrayList.add(player);
                }

                notifyandArrangeAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void notifyandArrangeAdapter() {
        List<Pair<Player, Integer>> playerPointPairs = new ArrayList<>();
        for (int i = 0; i < playerArrayList.size(); i++) {
            playerPointPairs.add(new Pair<>(playerArrayList.get(i), playerPointList.get(i)));
        }
        Collections.sort(playerPointPairs, new Comparator<Pair<Player, Integer>>() {
            @Override
            public int compare(Pair<Player, Integer> pair1, Pair<Player, Integer> pair2) {
                return Integer.compare(pair2.second, pair1.second);
            }
        });

        playerArrayList.clear();
        playerPointList.clear();

        for (Pair<Player, Integer> pair : playerPointPairs) {
            playerArrayList.add(pair.first);
            playerPointList.add(pair.second);
        }

        statsAdapter.notifyDataSetChanged();
    }
}