package com.example.dream11;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dream11.Fragments.Profile;
import com.example.dream11.PropertyClasses.User;
import com.github.nikartm.button.FitButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddCash extends AppCompatActivity {

    private static final int UPI_PAYMENT_REQUEST_CODE = 1;
    String money;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cash);

        mAuth = FirebaseAuth.getInstance();

        TextView current_balance = findViewById(R.id.current_balance);
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(mAuth.getUid());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);
                    if (user != null) {
                        Float balance_money = snapshot.child("wallet").getValue(Float.class);
                        current_balance.setText("₹ " + balance_money);
                    }
                } else {
                    Log.d("UserInfo", "User data does not exist");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        TextInputEditText amount = findViewById(R.id.amount);
        amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                money = amount.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        FitButton hun = findViewById(R.id.hundred);
        hun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amount.setText("100");
            }
        });
        FitButton fivehun = findViewById(R.id.fivehundred);
        fivehun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amount.setText("500");
            }
        });
        FitButton thou = findViewById(R.id.thousand);
        thou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amount.setText("1000");
            }
        });

        FitButton btn = findViewById(R.id.add_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!amount.getText().toString().isEmpty()) {
                    initiateUpiPayment("rugvedapraj1@oksbi", "Dream11", Float.parseFloat(money));
                } else {
                    Toast.makeText(AddCash.this, "PLEASE ENTER AN AMOUNT", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void initiateUpiPayment(String upiId, String name, float amount) {
        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
                .appendQueryParameter("mc", "")
                .appendQueryParameter("tid", "")
                .appendQueryParameter("tr", "")
                .appendQueryParameter("tn", "")
                .appendQueryParameter("am", String.valueOf(amount))
                .appendQueryParameter("cu", "INR")
                .build();

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, UPI_PAYMENT_REQUEST_CODE);
        } else {
            Toast.makeText(AddCash.this, "No UPI app found, please install one to make payment.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == UPI_PAYMENT_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                float amount_added = Float.parseFloat(money.toString());
                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(mAuth.getUid());
                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Float wallet = snapshot.child("wallet").getValue(Float.class);

                        if (wallet!=null) {
                            float total_amount = wallet + amount_added;
                            userRef.child("wallet").setValue(total_amount);
                        } else {
                            userRef.child("wallet").setValue(amount_added);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            } else if (resultCode == RESULT_CANCELED) {

            } else {
                // Payment failed or was not completed due to an error
                // You can handle the failure here
            }
        }
    }
}