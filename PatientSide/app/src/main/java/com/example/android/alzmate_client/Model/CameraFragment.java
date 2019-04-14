package com.example.android.alzmate_client.Model;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.alzmate_client.Holder.PersonHolder;
import com.example.android.alzmate_client.Holder.TrainingPersonHolder;
import com.example.android.alzmate_client.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class CameraFragment extends android.support.v4.app.Fragment {
    private Button camerabtn;
    private Button trainFaceButton;
    private TextView showPersonCountView;
    private ImageView photoView;
    private ProgressDialog progressDialog;
    private DatabaseReference trainDatabase;
    private static int RESULT_CAPTURE_IMAGE = 0;
    public static Bitmap imageBitmap1;
    public static Bitmap imageBitmap2;
    public static Bitmap imageBitmap3;
    public static Bitmap imageBitmap;
    private FirebaseAuth mAuth;
    private DatabaseReference peopleDatabase;
    private StorageReference mStorageTrainRef;
    Dialog myDialog;
    public String name;
    public String relation;
    public String bio;
    ArrayList<String> imageURL;
    private EditText nameTxt;
    private EditText relationshipTxt;
    private EditText bioTxt;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View retView = inflater.inflate(R.layout.fragment_camera, container, false);
        return retView;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        camerabtn = (Button) view.findViewById(R.id.camera_btn);
        trainFaceButton = (Button) view.findViewById(R.id.train_face_btn);
        showPersonCountView = (TextView) view.findViewById(R.id.person_image_txt);
        photoView = (ImageView) view.findViewById(R.id.image_detect_view);
        myDialog = new Dialog(this.getContext());
        imageURL=new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        progressDialog = (ProgressDialog) new ProgressDialog(this.getContext());
        trainDatabase = FirebaseDatabase.getInstance().getReference().child("train");
        peopleDatabase=FirebaseDatabase.getInstance().getReference().child("PersonAlz").child(user.getUid()).child("known-people");
        mStorageTrainRef = FirebaseStorage.getInstance().getReference();
        camerabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, RESULT_CAPTURE_IMAGE);
            }
        });

        trainFaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imageBitmap1!=null && imageBitmap2!=null && imageBitmap3!=null)
                {
                    ShowPopup();
                }
                else if(imageBitmap1==null)
                {
                    lessImagePopup("three image");
                }
                else if(imageBitmap2==null)
                {
                    lessImagePopup("two image");
                }
                else if(imageBitmap3==null)
                {
                    lessImagePopup("one image");
                }
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==RESULT_CAPTURE_IMAGE && resultCode==RESULT_OK && null!=data)
        {
            if(imageBitmap3!=null)
            {
                imageBitmap1.recycle();
                imageBitmap2.recycle();
                imageBitmap3.recycle();
                showPersonCountView.setText("Add Three Image of Person to train");
            }
            if(imageBitmap1==null)
            {
            imageBitmap1 = (Bitmap) data.getExtras().get("data");
            showPersonCountView.setText("Add Two Image of Person to train");
               // MediaStore.Images.Media.insertImage(getContentResolver(), imageBitmap1, "" , "");
            photoView.setImageBitmap(imageBitmap1);
            }
            else if(imageBitmap2==null)
            {
                imageBitmap2 = (Bitmap) data.getExtras().get("data");
                showPersonCountView.setText("Add One Image of Person to train");
                photoView.setImageBitmap(imageBitmap2);
            }
            else if(imageBitmap3==null)
            {
                imageBitmap3 = (Bitmap) data.getExtras().get("data");
                showPersonCountView.setText("Ready For Training");
                photoView.setImageBitmap(imageBitmap3);
            }

        }
    }
    public void lessImagePopup(String less)
    {
        myDialog.setContentView(R.layout.less_image_popup);
        TextView warning_view=(TextView)myDialog.findViewById(R.id.less_image_txt);
        warning_view.setText("Add more "+less);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }
    public void ShowPopup() {
        myDialog.setContentView(R.layout.train_popup);
        ImageView imageView=(ImageView)myDialog.findViewById(R.id.profile_image);
        nameTxt=(EditText)myDialog.findViewById(R.id.name_get_text);
        relationshipTxt=(EditText)myDialog.findViewById(R.id.relationship_get_text);
        bioTxt=(EditText)myDialog.findViewById(R.id.bio_get_text);
        imageView.setImageBitmap(imageBitmap1);
        TextView txtclose =(TextView) myDialog.findViewById(R.id.txtclose);
        Button confirmBtn=(Button)myDialog.findViewById(R.id.confirm_btn);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name=nameTxt.getText().toString().trim();
                relation=relationshipTxt.getText().toString().trim();
                bio=bioTxt.getText().toString().trim();
                if(TextUtils.isEmpty(name) && TextUtils.isEmpty(relation) && TextUtils.isEmpty(bio))
                {
                    //if email is empty
                    Toast.makeText(getContext(), R.string.fill_up_the_detail,Toast.LENGTH_SHORT).show();
                    //stopping the futher execution
                }
                else {

                   // new trainPeopleGroup().execute();
                    progressDialog.show();
                    ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
                    imageBitmap1.compress(Bitmap.CompressFormat.JPEG, 100, baos1);
                    //byte[] data = baos.toByteArray();
                    ByteArrayInputStream inputStream1 = new ByteArrayInputStream(baos1.toByteArray());
                    mStorageTrainRef.child(name + "" + Integer.toString(1) + ".jpg").putStream(inputStream1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            imageURL.add(downloadUrl.toString());
                            ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
                            imageBitmap2.compress(Bitmap.CompressFormat.JPEG, 100, baos2);
                            //byte[] data = baos.toByteArray();
                            ByteArrayInputStream inputStream2 = new ByteArrayInputStream(baos2.toByteArray());
                            mStorageTrainRef.child(name + "" + Integer.toString(2) + ".jpg").putStream(inputStream2).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                    imageURL.add(downloadUrl.toString());
                                    ByteArrayOutputStream baos3 = new ByteArrayOutputStream();
                                    imageBitmap3.compress(Bitmap.CompressFormat.JPEG, 100, baos3);
                                    //byte[] data = baos.toByteArray();
                                    ByteArrayInputStream inputStream3 = new ByteArrayInputStream(baos3.toByteArray());
                                    mStorageTrainRef.child(name + "" + Integer.toString(3) + ".jpg").putStream(inputStream3).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                            imageURL.add(downloadUrl.toString());
                                            PersonHolder personHolder=new PersonHolder(name,relation,bio,imageURL.get(0));
                                            peopleDatabase.child(name).setValue(personHolder).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(getContext(),"People added to database",Toast.LENGTH_SHORT).show();
                                                    TrainingPersonHolder trainPerson=new TrainingPersonHolder(name,imageURL);
                                                    trainDatabase.child("Love").setValue(trainPerson).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Toast.makeText(getContext(),"Training Complete",Toast.LENGTH_SHORT).show();
                                                            progressDialog.dismiss();
                                                            showPersonCountView.setText("Training Successfully Done");
                                                            myDialog.dismiss();
                                                         //   imageBitmap1.recycle();
                                                          //  imageBitmap2.recycle();
                                                          //  imageBitmap3.recycle();
                                                        }
                                                    });
                                                }

                                            });
                                            //  progressDialog.dismiss();
                                        }
                                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                                    .getTotalByteCount());
                                            progressDialog.setMessage("Uploading");
                                        }
                                    });
                                    // progressDialog.dismiss();
                                }
                            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                            .getTotalByteCount());
                                    progressDialog.setMessage("Uploading");
                                }
                            });
                            //progressDialog.dismiss();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploading");
                        }
                    });
                }


            }
        });
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }
}
