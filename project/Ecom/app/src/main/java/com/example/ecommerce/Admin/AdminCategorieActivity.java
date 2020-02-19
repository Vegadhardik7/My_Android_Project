package com.example.ecommerce.Admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.ecommerce.HomeActivity;
import com.example.ecommerce.MainActivity;
import com.example.ecommerce.R;

public class AdminCategorieActivity extends AppCompatActivity {

    private ImageView tshirts,sports,femaledress,jacket,glasses,bag,headphones,mobile,watch,shoes,laptop,hat,books;
    private Button Logoutbtn,Checkorderbtn,maintainprobtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_categorie);


        Logoutbtn=(Button)findViewById(R.id.admin_logout_btn);
        Checkorderbtn=(Button)findViewById(R.id.check_orders_btn);
        maintainprobtn=(Button)findViewById(R.id.maintain_pro_btn);

        tshirts=(ImageView)findViewById(R.id.t_Shirt);
        sports=(ImageView)findViewById(R.id.Sport);
        femaledress=(ImageView)findViewById(R.id.femaledress);
        jacket=(ImageView)findViewById(R.id.coarts);
        glasses=(ImageView)findViewById(R.id.glass);
        bag=(ImageView)findViewById(R.id.bags);
        headphones=(ImageView)findViewById(R.id.hearphones);
        mobile=(ImageView)findViewById(R.id.mobiles);
        watch=(ImageView)findViewById(R.id.watches);
        shoes=(ImageView)findViewById(R.id.shoes);
        laptop=(ImageView)findViewById(R.id.laptop);
        hat=(ImageView)findViewById(R.id.hat);
        books=(ImageView)findViewById(R.id.books);


        Logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(AdminCategorieActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        Checkorderbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(AdminCategorieActivity.this, AdminNewOrdersActivity.class);
                startActivity(intent);
            }
        });

        maintainprobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(AdminCategorieActivity.this, HomeActivity.class);
                intent.putExtra("Admin","Admin");
                startActivity(intent);

            }
        });

        tshirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(AdminCategorieActivity.this, AddProductActivity.class);
                intent.putExtra("category","tshirts");
                startActivity(intent);

            }
        });


        sports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(AdminCategorieActivity.this,AddProductActivity.class);
                intent.putExtra("category","sports");
                startActivity(intent);

            }
        });


        femaledress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(AdminCategorieActivity.this,AddProductActivity.class);
                intent.putExtra("category","femaledress");
                startActivity(intent);

            }
        });


        jacket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(AdminCategorieActivity.this,AddProductActivity.class);
                intent.putExtra("category","jacket");
                startActivity(intent);

            }
        });


        glasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(AdminCategorieActivity.this,AddProductActivity.class);
                intent.putExtra("category","glasses");
                startActivity(intent);

            }
        });


        bag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(AdminCategorieActivity.this,AddProductActivity.class);
                intent.putExtra("category","bag");
                startActivity(intent);

            }
        });


        headphones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(AdminCategorieActivity.this,AddProductActivity.class);
                intent.putExtra("category","headphones");
                startActivity(intent);

            }
        });


        mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(AdminCategorieActivity.this,AddProductActivity.class);
                intent.putExtra("category","mobile");
                startActivity(intent);

            }
        });


        watch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(AdminCategorieActivity.this,AddProductActivity.class);
                intent.putExtra("category","watch");
                startActivity(intent);

            }
        });


        shoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(AdminCategorieActivity.this,AddProductActivity.class);
                intent.putExtra("category","shoes");
                startActivity(intent);

            }
        });


        laptop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(AdminCategorieActivity.this,AddProductActivity.class);
                intent.putExtra("category","laptop");
                startActivity(intent);

            }
        });


        hat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(AdminCategorieActivity.this,AddProductActivity.class);
                intent.putExtra("category","hat");
                startActivity(intent);

            }
        });


        books.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(AdminCategorieActivity.this,AddProductActivity.class);
                intent.putExtra("category","books");
                startActivity(intent);

            }
        });

    }
}
