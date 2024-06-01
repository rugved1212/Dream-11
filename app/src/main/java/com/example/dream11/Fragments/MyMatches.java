package com.example.dream11.Fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.dream11.Adapters.MatchAdapter;
import com.example.dream11.LIVE.Match_Live_Score;
import com.example.dream11.PropertyClasses.Match;
import com.example.dream11.R;
import com.example.dream11.TeamsAndContest.TeamAndContest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;


public class MyMatches extends Fragment implements MatchAdapter.OnItemClickListener {

    private ArrayList<Match> matchesList = new ArrayList<>();
    String game;
    public MyMatches() {
        // Required empty public constructor
    }


    public static MyMatches newInstance(String param1, String param2) {
        MyMatches fragment = new MyMatches();
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
        View view = inflater.inflate(R.layout.fragment_my_matches, container, false);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference cricReference = FirebaseDatabase.getInstance().getReference("cricket/matches");
        DatabaseReference footReference = FirebaseDatabase.getInstance().getReference("football/matches");
        DatabaseReference kabadReference = FirebaseDatabase.getInstance().getReference("kabaddi/matches");

        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        MatchAdapter adapter = new MatchAdapter(matchesList, this);
        recyclerView.setAdapter(adapter);
        game = "cricket";

        cricReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                matchesList.clear();
                long currentTimeMillis = System.currentTimeMillis();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Match match = dataSnapshot.getValue(Match.class);
                    boolean add_match = false;
                    for (DataSnapshot user_join : dataSnapshot.child("contest").getChildren()) {
                        for (DataSnapshot user : user_join.child("participant").getChildren()) {
                            String uid = user.child("uid").getValue(String.class);
                            if (uid != null && uid.equals(auth.getCurrentUser().getUid())) {
                                add_match = true;
                            }
                        }
                    }
                    if (add_match) {
                        matchesList.add(match);
                    }
                }
                adapter.setMatchesList(sortMatches());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ImageView cricket = view.findViewById(R.id.cricket);
        ImageView football = view.findViewById(R.id.football);
        ImageView kabaddi = view.findViewById(R.id.kabaddi);

        cricket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game = "cricket";

                cricket.setPadding(18,18,18,18);
                cricket.setBackgroundResource(R.drawable.mymatches_drawable_selection);
                football.setPadding(28,28,28,28);
                football.setBackgroundResource(R.drawable.mymatches_drawable_deselection);
                kabaddi.setPadding(28,28,28,28);
                kabaddi.setBackgroundResource(R.drawable.mymatches_drawable_deselection);
                cricket.animate().alpha(1f).setDuration(100);
                football.animate().alpha(0.5f).setDuration(100);
                kabaddi.animate().alpha(0.5f).setDuration(100);

                cricReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        matchesList.clear();
                        long currentTimeMillis = System.currentTimeMillis();

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Match match = dataSnapshot.getValue(Match.class);
                            boolean add_match = false;
                            for (DataSnapshot user_join : dataSnapshot.child("contest").getChildren()) {
                                for (DataSnapshot user : user_join.child("participant").getChildren()) {
                                    String uid = user.child("uid").getValue(String.class);
                                    if (uid != null && uid.equals(auth.getCurrentUser().getUid())) {
                                        add_match = true;
                                    }
                                }
                            }
                            if (add_match) {
                                matchesList.add(match);
                            }
                        }
                        adapter.setMatchesList(sortMatches());
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        football.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game = "football";

                football.setPadding(18,18,18,18);
                football.setBackgroundResource(R.drawable.mymatches_drawable_selection);
                cricket.setPadding(28,28,28,28);
                cricket.setBackgroundResource(R.drawable.mymatches_drawable_deselection);
                kabaddi.setPadding(28,28,28,28);
                kabaddi.setBackgroundResource(R.drawable.mymatches_drawable_deselection);
                cricket.animate().alpha(0.5f).setDuration(100);
                football.animate().alpha(1f).setDuration(100);
                kabaddi.animate().alpha(0.5f).setDuration(100);


                footReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        matchesList.clear();
                        long currentTimeMillis = System.currentTimeMillis();

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Match match = dataSnapshot.getValue(Match.class);
                            boolean add_match = false;
                            for (DataSnapshot user_join : dataSnapshot.child("contest").getChildren()) {
                                for (DataSnapshot user : user_join.child("participant").getChildren()) {
                                    String uid = user.child("uid").getValue(String.class);
                                    if (uid != null && uid.equals(auth.getCurrentUser().getUid())) {
                                        add_match = true;
                                    }
                                }
                            }
                            if (add_match) {
                                matchesList.add(match);
                            }
                        }
                        adapter.setMatchesList(sortMatches());
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        kabaddi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game = "kabaddi";

                kabaddi.setPadding(18,18,18,18);
                kabaddi.setBackgroundResource(R.drawable.mymatches_drawable_selection);
                football.setPadding(28,28,28,28);
                football.setBackgroundResource(R.drawable.mymatches_drawable_deselection);
                cricket.setPadding(28,28,28,28);
                cricket.setBackgroundResource(R.drawable.mymatches_drawable_deselection);
                cricket.animate().alpha(0.5f).setDuration(100);
                football.animate().alpha(0.5f).setDuration(100);
                kabaddi.animate().alpha(1f).setDuration(100);


                kabadReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        matchesList.clear();
                        long currentTimeMillis = System.currentTimeMillis();

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Match match = dataSnapshot.getValue(Match.class);
                            boolean add_match = false;
                            for (DataSnapshot user_join : dataSnapshot.child("contest").getChildren()) {
                                for (DataSnapshot user : user_join.child("participant").getChildren()) {
                                    String uid = user.child("uid").getValue(String.class);
                                    if (uid != null && uid.equals(auth.getCurrentUser().getUid())) {
                                        add_match = true;
                                    }
                                }
                            }
                            if (add_match) {
                                matchesList.add(match);
                            }
                        }
                        adapter.setMatchesList(sortMatches());
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        return view;
    }

    private ArrayList<Match> sortMatches() {
        ArrayList<Match> sortList = new ArrayList<>(matchesList);
        Collections.sort(sortList, new Comparator<Match>() {
            @Override
            public int compare(Match match1, Match match2) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                try {
                    Date date1 = sdf.parse(match1.getDate());
                    Date date2 = sdf.parse(match2.getDate());
                    return date1.compareTo(date2);
                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        });
        return  sortList;
    }

    @Override
    public void onItemClick(Match match) {
        if (isMatchInFuture(match)) {
            Intent intent = new Intent(getContext(), TeamAndContest.class);
            intent.putExtra("team1", match.team1);
            intent.putExtra("team2", match.team2);
            intent.putExtra("date", match.date);
            intent.putExtra("status", match.status);
            intent.putExtra("series", match.series);
            intent.putExtra("game", game);
            startActivity(intent);
        } else {
            Intent intent = new Intent(getContext(), Match_Live_Score.class);
            intent.putExtra("team1", match.team1);
            intent.putExtra("team2", match.team2);
            intent.putExtra("date", match.date);
            intent.putExtra("status", match.status);
            intent.putExtra("series", match.series);
            intent.putExtra("game", game);
            startActivity(intent);
        }
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
}