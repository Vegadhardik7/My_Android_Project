package com.example.ecommerce;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.ecommerce.Model.Products;
import com.example.ecommerce.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class SearchProductActivity extends AppCompatActivity {

    private Button searchBtn;
    private EditText inputText;
    private RecyclerView searchList;
    private String SearchInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);

        inputText=findViewById(R.id.search_pro_name);
        searchBtn=findViewById(R.id.search_btn);
        searchList=findViewById(R.id.search_list);
        searchList.setLayoutManager(new LinearLayoutManager(SearchProductActivity.this));

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                SearchInput=inputText.getText().toString();
                onStart();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Products");
        FirebaseRecyclerOptions<Products>options=new FirebaseRecyclerOptions.Builder<Products>().setQuery(reference.orderByChild("category").startAt(SearchInput),Products.class).build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter=new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @SuppressLint("SetTextI18n")
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model) {

                holder.txtproductName.setText("Product Name = " + model.getPname());
                holder.txtproductPrice.setText("Price In Rupees = " + model.getPrice());
                holder.txtproductDescription.setText("Product Description = " + model.getDescription());

                Picasso.get().load(model.getImage()).into(holder.imageView);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(SearchProductActivity.this,ProductDetailsActivity.class);
                        intent.putExtra("pid",model.getPid());
                        startActivity(intent);
                    }
                });

            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
                View view = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.products_items_layout, parent, false);
                ProductViewHolder holder = new ProductViewHolder(view);
                return holder;
            }
        };
        searchList.setAdapter(adapter);
        adapter.startListening();
    }
}
