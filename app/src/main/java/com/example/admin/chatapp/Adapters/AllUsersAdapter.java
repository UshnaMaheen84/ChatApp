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

import com.example.admin.chatapp.ModelClasses.modelUser;
import com.example.admin.chatapp.R;
import com.example.admin.chatapp.UserProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AllUsersAdapter extends RecyclerView.Adapter<AllUsersAdapter.MyViewHolder>{

    ArrayList<modelUser> arrayList = new ArrayList<>();
    Context context;
    FirebaseUser currentuser;

    public AllUsersAdapter(ArrayList<modelUser> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
        currentuser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_users_recyclerview_output, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final modelUser myData = arrayList.get(position);


        if (myData.getImage().equals("defaultImage")) {

            Picasso.get().load(R.drawable.noimage).into(holder.imageView);

        } else {
            Picasso.get().load(myData.getImage()).into(holder.imageView);
        }

        holder.name.setText(myData.getName());
        holder.status.setText(myData.getUser_status());

        holder.holderview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UserProfile.class);
                intent.putExtra("name", myData.getName());
                intent.putExtra("email", myData.getEmail());
                intent.putExtra("num", myData.getNum());
                intent.putExtra("userStatus", myData.getUser_status());
                intent.putExtra("user_id", myData.getId());
                intent.putExtra("image", myData.getImage());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        View holderview;
        ImageView imageView;
        TextView name, status;

        public MyViewHolder(View itemView) {
            super(itemView);
            holderview = itemView;
            imageView = (ImageView) itemView.findViewById(R.id.image);
            name = (TextView) itemView.findViewById(R.id.userName);
            status = itemView.findViewById(R.id.userStatus);
        }
    }
}
