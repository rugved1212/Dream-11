package com.example.dream11.LIVE.Contest_Leatherboard.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.dream11.Adapters.Rank_ListView_Adapter;
import com.example.dream11.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Winnings extends Fragment {

    private String team1, team2, date, status, series, game, contest_key, contest_name;
    int limit, participant_join, entry_fee;
    private List<Map<String, String>> data = new ArrayList<>();
    Rank_ListView_Adapter adapter;

    public Winnings() {
    }


    public static Winnings newInstance(String param1, String param2) {
        Winnings fragment = new Winnings();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_winnings, container, false);

        team1 = getActivity().getIntent().getStringExtra("team1");
        team2 = getActivity().getIntent().getStringExtra("team2");
        date = getActivity().getIntent().getStringExtra("date");
        status = getActivity().getIntent().getStringExtra("status");
        series = getActivity().getIntent().getStringExtra("series");
        game = getActivity().getIntent().getStringExtra("game");
        contest_key = getActivity().getIntent().getStringExtra("contest_key");

        check_participant();

        ListView listView = view.findViewById(R.id.listview);

        adapter = new Rank_ListView_Adapter(getContext(), data);
        listView.setAdapter(adapter);

        return view;
    }

    private void check_participant() {
        DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference(game + "/matches/" + team1 + " VS " + team2 + " , " + series + "/contest/" + contest_key);
        dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                entry_fee = snapshot.child("entry").getValue(Integer.class);
                limit = snapshot.child("rules").child("limit").getValue(Integer.class);
                contest_name = snapshot.child("rules").child("contest_name").getValue(String.class);
                participant_join = (int) snapshot.child("participant").getChildrenCount();

                Log.d("LIMIT", "LIMIT: "+limit);
                Log.d("CONTEST_NAME", "NAME: "+contest_name);
                Log.d("PARTICIPANT_COUNT", "COUNT: "+participant_join);

                if (limit == participant_join) {
                    switch (contest_name) {
                        case "Mega Contest with Prize-Pool: 2.5 Lakhs and Entry: 49":
                            Map<String, String> mg1_1 = new HashMap<>();
                            mg1_1.put("rank", "1");
                            mg1_1.put("prize", "25000");
                            data.add(mg1_1);
                            Map<String, String> mg1_2 = new HashMap<>();
                            mg1_2.put("rank", "2");
                            mg1_2.put("prize", "10000");
                            data.add(mg1_2);
                            Map<String, String> mg1_3 = new HashMap<>();
                            mg1_3.put("rank", "3-10");
                            mg1_3.put("prize", "5000");
                            data.add(mg1_3);
                            Map<String, String> mg1_4 = new HashMap<>();
                            mg1_4.put("rank", "11-50");
                            mg1_4.put("prize", "500");
                            data.add(mg1_4);
                            Map<String, String> mg1_5 = new HashMap<>();
                            mg1_5.put("rank", "51-550");
                            mg1_5.put("prize", "100");
                            data.add(mg1_5);
                            Map<String, String> mg1_6 = new HashMap<>();
                            mg1_6.put("rank", "551-1550");
                            mg1_6.put("prize", "50");
                            data.add(mg1_6);
                            Map<String, String> mg1_7 = new HashMap<>();
                            mg1_7.put("rank", "1551-3000");
                            mg1_7.put("prize", "30");
                            data.add(mg1_7);
                            break;
                        case "Mega Contest with Prize-Pool: 1 Lakhs and Entry: 25":
                            Map<String, String> mg2_1 = new HashMap<>();
                            mg2_1.put("rank", "1");
                            mg2_1.put("prize", "10000");
                            data.add(mg2_1);
                            Map<String, String> mg2_2 = new HashMap<>();
                            mg2_2.put("rank", "2");
                            mg2_2.put("prize", "5000");
                            data.add(mg2_2);
                            Map<String, String> mg2_3 = new HashMap<>();
                            mg2_3.put("rank", "3-10");
                            mg2_3.put("prize", "1000");
                            data.add(mg2_3);
                            Map<String, String> mg2_4 = new HashMap<>();
                            mg2_4.put("rank", "11-50");
                            mg2_4.put("prize", "500");
                            data.add(mg2_4);
                            Map<String, String> mg2_5 = new HashMap<>();
                            mg2_5.put("rank", "51-100");
                            mg2_5.put("prize", "100");
                            data.add(mg2_5);
                            Map<String, String> mg2_6 = new HashMap<>();
                            mg2_6.put("rank", "101-600");
                            mg2_6.put("prize", "50");
                            data.add(mg2_6);
                            Map<String, String> mg2_7 = new HashMap<>();
                            mg2_7.put("rank", "601-1500");
                            mg2_7.put("prize", "30");
                            data.add(mg2_7);
                            break;
                        case "Multiplier Contest with Prize-Pool: 20k and Entry: 19":
                            Map<String, String> mc1_1 = new HashMap<>();
                            mc1_1.put("rank", "1");
                            mc1_1.put("prize", "5000");
                            data.add(mc1_1);
                            Map<String, String> mc1_2 = new HashMap<>();
                            mc1_2.put("rank", "2");
                            mc1_2.put("prize", "1000");
                            data.add(mc1_2);
                            Map<String, String> mc1_3 = new HashMap<>();
                            mc1_3.put("rank", "3-5");
                            mc1_3.put("prize", "500");
                            data.add(mc1_3);
                            Map<String, String> mc1_4 = new HashMap<>();
                            mc1_4.put("rank", "6-10");
                            mc1_4.put("prize", "300");
                            data.add(mc1_4);
                            Map<String, String> mc1_5 = new HashMap<>();
                            mc1_5.put("rank", "11-20");
                            mc1_5.put("prize", "150");
                            data.add(mc1_5);
                            Map<String, String> mc1_6 = new HashMap<>();
                            mc1_6.put("rank", "21-210");
                            mc1_6.put("prize", "50");
                            data.add(mc1_6);
                            break;
                        case "Multiplier Contest with Prize-Pool: 10k and Entry: 15":
                            Map<String, String> mc2_1 = new HashMap<>();
                            mc2_1.put("rank", "1");
                            mc2_1.put("prize", "1000");
                            data.add(mc2_1);
                            Map<String, String> mc2_2 = new HashMap<>();
                            mc2_2.put("rank", "2");
                            mc2_2.put("prize", "500");
                            data.add(mc2_2);
                            Map<String, String> mc2_3 = new HashMap<>();
                            mc2_3.put("rank", "3-5");
                            mc2_3.put("prize", "200");
                            data.add(mc2_3);
                            Map<String, String> mc2_4 = new HashMap<>();
                            mc2_4.put("rank", "6-10");
                            mc2_4.put("prize", "100");
                            data.add(mc2_4);
                            Map<String, String> mc2_5 = new HashMap<>();
                            mc2_5.put("rank", "11-20");
                            mc2_5.put("prize", "70");
                            data.add(mc2_5);
                            Map<String, String> mc2_6 = new HashMap<>();
                            mc2_6.put("rank", "21-150");
                            mc2_6.put("prize", "50");
                            data.add(mc2_6);
                            break;
                        case "Meet Your Match Contest with Prize-Pool: 100 and Entry: 27":
                            Map<String, String> mum1 = new HashMap<>();
                            mum1.put("rank", "1");
                            mum1.put("prize", "100");
                            data.add(mum1);
                            break;
                        case "Meet Your Match Contest with Prize-Pool: 50 and Entry: 19":
                            Map<String, String> mum2 = new HashMap<>();
                            mum2.put("rank", "1");
                            mum2.put("prize", "50");
                            data.add(mum2);
                            break;
                        case "Head-To-Head Contest with Prize-Pool: 66 and Entry: 35":
                            Map<String, String> hth1 = new HashMap<>();
                            hth1.put("rank", "1");
                            hth1.put("prize", "66");
                            data.add(hth1);
                            break;
                        case "Head-To-Head Contest with Prize-Pool: 35 and Entry: 20":
                            Map<String, String> hth2 = new HashMap<>();
                            hth2.put("rank", "1");
                            hth2.put("prize", "35");
                            data.add(hth2);
                            break;
                        default:
                            break;
                    }
                } else if (participant_join < 10) {
                    int prize = entry_fee * participant_join;
                    Map<String, String> hth = new HashMap<>();
                    hth.put("rank", "1");
                    hth.put("prize", String.valueOf(prize));
                    data.add(hth);
                } else if (participant_join > 10 && participant_join < limit) {
                    int numPrizeWinner = (int) Math.round(participant_join * 0.20);
                    int prizePool = participant_join * entry_fee;

                    int firstPrize = prizePool * 30/100;
                    int secondPrize = prizePool * 20/100;
                    int thirdPrize = prizePool * 10/100;

                    Map<String, String> first = new HashMap<>();
                    first.put("rank", "1");
                    first.put("prize", String.valueOf(firstPrize));
                    data.add(first);
                    Map<String, String> second = new HashMap<>();
                    second.put("rank", "2");
                    second.put("prize", String.valueOf(secondPrize));
                    data.add(second);
                    Map<String, String> third = new HashMap<>();
                    third.put("rank", "3");
                    third.put("prize", String.valueOf(thirdPrize));
                    data.add(third);

                    int remainingPrizePool = prizePool - (firstPrize + secondPrize + thirdPrize);
                    int numOtherWinners = numPrizeWinner - 3;
                    int otherPrize = remainingPrizePool / numOtherWinners;
                    for (int i = 4; i <= numPrizeWinner; i++) {
                        Map<String, String> player = new HashMap<>();
                        player.put("rank", String.valueOf(i));
                        player.put("prize", String.valueOf(otherPrize));
                        data.add(player);
                    }
                }
                adapter.notifyDataSetChanged();  
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}