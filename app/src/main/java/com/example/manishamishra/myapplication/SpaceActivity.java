package com.example.manishamishra.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class SpaceActivity extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference();
    private DatabaseReference dRef = database.getReference("QRS");
    private Button bookit;
    private RadioGroup radioGroup;
    public SharedPreferences sharedPreferences;
    public String selected;
    int i;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_space);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().hide();

        final ProgressDialog progress = new ProgressDialog(this);

        progress.setMessage("Loading spaces... ");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setIndeterminate(true);
        progress.show();

        sharedPreferences = getSharedPreferences("shared",Context.MODE_PRIVATE);

        radioGroup = findViewById(R.id.radiog);
        radioGroup.setOrientation(LinearLayout.VERTICAL);

        bookit = findViewById(R.id.book);

        i=0;

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String,Boolean> values = (Map<String, Boolean>) dataSnapshot.child("space").getValue();

                List<String> strings1 = new ArrayList<>(values.keySet());

                Collections.sort(strings1);

                for(String s: strings1){

                    RadioButton button = new RadioButton(getApplicationContext());
                    button.setId(i++);

                    button.setText(s);
                    button.setTextSize(60);
                    button.setBackgroundColor(Color.rgb(255,255,255));
                    if(values.get(s) || (Boolean) dataSnapshot.child("QRS").child(s).child("taken").getValue()){
                        button.setEnabled(false);
                    }
                    radioGroup.addView(button);

                }

                progress.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        bookit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int ch = radioGroup.getCheckedRadioButtonId();
                selected = (String) ((RadioButton)findViewById(ch)).getText();
                Toast.makeText(getApplicationContext(),String.valueOf(ch+1)+" Booked",Toast.LENGTH_LONG).show();

                dRef.child(selected).child("taken").setValue(true);

                dRef.child(selected).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Map<String,String> map = (Map<String, String>) dataSnapshot.getValue();

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("code",String.valueOf(map.get("code")));
                        editor.putString("booked","booked");
                        editor.putString("space",selected);
                        editor.commit();
                        startActivity(new Intent(getApplicationContext(),Main3Activity.class));
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });

    }

}
