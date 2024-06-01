package com.example.dream11;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dream11.Authentication.UserInfo;
import com.example.dream11.PropertyClasses.User;
import com.github.nikartm.button.FitButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditUser extends AppCompatActivity {

    TextInputEditText username, email, mobile, age;
    FirebaseAuth mAuth;
    private static final int REQUEST_IMAGE_PICK = 1;
    String profile_url;
    CircleImageView img;
    DatabaseReference pathref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        mAuth = FirebaseAuth.getInstance();
        pathref = FirebaseDatabase.getInstance().getReference("users/"+ mAuth.getCurrentUser().getUid());

        username = findViewById(R.id.username);
        email = findViewById(R.id.emailid);
        mobile = findViewById(R.id.mobile);
        age = findViewById(R.id.age);
        img = findViewById(R.id.profile_img);

        pathref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String user_name = snapshot.child("username").getValue(String.class);
                    String email_id = snapshot.child("email").getValue(String.class);
                    String pro_img = snapshot.child("profile_pic").getValue(String.class);
                    String mobile_no = snapshot.child("mobile_no").getValue(String.class);
                    String age_user = snapshot.child("age").getValue(String.class);

                    username.setText(user_name);
                    email.setText(email_id);
                    mobile.setText(mobile_no);
                    age.setText(age_user);

                    Glide.with(EditUser.this)
                            .load(pro_img)
                            .error(R.drawable.default_profile)
                            .into(img);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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
                if (!userName.isEmpty() && !email_ID.isEmpty() && !ageNo.isEmpty()) {
                    if (profile_url != null && !profile_url.isEmpty()) {
                        StorageReference storageReference = FirebaseStorage.getInstance().getReference("user/"+ userName);
                        storageReference.putFile(Uri.parse(profile_url))
                                .addOnSuccessListener(taskSnapshot -> {
                                    storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                                        pathref.child("username").setValue(userName);
                                        pathref.child("email").setValue(email_ID);
                                        pathref.child("profile_pic").setValue(uri.toString());
                                        pathref.child("mobile_no").setValue(mobileNO);
                                        pathref.child("age").setValue(ageNo);
                                        Intent intent = new Intent(EditUser.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    });
                                });
                    } else {
                        pathref.child("username").setValue(userName);
                        pathref.child("email").setValue(email_ID);
                        pathref.child("mobile_no").setValue(mobileNO);
                        pathref.child("age").setValue(ageNo);
                        Intent intent = new Intent(EditUser.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Toast.makeText(EditUser.this, "ADD ALL THE REQUIRED DETAILS", Toast.LENGTH_SHORT).show();
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
                img.setImageURI(selectedImageUri);
                profile_url = String.valueOf(selectedImageUri);
            }
        }
    }
}