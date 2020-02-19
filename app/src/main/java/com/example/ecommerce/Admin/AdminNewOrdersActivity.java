package com.example.ecommerce.Admin;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.ecommerce.Model.AdminOrders;
import com.example.ecommerce.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminNewOrdersActivity extends AppCompatActivity {

    private RecyclerView orderList;
    private DatabaseReference ordersRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_orders);

        ordersRef= FirebaseDatabase.getInstance().getReference().child("Orders");
        orderList=(RecyclerView) findViewById(R.id.order_list);
        orderList.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseRecyclerOptions<AdminOrders>options=new FirebaseRecyclerOptions.Builder<AdminOrders>().setQuery(ordersRef,AdminOrders.class).build();

        FirebaseRecyclerAdapter<AdminOrders,AdminOrdersViewHolder> adapter=
                new FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder>(options) {
                    @SuppressLint("SetTextI18n")
                    @Override
                    protected void onBindViewHolder(@NonNull AdminOrdersViewHolder holder, @SuppressLint("RecyclerView") final int position, @NonNull final AdminOrders model)
                    {
                        holder.username.setText("Name : "+model.getName());
                        holder.userPhonenumber.setText("pnumber : "+model.getPnumber());
                        holder.userTotalPrice.setText("Total_Amount : "+model.getTotal_Amount());
                        holder.userDateTime.setText("Order_At : "+model.getDate()+" "+model.getTime());
                        holder.userShippingAddress.setText("Shipping_Address : "+model.getAddress()+" , "+model.getCity());

                        holder.ShowOrderbtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String uID=getRef(position).getKey();

                                Intent intent=new Intent(AdminNewOrdersActivity.this, AdminUserProductsActivity.class);
                                intent.putExtra("uid",uID);
                                startActivity(intent);
                            }
                        });
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {
                                CharSequence options[]=new CharSequence[]
                                        {
                                                "Yes","No"

                                        };
                                AlertDialog.Builder builder=new AlertDialog.Builder(AdminNewOrdersActivity.this);
                                builder.setTitle("Does The Shipping Of Yor Ordered Product Is Done?");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i)
                                    {
                                            if(i==0)
                                            {
                                                String uID=getRef(position).getKey();
                                                RemoveOrder(uID);
                                            }
                                            else
                                            {
                                                finish();
                                            }
                                    }
                                });
                                builder.show();
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public AdminOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout,parent,false);
                        return new AdminOrdersViewHolder(view);
                    }
                };
        orderList.setAdapter(adapter);
        adapter.startListening();
    }


    public static class AdminOrdersViewHolder extends RecyclerView.ViewHolder
    {
        public TextView username,userPhonenumber,userTotalPrice,userDateTime,userShippingAddress;
        public Button ShowOrderbtn;

        public AdminOrdersViewHolder(@NonNull View itemView)
        {
            super(itemView);

            username=itemView.findViewById(R.id.User_Name);
            userPhonenumber=itemView.findViewById(R.id.phone_number);
            userTotalPrice=itemView.findViewById(R.id.Order_Total_price);
            userDateTime=itemView.findViewById(R.id.Date_Time);
            userShippingAddress=itemView.findViewById(R.id.Order_address);
            ShowOrderbtn=itemView.findViewById(R.id.show_all_pro);
        }
    }
    private void RemoveOrder(String uID)
    {
        ordersRef.child(uID).removeValue();
    }

}
