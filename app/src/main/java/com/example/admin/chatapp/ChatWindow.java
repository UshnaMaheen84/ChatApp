package com.example.admin.chatapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.daasuu.bl.ArrowDirection;
import com.example.admin.chatapp.Adapters.MessagesAdapters.MyMsgAdapter;
import com.example.admin.chatapp.Adapters.MessagesAdapters.MyVH;
import com.example.admin.chatapp.ModelClasses.ChatMessage;
import com.example.admin.chatapp.common.Common;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatWindow extends AppCompatActivity {

    FirebaseRecyclerAdapter<ChatMessage, MyVH> adapter;
    FirebaseRecyclerAdapter<ChatMessage, MyVH> adapter2;
    Button submit_button;
    EditText emojiconEditText;
    Toolbar toolbar;
    String username, userId, my_id;
    MyMsgAdapter myMsgAdapter;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    ArrayList<ChatMessage> messagesArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat__window);

        toolbar = (Toolbar) findViewById(R.id.page_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        username = getIntent().getExtras().getString("name");
        userId = getIntent().getExtras().getString("user_id");
        getSupportActionBar().setTitle(username);

        submit_button = findViewById(R.id.submit_button);
        emojiconEditText = findViewById(R.id.emojicon_edit_text);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("UserTable");

        my_id = FirebaseAuth.getInstance().getCurrentUser().getUid();


        displayMessages();

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseDatabase.getInstance().getReference("Messages").child(my_id).child(userId).push().setValue(new ChatMessage
                        (emojiconEditText.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getEmail()));

                 FirebaseDatabase.getInstance().getReference("Messages").child(userId).child(my_id).push().setValue(new ChatMessage
                        (emojiconEditText.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getEmail()));


                emojiconEditText.setText("");
                emojiconEditText.requestFocus();
                Log.e("Sending Message", "successfully send message");

            }
        });

    }


    private void displayMessages() {


        final RecyclerView listOfMessage = findViewById(R.id.list_of_message);
        listOfMessage.setLayoutManager(new LinearLayoutManager(ChatWindow.this));

        Log.e("myid+userid", my_id + " " + userId);

        Query query = FirebaseDatabase.getInstance().getReference("Messages").child(my_id).child(userId);



   FirebaseRecyclerOptions<ChatMessage> options = new FirebaseRecyclerOptions.Builder<ChatMessage>()
                .setQuery(query, ChatMessage.class)
                .build();


        adapter = new FirebaseRecyclerAdapter<ChatMessage, MyVH>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyVH holder, int position, @NonNull ChatMessage model) {
                holder.message.setText(model.getMessageText());
                holder.messageTime.setText(DateFormat.format("dd-MM-yy (HH:mm)", model.getMessageTime()));
                holder.messageUserName.setText(model.getMessageUser());

                if (FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(model.getMessageUser())) {
                    holder.bubbleLayout.setArrowDirection(ArrowDirection.RIGHT);
                    holder.linearLayout.setGravity(Gravity.RIGHT);
                }

                listOfMessage.scrollToPosition(position);
                messagesArrayList.add(model);
                //using mymessageAdapter for recview

                myMsgAdapter.notifyDataSetChanged();
                Log.e("adapter1", "in adapter 1, size of arraylist " + messagesArrayList.size());

            }

            @NonNull
            @Override
            public MyVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                return new MyVH(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.message, viewGroup, false));

            }
        };


        myMsgAdapter = new MyMsgAdapter(this, messagesArrayList);
        listOfMessage.setAdapter(myMsgAdapter);



        adapter.startListening();
//        adapter2.startListening();
         listOfMessage.setAdapter(adapter);

    }

//    private void addMessageAfterFilter(ChatMessage model, String filterKey) {
//        //here add message
//        if (!Common.MsgHashMap.containsKey(filterKey)) {
//            messagesArrayList.add(model);
//            myMsgAdapter.notifyDataSetChanged();
//            Common.MsgHashMap.put(filterKey, model);
//            Log.e("MessageAfterFilter", ""+Common.MsgHashMap.size());
//
//        }else
//            {
//                //do nothing
//                Log.e("MessageAfterFilter", "doing nothing");
//
//            }
//
//    }

    @Override
    protected void onStart() {
        super.onStart();

        Common.MsgHashMap.clear();
        messagesArrayList.clear();
        myMsgAdapter.notifyDataSetChanged();


        if (adapter2 != null && adapter != null) {
            adapter2.startListening();
            adapter.startListening();
        }

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("status").setValue("online");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        Common.MsgHashMap.clear();
        messagesArrayList.clear();
        myMsgAdapter.notifyDataSetChanged();


        if (adapter2 != null && adapter != null) {
            adapter2.stopListening();
            adapter.stopListening();
        }

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("status").setValue("offline");
        }
    }
}
