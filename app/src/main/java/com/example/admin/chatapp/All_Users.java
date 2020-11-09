package com.example.admin.chatapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.admin.chatapp.Adapters.AllUsersAdapter;
import com.example.admin.chatapp.ModelClasses.chatModel;
import com.example.admin.chatapp.ModelClasses.modelUser;
import com.example.admin.chatapp.common.Common;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class All_Users extends AppCompatActivity {

    Toolbar toolbar;

    RecyclerView recyclerView;
    AllUsersAdapter myAdapter;
    DatabaseReference databaseReference;
    Query databaseReference2;
    ArrayList<modelUser> arrayList = new ArrayList<>();
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);

        toolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        toolbar.setTitle("All Users");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("UserTable");
        databaseReference2 = FirebaseDatabase.getInstance().getReference().child("UserTable").orderByChild("name");

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myAdapter = new AllUsersAdapter(arrayList,this);
        recyclerView.setAdapter(myAdapter);

            prepareData();

    }
    private void prepareData() {


        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                firebaseUser = firebaseAuth.getCurrentUser();

                for (DataSnapshot data : dataSnapshot.getChildren()) {

                    modelUser model = data.getValue(modelUser.class);
                    if (firebaseUser != null) {
                        if (model != null && !model.getId().equals(firebaseUser.getUid())) {

                            UserAfterFilter(model, data.getKey());
                        }
                    }
                    myAdapter.notifyDataSetChanged();
                    Log.e("arraylistsize", "" + String.valueOf(arrayList.size()));


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Log.e("Exception", databaseError.getMessage());

            }
        });


    }
    private void UserAfterFilter(modelUser model, String key) {
        if (!Common.UserHashMap.containsKey(key)) {
            arrayList.add(model);
            myAdapter.notifyDataSetChanged();
            Common.UserHashMap.put(key, model);
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        Common.UserHashMap.clear();
        arrayList.clear();
        myAdapter.notifyDataSetChanged();

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("status").setValue("offline");
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        Common.UserHashMap.clear();
        arrayList.clear();
        myAdapter.notifyDataSetChanged();
        prepareData();


        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("status").setValue("online");
        }
    }
}
