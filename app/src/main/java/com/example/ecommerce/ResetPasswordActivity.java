package com.example.ecommerce;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ResetPasswordActivity extends AppCompatActivity {

    private String check="";
    private TextView pgtitle,titlequestion;
    private EditText pnumberEditText,q1EditText,q2EditText;
    private Button verifyBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        check=getIntent().getStringExtra("check");
        pgtitle=findViewById(R.id.pgTitle);
        pnumberEditText=findViewById(R.id.security_pnumber);
        q1EditText=findViewById(R.id.question_1);
        q2EditText=findViewById(R.id.question_2);
        titlequestion=findViewById(R.id.textsecurity);
        verifyBtn=findViewById(R.id.verify_btn);
    }

    @Override
    protected void onStart() {
        super.onStart();
        pnumberEditText.setVisibility(View.GONE);
        if(check.equals("setting"))
        {
            pgtitle.setText("Set Questions");
            titlequestion.setText("Set Answers For The Following Security Questions");
            verifyBtn.setText("set");
            DisplayPreAns();
            verifyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    setAnswers();
                }
            });
        }
        else if(check.equals("login"))
        {
            pnumberEditText.setVisibility(View.VISIBLE);
            verifyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    verifyUser();
                }
            });
        }
    }

    private void setAnswers()
    {
        String answer1= q1EditText.getText().toString();
        String answer2= q2EditText.getText().toString().toLowerCase();

        if(q1EditText.equals("") && q2EditText.equals(""))
        {
            Toast.makeText(ResetPasswordActivity.this,"Please Answer Both The Questions",Toast.LENGTH_SHORT).show();
        }
        else
        {
            DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Users")
                    .child(Prevalent.CurrOnlineUser.getPnumber());

            HashMap<String, Object> userdatamap = new HashMap<>();
            userdatamap.put("answer1",answer1);
            userdatamap.put("answer2",answer2);
            ref.child("Security Question").updateChildren(userdatamap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(ResetPasswordActivity.this,"You Have Answered The Security Questions Successfully...",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(ResetPasswordActivity.this,HomeActivity.class);
                        startActivity(intent);
                    }
                }
            });

        }

    }
    private void DisplayPreAns()
    {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Users")
                .child(Prevalent.CurrOnlineUser.getPnumber());
        ref.child("Security Question").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    String ans1=dataSnapshot.child("answer1").getValue().toString();
                    String ans2=dataSnapshot.child("answer2").getValue().toString();

                    q1EditText.setText(ans1);
                    q2EditText.setText(ans2);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void verifyUser()
    {
        final String phone=pnumberEditText.getText().toString();

        final String answer1= q1EditText.getText().toString();
        final String answer2= q2EditText.getText().toString().toLowerCase();

        if(!phone.equals("") && !answer1.equals("") && !answer2.equals(""))
        {

            final DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Users")
                    .child(phone);

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if(dataSnapshot.exists())
                    {
                        String mPhone=dataSnapshot.child("pnumber").getValue().toString();

                        if(dataSnapshot.hasChild("Security Question"))
                        {
                            String ans1=dataSnapshot.child("Security Question").child("answer1").getValue().toString();
                            String ans2=dataSnapshot.child("Security Question").child("answer2").getValue().toString();

                            if(!ans1.equals(answer1))
                            {
                                Toast.makeText(ResetPasswordActivity.this,"Your PIN Is Incorrect...Please Remember It Properly",Toast.LENGTH_LONG).show();
                            }
                            else if(!ans2.equals(answer2))
                            {
                                Toast.makeText(ResetPasswordActivity.this,"Your Email Is Incorrect...Please Remember It Properly",Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                final AlertDialog.Builder builder=new AlertDialog.Builder(ResetPasswordActivity.this);
                                builder.setTitle("New Password");

                                final EditText newPass=new EditText(ResetPasswordActivity.this);
                                newPass.setHint("Write New Password");
                                builder.setView(newPass);

                                builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        if(!newPass.getText().toString().equals(""))
                                        {
                                            ref.child("pass").setValue(newPass.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task)
                                                {
                                                    if(task.isSuccessful())
                                                    {
                                                        Toast.makeText(ResetPasswordActivity.this,"Password Change Successfully...",Toast.LENGTH_LONG).show();
                                                        Intent intent=new Intent(ResetPasswordActivity.this,LoginActivity.class);
                                                        startActivity(intent);
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i)
                                    {
                                        dialog.cancel();
                                    }
                                });
                                builder.show();
                            }
                        }
                        else
                        {
                            Toast.makeText(ResetPasswordActivity.this,"Sorry...You Have Not Set The Security Questions",Toast.LENGTH_SHORT).show();
                        }

                    }
                    else
                    {
                        Toast.makeText(ResetPasswordActivity.this,"This Phone Number Does Not Exist...6Please Enter Your Correct Phone Number.",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else
        {
            Toast.makeText(ResetPasswordActivity.this,"Please Complete The Form...",Toast.LENGTH_LONG).show();
        }
    }
}
