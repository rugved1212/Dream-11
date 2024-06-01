package com.example.dream11;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.dream11.Fragments.Home;
import com.example.dream11.Fragments.MyMatches;
import com.example.dream11.Fragments.Profile;
import com.example.dream11.PropertyClasses.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNAV);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                if (item.getItemId() == R.id.home) {
                    selectedFragment = new Home();
                } else if (item.getItemId() == R.id.mymatches) {
                    selectedFragment = new MyMatches();
                } else if (item.getItemId() == R.id.profile) {
                    selectedFragment = new Profile();
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, selectedFragment).commit();
                return true;
            }
        });
        Fragment defaultFragment = new Home();
        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, defaultFragment).commit();

        ImageView add_money = findViewById(R.id.add_money_btn);
        add_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddCash.class);
                startActivity(intent);
            }
        });

        TextView wallet_amount = findViewById(R.id.wallet_amount);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(mAuth.getUid());
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);
                    if (user != null) {
                        Float money = snapshot.child("wallet").getValue(Float.class);
                        wallet_amount.setText("₹ " + money);
                    }
                } else {
                    Log.d("UserInfo", "User data does not exist");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}