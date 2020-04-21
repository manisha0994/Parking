package com.example.manishamishra.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class QRActivity extends AppCompatActivity {

    Button scan;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String status;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("QRS");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().hide();

        final Activity activity = this;

        sharedPreferences = getSharedPreferences("shared",Context.MODE_PRIVATE);

        status = sharedPreferences.getString("booked",null);
        editor = sharedPreferences.edit();
        scan = findViewById(R.id.scan);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result!=null){
            if(result.getContents() == null){
                Toast.makeText(getApplicationContext(),"You cancelled the scanning",Toast.LENGTH_SHORT).show();
            }else {
                String info = result.getContents();
                int l = info.length();
                String sub = info.substring(l-6,l-2);
                String code = sharedPreferences.getString("code",null);
                String sp = sharedPreferences.getString("space",null);

                if(status.equals(new String("Redeemed"))){
                    Toast.makeText(getApplicationContext(),"Already Redeemed",Toast.LENGTH_LONG).show();
                }else if(sub.equals(code)){
                    editor.putString("booked","Redeemed");

                    Toast.makeText(getApplicationContext(),"Redeemed successfully",Toast.LENGTH_LONG).show();

                    databaseReference.child(sp).child("taken").setValue(false);

                    startActivity(new Intent(getApplicationContext(),ConfirmActivity.class));
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(),"Not at a Right place",Toast.LENGTH_LONG).show();
                }

                editor.commit();
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);

        }
    }
}
