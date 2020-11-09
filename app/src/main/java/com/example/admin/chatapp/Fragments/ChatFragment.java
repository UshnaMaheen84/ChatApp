package com.example.admin.chatapp.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.admin.chatapp.Adapters.chatAdapter;
import com.example.admin.chatapp.ModelClasses.ChatMessage;
import com.example.admin.chatapp.ModelClasses.modelUser;
import com.example.admin.chatapp.R;
import com.example.admin.chatapp.common.Common;
import com.example.admin.chatapp.myInterface;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {
    RecyclerView recyclerView;
    chatAdapter myAdapter;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference2;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    ArrayList<modelUser> arrayList = new ArrayList<modelUser>();
    myInterface myinterfaced;
    String my_id, userId;
    List<String> usersList = new ArrayList<>();

    public void setMyinterfaced(myInterface myinterfaced) {
        this.myinterfaced = myinterfaced;
    }

    public ChatFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        my_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("UserTable");
        databaseReference2 = FirebaseDatabase.getInstance().getReference().child("Messages").child(my_id);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        myAdapter = new chatAdapter(arrayList, getContext());
        recyclerView.setAdapter(myAdapter);

//        if (arrayList.size() <= 0)
            prepareData();



        return view;
    }

    private void prepareData() {
        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot data : dataSnapshot.getChildren()) {

                    userId = data.getKey();
                    usersList.add(userId);
                    Log.e("userid", userId);
                    Log.e("userlistsize", "" + String.valueOf(usersList.size()));

                }
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        firebaseUser = firebaseAuth.getCurrentUser();

                        for (DataSnapshot data : dataSnapshot.getChildren()) {

                            modelUser model = data.getValue(modelUser.class);
                            for (String userlist : usersList){
                                if (firebaseUser != null) {
                                    if (model != null && model.getId().equals(userlist)) {
                                        UserAfterFilter(model, data.getKey());
                                    }
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

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Log.e("Exception", databaseError.getMessage());

            }
        });





    }

    private void UserAfterFilter(modelUser model, String key) {
        if (!Common.ChatHashMap.containsKey(key)) {
            arrayList.add(model);
            myAdapter.notifyDataSetChanged();
            Common.ChatHashMap.put(key, model);
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        Common.ChatHashMap.clear();
        arrayList.clear();
        myAdapter.notifyDataSetChanged();

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("status").setValue("offline");
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        Common.ChatHashMap.clear();
        arrayList.clear();
        myAdapter.notifyDataSetChanged();

        prepareData();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("status").setValue("online");
        }
    }
}
