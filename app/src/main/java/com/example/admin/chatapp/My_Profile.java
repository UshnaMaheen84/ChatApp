package com.example.admin.chatapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.chatapp.ModelClasses.modelUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class My_Profile extends AppCompatActivity {
    TextView mName, mNum, mEmail, mStatus;
    CircleImageView mImage;
    String uid;
    private Button ChangeStatus;
    private Button change_image;
    private static final int GALLERY_PICK = 1;
    StorageReference storageReference;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference2;
    FirebaseUser firebaseUser;
    Uri uri;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        mName = (TextView) findViewById(R.id.userName);
        mNum = (TextView) findViewById(R.id.userNum);
        mEmail = (TextView) findViewById(R.id.userEmail);
        mStatus = (TextView) findViewById(R.id.userStatus);
        mImage = findViewById(R.id.imageView);
        ChangeStatus = (Button) findViewById(R.id.btn_status);
        change_image = (Button) findViewById(R.id.btn_image);


        firebaseAuth = FirebaseAuth.getInstance();
        uid = firebaseAuth.getCurrentUser().getUid();
        storageReference = FirebaseStorage.getInstance().getReference("Images");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("UserTable");
        databaseReference2 = FirebaseDatabase.getInstance().getReference().child("UserTable").child(uid);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                firebaseUser = firebaseAuth.getCurrentUser();

                for (DataSnapshot data : dataSnapshot.getChildren()) {


                    modelUser model = data.getValue(modelUser.class);
                    if (firebaseUser != null) {
                        if (model.getId().equals(firebaseUser.getUid())) {
                            mName.setText(model.getName());
                            mEmail.setText(model.getEmail());
                            mNum.setText(model.getNum());
                            mStatus.setText(model.getUser_status());
                            if (model.getImage().equals("defaultImage")) {

                                Picasso.get().load(R.drawable.noimage).into(mImage);

                            } else {
                                Picasso.get().load(model.getImage()).into(mImage);
                            }
                        }
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Log.e("Exception", databaseError.getMessage());

            }
        });

        ChangeStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String status = mStatus.getText().toString();

                Intent intent = new Intent(My_Profile.this, StatusActivity.class);
                intent.putExtra("status_value", status);

                startActivity(intent);
            }
        });


        change_image = (Button) findViewById(R.id.btn_image);
        change_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent galleryintent = new Intent();
                galleryintent.setType("image/*");
                galleryintent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryintent, "SELECT IMAGE"), GALLERY_PICK);

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK) {

            progressDialog = new ProgressDialog(My_Profile.this);
            progressDialog.setTitle("Uploading Image");
            progressDialog.setMessage("Please Wait ");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            uri = data.getData();

            final String imageName = UUID.randomUUID().toString();



            storageReference.child(imageName).putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        storageReference.child(imageName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                databaseReference2.child("image").setValue(uri.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful())
                                        {
                                            progressDialog.dismiss();

                                            Toast.makeText(My_Profile.this,
                                                    "image uploaded successfully...", Toast.LENGTH_SHORT).show();

                                        }

                                    }
                                });


                            }
                        });
                    }
                }
            });


        }

    }
}
