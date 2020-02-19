package com.example.ecommerce;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {


    private Button createacc;
    private EditText uname,pnum,upass;
    private ProgressDialog loadingbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        createacc=(Button)findViewById(R.id.resbtn);
        uname=(EditText)findViewById(R.id.resname);
        pnum=(EditText) findViewById(R.id.resnumber);
        upass=(EditText)findViewById(R.id.respass);
        loadingbar=new ProgressDialog(this);
        createacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });
    }
    public void createAccount(){
        String name = uname.getText().toString();
        String pnumber = pnum.getText().toString();
        String pass = upass.getText().toString();


        if(TextUtils.isEmpty(name)){
            Toast.makeText(this,"Please Enter UserName",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(pnumber)){
            Toast.makeText(this,"Please Enter Phone Number",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(pass)){
            Toast.makeText(this,"Please Enter Password",Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingbar.setTitle("Create Account");
            loadingbar.setMessage("Please Wait...");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();

            ValidatePhoneNumber(name,pnumber,pass);
        }
    }

    private void ValidatePhoneNumber(final String name, final String pnumber, final String pass) {

        final DatabaseReference Rootref;
        Rootref= FirebaseDatabase.getInstance().getReference();

        Rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!(dataSnapshot.child("Users").child(pnumber).exists())){

                    HashMap<String, Object> userdatamap = new HashMap<>();
                    userdatamap.put("pnumber",pnumber);
                    userdatamap.put("pass",pass);
                    userdatamap.put("name",name);


                    Rootref.child("Users").child(pnumber).updateChildren(userdatamap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(RegisterActivity.this,"Congratulations Your Account Has Created Successfully.",Toast.LENGTH_SHORT).show();
                                        loadingbar.dismiss();
                                        Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                                        startActivity(intent);

                                    }
                                    else{
                                        loadingbar.dismiss();
                                        Toast.makeText(RegisterActivity.this,"Network Error: Please Try Again",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else{
                    Toast.makeText(RegisterActivity.this,"This "+pnumber+" Already Exist.",Toast.LENGTH_SHORT).show();
                    loadingbar.dismiss();
                    Toast.makeText(RegisterActivity.this,"This Number Is Already In Use, Please Use Another Mobile Number",Toast.LENGTH_SHORT).show();

                    Intent intent=new Intent(RegisterActivity.this,MainActivity.class);
                    startActivity(intent);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
