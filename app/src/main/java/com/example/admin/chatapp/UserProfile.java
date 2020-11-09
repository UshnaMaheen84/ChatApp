package com.example.admin.chatapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.admin.chatapp.ModelClasses.FriendsModel;
import com.example.admin.chatapp.ModelClasses.RequestModel;
import com.example.admin.chatapp.ModelClasses.modelUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfile extends AppCompatActivity {

    CircleImageView imageView;
    TextView userName, userStatus, userEmail, userNum;
    Button frndBtn, unfrndBtn, msgBtn, cnclBtn, acptBtn;
    String name, status, image, email, num, user_id;
    String my_id;
    String senderEmail, senderName, senderStatus, senderImage, senderId, myEmai;
    String rName, rStatus, rImage, rEmail, rUser_id;
    LinearLayout req_layout, friend_layout;

    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference, friendReference, reqReference;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        my_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        friend_layout = findViewById(R.id.friend_layout);
        req_layout = findViewById(R.id.cancel_request_layout);

        imageView = findViewById(R.id.imageView);
        userName = findViewById(R.id.user_name);
        userEmail = findViewById(R.id.user_email);
        userNum = findViewById(R.id.user_num);
        userStatus = findViewById(R.id.user_status);
        frndBtn = findViewById(R.id.friend_request_button);
        unfrndBtn = findViewById(R.id.unfriend_button);
        msgBtn = findViewById(R.id.message_button);
        cnclBtn = findViewById(R.id.cancel_request_button);
        acptBtn = findViewById(R.id.Accept_request_button);

        name = getIntent().getExtras().getString("name");
        status = getIntent().getExtras().getString("userStatus");
        image = getIntent().getExtras().getString("image");
        email = getIntent().getExtras().getString("email");
        num = getIntent().getExtras().getString("num");
        user_id = getIntent().getExtras().getString("user_id");

        if (image.equals("defaultImage")) {
            Picasso.get().load(R.drawable.noimage).into(imageView);
        } else {
            Picasso.get().load(image).into(imageView);
        }

        userName.setText(name);
        userEmail.setText(email);
        userNum.setText(num);
        userStatus.setText(status);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("UserTable");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                firebaseUser = firebaseAuth.getCurrentUser();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    modelUser model = data.getValue(modelUser.class);
                    if (firebaseUser != null) {
                        if (model.getId().equals(firebaseUser.getUid())) {

                            senderName = model.getName();
                            senderEmail = model.getEmail();
                            myEmai=senderEmail;
                            senderImage = model.getImage();
                            senderStatus = model.getUser_status();
                            senderId = model.getId();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Log.e("Exception", databaseError.getMessage());

            }
        });

        friendReference = FirebaseDatabase.getInstance().getReference().child("Friends").child(my_id);
        friendReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {

                    if (data.getKey().equals(user_id)) {

                        frndBtn.setVisibility(View.GONE);
                        friend_layout.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        reqReference = FirebaseDatabase.getInstance().getReference().child("Friend_Request").child(my_id);
        reqReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {

                    if (data.getKey().equals(user_id)) {
                        RequestModel model = data.getValue(RequestModel.class);
                        if(model != null && !model.getSenderEmail().equals(myEmai)){
                            Log.e("myEmail",myEmai);
                            Log.e("senderEmail",model.getSenderEmail().toString());
                            acptBtn.setVisibility(View.VISIBLE);
                        }

                        frndBtn.setVisibility(View.GONE);
                        req_layout.setVisibility(View.VISIBLE);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        frndBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                frndBtn.setVisibility(View.GONE);

                rName = name;
                rStatus = status;
                rImage = image;
                rEmail = email;
                rUser_id = user_id;

                final RequestModel user = new RequestModel(senderEmail, senderName, senderStatus, senderImage, senderId,
                        rName, rStatus, rImage, rEmail, rUser_id);

                FirebaseDatabase.getInstance().getReference("Friend_Request").child(my_id).child(user_id).setValue
                        (user).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });

                FirebaseDatabase.getInstance().getReference("Friend_Request").child(user_id).child(my_id).setValue
                        (user).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });

            }
        });

        cnclBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseDatabase.getInstance().getReference("Friend_Request").child(my_id).child(user_id).removeValue();
                FirebaseDatabase.getInstance().getReference("Friend_Request").child(user_id).child(my_id).removeValue();

                frndBtn.setVisibility(View.VISIBLE);
                req_layout.setVisibility(View.GONE);

            }
        });

        acptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseDatabase.getInstance().getReference("Friends").child(my_id).child(user_id).setValue
                        (new FriendsModel(name,email,image,user_id,status));

                FirebaseDatabase.getInstance().getReference("Friends").child(user_id).child(my_id).setValue
                        (new FriendsModel(senderName,senderEmail,senderImage,my_id,senderStatus));

                FirebaseDatabase.getInstance().getReference("Friend_Request").child(my_id).child(user_id).removeValue();
                FirebaseDatabase.getInstance().getReference("Friend_Request").child(user_id).child(my_id).removeValue();

                friend_layout.setVisibility(View.VISIBLE);
                req_layout.setVisibility(View.GONE);

            }
        });

        unfrndBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseDatabase.getInstance().getReference("Friends").child(my_id).child(user_id).removeValue();
                FirebaseDatabase.getInstance().getReference("Friends").child(my_id).child(user_id).removeValue();

                frndBtn.setVisibility(View.VISIBLE);
                friend_layout.setVisibility(View.GONE);
            }
        });

        msgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfile.this, ChatWindow.class);
                intent.putExtra("name", name);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
            }
        });
    }
}