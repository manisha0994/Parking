package com.example.manishamishra.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class Main3Activity extends AppCompatActivity {
    TextView token;
    Double val;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        SharedPreferences sharedPreferences = getSharedPreferences("shared",Context.MODE_PRIVATE);
        String spac = sharedPreferences.getString("space",null);
        Toast.makeText(getApplicationContext(),"Selected "+spac,Toast.LENGTH_LONG).show();
        token=findViewById(R.id.token);
        Random random = new Random(System.currentTimeMillis());
        val = random.nextDouble();
        token.setText(String.valueOf((int) (val*100000)));
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token",String.valueOf((int) (val*100000)));
        editor.commit();



    }
}
