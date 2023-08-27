package com.example.assignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class MainActivity2 extends AppCompatActivity {

    Toolbar toolbar;
    private TextView cal_tv;
    FloatingActionButton gotoMarket;
    private DatabaseReference db;
    private int vis = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        String groupName = getIntent().getStringExtra("GroupName");

        toolbar = findViewById(R.id.toolbarr);
        setSupportActionBar(toolbar);
        toolbar.setTitle(groupName);

        //read and set the value of the market button
        db = FirebaseDatabase.getInstance().getReference("BtnVis").child("isOn");
        ReadAndSet(db);


        gotoMarket = findViewById(R.id.gotomarket);
        gotoMarket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), StoreiActivity.class);
                startActivity(i);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        cal_tv = findViewById(R.id.textView);
        Calendar dc  = Calendar.getInstance();
        cal_tv.setText(""+dc.getTime());

    }

    private void ReadAndSet(DatabaseReference db) {
        View marketButton = gotoMarket;
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue().toString() == "true"){
                    gotoMarket.setVisibility(View.VISIBLE);

                }
                else{
                    gotoMarket.setVisibility(View.GONE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sett, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        db = FirebaseDatabase.getInstance().getReference("BtnVis").child("isOn");

        if (id == R.id.market_on_off) {

            //To toggle the visibility of market between 0 & 1
            vis = vis ^ 1;

            if(vis == 1){
                db.setValue(true);
            }
            else{
                db.setValue(false);
            }
        }

        return super.onOptionsItemSelected(item);
    }





}