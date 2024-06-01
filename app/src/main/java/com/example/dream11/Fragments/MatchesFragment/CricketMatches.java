package com.example.dream11.Fragments.MatchesFragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dream11.Adapters.MatchAdapter;
import com.example.dream11.LIVE.Match_Live_Score;
import com.example.dream11.PropertyClasses.Match;
import com.example.dream11.R;
import com.example.dream11.TeamsAndContest.TeamAndContest;
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


public class CricketMatches extends Fragment implements MatchAdapter.OnItemClickListener{

    private ArrayList<Match> matchesList = new ArrayList<>();

    public CricketMatches() {
        // Required empty public constructor
    }


    public static CricketMatches newInstance(String param1, String param2) {
        CricketMatches fragment = new CricketMatches();
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
        View view = inflater.inflate(R.layout.fragment_cricket_matches, container, false);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("cricket/matches");
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        MatchAdapter adapter = new MatchAdapter(matchesList, this);
        recyclerView.setAdapter(adapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                matchesList.clear();
                long currentTimeMillis = System.currentTimeMillis();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Match match = dataSnapshot.getValue(Match.class);
                    if (isMatchInFuture(match)) {
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

        return view;
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

    @Override
    public void onItemClick(Match match) {
        if (isMatchInFuture(match)) {
            Intent intent = new Intent(getContext(), TeamAndContest.class);
            intent.putExtra("team1", match.team1);
            intent.putExtra("team2", match.team2);
            intent.putExtra("date", match.date);
            intent.putExtra("status", match.status);
            intent.putExtra("series", match.series);
            intent.putExtra("game", "cricket");
            startActivity(intent);
        } else {
            Intent intent = new Intent(getContext(), Match_Live_Score.class);
            intent.putExtra("team1", match.team1);
            intent.putExtra("team2", match.team2);
            intent.putExtra("date", match.date);
            intent.putExtra("status", match.status);
            intent.putExtra("series", match.series);
            intent.putExtra("game", "cricket");
            startActivity(intent);
        }
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
}