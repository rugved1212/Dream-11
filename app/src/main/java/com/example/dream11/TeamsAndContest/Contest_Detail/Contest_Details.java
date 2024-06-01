package com.example.dream11.TeamsAndContest.Contest_Detail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dream11.Adapters.Rank_ListView_Adapter;
import com.example.dream11.PropertyClasses.ContestModelClass;
import com.example.dream11.R;
import com.example.dream11.TeamsAndContest.SelectTeamForContest.SelectTeam;
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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class Contest_Details extends AppCompatActivity {

    private String team1, team2, date, status, series, game, contest_name;
    private Toolbar toolbar;
    private CountDownTimer countDown;
    private Date targetDate;
    int entry, limit, prizepool;
    boolean mega;
    private List<Map<String, String>> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contest_details);

        team1 = getIntent().getStringExtra("team1");
        team2 = getIntent().getStringExtra("team2");
        date = getIntent().getStringExtra("date");
        status = getIntent().getStringExtra("status");
        series = getIntent().getStringExtra("series");
        game = getIntent().getStringExtra("game");
        contest_name = getIntent().getStringExtra("contest_name");

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (team1!=null && team2!=null){
            toolbar.setTitle(teamName(team1) + " VS " + teamName(team2));
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        try {
            targetDate = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        startCountdownTimer();

        TextView prize_pool = findViewById(R.id.prize_pool);
        TextView slot_remain = findViewById(R.id.slot_remain);
        TextView limittv = findViewById(R.id.limit);
        FitButton join = findViewById(R.id.join_btn);
        ListView listView = findViewById(R.id.listview);


        switch (contest_name) {
            case "Mega Contest with Prize-Pool: 2.5 Lakhs and Entry: 49":
                prize_pool.setText("₹ 2.5 Lakhs");
                join.setText("JOIN ₹ 49");
                slot_remain.setText("3000 spots left");
                limittv.setText("6000 spots");
                entry = 49;
                prizepool = 250000;
                limit = 6000;
                mega = true;
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
                prize_pool.setText("₹1 Lakh");
                join.setText("JOIN ₹ 25");
                slot_remain.setText("2500 spots left");
                limittv.setText("5000 spots");
                entry = 25;
                prizepool = 100000;
                limit = 5000;
                mega = true;
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
                prize_pool.setText("₹20,000");
                join.setText("JOIN ₹ 19");
                slot_remain.setText("625 spots left");
                limittv.setText("1250 spots");
                entry = 19;
                prizepool = 20000;
                limit = 1250;
                mega = false;
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
                prize_pool.setText("₹10,000");
                join.setText("JOIN ₹ 15");
                slot_remain.setText("500 spots left");
                limittv.setText("1000 spots");
                entry = 15;
                prizepool = 10000;
                limit = 1000;
                mega = false;
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
                prize_pool.setText("₹100");
                join.setText("JOIN ₹ 27");
                slot_remain.setText("2 spots left");
                limittv.setText("4 spots");
                entry = 27;
                prizepool = 100;
                limit = 4;
                mega = false;
                Map<String, String> mum1 = new HashMap<>();
                mum1.put("rank", "1");
                mum1.put("prize", "100");
                data.add(mum1);
                break;
            case "Meet Your Match Contest with Prize-Pool: 50 and Entry: 19":
                prize_pool.setText("₹50");
                join.setText("JOIN ₹ 19");
                slot_remain.setText("2 spots left");
                limittv.setText("4 spots");
                entry = 19;
                prizepool = 50;
                limit = 4;
                mega = false;
                Map<String, String> mum2 = new HashMap<>();
                mum2.put("rank", "1");
                mum2.put("prize", "50");
                data.add(mum2);
                break;
            case "Head-To-Head Contest with Prize-Pool: 66 and Entry: 35":
                prize_pool.setText("₹66");
                join.setText("JOIN ₹ 35");
                slot_remain.setText("1 spots left");
                limittv.setText("2 spots");
                entry = 35;
                prizepool = 66;
                limit = 2;
                mega = false;
                Map<String, String> hth1 = new HashMap<>();
                hth1.put("rank", "1");
                hth1.put("prize", "66");
                data.add(hth1);
                break;
            case "Head-To-Head Contest with Prize-Pool: 35 and Entry: 20":
                prize_pool.setText("₹35");
                join.setText("JOIN ₹ 20");
                slot_remain.setText("1 spots left");
                limittv.setText("2 spots");
                entry = 20;
                prizepool = 35;
                limit = 2;
                mega = false;
                Map<String, String> hth2 = new HashMap<>();
                hth2.put("rank", "1");
                hth2.put("prize", "35");
                data.add(hth2);
                break;
            default:
                prize_pool.setText("");
                join.setText("JOIN");
                slot_remain.setText("");
                limittv.setText("");
                entry = 0;
                prizepool = 0;
                limit = 0;
                mega = false;
                break;
        }

        Rank_ListView_Adapter adapter = new Rank_ListView_Adapter(this, data);
        listView.setAdapter(adapter);

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mega) {
                    join_mega_contest(contest_name, entry, prizepool, limit);
                } else {
                    join_normal_contest(contest_name, entry, prizepool, limit);
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

    private void join_mega_contest(String contest_name, int entry, int prize_pool, int limit) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference userteam = FirebaseDatabase.getInstance().getReference("users/"+ auth.getCurrentUser().getUid()+"/teams/"+game).child(team1 + " VS " + team2 + " , " + series);
        DatabaseReference dref = FirebaseDatabase.getInstance().getReference(game + "/matches/" + team1 + " VS " + team2 + " , " + series + "/contest");

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

                                Intent intent = new Intent(Contest_Details.this, SelectTeam.class);
                                intent.putExtra("team1", team1);
                                intent.putExtra("team2", team2);
                                intent.putExtra("date", date);
                                intent.putExtra("status", status);
                                intent.putExtra("series", series);
                                intent.putExtra("game", game);
                                intent.putStringArrayListExtra("contest_list", (ArrayList<String>) contestIds);
                                startActivity(intent);

                            } else if (teamCount == 1){

                                String key = dref.child(id).child("participant").push().getKey();
                                HashMap<String, Object> participantMap = new HashMap<>();
                                participantMap.put("uid", auth.getCurrentUser().getUid());
                                participantMap.put("user_team", snapshot.getValue());
                                dref.child(id).child("participant").child(key).setValue(participantMap);

                            } else if (teamCount == 0) {

                                Toast.makeText(Contest_Details.this, "PLEASE MAKE TEAM FIRST", Toast.LENGTH_SHORT).show();

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
    }

    private void join_normal_contest(String contest_name, int entry, int prize_pool, int limit) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference userteam = FirebaseDatabase.getInstance().getReference("users/"+ auth.getCurrentUser().getUid()+"/teams/"+game).child(team1 + " VS " + team2 + " , " + series);
        DatabaseReference dref = FirebaseDatabase.getInstance().getReference(game + "/matches/" + team1 + " VS " + team2 + " , " + series + "/contest");

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

                                Intent intent = new Intent(Contest_Details.this, SelectTeam.class);
                                intent.putExtra("team1", team1);
                                intent.putExtra("team2", team2);
                                intent.putExtra("date", date);
                                intent.putExtra("status", status);
                                intent.putExtra("series", series);
                                intent.putExtra("game", game);
                                intent.putStringArrayListExtra("contest_list", (ArrayList<String>) contestIds);
                                startActivity(intent);

                            } else if (teamCount == 1){

                                String key = dref.child(id).child("participant").push().getKey();
                                HashMap<String, Object> participantMap = new HashMap<>();
                                participantMap.put("uid", auth.getCurrentUser().getUid());
                                participantMap.put("user_team", snapshot.getValue());
                                dref.child(id).child("participant").child(key).setValue(participantMap);

                            } else if (teamCount == 0) {

                                Toast.makeText(Contest_Details.this, "PLEASE MAKE TEAM FIRST", Toast.LENGTH_SHORT).show();

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
    }

    private void create_normal_contest (int entry, int prize_pool, int limit, String contest_name) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference dref = FirebaseDatabase.getInstance().getReference(game + "/matches/" + team1 + " VS " + team2 + " , " + series + "/contest");
        DatabaseReference userteam = FirebaseDatabase.getInstance().getReference("users/"+ auth.getCurrentUser().getUid()+"/teams/"+game).child(team1 + " VS " + team2 + " , " + series);
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

                    Intent intent = new Intent(Contest_Details.this, SelectTeam.class);
                    intent.putExtra("team1", team1);
                    intent.putExtra("team2", team2);
                    intent.putExtra("date", date);
                    intent.putExtra("status", status);
                    intent.putExtra("series", series);
                    intent.putExtra("game", game);
                    intent.putExtra("contestId", contestId);
                    startActivity(intent);

                } else if (teamCount == 1){

                    String key = dref.child(contestId).child("participant").push().getKey();
                    HashMap<String, Object> participantMap = new HashMap<>();
                    participantMap.put("uid", auth.getCurrentUser().getUid());
                    participantMap.put("user_team", snapshot.child("Team1").getValue());
                    dref.child(contestId).child("participant").child(key).setValue(participantMap);

                } else if (teamCount == 0) {

                    Toast.makeText(Contest_Details.this, "PLEASE MAKE TEAM FIRST", Toast.LENGTH_SHORT).show();

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

class Rule {

    public int limit;
    public String contest_name;
    public Rule() {}
    public Rule(int limit, String contest_name) {
        this.limit = limit;
        this.contest_name = contest_name;
    }
}