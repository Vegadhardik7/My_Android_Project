package com.example.ecommerce;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Admin.AdminCategorieActivity;
import com.example.ecommerce.Model.Users;
import com.example.ecommerce.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private EditText InputNum,InputPass;
    private Button loginbtn;
    private ProgressDialog loadingBar;
    private CheckBox chkbox;
    private String parentdbName="Users";
    private TextView AdminsLinks,NotAdminsLinks,forgetpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        forgetpass=(TextView)findViewById(R.id.txt2);
        loginbtn=(Button)findViewById(R.id.loginbtn);
        InputPass=(EditText)findViewById(R.id.loginpass);
        InputNum=(EditText)findViewById(R.id.phonenumber);
        loadingBar=new ProgressDialog(this);
        chkbox=(CheckBox)findViewById(R.id.chkbx1);
        AdminsLinks=(TextView)findViewById(R.id.adm);
        NotAdminsLinks=(TextView)findViewById(R.id.notadm);

        Paper.init(this);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
        AdminsLinks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginbtn.setText("Login Admin");
                AdminsLinks.setVisibility(View.INVISIBLE);
                NotAdminsLinks.setVisibility(View.VISIBLE);
                parentdbName="Admins";
            }
        });

        NotAdminsLinks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginbtn.setText("Login");
                AdminsLinks.setVisibility(View.VISIBLE);
                NotAdminsLinks.setVisibility(View.INVISIBLE);
                parentdbName="Users";
            }
        });

        forgetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,ResetPasswordActivity.class);
                intent.putExtra("check","login");
                startActivity(intent);
            }
        });


    }
    private void loginUser()
    {
        String phone= InputNum.getText().toString();
        String pass= InputPass.getText().toString();

        if(TextUtils.isEmpty(phone))
        {
            Toast.makeText(this,"Please Enter Your Phone Number",Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(pass))
        {
            Toast.makeText(this,"Please Enter Your Password",Toast.LENGTH_LONG).show();
        }
        else
        {
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please Wait...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            AllowAccessToAccount(phone,pass);
        }
    }

    private void AllowAccessToAccount(final String phone, final String pass) {

        if(chkbox.isChecked())
        {
            Paper.book().write(Prevalent.UserPhoneKey,phone);
            Paper.book().write(Prevalent.UserPasswordKey,pass);
        }

        final DatabaseReference Rootref;
        Rootref= FirebaseDatabase.getInstance().getReference();

        Rootref.addListenerForSingleValueEvent(new ValueEventListener() {



            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {


                if(dataSnapshot.child(parentdbName).child(phone).exists())
                {
                    Users usersData=dataSnapshot.child(parentdbName).child(phone).getValue(Users.class);

                    if(usersData.getPnumber().equals(phone))
                    {
                        if(usersData.getPass().equals(pass))
                        {

                            if(parentdbName.equals("Admins"))
                            {
                                Intent intent=new Intent(LoginActivity.this, AdminCategorieActivity.class);
                                startActivity(intent);

                                Toast.makeText(LoginActivity.this," Welcome Admin ",Toast.LENGTH_LONG).show();
                                loadingBar.dismiss();
                            }
                            else if(parentdbName.equals("Users"))
                            {
                                Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
                                startActivity(intent);


                                Toast.makeText(LoginActivity.this," Welcome User ",Toast.LENGTH_LONG).show();
                                Prevalent.setCurrOnlineUser(usersData);
                                loadingBar.dismiss();
                            }

                        }
                        else
                        {
                            loadingBar.dismiss();
                            Toast.makeText(LoginActivity.this," Incorrect Password ",Toast.LENGTH_LONG).show();

                        }
                    }

                }
                else
                {
                    Toast.makeText(LoginActivity.this,"Account With This Number "+phone+" Do Not Exists",Toast.LENGTH_LONG).show();
                    loadingBar.dismiss();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
