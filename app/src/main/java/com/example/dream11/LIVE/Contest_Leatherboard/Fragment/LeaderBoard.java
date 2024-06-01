package com.example.dream11.LIVE.Contest_Leatherboard.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dream11.Adapters.LeaderboardAdapter;
import com.example.dream11.PropertyClasses.Player;
import com.example.dream11.PropertyClasses.User;
import com.example.dream11.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class LeaderBoard extends Fragment implements LeaderboardAdapter.OnItemClickListener{


    String team1, team2, date, status, series, game, contest_id;
    DatabaseReference databaseReference;
    LeaderboardAdapter adapter;
    private ArrayList<User> participantList = new ArrayList<>();
    private ArrayList<Float> participantPointList = new ArrayList<>();
    SwipeRefreshLayout swipeRefreshLayout;
    public LeaderBoard() {
        // Required empty public constructor
    }


    public static LeaderBoard newInstance(String param1, String param2) {
        LeaderBoard fragment = new LeaderBoard();
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
        contest_id = getActivity().getIntent().getStringExtra("contest_key");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leader_board, container, false);

        databaseReference = FirebaseDatabase.getInstance().getReference(game + "/matches/" + team1 + " VS " + team2 + " , " + series + "/contest/" + contest_id + "/participant");
        RecyclerView recyclerview = view.findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new LeaderboardAdapter(participantList, participantPointList, this);
        recyclerview.setAdapter(adapter);

        calculatePoints();
        retrieveParticipants();

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                calculatePoints();
                retrieveParticipants();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return view;
    }

    private void calculatePoints() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot participantSnapshot : snapshot.getChildren()) {
                    String contestId = participantSnapshot.getKey();
                    String uid = participantSnapshot.child("uid").getValue(String.class);

                    Integer captainIndex = participantSnapshot.child("user_team").child("captain_position").getValue(Integer.class);
                    Log.d("captainIndex", String.valueOf(captainIndex));
                    Integer viceCaptainIndex = participantSnapshot.child("user_team").child("vicecaptain_position").getValue(Integer.class);
                    Log.d("viceCaptainIndex", String.valueOf(viceCaptainIndex));


                    List<Float> player_point_list = new ArrayList<>();
                    String captainName = participantSnapshot.child("user_team").child("players").child(String.valueOf(captainIndex)).child("player_name").getValue(String.class);
                    String viceCaptainName = participantSnapshot.child("user_team").child("players").child(String.valueOf(viceCaptainIndex)).child("player_name").getValue(String.class);

                    databaseReference.child(contestId).child("user_team").child("players").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                String playerName = dataSnapshot.child("player_name").getValue(String.class);

                                DatabaseReference teamref = FirebaseDatabase.getInstance().getReference(game + "/matches/" + team1 + " VS " + team2 + " , " + series);
                                teamref.child("Team1").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot teamplayer : snapshot.getChildren()) {
                                            if (playerName.equals(teamplayer.child("player_name").getValue(String.class))) {
                                                Float points = teamplayer.child("points").getValue(Float.class);
                                                if (playerName.equals(captainName)) {
                                                    if (points!=null) {
                                                        float twoX_points = points * 2f;
                                                        player_point_list.add(twoX_points);
                                                        Log.d("POINTS", "POINTS: "+ points);
                                                        Log.d("POINTS", "player_point_list: "+ player_point_list);
                                                    }
                                                } else if (playerName.equals(viceCaptainName)) {
                                                    if (points!=null) {
                                                        float one_half_points = points * 1.5f;
                                                        player_point_list.add(one_half_points);
                                                        Log.d("POINTS", "POINTS: "+ points);
                                                        Log.d("POINTS", "player_point_list: "+ player_point_list);
                                                    }
                                                } else {
                                                    if (points!=null) {
                                                        player_point_list.add(points);
                                                        Log.d("POINTS", "POINTS: "+ points);
                                                        Log.d("POINTS", "player_point_list: "+ player_point_list);
                                                    }
                                                }
                                            }
                                        }
                                        float sum = 0;
                                        for (float num : player_point_list) {
                                            sum += num;
                                        }
                                        Log.d("LIST ADDED", "LISTPOINTS: "+ sum);
                                        participantSnapshot.getRef().child("points").setValue(sum);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                                teamref.child("Team2").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot teamplayer : snapshot.getChildren()) {
                                            if (playerName.equals(teamplayer.child("player_name").getValue(String.class))) {
                                                Float points = teamplayer.child("points").getValue(Float.class);
                                                if (playerName.equals(captainName)) {
                                                    if (points!=null) {
                                                        float twoX_points = points * 2f;
                                                        player_point_list.add(twoX_points);
                                                        Log.d("POINTS", "POINTS: "+ points);
                                                        Log.d("POINTS", "player_point_list: "+ player_point_list);
                                                    }
                                                } else if (playerName.equals(viceCaptainName)) {
                                                    if (points!=null) {
                                                        float one_half_points = points * 1.5f;
                                                        player_point_list.add(one_half_points);
                                                        Log.d("POINTS", "POINTS: "+ points);
                                                        Log.d("POINTS", "player_point_list: "+ player_point_list);
                                                    }
                                                } else {
                                                    if (points!=null) {
                                                        player_point_list.add(points);
                                                        Log.d("POINTS", "POINTS: "+ points);
                                                        Log.d("POINTS", "player_point_list: "+ player_point_list);
                                                    }
                                                }
                                            }
                                        }
                                        float sum = 0;
                                        for (float num : player_point_list) {
                                            sum += num;
                                        }
                                        Log.d("LIST ADDED", "LISTPOINTS: "+ sum);
                                        participantSnapshot.getRef().child("points").setValue(sum);
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void retrieveParticipants() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                participantList.clear();
                participantPointList.clear();
                for (DataSnapshot participantSnapshot : snapshot.getChildren()) {
                    String uid_user = participantSnapshot.child("uid").getValue(String.class);
                    Float points = participantSnapshot.child("points").getValue(Float.class);
                    DatabaseReference userref = FirebaseDatabase.getInstance().getReference("users");
                    userref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot user : snapshot.getChildren()) {
                                User user_detail = user.getValue(User.class);
                                if (uid_user.equals(user.getKey())) {
                                    participantList.add(user_detail);
                                    if (points!=null) {
                                        participantPointList.add(points);
                                    } else {
                                        participantPointList.add(0f);
                                    }
                                }
                            }
                            notifyandArrangeAdapter();
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

    private void notifyandArrangeAdapter() {
        List<Pair<User, Float>> playerPointPairs = new ArrayList<>();
        for (int i = 0; i < participantList.size(); i++) {
            playerPointPairs.add(new Pair<>(participantList.get(i), participantPointList.get(i)));
        }
        Collections.sort(playerPointPairs, new Comparator<Pair<User, Float>>() {
            @Override
            public int compare(Pair<User, Float> pair1, Pair<User, Float> pair2) {
                return Float.compare(pair2.second, pair1.second);
            }
        });

        participantList.clear();
        participantPointList.clear();

        for (Pair<User, Float> pair : playerPointPairs) {
            participantList.add(pair.first);
            participantPointList.add(pair.second);
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(User user) {

    }
}