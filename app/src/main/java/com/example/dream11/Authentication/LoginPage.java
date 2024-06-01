package com.example.dream11.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.dream11.MainActivity;
import com.example.dream11.R;
import com.github.nikartm.button.FitButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginPage extends AppCompatActivity {

    private FirebaseAuth mAuth;
    TextInputEditText email, pass;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(LoginPage.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        mAuth = FirebaseAuth.getInstance();

        FitButton move_to_signUpPage = findViewById(R.id.signUpBtn);
        move_to_signUpPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginPage.this, SignUpPage.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right_activity, R.anim.slide_out_left_activity);
                finish();
            }
        });

        email = findViewById(R.id.emailid);
        pass = findViewById(R.id.password);

        FitButton loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emaidID = email.getText().toString();
                String password = pass.getText().toString();
                if (!emaidID.isEmpty() && !password.isEmpty()) {
                    loginmethod(emaidID, password);
                } else {
                    Toast.makeText(LoginPage.this, "Plzz enter email and password", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void loginmethod(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(LoginPage.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Log.w("signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginPage.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}