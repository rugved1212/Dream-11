package com.example.dream11.TeamsAndContest.CreateTeam.Cricket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.dream11.PropertyClasses.Player;
import com.example.dream11.R;
import com.example.dream11.TeamsAndContest.BottomSheet.CricketBottomSheet;
import com.example.dream11.TeamsAndContest.CreateTeam.Select_C_VC;
import com.example.dream11.TeamsAndContest.TeamAndContest;
import com.github.nikartm.button.FitButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.mackhartley.roundedprogressbar.RoundedProgressBar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class CreateCricketTeam extends AppCompatActivity implements BAT.OnSelectedPlayerListener, BOWL.OnSelectedPlayerListener, AR.OnSelectedPlayerListener, WK.OnSelectedPlayerListener {

    private ViewPager2 viewPager2;
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private CountDownTimer countDown;
    private Date targetDate;
    String team1, team2, date, status, series;
    Uri flag1, flag2;
    TextView player_selected;
    private List<Player> selectedPlayers = new ArrayList<>();
    FitButton next;
    RoundedProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_cricket_team);

        team1 = getIntent().getStringExtra("team1");
        team2 = getIntent().getStringExtra("team2");
        date = getIntent().getStringExtra("date");
        status = getIntent().getStringExtra("status");
        series = getIntent().getStringExtra("series");

        progressBar = findViewById(R.id.progress_bar);
        viewPager2 = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tablayout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        player_selected = findViewById(R.id.player_selected);
        TextView team1name = findViewById(R.id.team1name);
        TextView team2name = findViewById(R.id.team2name);
        ImageView img1 = findViewById(R.id.IMG1);
        ImageView img2 = findViewById(R.id.IMG2);


        if (team1!=null && team2!=null && date!=null && status!=null){

            team1name.setText(teamName(team1));
            Glide.with(this)
                    .load(flags(team1))
                    .error(R.drawable.lock)
                    .into(img1);

            team2name.setText(teamName(team2));
            Glide.with(this)
                    .load(flags(team2))
                    .error(R.drawable.lock)
                    .into(img2);
        }

        PlayerAdapter adapter = new PlayerAdapter(this);
        viewPager2.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("WK");
                    tabLayout.setTabTextColors(getResources().getColor(R.color.blackshade), getResources().getColor(R.color.amber));
                    break;
                case 1:
                    tab.setText("BAT");
                    break;
                case 2:
                    tab.setText("AR");
                    break;
                case 3:
                    tab.setText("BOWL");
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
            if (date!=null) {
                targetDate = sdf.parse(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (date!=null) {
            startCountdownTimer();
        }

        FitButton preview = findViewById(R.id.preview);
        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Player> wk = new ArrayList<>();
                List<Player> bat = new ArrayList<>();
                List<Player> ar = new ArrayList<>();
                List<Player> bowl = new ArrayList<>();
                for (Player player : selectedPlayers) {
                    if (player.getPlayer_role().equals("WICKETKEEPER-BATSMAN")) {
                        wk.add(player);
                    } else if (player.getPlayer_role().equals("BATSMAN")) {
                        bat.add(player);
                    } else if (player.getPlayer_role().equals("ALL-ROUNDER")) {
                        ar.add(player);
                    } else if (player.getPlayer_role().equals("BOWLER")) {
                        bowl.add(player);
                    }
                }
                CricketBottomSheet cricketBottomSheet = new CricketBottomSheet(wk, bat, ar, bowl, "", "", player_selected.getText().toString(), teamName(team1), teamName(team2));
                cricketBottomSheet.show(getSupportFragmentManager(), cricketBottomSheet.getTag());
            }
        });

        FitButton lineup = findViewById(R.id.lineup);
        lineup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        next = findViewById(R.id.next);
        if (selectedPlayers.size() >= 11) {
            next.setButtonColor(Color.parseColor("#45A510"));
        } else {
            next.setButtonColor(Color.parseColor("#80A8A8A8"));
        }
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedPlayers.size() >= 11) {
                    Intent intent = new Intent(CreateCricketTeam.this, Select_C_VC.class);
                    intent.putExtra("team1", team1);
                    intent.putExtra("team2", team2);
                    intent.putExtra("date", date);
                    intent.putExtra("status", status);
                    intent.putExtra("series", series);
                    intent.putExtra("game", "cricket");
                    intent.putExtra("selectedPlayers", new Gson().toJson(selectedPlayers));
                    startActivity(intent);
                }
            }
        });
    }

    private Uri flags (String teamName) {
        String flagUrl = "";
        switch (teamName) {
            case "AFGHANISTAN":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2FAFG.png?alt=media&token=2b4a9da7-00b0-4f3b-8375-0e1587dd9d6c";
                break;
            case "AUSTRALIA":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2FAUS.png?alt=media&token=bee40ee4-bcee-43a5-971c-ceb022e1b01b";
                break;
            case "BANGLADESH":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2FBAN.png?alt=media&token=12667c0c-04ca-4f3d-9377-c5c1ce9de9fc";
                break;
            case "ENGLAND":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2FENG.png?alt=media&token=323c26df-a18a-4204-be74-7f1468366110";
                break;
            case "INDIA":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2Findia.jpeg?alt=media&token=aebe3a92-6b59-4b09-8f5a-0e3a34873ce5";
                break;
            case "IRELAND":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2FIRL.png?alt=media&token=571f690b-bc1b-4ed4-94f3-e60c0b9d5729";
                break;
            case "NEW ZEALAND":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2FNZ.png?alt=media&token=d6be2a03-700b-4a94-8bef-79962e24aa4c";
                break;
            case "PAKISTAN":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2FPAK.png?alt=media&token=96371d93-14b1-4efd-bb12-d3eda4629535";
                break;
            case "SOUTH AFRICA":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2FSA.png?alt=media&token=79744fb9-01de-44a4-872b-e9267d3020b4";
                break;
            case "SRI LANKA":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2FSL.png?alt=media&token=fe347d2d-66ae-46d4-b28e-e8689ff4bd05";
                break;
            case "WEST INDIES":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2FWI.png?alt=media&token=973794db-2d8f-43ab-9a0a-8ef012cf0e45";
                break;
            case "ZIMBABWE":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2FZIM.png?alt=media&token=3a359a5e-54de-4ace-bc52-4f0bad65715a";
                break;
            case "NAMIBIA":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2FNAM.png?alt=media&token=17dfb0f1-e809-4eb0-8e12-3b87e929ee82";
                break;
            case "NEPAL":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2FNEP.png?alt=media&token=96c7d6c0-5ba2-40de-9cb4-85c18ff1ec0d";
                break;
            case "NETHERLANDS":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2FNET.png?alt=media&token=96768f2c-b931-4bf4-9ec6-4b8665a48921";
                break;
            case "OMAN":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2FOMA.png?alt=media&token=52dfc8ef-9891-4cfe-b410-988d8454fe25";
                break;
            case "PAPUA NEW GUINEA":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2FPNG.png?alt=media&token=e4b5e853-0c72-4c57-8597-cce6e089558c";
                break;
            case "SCOTLAND":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2FSCO.png?alt=media&token=3512f5ae-6898-4ec4-b7f3-b05e16f01630";
                break;
            case "UNITED ARAB EMIRATES":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2FUAE.png?alt=media&token=c84b4941-5bb5-49a6-8989-dfee68ee3165";
                break;
            case "ALGERIA":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2Falgeria.png?alt=media&token=99133a4e-927d-4bf9-b12b-430c60aac0f6";
                break;
            case "ANGOLA":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2Fangola.png?alt=media&token=c8204d11-e0a0-4728-8d43-28d7626f1fc3";
                break;
            case "ARGENTINA":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2Fargentina.png?alt=media&token=f05baa79-0835-44ec-92af-629ae1edc7d2";
                break;
            case "AUSTRIA":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2Faustria.png?alt=media&token=55fbf6f2-7b0d-4360-85bd-800ee157dc3c";
                break;
            case "BELGIUM":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2Fbelgium.png?alt=media&token=21ef6f13-13b3-49be-a727-c609f88a28a8";
                break;
            case "BOLIVIA":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2Fbolivia.png?alt=media&token=f5194493-975e-40bf-a4d3-84073b199323";
                break;
            case "BOSNIA & HERZEGOVINA":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2Fbosnia_herzegovina.png?alt=media&token=66ba7094-78dc-45fa-ad4a-f8d7e7470bdd";
                break;
            case "BRAZIL":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2Fbrazil.png?alt=media&token=0ae33732-fbfc-4cfb-a08e-dfcdbcfc9224";
                break;
            case "BULGARIA":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2Fbulgaria.png?alt=media&token=de35c390-bfb6-4495-ac29-6b6163896397";
                break;
            case "CAMEROON":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2Fcameroon.png?alt=media&token=9136e184-ba64-4a11-896a-f881a93c3420";
                break;
            case "CANADA":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2Fcanada.png?alt=media&token=ed3debfb-c2de-4bbf-8ab2-533dbde7d516";
                break;
            case "CHILE":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2Fchile.png?alt=media&token=3b73b85d-45f9-4ecc-ac2d-b9412d3e0abb";
                break;
            case "CHINA":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2Fchina.png?alt=media&token=a862a21f-8174-4501-a9a3-c0f8dcd978dc";
                break;
            case "COLOMBIA":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2Fcolombia.png?alt=media&token=7a495790-19c3-4707-a197-205062f1f6f7";
                break;
            case "CONGO DR (ZAIRE)":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2Fcongo.png?alt=media&token=d00a72f0-4100-4e01-9fd3-541e649635ef";
                break;
            case "COSTA RICA":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2Fcosta_rica.png?alt=media&token=3ade02b5-eac0-421d-89c7-01ac8ba34526";
                break;
            case "COTE D'IVOIRE":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2Fcote_divoire.png?alt=media&token=c51ae97f-4f22-4225-aba4-669d1725fab8";
                break;
            case "CROATIA":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2Fcroatia.png?alt=media&token=8eaeae5b-d212-4131-9836-abab639cf674";
                break;
            case "CUBA":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2Fcuba.png?alt=media&token=a6fc9e8d-f002-4b0b-9b3b-931a83309a3a";
                break;
            case "CZECHIA":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2Fczech.png?alt=media&token=fb1ff93b-6eb3-4070-b322-31fb1b87c93b";
                break;
            case "DENMARK":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2Fdenmark.png?alt=media&token=e5e32438-01e2-4c32-85bc-1160fd142a29";
                break;
            case "ECUADOR":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2Fecuador.png?alt=media&token=aad0b2cd-15bd-4bf3-ada3-6ebc2268a04d";
                break;
            case "EGYPT":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2Fegypt.png?alt=media&token=61a63cb8-2edc-415a-9222-9223eb2473ed";
                break;
            case "EL SALVADOR":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2Fel_salvador.png?alt=media&token=9b74757c-2aa0-4cd6-b01b-d7f034a4500e";
                break;
            case "FRANCE":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2Ffrance.png?alt=media&token=205344cb-8b24-4f19-ba88-640138957f06";
                break;
            case "GERMANY":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2Fgermany.png?alt=media&token=5ab36a5a-fe29-4d7e-9817-1acc2d163b43";
                break;
            case "GHANA":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2Fghana.png?alt=media&token=73d63841-7d37-4078-884a-8429fbaa4828";
                break;
            case "GREECE":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2Fgreece.png?alt=media&token=ffb69d6e-67a0-4712-87b6-4a1e801b257a";
                break;
            case "HAITI":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2Fhalti.png?alt=media&token=ca62bcb2-c014-4e98-8df9-b4c2bf237459";
                break;
            case "HONDURAS":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2Fhonduras.png?alt=media&token=b3d4c0cf-40b9-4282-b1ee-74b7a93048a5";
                break;
            case "HUNGARY":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2Fhundary.png?alt=media&token=1e2e75bf-baa4-4eb1-937a-443e06094bdc";
                break;
            case "ICELAND":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2Ficeland.png?alt=media&token=9ceee976-ba82-44fc-bf9a-e281c31b8c19";
                break;
            case "INDONESIA":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2Findonesia.png?alt=media&token=e8050b6f-7e09-437e-9d20-7ae3a948b965";
                break;
            case "IRAN":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2Firan.png?alt=media&token=aa3d0f8c-920e-4689-9fcb-952cc69e4dd1";
                break;
            case "IRAQ":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2Firaq.png?alt=media&token=bedc70fd-21f4-466c-bb6f-a6d0cbca4ba7";
                break;
            case "ISRAEL":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2Fisrael.png?alt=media&token=b0dda3bd-eb52-4ac1-a726-86e912c391a3";
                break;
            case "ITALY":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2Fitaly.png?alt=media&token=3ffea4a3-3415-43e0-b628-8e42f7731e30";
                break;
            case "JAMAICA":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2Fjamaica.png?alt=media&token=1f8dcb81-b27e-40d5-92f7-18720e6b2b39";
                break;
            case "JAPAN":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2Fjapan.png?alt=media&token=ec7ab315-4de4-4989-993c-9ebe79b7b1de";
                break;
            case "KUWAIT":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2Fkuwait.png?alt=media&token=2391fcb6-3cf5-49df-9c68-f1708b5d3c6d";
                break;
            case "MEXICO":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2Fmexico.png?alt=media&token=6d471730-2411-4743-a6cf-9e6896d506e0";
                break;
            case "MOROCCO":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2Fmorocco.png?alt=media&token=f1a18195-75fc-4d76-8f4d-c902223080f4";
                break;
            case "NILGERIA":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2Fnigeria.png?alt=media&token=72b571f3-b22f-4be4-a5dd-a2256ffa5157";
                break;
            case "NORTHERN IRELAND":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2Fnorthern_ireland.png?alt=media&token=742ad64e-f28c-44d4-b699-2d7b2fac973a";
                break;
            case "NORTH KOREA":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2Fnorth_korea.png?alt=media&token=f2a8ae2a-ed4b-44a9-bdaf-88c174e4a472";
                break;
            case "NORWAY":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2Fnorway.png?alt=media&token=503f3f27-4aab-4248-8f99-07ddc5a8e240";
                break;
            case "PANAMA":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2Fpanama.png?alt=media&token=863b1fb3-f26a-4da8-b165-8114da6b1df2";
                break;
            case "PARAGUAY":
                flagUrl = "";
                break;
            case "PERU":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2Fperu.png?alt=media&token=28583938-f23b-4fbd-98fc-ed4ebea51244";
                break;
            case "POLAND":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2Fpoland.png?alt=media&token=42936a8d-3918-49fd-8900-58b0bed609e0";
                break;
            case "PORTUGAL":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2Fportugal.png?alt=media&token=cebf48f2-d439-4a60-bb51-7aa9bed81150";
                break;
            case "QATAR":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2Fqatar.png?alt=media&token=32a949ca-c9cf-4f67-a849-67ea1e7b33e6";
                break;
            case "ROMANIA":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2Fromania.png?alt=media&token=f46f21c5-8fdc-4c1a-b709-555bd00582c8";
                break;
            case "RUSSIA":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2Frussia.png?alt=media&token=65596119-fada-4c76-8e09-5a2aa3755f16";
                break;
            case "SAUDI ARABIA":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2Fsaudi_arabia.png?alt=media&token=3ef0ea5b-1d4a-492a-bd53-65b0040246a8";
                break;
            case "SENEGAL":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2Fsenegal.png?alt=media&token=a97d4533-3d77-40c4-9931-e5875b7d9cfc";
                break;
            case "SERBIA":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2Fserbia.png?alt=media&token=1b6606f1-1e89-4d36-8002-a174ccc4b36f";
                break;
            case "SLOVAKIA":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2Fslovakia.png?alt=media&token=2ef428a0-5cae-418f-afa7-67186fabf86c";
                break;
            case "SLOVENIA":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2Fslovenia.png?alt=media&token=8209e4b1-7dc2-484a-81bd-74cfc1a580ae";
                break;
            case "SOUTH KOREA":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2Fsouth_korea.png?alt=media&token=e7c30e13-8ec7-4d95-bdba-3b34dafdf4fa";
                break;
            case "SPAIN":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2Fspain.png?alt=media&token=8e2aebbc-84a2-4bd0-bd01-486057e1493b";
                break;
            case "SWEDEN":
                flagUrl = "";
                break;
            case "SWITZERLAND":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2Fswitzerland.png?alt=media&token=29ecba2d-5665-432b-ab90-5ef0bab01a89";
                break;
            case "TOGO":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2Ftogo.png?alt=media&token=005e1a03-107a-4eca-9b8a-a003dce3f145";
                break;
            case "TRINIDAD & TOBAGO":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2Ftrindad_tobago.png?alt=media&token=bd9ce614-e760-4ae0-a70f-df18f90a37da";
                break;
            case "TUNISIA":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2Ftunusia.png?alt=media&token=88f2459e-24e5-4b16-bb0f-bc308f052a0e";
                break;
            case "TURKEY":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2Fturkey.png?alt=media&token=2aae7369-ddcb-400a-8714-32416e4eaa04";
                break;
            case "UKRAINE":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2Fukraine.png?alt=media&token=be0eabe7-5387-4731-a74d-7ff2d760d84d";
                break;
            case "URUGUAY":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2Furuguay.png?alt=media&token=6cbd1e95-e8b0-4b10-be6b-373b32deb7eb";
                break;
            case "WALES":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fcountry_flag%2Fwales.png?alt=media&token=6873a977-2c1e-406c-b0bb-64685f8e39bf";
                break;
            case "BENGAL WARRIORS":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fpro_kabaddi_flags%2FBW.png?alt=media&token=3661cbe0-e2b5-4463-9e5a-e9d0879fa48f";
                break;
            case "BENGALURU BULLS":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fpro_kabaddi_flags%2FBB.png?alt=media&token=603ceb49-5862-437f-ba68-7634cdeadbf7";
                break;
            case "DABANG DELHI K.C.":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fpro_kabaddi_flags%2FDD.png?alt=media&token=5bd4b925-51b2-4e76-9f6f-9ceb09b842e0";
                break;
            case "GUJARAT GIANTS":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fpro_kabaddi_flags%2FGG.png?alt=media&token=3b43bca8-d4c7-48a9-bd1d-76751d2ee535";
                break;
            case "HARYANA STEELERS":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fpro_kabaddi_flags%2FHS.png?alt=media&token=66e11fb8-3763-4fa7-a533-f3a70720fe2b";
                break;
            case "JAIPUR PINK PANTHERS":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fpro_kabaddi_flags%2FJPP.png?alt=media&token=f27c18c1-39f8-44f6-b011-9be9e3cafed7";
                break;
            case "PATNA PIRATES":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fpro_kabaddi_flags%2FPP.png?alt=media&token=c3bf58cf-9342-429c-ba68-2b8512e623d8";
                break;
            case "PUNERI PALTAN":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fpro_kabaddi_flags%2FPuneri_Paltan.png?alt=media&token=290cb010-0df4-4274-be42-4a0b83e74bc1";
                break;
            case "TAMIL THALAIVAS":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fpro_kabaddi_flags%2FTT.png?alt=media&token=3ba854ca-4a2b-4cd5-9538-fa6523b3afda";
                break;
            case "TELUGU TITANS":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fpro_kabaddi_flags%2FTelugu_Titans.png?alt=media&token=cd09b359-a210-43e5-88e1-b09893e041bc";
                break;
            case "U MUMBA":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fpro_kabaddi_flags%2Fu_mumba.png?alt=media&token=d03ac5a2-fb84-49a2-8bf3-50cb7515ac83";
                break;
            case "U.P. YODDHAS":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fpro_kabaddi_flags%2FUP_Yoddhas.png?alt=media&token=9f8f5f35-776b-493e-a784-1328dea3fbf3";
                break;
            case "MUMBAI INDIANS":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fipl_flags%2FMI.png?alt=media&token=7090c193-f7ae-4260-8feb-c684dddcc4be";
                break;
            case "CHENNAI SUPER KINGS":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fipl_flags%2FCSK.png?alt=media&token=2ce91cd5-7d7d-4575-b3f7-928ac2739d62";
                break;
            case "ROYAL CHALLENGERS BANGALORE":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fipl_flags%2FRCB.png?alt=media&token=7eaba176-7906-4da1-9dd3-15f1da46ddac";
                break;
            case "DELHI CAPITALS":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fipl_flags%2FDC.png?alt=media&token=77387e2f-cfba-49d1-a4ec-32ae9bb2d192";
                break;
            case "GUJARAT TITANS":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fipl_flags%2FGT.png?alt=media&token=008857ce-7642-4f9e-bdf9-d2b117937418";
                break;
            case "KOLKATA KNIGHT RIDERS":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fipl_flags%2FKKR.png?alt=media&token=28dfba05-5a96-40e5-b6d2-62c6e1328822";
                break;
            case "LUCKNOW SUPER GIANTS":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fipl_flags%2FLSK.png?alt=media&token=c8fe6c65-b931-4907-b741-6e784f5d4156";
                break;
            case "PUNJAB KINGS":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fipl_flags%2FPKXI.png?alt=media&token=8253b1f7-98a9-49f6-91d4-925e5a8d6e9f";
                break;
            case "RAJASTHAN ROYALS":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fipl_flags%2FRR.png?alt=media&token=2423a311-79a3-4ed1-bdad-136c274fc6ae";
                break;
            case "SUNRISERS HYDERABAD":
                flagUrl = "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/flags%2Fipl_flags%2FSRH.png?alt=media&token=8dd7bfdd-f206-452a-8d8d-d9d6ec8f13ff";
                break;

        }
        return Uri.parse(flagUrl);
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


    @Override
    public void onPlayersSelected(List<Player> selected) {

        String currentFragmentTag = null;
        FragmentStateAdapter adapter = (FragmentStateAdapter) viewPager2.getAdapter();
        if (adapter != null) {
            Fragment currentFragment = getSupportFragmentManager().findFragmentByTag("f" + viewPager2.getCurrentItem());
            if (currentFragment != null) {
                currentFragmentTag = currentFragment.getTag();
                System.out.println(currentFragmentTag);

                Iterator<Player> iterator = selectedPlayers.iterator();
                while (iterator.hasNext()) {
                    Player currentPlayer = iterator.next();
                    switch (currentFragmentTag) {
                        case "f0":
                            if (currentPlayer.getPlayer_role().equals("WICKETKEEPER-BATSMAN")) {
                                iterator.remove();
                                System.out.println("deleted" + currentPlayer);
                            }
                            System.out.println("WK" + currentPlayer.getPlayer_name());
                            break;
                        case "f1":
                            if (currentPlayer.getPlayer_role().equals("BATSMAN")) {
                                iterator.remove();
                                System.out.println("deleted" + currentPlayer);
                            }
                            System.out.println("BAT" + currentPlayer.getPlayer_name());
                            break;
                        case "f2":
                            if (currentPlayer.getPlayer_role().equals("ALL-ROUNDER")) {
                                iterator.remove();
                                System.out.println("deleted" + currentPlayer);
                            }
                            System.out.println("AR" + currentPlayer.getPlayer_name());
                            break;
                        case "f3":
                            if (currentPlayer.getPlayer_role().equals("BOWLER")) {
                                iterator.remove();
                                System.out.println("deleted" + currentPlayer);
                            }
                            System.out.println("BOWL" + currentPlayer.getPlayer_name());
                            break;
                    }
                }

            } else {
                Log.e("ParentActivity", "Current fragment is null");
            }
        } else {
            Log.e("ParentActivity", "ViewPager adapter is null");
        }

        for (Player player : selected) {
            if (!selectedPlayers.contains(player)) {
                selectedPlayers.add(player);
                Log.d("ParentActivity", "Selected player: " + player.getPlayer_name());
            }
        }
        player_selected.setText(selectedPlayers.size() + "/11");
        if (selectedPlayers.size() >= 11) {
            next.setButtonColor(Color.parseColor("#45A510"));
        } else {
            next.setButtonColor(Color.parseColor("#80A8A8A8"));
        }

        double percent = selectedPlayers.size() * 9.09090909f;

        progressBar.setProgressPercentage(percent, true);

        int countWK = 0;
        int countBAT = 0;
        int countAR = 0;
        int countBOWL = 0;
        for (Player player : selectedPlayers) {
            if (player.getPlayer_role().equals("WICKETKEEPER-BATSMAN")) {
                countWK++;
            } else if (player.getPlayer_role().equals("BATSMAN")) {
                countBAT++;
            } else if (player.getPlayer_role().equals("ALL-ROUNDER")) {
                countAR++;
            } else if (player.getPlayer_role().equals("BOWLER")) {
                countBOWL++;
            }
        }
        Log.w("countWK", " "+countWK);
        Log.w("countBAT", " "+countBAT);
        Log.w("countAR", " "+countAR);
        Log.w("countBOWL", " "+countBOWL);
        WK wkfragment = (WK) getSupportFragmentManager().findFragmentByTag("f0");
        BAT batfragment = (BAT) getSupportFragmentManager().findFragmentByTag("f1");
        AR arfragment = (AR) getSupportFragmentManager().findFragmentByTag("f2");
        BOWL bowlfragment = (BOWL) getSupportFragmentManager().findFragmentByTag("f3");



        if (selectedPlayers.size() == 11) {
            System.out.println("SELECTED PLAYER SIZE = " + selectedPlayers.size());
            if (wkfragment!=null) {
                System.out.println("wkfound");
                wkfragment.maxSelectionMethod(true);
            }
            if (batfragment!=null) {
                System.out.println("batfound");
                batfragment.maxSelectionMethod(true);
            }
            if (arfragment!=null) {
                System.out.println("arfound");
                arfragment.maxSelectionMethod(true);
            }
            if (bowlfragment!=null) {
                System.out.println("bowlfound");
                bowlfragment.maxSelectionMethod(true);
            }

        } else {
            if (countWK == 8) {
                if (wkfragment!=null)
                    wkfragment.maxSelectionMethod(true);
            } else {
                if (wkfragment!=null)
                    wkfragment.maxSelectionMethod(false);
            }
            if (countBAT == 8) {
                if (batfragment!=null)
                    batfragment.maxSelectionMethod(true);
            } else {
                if (batfragment!=null)
                    batfragment.maxSelectionMethod(false);
            }
            if (countAR == 8) {
                if (arfragment!=null)
                    arfragment.maxSelectionMethod(true);
            } else {
                if (arfragment!=null)
                    arfragment.maxSelectionMethod(false);
            }
            if (countBOWL == 8) {
                if (bowlfragment!=null)
                    bowlfragment.maxSelectionMethod(true);
            } else {
                if (bowlfragment!=null)
                    bowlfragment.maxSelectionMethod(false);
            }
        }
    }


    private static class PlayerAdapter extends FragmentStateAdapter {

        public PlayerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new WK().setTag("WK");
                case 1:
                    return new BAT().setTag("BAT");
                case 2:
                    return new AR().setTag("AR");
                case 3:
                    return new BOWL().setTag("BOWL");
                default:
                    return null;
            }
        }

        @Override
        public int getItemCount() {
            return 4;
        }
    }
}