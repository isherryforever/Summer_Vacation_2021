package com.upc.smartfamily;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class People extends AppCompatActivity {
ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people);
        this.getWindow().setBackgroundDrawableResource(R.drawable.b);
        back=findViewById(R.id.about_iv_back);
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(People.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}