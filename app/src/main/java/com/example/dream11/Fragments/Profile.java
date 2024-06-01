package com.example.dream11.Fragments;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dream11.AddCash;
import com.example.dream11.Authentication.LoginPage;
import com.example.dream11.EditUser;
import com.example.dream11.MainActivity;
import com.example.dream11.PropertyClasses.User;
import com.example.dream11.R;
import com.github.nikartm.button.FitButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;


public class Profile extends Fragment {


    private static final int UPI_PAYMENT_REQUEST_CODE = 1;
    FirebaseAuth mAuth;

    public Profile() {
        // Required empty public constructor
    }


    public static Profile newInstance(String param1, String param2) {
        Profile fragment = new Profile();
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mAuth = FirebaseAuth.getInstance();

        TextView username = view.findViewById(R.id.username);
        TextView email = view.findViewById(R.id.email);
        TextView wallet = view.findViewById(R.id.wallet);
        TextView total_winning = view.findViewById(R.id.total_winning);
        CircleImageView dp = view.findViewById(R.id.dp);

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(mAuth.getUid());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);
                    if (user != null) {
                        String mail = user.getEmail();
                        String name = user.getUsername();
                        String img = user.getProfile_pic();

                        Float money = snapshot.child("wallet").getValue(Float.class);
                        wallet.setText(String.valueOf(money));

                        username.setText(name);
                        email.setText(mail);
                        Glide.with(Profile.this)
                                .load(img)
                                .error(R.drawable.default_profile)
                                .placeholder(R.drawable.default_profile)
                                .into(dp);
                    }
                } else {
                    Log.d("UserInfo", "User data does not exist");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FitButton add_money = view.findViewById(R.id.add_money);
        add_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddCash.class);
                startActivity(intent);
            }
        });

        FitButton profile_set = view.findViewById(R.id.profile_setting);
        profile_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), EditUser.class);
                startActivity(intent);
            }
        });

        FitButton logout = view.findViewById(R.id.log_out);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent intent = new Intent(getContext(), LoginPage.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return view;
    }

}