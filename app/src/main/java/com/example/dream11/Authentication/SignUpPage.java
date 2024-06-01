package com.example.dream11.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.dream11.PropertyClasses.User;
import com.example.dream11.R;
import com.github.nikartm.button.FitButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpPage extends AppCompatActivity {

    FirebaseAuth mAuth;
    TextInputEditText email, pass, confirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

        mAuth = FirebaseAuth.getInstance();

        FitButton move_to_signUpPage = findViewById(R.id.loginBtn);
        move_to_signUpPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpPage.this, LoginPage.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out_right_activity, R.anim.slide_in_left_activity);
                finish();
            }
        });

        email = findViewById(R.id.emailid);
        pass = findViewById(R.id.password);
        confirm = findViewById(R.id.conpassword);

        FitButton signUp = findViewById(R.id.signUpBtn);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailID = email.getText().toString();
                String password = pass.getText().toString();
                String confirmpass = confirm.getText().toString();

                if (password.equals(confirmpass)) {
                    signUpmethod(emailID, password);
                } else {
                    Toast.makeText(SignUpPage.this, "The Password Doesn't Match", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void signUpmethod(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(SignUpPage.this, UserInfo.class);
                            intent.putExtra("email", email);
                            startActivity(intent);
                            finish();
                        } else {
                            Log.w("failure", task.getException());
                            Toast.makeText(SignUpPage.this, "Failed to create an account !!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}