package com.example.admin.chatapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.admin.chatapp.Adapters.FriendsAdapter;
import com.example.admin.chatapp.ModelClasses.FriendsModel;
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

public class Friends extends AppCompatActivity {


    Toolbar toolbar;

    RecyclerView recyclerView;
    FriendsAdapter myAdapter;
    DatabaseReference databaseReference2;
    Query databaseReference;
    ArrayList<FriendsModel> arrayList = new ArrayList<>();
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    String my_id, userKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        toolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        toolbar.setTitle("My Friends");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);

        arrayList.clear();

        firebaseAuth = FirebaseAuth.getInstance();
        my_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Friends").child(my_id).orderByChild("name");
        databaseReference2 = FirebaseDatabase.getInstance().getReference().child("UserTable");

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myAdapter = new FriendsAdapter(arrayList,this);
        recyclerView.setAdapter(myAdapter);

        if (arrayList.size() <= 0)
            prepareData();
    }

    private void prepareData() {

        arrayList.clear();
        myAdapter.notifyDataSetChanged();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {

                    userKey = "" + data.getKey();
                    FriendsModel model = data.getValue(FriendsModel.class);
                    UserAfterFilter(model, data.getKey());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Exception", databaseError.getMessage());

            }
        });


    }

    private void UserAfterFilter(FriendsModel model, String key) {

        if (!Common.FriendHashMap.containsKey(key)) {
            arrayList.add(model);
            myAdapter.notifyDataSetChanged();
            Common.FriendHashMap.put(key, model);
        }
    }


    @Override
    public void onStop() {
        super.onStop();

        Common.FriendHashMap.clear();
        arrayList.clear();
        myAdapter.notifyDataSetChanged();

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            databaseReference2.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("status").setValue("offline");
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        Common.FriendHashMap.clear();
        arrayList.clear();
        myAdapter.notifyDataSetChanged();
        prepareData();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            databaseReference2.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("status").setValue("online");
        }
    }
}
