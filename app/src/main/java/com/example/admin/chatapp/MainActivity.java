package com.example.admin.chatapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.admin.chatapp.Fragments.ChatFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();


    }

    @Override
    protected void onStart() {
        super.onStart();

        if (currentUser == null) {

            startActivity(new Intent(MainActivity.this, StartActivity.class));
            finish();
        } else {
            startActivity(new Intent(MainActivity.this, Home.class));
            finish();
        }
    }


}