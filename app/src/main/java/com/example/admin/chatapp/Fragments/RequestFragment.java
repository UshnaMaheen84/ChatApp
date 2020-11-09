package com.example.admin.chatapp.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.admin.chatapp.Adapters.RequestAdapter;
import com.example.admin.chatapp.ModelClasses.RequestModel;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestFragment extends Fragment {

    private RecyclerView recyclerView;
    private RequestAdapter myAdapter;
    myInterface myinterfaced;
    FloatingActionButton fab;
    String my_id;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference2;
    ArrayList<RequestModel> arrayList = new ArrayList<>();
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    String myEmail;
    String userKey;

    public RequestFragment() {
        // Required empty public constructor
    }

    public void setMyinterfaced(myInterface myinterfaced) {
        this.myinterfaced = myinterfaced;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_request, container, false);


        firebaseAuth = FirebaseAuth.getInstance();
        my_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Friend_Request").child(my_id);
        databaseReference2 = FirebaseDatabase.getInstance().getReference().child("UserTable");

        myEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        Log.e("myid", my_id);


        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        myAdapter = new RequestAdapter(arrayList, getContext(), myEmail);
        recyclerView.setAdapter(myAdapter);

        if (arrayList.size() <= 0)
            prepareData();

        return view;
    }

    private void prepareData() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Common.RequestHashMap.clear();
                arrayList.clear();

                for (DataSnapshot data : dataSnapshot.getChildren()) {

                    RequestModel model = data.getValue(RequestModel.class);
                    UserAfterFilter(model, data.getKey());
                    myAdapter.notifyDataSetChanged();
                    Log.e("requestsize", "" + String.valueOf(arrayList.size()));

                    }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Exception", databaseError.getMessage());

            }
        });

    }

    private void UserAfterFilter(RequestModel model, String key) {


        if (!Common.RequestHashMap.containsKey(key)) {

            arrayList.add(model);
            myAdapter.notifyDataSetChanged();
            Common.RequestHashMap.put(key, model);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("logoff", "Null");

        Common.RequestHashMap.clear();
        arrayList.clear();
        myAdapter.notifyDataSetChanged();

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            databaseReference2.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("status").setValue("offline");
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        Log.e("logon", "Null");

        Common.RequestHashMap.clear();
        arrayList.clear();
        myAdapter.notifyDataSetChanged();
        prepareData();

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            databaseReference2.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("status").setValue("online");
        }
    }
}