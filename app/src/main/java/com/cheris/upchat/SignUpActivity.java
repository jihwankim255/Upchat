package com.cheris.upchat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cheris.upchat.Model.User;
import com.cheris.upchat.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        binding.signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.emailET.getText().toString().trim(), password = binding.passwordET.getText().toString().trim();

                if (binding.nameET.getText().toString().trim().length() == 0) {
                    Toast.makeText(SignUpActivity.this, "Please enter your name.", Toast.LENGTH_SHORT).show();
                } else if (binding.professionET.getText().toString().trim().length() == 0) {
                    Toast.makeText(SignUpActivity.this, "Please enter your profession.", Toast.LENGTH_SHORT).show();
                } else if (email.length() == 0) {
                    Toast.makeText(SignUpActivity.this, "Please enter your email.", Toast.LENGTH_SHORT).show();
                } else if ( password.length() == 0 ){
                    Toast.makeText(SignUpActivity.this, "Please enter your password.", Toast.LENGTH_SHORT).show();
                } else {
                    auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                User user = new User(binding.nameET.getText().toString().trim(),binding.professionET.getText().toString().trim(),email, password);
                                String id = task.getResult().getUser().getUid();
                                database.getReference().child("Users").child(id).setValue(user);
                                Toast.makeText(SignUpActivity.this, "User Data saved", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                startActivity(intent);

                            } else {
                                Toast.makeText(SignUpActivity.this, "You failed to sign up.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });

        binding.goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}