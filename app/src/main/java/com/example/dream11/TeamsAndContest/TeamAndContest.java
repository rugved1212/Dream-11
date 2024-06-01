package com.example.dream11.TeamsAndContest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.example.dream11.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TeamAndContest extends AppCompatActivity {

    private ViewPager2 viewPager2;
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private CountDownTimer countDown;
    private Date targetDate;
    String team1, team2, date, status, series, game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_and_contest);

        team1 = getIntent().getStringExtra("team1");
        team2 = getIntent().getStringExtra("team2");
        date = getIntent().getStringExtra("date");
        status = getIntent().getStringExtra("status");
        series = getIntent().getStringExtra("series");
        game = getIntent().getStringExtra("game");

        viewPager2 = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tablayout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (team1!=null && team2!=null){
            toolbar.setTitle(teamName(team1) + " VS " + teamName(team2));
        }

        MyPageAdapter myPageAdapter = new MyPageAdapter(this, team1, team2, date, status, series);
        viewPager2.setAdapter(myPageAdapter);

        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Contest");
                    tabLayout.setTabTextColors(getResources().getColor(R.color.blackshade), getResources().getColor(R.color.amber));
                    break;
                case 1:
                    tab.setText("My Contest");
                    break;
                case 2:
                    tab.setText("My Teams");
                    break;
            }
        }).attach();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabLayout.setTabTextColors(getResources().getColor(R.color.blackshade), getResources().getColor(R.color.amber));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tabLayout.setTabTextColors(getResources().getColor(R.color.blackshade), getResources().getColor(R.color.blackshade));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        try {
            targetDate = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        startCountdownTimer();

    }

    private static class MyPageAdapter extends FragmentStateAdapter {

        private String team1, team2, date, status, series;
        public MyPageAdapter(@NonNull FragmentActivity fragmentActivity, String team1, String team2, String date, String status, String series) {
            super(fragmentActivity);
            this.team1 = team1;
            this.team2 = team2;
            this.date = date;
            this.status = status;
            this.series = series;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new Contest(team1, team2, date, status, series);
                case 1:
                    return new MyContest(team1, team2, date, status, series);
                case 2:
                    return new MyTeams(team1, team2, date, status, series);
                default:
                    return null;
            }
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (countDown != null) {
//            countDown.cancel();
//        }
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
}