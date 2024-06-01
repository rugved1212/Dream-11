package com.example.dream11.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dream11.Fragments.MatchesFragment.CricketMatches;
import com.example.dream11.Fragments.MatchesFragment.FootballMatches;
import com.example.dream11.Fragments.MatchesFragment.KabaddiMatches;
import com.example.dream11.R;


public class Home extends Fragment implements View.OnClickListener{

    TextView crickettab, footballtab, kabadditab, select;
    ImageView cricket_img, football_img, kabaddi_img;
    Boolean cric = true, foot = false, kabad = false;
    public Home() {

    }

    public static Home newInstance(String param1, String param2) {
        Home fragment = new Home();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        crickettab = view.findViewById(R.id.cricketTV);
        footballtab = view.findViewById(R.id.footballTV);
        kabadditab = view.findViewById(R.id.kabaddiTV);
        select = view.findViewById(R.id.selectField);

        cricket_img = view.findViewById(R.id.cricket_img);
        football_img = view.findViewById(R.id.football_img);
        kabaddi_img = view.findViewById(R.id.kabaddi_img);

        crickettab.setOnClickListener(this);
        footballtab.setOnClickListener(this);
        kabadditab.setOnClickListener(this);

        Fragment fragment = new CricketMatches();
        getParentFragmentManager().beginTransaction()
                .replace(R.id.home_frameLayout, fragment)
                .addToBackStack(null)
                .commit();

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.cricketTV) {
            crickettab.setTextColor(getResources().getColor(R.color.amber));
            footballtab.setTextColor(getResources().getColor(R.color.blackshade));
            kabadditab.setTextColor(getResources().getColor(R.color.blackshade));

            cricket_img.setImageResource(R.drawable.cricket_white);
            football_img.setImageResource(R.drawable.football);
            kabaddi_img.setImageResource(R.drawable.kabaddi);

            select.animate().x(0).setDuration(60);

            Fragment fragment = new CricketMatches();
            getParentFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.slide_out_right_activity, R.anim.slide_in_left_activity)
                    .replace(R.id.home_frameLayout, fragment)
                    .addToBackStack(null)
                    .commit();

            cric = true;
            foot = false;
            kabad = false;

        } else if (view.getId() == R.id.footballTV) {
            crickettab.setTextColor(getResources().getColor(R.color.blackshade));
            footballtab.setTextColor(getResources().getColor(R.color.amber));
            kabadditab.setTextColor(getResources().getColor(R.color.blackshade));

            cricket_img.setImageResource(R.drawable.cricket);
            football_img.setImageResource(R.drawable.football_white);
            kabaddi_img.setImageResource(R.drawable.kabaddi);

            float size = footballtab.getWidth() * 2.3f;
            select.animate().x(size).setDuration(60);

            if (cric == true) {
                Fragment fragment = new FootballMatches();
                getParentFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right_activity, R.anim.slide_out_left_activity)
                        .replace(R.id.home_frameLayout, fragment)
                        .addToBackStack(null)
                        .commit();
            } else {
                Fragment fragment = new FootballMatches();
                getParentFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_out_right_activity, R.anim.slide_in_left_activity)
                        .replace(R.id.home_frameLayout, fragment)
                        .addToBackStack(null)
                        .commit();
            }


            cric = false;
            foot = true;
            kabad = false;

        } else if (view.getId() == R.id.kabaddiTV) {
            crickettab.setTextColor(getResources().getColor(R.color.blackshade));
            footballtab.setTextColor(getResources().getColor(R.color.blackshade));
            kabadditab.setTextColor(getResources().getColor(R.color.amber));

            cricket_img.setImageResource(R.drawable.cricket);
            football_img.setImageResource(R.drawable.football);
            kabaddi_img.setImageResource(R.drawable.kabaddi_white);

            float size = kabadditab.getWidth() * 4.53f;
            select.animate().x(size).setDuration(60);

            Fragment fragment = new KabaddiMatches();
            getParentFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_right_activity, R.anim.slide_out_left_activity)
                    .replace(R.id.home_frameLayout, fragment)
                    .addToBackStack(null)
                    .commit();

            cric = false;
            foot = false;
            kabad = true;
        }
    }
}