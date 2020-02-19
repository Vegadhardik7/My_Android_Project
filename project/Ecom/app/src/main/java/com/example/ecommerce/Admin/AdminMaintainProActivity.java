package com.example.ecommerce.Admin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminMaintainProActivity extends AppCompatActivity {

    private Button applychangesBtn,deleteprobtn;
    private EditText name,price,description;
    private ImageView imageView;
    private String proID="";
    private DatabaseReference productRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintain_pro);

        proID=getIntent().getStringExtra("pid");
        productRef= FirebaseDatabase.getInstance().getReference().child("Products").child(proID);

        deleteprobtn=(Button)findViewById(R.id.pro_delete_btn);
        applychangesBtn=(Button)findViewById(R.id.pro_maintain_btn);
        name=findViewById(R.id.pro_maintain_name);
        price=findViewById(R.id.pro_maintain_price);
        description=findViewById(R.id.pro_maintain_description);
        imageView=findViewById(R.id.pro_maintain_image);

        displaySpecificProInfo();

        applychangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                applyChanges();
            }
        });

        deleteprobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                deleteThisProduct();
            }
        });
    }

    private void deleteThisProduct()
    {
        productRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                Intent intent=new Intent(AdminMaintainProActivity.this, AdminCategorieActivity.class);
                startActivity(intent);
                finish();

                Toast.makeText(AdminMaintainProActivity.this, "This Product Is Deleted Successfully...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void applyChanges()
    {
        String pName=name.getText().toString();
        String pPrice=price.getText().toString();
        String pDescription=description.getText().toString();

        if(pName.equals(""))
        {
            Toast.makeText(this,"Write Down Product Name.",Toast.LENGTH_LONG).show();
        }
        else if(pPrice.equals(""))
        {
            Toast.makeText(this,"Write Down Product Price.",Toast.LENGTH_LONG).show();
        }
        else if(pDescription.equals(""))
        {
            Toast.makeText(this,"Write Down Product Description.",Toast.LENGTH_LONG).show();
        }
        else
        {
            HashMap<String, Object> productMap = new HashMap<>();
            productMap.put("pid", proID);
            productMap.put("description", pDescription);
            productMap.put("price", pPrice);
            productMap.put("pname", pName);

            productRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(AdminMaintainProActivity.this,"Changes Applied Successfully...",Toast.LENGTH_LONG).show();

                        Intent intent=new Intent(AdminMaintainProActivity.this,AdminCategorieActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });

        }
    }
    private void displaySpecificProInfo()
    {
        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    String pName=dataSnapshot.child("pname").getValue().toString();
                    String pPrice=dataSnapshot.child("price").getValue().toString();
                    String pDescription=dataSnapshot.child("description").getValue().toString();
                    String pImage=dataSnapshot.child("image").getValue().toString();

                    name.setText(pName);
                    price.setText(pPrice);
                    description.setText(pDescription);

                    Picasso.get().load(pImage).into(imageView);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
    }
}
