package com.example.manishamishra.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class BookDetailActivity extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

//        getSupportActionBar().hide();

        textView = findViewById(R.id.book_det);

        SharedPreferences sharedPreferences = getSharedPreferences("shared",Context.MODE_PRIVATE);

        String spac = sharedPreferences.getString("space",null);
        String token = sharedPreferences.getString("token",null);
        String status = sharedPreferences.getString("booked",null);

        String seto = new String("Space : " + spac + "\nToken : " + token + "\nStatus : " + status);

        textView.setText(seto);
    }
}
