package com.example.dream11.TeamsAndContest.CreateTeam.Football;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.dream11.Adapters.PlayerAdapt;
import com.example.dream11.PropertyClasses.Player;
import com.example.dream11.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class FOR extends Fragment implements PlayerAdapt.OnItemClickListener {

    String team1, team2, date, status, series;
    private List<Player> selectedPlayersList = new ArrayList<>();
    PlayerAdapt playerAdapt;
    Uri flag;

    public FOR() {
        // Required empty public constructor
    }


    public static FOR newInstance(String param1, String param2) {
        FOR fragment = new FOR();
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


    private OnSelectedPlayerListener listener;

    @Override
    public void onItemClick(Player player) {
        if (player.getNationality().equals("AFGHANISTAN")) {
            flag = Uri.parse("https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/country_flag%2Fafghanistan.jpeg?alt=media&token=697b23a6-ac2b-4e6e-9654-6f5f16e37854");
        } else if (player.getNationality().equals("AUSTRALIA")) {
            flag = Uri.parse("https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/country_flag%2Faustralia.jpeg?alt=media&token=beaa7622-4a1b-4ce3-af1a-68ad9529fdf9");
        } else if (player.getNationality().equals("BANGLADESH")) {
            flag = Uri.parse("https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/country_flag%2Fbangladesh.jpeg?alt=media&token=6c2c52be-2706-40fa-a883-a9195ccd99f7");
        } else if (player.getNationality().equals("ENGLAND")) {
            flag = Uri.parse("https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/country_flag%2Fengland.jpeg?alt=media&token=dfcd8caf-f10a-499f-8012-369b368d109a");
        } else if (player.getNationality().equals("INDIA")) {
            flag = Uri.parse("https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/country_flag%2Findia.jpeg?alt=media&token=aa9f0219-07c4-402d-a00b-a7b205faa6dc");
        } else if (player.getNationality().equals("IRELAND")) {
            flag = Uri.parse("https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/country_flag%2Fireland.jpeg?alt=media&token=4b9c5d3b-8824-474b-9b25-08200d9e8d35");
        } else if (player.getNationality().equals("NEW ZEALAND")) {
            flag = Uri.parse("https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/country_flag%2FNew_Zealand.jpeg?alt=media&token=d0db3ae5-d67b-49b8-b390-bdfb33c2346a");
        } else if (player.getNationality().equals("PAKISTAN")) {
            flag = Uri.parse("https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/country_flag%2Fpakistan.jpeg?alt=media&token=87c971ee-9528-4afc-9920-4e58b2448e78");
        } else if (player.getNationality().equals("SOUTH AFRICA")) {
            flag = Uri.parse("https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/country_flag%2FSouth_Africa.jpeg?alt=media&token=be33c148-ecab-4989-b804-8b339f930e5f");
        } else if (player.getNationality().equals("SRI LANKA")) {
            flag = Uri.parse("https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/country_flag%2FSri_Lanka.jpeg?alt=media&token=952dcf17-9c00-45be-89b2-8609475b55e9");
        } else if (player.getNationality().equals("WEST INDIES")) {
            flag = Uri.parse("https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/country_flag%2Fwest-indies.jpg?alt=media&token=91c7e62e-42a1-44cc-b1c2-55b5eebe12d1");
        } else if (player.getNationality().equals("ZIMBABWE")) {
            flag = Uri.parse("https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/country_flag%2FZimbabwe.jpeg?alt=media&token=17846c16-4469-4b05-9f91-a244625a0011");
        } else if (player.getNationality().equals("NAMIBIA")) {
            flag = Uri.parse("https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/country_flag%2FNamibia.jpeg?alt=media&token=30c63b83-4348-4ba8-8890-65dc4e498a5c");
        } else if (player.getNationality().equals("NEPAL")) {
            flag = Uri.parse("https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/country_flag%2FNepal.jpeg?alt=media&token=727d19ec-5e8c-49f7-a22c-c19a69ee5dc1");
        } else if (player.getNationality().equals("NETHERLANDS")) {
            flag = Uri.parse("https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/country_flag%2FNetherlands.jpeg?alt=media&token=37a5d1ec-2315-4cfa-abf8-3a3cb4c73ecb");
        } else if (player.getNationality().equals("PAPUA NEW GUINEA")) {
            flag = Uri.parse("https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/country_flag%2FPapua_New_Guinea.jpeg?alt=media&token=ec693233-12b8-4ef7-b4b8-e0a1148a8fae");
        } else if (player.getNationality().equals("SCOTLAND")) {
            flag = Uri.parse("https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/country_flag%2FScotland.jpeg?alt=media&token=4b0035c7-4ecc-4d3e-8f18-1fdf64bf71d3");
        } else if (player.getNationality().equals("UNITED ARAB EMIRATES")) {
            flag = Uri.parse("https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/country_flag%2FUnited_Arab_Emirates.jpeg?alt=media&token=eae6b325-216a-49f1-ae30-1b2a11dc40ed");
        } else if (player.getNationality().equals("UNITED STATES OF AMERICA")) {
            flag = Uri.parse("https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/country_flag%2FUnited_States.jpeg?alt=media&token=b23efb05-a8ac-4eb1-9fc7-46a759805841");
        }
        showPlayerInfoDialog(getContext(), player.getPlayer_name(), player.getDateOfBirth(), String.valueOf(player.getJersy_no()), player.getPlayer_position(), player.getImageUrl(), String.valueOf(flag));
    }

    private String calculateAge(String birthDateString) {
        int age = 0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        try {
            Date birthDate = dateFormat.parse(birthDateString);
            Log.d("Birthdate", "Parsed birthdate: " + birthDate.toString());
            Calendar dob = Calendar.getInstance();
            dob.setTime(birthDate);
            Calendar today = Calendar.getInstance();

            age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
            if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
                age--;
            }
            Log.d("Age", "Calculated age: " + age);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return String.valueOf(age);
    }

    public void showPlayerInfoDialog(Context context, String name, String birthday, String jNo, String role, String img, String flag) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.player_detail_alertbox, null);

        TextView playerName = dialogView.findViewById(R.id.playerName);
        TextView jersyNo = dialogView.findViewById(R.id.jersyNo);
        TextView playerrole_textview = dialogView.findViewById(R.id.playerrole_textview);
        TextView card_age = dialogView.findViewById(R.id.card_age);
        ImageView playerImage = dialogView.findViewById(R.id.playerImage);
        ImageView flagimg = dialogView.findViewById(R.id.flagimg);
        ImageView bg = dialogView.findViewById(R.id.bgimage);
        bg.setBackgroundResource(R.drawable.bg_football);

        playerName.setText(name);
        jersyNo.setText(jNo);
        playerrole_textview.setText(role);

        String age = calculateAge(birthday);
        card_age.setText(age);

        Glide.with(context)
                .load(img)
                .placeholder(R.drawable.default_profile)
                .error(R.drawable.default_profile)
                .into(playerImage);

        Glide.with(context)
                .load(flag)
                .placeholder(R.drawable.question_mark)
                .error(R.drawable.question_mark)
                .into(flagimg);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);

        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();
    }

    public interface OnSelectedPlayerListener {
        void onPlayersSelected(List<Player> selectedPlayers);
    }

    private void sendDatatoActivity(List<Player> selectedPlayersList) {
        if (listener != null) {
            listener.onPlayersSelected(selectedPlayersList);
            System.out.println(selectedPlayersList);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnSelectedPlayerListener) {
            listener = (OnSelectedPlayerListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnSelectedPlayerListener");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_f_o_r, container, false);

        team1 = getActivity().getIntent().getStringExtra("team1");
        team2 = getActivity().getIntent().getStringExtra("team2");
        date = getActivity().getIntent().getStringExtra("date");
        status = getActivity().getIntent().getStringExtra("status");
        series = getActivity().getIntent().getStringExtra("series");

        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        List<Player> team = new ArrayList<>();
        playerAdapt = new PlayerAdapt(team);
        playerAdapt.setOnItemClickListener(this);
        recyclerView.setAdapter(playerAdapt);

        fetch_players(team, playerAdapt);
        return view;
    }

    public void maxSelectionMethod(boolean maxSelection) {
        if (playerAdapt != null) {
            playerAdapt.setMaxSelection(maxSelection);
            playerAdapt.notifyDataSetChanged();
        }
    }

    private void fetch_players(List<Player> playerList, PlayerAdapt adapter) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("football/matches/" + team1 + " VS " + team2 + " , " + series);
        reference.child("Team1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                playerList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Player player = dataSnapshot.getValue(Player.class);
                    player.setTeamName(team1);
                    if (player.getPlayer_position().equals("FORWARD")) {
                        playerList.add(player);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("PlayerList", "Failed to read player data", error.toException());
            }
        });

        reference.child("Team2").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Player player = dataSnapshot.getValue(Player.class);
                    player.setTeamName(team2);
                    if (player.getPlayer_position().equals("FORWARD")) {
                        playerList.add(player);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("PlayerList", "Failed to read player data", error.toException());
            }
        });

        adapter.setOnPlayerSelectedListener(new PlayerAdapt.OnPlayerSelectedListener() {
            @Override
            public void onPlayerSelected(Player player) {
                selectedPlayersList.add(player);
                sendDatatoActivity(selectedPlayersList);
            }

            @Override
            public void onPlayerDeselected(Player player) {
                selectedPlayersList.remove(player);
                sendDatatoActivity(selectedPlayersList);
            }
        });
    }
}