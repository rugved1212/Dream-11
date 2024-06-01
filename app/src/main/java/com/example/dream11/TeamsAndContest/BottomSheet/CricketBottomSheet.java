package com.example.dream11.TeamsAndContest.BottomSheet;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dream11.Fragments.Profile;
import com.example.dream11.PropertyClasses.Player;
import com.example.dream11.PropertyClasses.User;
import com.example.dream11.R;
import com.github.nikartm.button.FitButton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CricketBottomSheet extends BottomSheetDialogFragment {
    private List<Player> wkList;
    private List<Player> batList;
    private List<Player> arList;
    private List<Player> bowlList;
    private String captain;
    private String vicecaptain;
    String total_no, team1, team2;
    int backgroundResourceId;

    public CricketBottomSheet(List<Player> wkList, List<Player> batList, List<Player> arList, List<Player> bowlList, String captain, String vicecaptain, String total_no, String team1, String team2) {
        this.wkList = wkList;
        this.batList = batList;
        this.arList = arList;
        this.bowlList = bowlList;
        this.captain = captain;
        this.vicecaptain = vicecaptain;
        this.total_no = total_no;
        this.team1 = team1;
        this.team2 = team2;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cricket_bottom_sheet_team, container, false);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        RecyclerView wkrecycler = view.findViewById(R.id.wkrecycler);
        RecyclerView batrecycler = view.findViewById(R.id.batrecycler);
        RecyclerView arrecycler = view.findViewById(R.id.arrecycler);
        RecyclerView bowlrecycler = view.findViewById(R.id.bowlrecycler);


        wkrecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        batrecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        arrecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        bowlrecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        TeamAdapter wkadapter = new TeamAdapter(wkList, captain, vicecaptain);
        TeamAdapter batadapter = new TeamAdapter(batList, captain, vicecaptain);
        TeamAdapter aradapter = new TeamAdapter(arList, captain, vicecaptain);
        TeamAdapter bowladapter = new TeamAdapter(bowlList, captain, vicecaptain);

        wkrecycler.setAdapter(wkadapter);
        batrecycler.setAdapter(batadapter);
        arrecycler.setAdapter(aradapter);
        bowlrecycler.setAdapter(bowladapter);

        TextView total_player = view.findViewById(R.id.total_player);
        total_player.setText(total_no);

        TextView team1TV = view.findViewById(R.id.team1);
        team1TV.setText(team1);
        TextView team2TV = view.findViewById(R.id.team2);
        team2TV.setText(team2);

        TextView username = view.findViewById(R.id.username);
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(mAuth.getUid());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);
                    if (user != null) {
                        String name = user.getUsername();
                        username.setText(name);
                    }
                } else {
                    Log.d("UserInfo", "User data does not exist");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }

    public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.ViewHolder> {

        private List<Player> playerList;
        private String captain;
        private String vicecaptain;
        Uri flag;

        public TeamAdapter(List<Player> playerList, String captain, String vicecaptain) {
            this.playerList = playerList;
            this.captain = captain;
            this.vicecaptain = vicecaptain;
        }

        @NonNull
        @Override
        public TeamAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.team_player_view, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TeamAdapter.ViewHolder holder, int position) {
            Player player = playerList.get(position);

            if (captain.equals(player.getPlayer_name())) {
                holder.captain_mark.setVisibility(View.VISIBLE);
                holder.vicecaptain_mark.setVisibility(View.GONE);
            } else if (vicecaptain.equals(player.getPlayer_name())) {
                holder.captain_mark.setVisibility(View.GONE);
                holder.vicecaptain_mark.setVisibility(View.VISIBLE);
            } else {
                holder.captain_mark.setVisibility(View.GONE);
                holder.vicecaptain_mark.setVisibility(View.GONE);
            }

            holder.playerName.setText(nameSurname(player.getPlayer_name()));

            Glide.with(holder.player_img.getContext())
                    .load(player.getImageUrl())
                    .placeholder(R.drawable.default_profile)
                    .error(R.drawable.default_profile)
                    .into(holder.player_img);

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

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPlayerInfoDialog(holder.itemView.getContext(), player.getPlayer_name(), player.getDateOfBirth(), String.valueOf(player.getJersy_no()), player.getPlayer_role(), player.getImageUrl(), String.valueOf(flag));
                }
            });
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

        public String nameSurname (String name) {
            char firstNameLetter = name.charAt(0);
            String[] parts = name.split(" ");
            String surname = "";
            if (parts.length > 1) {
                for (int i = 1; i < parts.length; i++) {
                    surname += parts[i];
                }
            }
            String result = firstNameLetter+". " + surname;
            return result;
        }

        @Override
        public int getItemCount() {
            return playerList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView playerName;
            public ImageView player_img;
            public CardView captain_mark;
            public CardView vicecaptain_mark;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                playerName = itemView.findViewById(R.id.player_name);
                player_img = itemView.findViewById(R.id.player_img);
                captain_mark = itemView.findViewById(R.id.captain_mark);
                vicecaptain_mark = itemView.findViewById(R.id.vicecaptain_mark);
            }
        }
    }
}
