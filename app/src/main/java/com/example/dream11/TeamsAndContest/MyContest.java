package com.example.dream11.TeamsAndContest;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dream11.Adapters.ContestAdapter;

import com.example.dream11.LIVE.Contest_Leatherboard.Contest_Live_Score;
import com.example.dream11.PropertyClasses.ContestClass;

import com.example.dream11.PropertyClasses.Rule;
import com.example.dream11.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyContest#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyContest extends Fragment implements ContestAdapter.OnItemClickListener{

    private String team1, team2, date, status, series, game;
    private ArrayList<ContestClass> contestList = new ArrayList<>();
    private ArrayList<Rule> ruleList = new ArrayList<>();
    private ArrayList<String> contestIDs = new ArrayList<>();
    DatabaseReference databaseReference;
    FirebaseAuth auth;
    ContestAdapter adapter;
    public MyContest() {
    }

    public MyContest(String team1, String team2, String date, String status, String series) {
        this.team1 = team1;
        this.team2 = team2;
        this.date = date;
        this.status = status;
        this.series = series;
    }

    public static MyContest newInstance(String param1, String param2) {
        MyContest fragment = new MyContest();
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
        View view = inflater.inflate(R.layout.fragment_my_contest, container, false);

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference(game + "/matches/" + team1 + " VS " + team2 + " , " + series + "/contest");

        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ContestAdapter(contestIDs, contestList, ruleList, this);
        recyclerView.setAdapter(adapter);

        retrieve_contest();
        adapter.notifyDataSetChanged();

        return view;
    }

    private void retrieve_contest() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                contestList.clear();
                for (DataSnapshot contest : snapshot.getChildren()) {
                    boolean add_contest = false;
                    for (DataSnapshot participant : contest.child("participant").getChildren()) {
                        String uid = participant.child("uid").getValue(String.class);
                        if (uid != null && uid.equals(auth.getCurrentUser().getUid())) {
                            add_contest = true;
                            break;
                        }
                    }
                    if (add_contest) {
                        ContestClass contestClass = contest.getValue(ContestClass.class);
                        Rule rules = contest.child("rules").getValue(Rule.class);

                        contestList.add(contestClass);
                        ruleList.add(rules);
                        contestIDs.add(contest.getKey());
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onItemClick(String contestkey, ContestClass contestClass, Rule contestRule) {
        Intent intent = new Intent(getContext(), Contest_Live_Score.class);
        intent.putExtra("team1", team1);
        intent.putExtra("team2", team2);
        intent.putExtra("date", date);
        intent.putExtra("status", status);
        intent.putExtra("series", series);
        intent.putExtra("game", game);
        intent.putExtra("contest_key", contestkey);
        startActivity(intent);
    }
}
