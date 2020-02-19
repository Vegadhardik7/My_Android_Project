package com.example.ecommerce;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Prevalent.Prevalent;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private CircleImageView profileImageView;
    private EditText userNameEditText,userPhoneNumber,addressEditText;
    private TextView profileChangeTextbtn,closeTextbtn,saveTextButton;
    private Button securitybtn;
    private Uri imageUri;
    private String myUrl="";
    private StorageReference storageProfilePictureRef;
    private String checker="";
    private StorageTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        storageProfilePictureRef= FirebaseStorage.getInstance().getReference().child("Profile Pictures");

        profileImageView=(CircleImageView)findViewById(R.id.setting_profile_image);

        userNameEditText=(EditText)findViewById(R.id.setting_User_Name);
        userPhoneNumber=(EditText)findViewById(R.id.setting_phone_number);
        addressEditText=(EditText)findViewById(R.id.setting_address);
        securitybtn=(Button)findViewById(R.id.security_question_btn);
        profileChangeTextbtn=(TextView)findViewById(R.id.profile_change);
        closeTextbtn=(TextView)findViewById(R.id.close_settings);
        saveTextButton=(TextView)findViewById(R.id.update_settings);

        userInfoDisplay(profileImageView,userNameEditText,userPhoneNumber,addressEditText);
        Picasso.get().load(Prevalent.CurrOnlineUser.getImage()).placeholder(R.drawable.profile);

        securitybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(SettingsActivity.this,ResetPasswordActivity.class);
                intent.putExtra("check","setting");
                startActivity(intent);
            }
        });
        closeTextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        saveTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(checker.equals("clicked"))
                {
                    userInfoSaved();
                }
                else
                {
                    updateOnlyUSerInfo();
                }
            }
        });

        profileChangeTextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checker="clicked";

                CropImage.activity(imageUri)
                        .setAspectRatio(1,1)
                        .start(SettingsActivity.this);
            }
        });
    }

    private void updateOnlyUSerInfo()
    {
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Users");
        HashMap<String,Object> userMap=new HashMap<>();
        userMap.put("name",userNameEditText.getText().toString());
        userMap.put("address",addressEditText.getText().toString());
        userMap.put("PhoneOrder",userPhoneNumber.getText().toString());
        ref.child(Prevalent.CurrOnlineUser.getPnumber()).updateChildren(userMap);

        startActivity(new Intent(SettingsActivity.this,HomeActivity.class));
        Toast.makeText(SettingsActivity.this,"Profile Image Updated Successfully",Toast.LENGTH_LONG).show();
        finish();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK && data!=null)
        {
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            imageUri = result.getUri();
            profileImageView.setImageURI(imageUri);
        }
        else
        {
            Toast.makeText(this,"Error, Try Again",Toast.LENGTH_LONG).show();

            startActivity(new Intent(SettingsActivity.this,MainActivity.class));
            finish();
        }
    }

    private void userInfoSaved()
    {
        if(TextUtils.isEmpty(userNameEditText.getText().toString()))
        {
            Toast.makeText(this,"Error: User Name Is Mandatory",Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(userPhoneNumber.getText().toString()))
        {
            Toast.makeText(this,"Error: Phone Number Is Mandatory",Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(addressEditText.getText().toString()))
        {
            Toast.makeText(this,"Error: Address Is Mandatory",Toast.LENGTH_LONG).show();
        }
        else if(checker.equals("clicked"))
        {
            uploadImage();
        }

    }

    private void uploadImage()
    {
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Profile Update");
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if(imageUri!=null)
        {
            final StorageReference fileRef=storageProfilePictureRef.child(Prevalent.CurrOnlineUser.getPnumber() + ".jpg");
            uploadTask=fileRef.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception
                {
                    if(!task.isSuccessful())
                    {
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful())
                    {
                        Uri downloadUrl=task.getResult();
                        myUrl=downloadUrl.toString();

                        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Users");
                        HashMap<String,Object> userMap=new HashMap<>();
                        userMap.put("name",userNameEditText.getText().toString());
                        userMap.put("address",addressEditText.getText().toString());
                        userMap.put("PhoneOrder",userPhoneNumber.getText().toString());
                        userMap.put("image",myUrl);
                        ref.child(Prevalent.CurrOnlineUser.getPnumber()).updateChildren(userMap);

                        progressDialog.dismiss();
                        startActivity(new Intent(SettingsActivity.this,HomeActivity.class));
                        Toast.makeText(SettingsActivity.this,"Profile Information Updated Successfully",Toast.LENGTH_LONG).show();
                        finish();

                    }
                    else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(SettingsActivity.this,"Error Please Try Again",Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
        else
        {
            Toast.makeText(this,"Image Is Not Selected",Toast.LENGTH_LONG).show();
        }
    }

    private void userInfoDisplay(final CircleImageView profileImageView, final EditText userNameEditText, final EditText userPhoneNumber, final EditText addressEditText)
    {
        DatabaseReference UsersRef =FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.CurrOnlineUser.getPnumber());

        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    if(dataSnapshot.child("image").exists())
                    {
                        String image = dataSnapshot.child("image").getValue().toString();
                        String name = dataSnapshot.child("name").getValue().toString();
                        String pnumber = dataSnapshot.child("pnumber").getValue().toString();
                        String address = dataSnapshot.child("address").getValue().toString();

                        Picasso.get().load(image).into(profileImageView);

                        userNameEditText.setText(name);
                        userPhoneNumber.setText(pnumber);
                        addressEditText.setText(address);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
