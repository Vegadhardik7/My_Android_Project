package com.example.ecommerce;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Model.Cart;
import com.example.ecommerce.Prevalent.Prevalent;
import com.example.ecommerce.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button nextBtn;
    private TextView TotalAmt,Message1;
    private int TotalPrice=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);


        Message1=(TextView)findViewById(R.id.msg1);
        recyclerView=(RecyclerView)findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        nextBtn=(Button)findViewById(R.id.next_btn);
        TotalAmt=(TextView)findViewById(R.id.relative_textview);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TotalAmt.setText("Total Price In Rupees : "+String.valueOf(TotalPrice));
                Intent intent=new Intent(CartActivity.this,ConfirmFinalOrderActivity.class);
                intent.putExtra("Total Price In Rupees : ",String.valueOf(TotalPrice));
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        CheckOrderState();
        final DatabaseReference cartListRef= FirebaseDatabase.getInstance().getReference().child("Cart List");


        FirebaseRecyclerOptions<Cart> options=new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef.child("User View").child(Prevalent.CurrOnlineUser.getPnumber()).child("Products"), Cart.class).build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter=new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull final Cart model)
            {
                holder.txtProductQuantity.setText("Product Quantity : " + model.getQuantity());
                holder.txtProductPrice.setText("PRoduct Price : " + model.getPrice());
                holder.txtProductName.setText("Product Name : " + model.getPname());

                int OneTypeProductPrice=((Integer.valueOf(model.getPrice())))*((Integer.valueOf(model.getQuantity())));
                TotalPrice=TotalPrice+OneTypeProductPrice;

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[]=new CharSequence[]
                                {
                                  "Edit",
                                  "Remove"
                                };
                        AlertDialog.Builder builder=new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("Cart Options");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i)
                            {
                                if(i==0)
                                {
                                    Intent intent=new Intent(CartActivity.this,ProductDetailsActivity.class);
                                    intent.putExtra("pid",model.getPid());
                                    startActivity(intent);
                                }
                                if(i==1)
                                {
                                    cartListRef.child("User View").child(Prevalent.CurrOnlineUser.getPnumber())
                                            .child("Products")
                                            .child(model.getPid())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task)
                                                {
                                                    if(task.isSuccessful())
                                                    {
                                                        Toast.makeText(CartActivity.this,"Item Removed Successfully...",Toast.LENGTH_LONG).show();


                                                        Intent intent=new Intent(CartActivity.this,HomeActivity.class);
                                                        startActivity(intent);
                                                    }
                                                }
                                            });
                                }
                            }
                        });
                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parents, int i) {
                View view= LayoutInflater.from(parents.getContext()).inflate(R.layout.cart_items_layout,parents,false);
                CartViewHolder holder=new CartViewHolder(view);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
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
                    String username=dataSnapshot.child("name").getValue().toString();

                    if(shippingstate.equals("Shipped"))
                    {
                        TotalAmt.setText("Dear, "+username+" Order Is Shipped Successfully...");
                        recyclerView.setVisibility(View.GONE);

                        Message1.setVisibility(View.VISIBLE);
                        Message1.setText("Congratulation, Your Order Has Been Placed Shipped...You Will Receive Your Order At Your Door Step");
                        nextBtn.setVisibility(View.GONE);

                        Toast.makeText(CartActivity.this,"You Can Purchase More Products, One you Received Yor First Final Order",Toast.LENGTH_LONG).show();
                    }
                    else if(shippingstate.equals("Not Shipped"))
                    {
                        TotalAmt.setText("Shipping State = Not Shipped");
                        recyclerView.setVisibility(View.GONE);

                        Message1.setVisibility(View.VISIBLE);
                        nextBtn.setVisibility(View.GONE);

                        Toast.makeText(CartActivity.this,"You Can Purchase More Products, Once you Received Yor First Final Order",Toast.LENGTH_LONG).show();
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
