package com.example.admin.chatapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.chatapp.ModelClasses.FriendsModel;
import com.example.admin.chatapp.ModelClasses.RequestModel;
import com.example.admin.chatapp.ModelClasses.modelUser;
import com.example.admin.chatapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.MyViewHolder> {


    ArrayList<RequestModel> arrayList = new ArrayList<>();
    Context context;
    String uid, myEmail;


    public RequestAdapter(ArrayList<RequestModel> arrayList, Context context, String myEmail) {
        this.arrayList = arrayList;
        this.context = context;
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        this.myEmail=myEmail;

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_recyclerview_output, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        final RequestModel myData = arrayList.get(position);


        Log.e("NAME", ""+myData.getSenderName());

        if (myData.getSenderEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
            holder.req_tv.setText("Friend Request Sent");

            holder.name.setText(myData.getrName());
            holder.email.setText(myData.getrEmail());
            holder.status.setText(myData.getrStatus());
            if (myData.getrImage().equals("defaultImage")) {

                Picasso.get().load(R.drawable.noimage).into(holder.imageView);

            } else {
                Picasso.get().load(myData.getrImage()).into(holder.imageView);
            }
            holder.btnLayout.setVisibility(View.GONE);
        }

        else {
            holder.req_tv.setText("Friend Request Receive");
            holder.name.setText(myData.getSenderName());
            holder.email.setText(myData.getSenderEmail());
            holder.status.setText(myData.getSenderStatus());
            if (myData.getSenderImage().equals("defaultImage")) {

                Picasso.get().load(R.drawable.noimage).into(holder.imageView);

            } else {
                Picasso.get().load(myData.getSenderImage()).into(holder.imageView);
            }

            holder.btnLayout.setVisibility(View.VISIBLE);
        }

        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference("Friends").child(myData.getSenderId()).child(myData.getrUser_id()).setValue
                        (new FriendsModel(myData.getrName(),myData.getrEmail(),myData.getrImage(),myData.getrUser_id(),myData.getrStatus()));

                FirebaseDatabase.getInstance().getReference("Friends").child(myData.getrUser_id()).child(myData.getSenderId()).setValue
                        (new FriendsModel(myData.getSenderName(),myData.getSenderEmail(),myData.getSenderImage(),myData.getSenderId(),myData.getSenderStatus()));

                FirebaseDatabase.getInstance().getReference("Friend_Request").child(myData.getSenderId()).child(myData.getrUser_id()).removeValue().
                        addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(context, "Request Accepted", Toast.LENGTH_SHORT).show();

                            }
                        });
                FirebaseDatabase.getInstance().getReference("Friend_Request").child(myData.getrUser_id()).child(myData.getSenderId()).removeValue().
                        addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        });
                updateArrayList(arrayList.get(holder.getAdapterPosition()), holder.getAdapterPosition());

            }
        });

        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseDatabase.getInstance().getReference("Friend_Request").child(myData.getSenderId()).child(myData.getrUser_id()).removeValue();
                FirebaseDatabase.getInstance().getReference("Friend_Request").child(myData.getrUser_id()).child(myData.getSenderId()).removeValue();
                updateArrayList(arrayList.get(holder.getAdapterPosition()), holder.getAdapterPosition());

            }
        });

    }
    private void updateArrayList(RequestModel model, int position) {

        arrayList.remove(model);
        try {
            notifyItemRemoved(position);
            //  notifyDataSetChanged();
        } catch (Exception e) {

            notifyItemRemoved(position);
            //notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        View holderview;
        CircleImageView imageView;
        TextView name, status, email, req_tv;
        LinearLayout btnLayout;
        Button accept, reject;

        public MyViewHolder(View itemView) {
            super(itemView);
            holderview = itemView;
            imageView = itemView.findViewById(R.id.image);
            name = (TextView) itemView.findViewById(R.id.userName);
            status = itemView.findViewById(R.id.userStatus);
            email = itemView.findViewById(R.id.userEmail);
            req_tv = itemView.findViewById(R.id.req_tv);
            btnLayout = itemView.findViewById(R.id.btns_layout);
            accept = itemView.findViewById(R.id.friend_request_button);
            reject = itemView.findViewById(R.id.cancel_request_button);
        }
    }
}
