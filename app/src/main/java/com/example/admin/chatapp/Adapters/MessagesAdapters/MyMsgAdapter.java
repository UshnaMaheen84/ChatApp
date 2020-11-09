package com.example.admin.chatapp.Adapters.MessagesAdapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daasuu.bl.ArrowDirection;
import com.daasuu.bl.BubbleLayout;
import com.example.admin.chatapp.ModelClasses.ChatMessage;
import com.example.admin.chatapp.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MyMsgAdapter extends RecyclerView.Adapter<MyMsgAdapter.MyVH> {

    Context context;
    public  ArrayList<ChatMessage> arrayList = new ArrayList<>();

    public MyMsgAdapter(Context context, ArrayList<ChatMessage> arrayList) {
        this.context = context;
        this.arrayList = arrayList;


    }

    @NonNull
    @Override
    public  MyVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message, parent, false);
        return new MyVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyVH holder, int i) {

        ChatMessage model = arrayList.get(i);

        holder.message.setText(model.getMessageText());
        holder.messageTime.setText(DateFormat.format("dd-MM-yy (HH:mm)", model.getMessageTime()));
        holder.messageUserName.setText(model.getMessageUser());

        if (FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(model.getMessageUser())) {
                    holder.bubbleLayout.setArrowDirection(ArrowDirection.RIGHT);
                    holder.linearLayout.setGravity(Gravity.RIGHT);
                }
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyVH extends RecyclerView.ViewHolder {

        public TextView messageUserName, messageTime, message;
        public LinearLayout linearLayout;
        public BubbleLayout bubbleLayout;

        public MyVH(@NonNull View itemView) {
            super(itemView);

            messageUserName = itemView.findViewById(R.id.message_user);
            messageTime = itemView.findViewById(R.id.message_time);
            message = (TextView) itemView.findViewById(R.id.bubbleText);
            linearLayout = itemView.findViewById(R.id.bubbleLinearLayout);
            bubbleLayout = itemView.findViewById(R.id.bubbleLayout);

        }
    }
}