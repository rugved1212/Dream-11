package com.example.dream11.Authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.dream11.MainActivity;
import com.example.dream11.PropertyClasses.User;
import com.example.dream11.R;
import com.github.nikartm.button.FitButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserInfo extends AppCompatActivity {

    TextInputEditText username, email, mobile, age;
    FirebaseAuth mAuth;
    private static final int REQUEST_IMAGE_PICK = 1;
    String profile_url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        mAuth = FirebaseAuth.getInstance();

        username = findViewById(R.id.username);
        email = findViewById(R.id.emailid);
        mobile = findViewById(R.id.mobile);
        age = findViewById(R.id.age);
        
        String emailname = getIntent().getStringExtra("email");
        email.setText(emailname);

        ImageView select_img = findViewById(R.id.select_img);
        select_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_IMAGE_PICK);
            }
        });

        FitButton submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = username.getText().toString();
                String email_ID = email.getText().toString();
                String mobileNO = mobile.getText().toString();
                String ageNo = age.getText().toString();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
                if (!userName.isEmpty() && !email_ID.isEmpty() && !ageNo.isEmpty()) {
                    if (profile_url != null && !profile_url.isEmpty()) {
                        StorageReference storageReference = FirebaseStorage.getInstance().getReference("user/"+ userName);
                        storageReference.putFile(Uri.parse(profile_url))
                                .addOnSuccessListener(taskSnapshot -> {
                                   storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                                       User user = new User(email_ID, userName, uri.toString(), mobileNO, ageNo);
                                       ref.child(mAuth.getUid()).setValue(user);
                                       Intent intent = new Intent(UserInfo.this, MainActivity.class);
                                       startActivity(intent);
                                       finish();
                                   });
                                });
                    } else {
                        User user = new User(email_ID, userName, "https://firebasestorage.googleapis.com/v0/b/dream-11-54f35.appspot.com/o/default.png?alt=media&token=13984fff-1841-4ae7-8b1a-ed4614e29e40", mobileNO, ageNo);
                        ref.child(mAuth.getUid()).setValue(user);
                        Intent intent = new Intent(UserInfo.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Toast.makeText(UserInfo.this, "ADD ALL THE REQUIRED DETAILS", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_IMAGE_PICK) {
            if (data != null && data.getData() != null) {
                Uri selectedImageUri = data.getData();
                CircleImageView img = findViewById(R.id.profile_img);
                img.setImageURI(selectedImageUri);
                profile_url = String.valueOf(selectedImageUri);
            }
        }
    }
}