package com.example.ecommerce;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ecommerce.Prevalent.Prevalent;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmFinalOrderActivity extends AppCompatActivity {

    private EditText name,city,address,pnumber;
    private Button btn;
    private String TotalAmount="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        TotalAmount=getIntent().getStringExtra("Total Price In Rupees : ");
        Toast.makeText(ConfirmFinalOrderActivity.this,"Total Price In Rupees is "+TotalAmount,Toast.LENGTH_LONG).show();

        name=(EditText)findViewById(R.id.edit_name);
        city=(EditText)findViewById(R.id.edit_city);
        address=(EditText)findViewById(R.id.edit_address);
        pnumber=(EditText)findViewById(R.id.edit_num);

        btn=(Button) findViewById(R.id.final_btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Check();
            }
        });
    }

    private void Check()
    {
        if(TextUtils.isEmpty(name.getText().toString()))
        {
            Toast.makeText(this,"Please Provide Your Full Name",Toast.LENGTH_LONG).show();
        }
        else if (TextUtils.isEmpty(city.getText().toString()))
        {
            Toast.makeText(this,"Please Provide Your City",Toast.LENGTH_LONG).show();
        }
        else if (TextUtils.isEmpty(address.getText().toString()))
        {
            Toast.makeText(this,"Please Provide Your Full Address",Toast.LENGTH_LONG).show();
        }
        else if (TextUtils.isEmpty(pnumber.getText().toString()))
        {
            Toast.makeText(this,"Please Provide Your Full Phone Number",Toast.LENGTH_LONG).show();
        }
        else
        {
            ConfirmOrder();
        }
    }

    private void ConfirmOrder()
    {
        final String saveCurrentTime,saveCurrentDate;
        Calendar calfordate= Calendar.getInstance();

        SimpleDateFormat currDate=new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate=currDate.format(calfordate.getTime());

        SimpleDateFormat currTime=new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime=currTime.format(calfordate.getTime());

        final DatabaseReference orderRef= FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.CurrOnlineUser.getPnumber());
        HashMap<String,Object> ordersMap=new HashMap<>();


        ordersMap.put("Total_Amount",TotalAmount);
        ordersMap.put("name",name.getText().toString());
        ordersMap.put("pnumber",pnumber.getText().toString());
        ordersMap.put("date",saveCurrentDate);
        ordersMap.put("time",saveCurrentTime);
        ordersMap.put("address",address.getText().toString());
        ordersMap.put("city",city.getText().toString());
        ordersMap.put("state","Not Shipped");

        orderRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if(task.isSuccessful())
                {
                    FirebaseDatabase.getInstance().getReference().child("Cart List")
                            .child("User View")
                            .child(Prevalent.CurrOnlineUser.getPnumber())
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(ConfirmFinalOrderActivity.this,"Final Order Has Been Placed Successfully",Toast.LENGTH_LONG).show();

                                        Intent intent=new Intent(ConfirmFinalOrderActivity.this,HomeActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                }
            }
        });


    }
}
