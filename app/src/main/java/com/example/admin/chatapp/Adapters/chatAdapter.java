package com.example.admin.chatapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.chatapp.ChatWindow;
import com.example.admin.chatapp.ModelClasses.ChatMessage;
import com.example.admin.chatapp.ModelClasses.chatModel;
import com.example.admin.chatapp.ModelClasses.modelUser;
import com.example.admin.chatapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class chatAdapter extends RecyclerView.Adapter<chatAdapter.MyViewHolder> {

    ArrayList<modelUser> arrayList = new ArrayList<>();
    Context context;
    DatabaseReference messageReference;
    String lastMessage;
    String my_id;

    public chatAdapter(ArrayList<modelUser> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_recyclerview_output, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final modelUser myData = arrayList.get(position);

        if (myData.getImage().equals("defaultImage")) {

            Picasso.get().load(R.drawable.noimage).into(holder.imageView);

        } else {
            Picasso.get().load(myData.getImage()).into(holder.imageView);
        }

        holder.name.setText(myData.getName());

        if (myData.getStatus().equals("online"))
            holder.online_icon.setVisibility(View.VISIBLE);
        else holder.online_icon.setVisibility(View.INVISIBLE);

        setLastMessage(myData.getId(),holder.msg);

        holder.holderview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatWindow.class);
                intent.putExtra("name", myData.getName());
                intent.putExtra("user_id", myData.getId());
                context.startActivity(intent);
            }
        });
    }

    private void setLastMessage(String id, final TextView msg) {

        my_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        messageReference = FirebaseDatabase.getInstance().getReference("Messages").child(my_id).child(id);

        messageReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot data : dataSnapshot.getChildren()) {

                    ChatMessage model = data.getValue(ChatMessage.class);
                    lastMessage = model.getMessageText();
                    msg.setText(lastMessage);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        View holderview;
        CircleImageView imageView;
        ImageView online_icon;
        TextView name, msg;

        public MyViewHolder(View itemView) {
            super(itemView);
            holderview = itemView;
            imageView = itemView.findViewById(R.id.image);
            online_icon = (ImageView) itemView.findViewById(R.id.online);
            name = (TextView) itemView.findViewById(R.id.tv1);
            msg = (TextView) itemView.findViewById(R.id.tv2);
        }
    }


}