package com.example.ecommerce;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.ecommerce.Model.Products;
import com.example.ecommerce.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {

    private Button addtoCart;
    private ImageView productImage;
    private ElegantNumberButton numberButton;
    private TextView productPrice,productDescription,productName;
    private String productID="",state="Normal";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        addtoCart=(Button)findViewById(R.id.pd_add_to_cart_btn);
        numberButton=(ElegantNumberButton)findViewById(R.id.number_counter_btn);
        productImage=(ImageView)findViewById(R.id.product_image_details);
        productPrice=(TextView)findViewById(R.id.product_price_details);
        productName=(TextView)findViewById(R.id.product_name_details);
        productDescription=(TextView)findViewById(R.id.product_description_details);
        productID = getIntent().getStringExtra("pid");

        getProductDetails(productID);

        addtoCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                if(state.equals("Order Placed") || state.equals("Order Shipped"))
                {
                    Toast.makeText(ProductDetailsActivity.this,"You can purchase more products, Once your order is Shipped or Confirmed",Toast.LENGTH_LONG).show();
                }
                else
                {
                    AddingToCartList();
                }
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        CheckOrderState();
    }

    private void AddingToCartList()
    {
        String saveCurrentTime,saveCurrentDate;
        Calendar calfordate= Calendar.getInstance();

        SimpleDateFormat currDate=new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate=currDate.format(calfordate.getTime());

        SimpleDateFormat currTime=new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime=currTime.format(calfordate.getTime());

        final DatabaseReference cartListRef=FirebaseDatabase.getInstance().getReference().child("Cart List");
        final HashMap<String,Object> cartMap=new HashMap<>();

        cartMap.put("pid",productID);
        cartMap.put("pname",productName.getText().toString());
        cartMap.put("price",productPrice.getText().toString());
        cartMap.put("date",saveCurrentDate);
        cartMap.put("time",saveCurrentTime);
        cartMap.put("quantity",numberButton.getNumber());
        cartMap.put("discount","");

        cartListRef.child("User View").child(Prevalent.CurrOnlineUser.getPnumber()).child("Products")
                .child(productID)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if(task.isSuccessful()) {
                            cartListRef.child("Admin View").child(Prevalent.CurrOnlineUser.getPnumber()).child("Products")
                                    .child(productID)
                                    .updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                            if(task.isSuccessful()) {

                                                Toast.makeText(ProductDetailsActivity.this,"Added To Cart Successfully",Toast.LENGTH_LONG).show();
                                                Intent intent=new Intent(ProductDetailsActivity.this,HomeActivity.class);
                                                startActivity(intent);
                                            }
                                        }
                                    });
                        }

                    }
                });


    }

    private void getProductDetails(String productID)
    {
        DatabaseReference productRef= FirebaseDatabase.getInstance().getReference().child("Products");
        productRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    Products products=dataSnapshot.getValue(Products.class);
                    productName.setText(products.getPname());
                    productPrice.setText(products.getPrice());
                    productDescription.setText(products.getDescription());
                    Picasso.get().load(products.getImage()).into(productImage);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void CheckOrderState()
    {
        DatabaseReference orderRef=FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.CurrOnlineUser.getPnumber());
        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists())
                {
                    String shippingstate=dataSnapshot.child("state").getValue().toString();


                    if(shippingstate.equals("Shipped"))
                    {
                        state="Order Shipped";
                    }
                    else if(shippingstate.equals("Not Shipped"))
                    {
                        state="Order Placed";
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
