package com.example.assignment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class StoreiActivity extends AppCompatActivity {


    FloatingActionButton addItemFab;
    TextView itemName, itemPrice, noOfDAys;
    long daysBetween;

    private ArrayList<Items> itemList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ItemsAdapter adapter;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference fireRef = db.collection("Dummy Group")
            .document("Market");
    private CollectionReference collectionReference = db.collection("Dummy Group");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storei);

        //RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new ItemsAdapter(itemList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);




        //To add new item in the market
        addItemFab = findViewById(R.id.add_item_btn);
        addItemFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddItemActivity.class);
                startActivity(intent);
            }
        });


//        ReadDataFromFirebase();

    }

    private void ReadAllData() {

        collectionReference.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot snapshot: queryDocumentSnapshots){

                            //Getting all the items in the form of an object
                            Items items = snapshot.toObject(Items.class);

                            getNoOfDays(items.date_str);
                            String id = snapshot.getId();
                            items.itemId = id;


                            //If the item is more than 7 days old it will be deleted
                            if(daysBetween > 7){
                                DeleteData(id);
                                itemList.remove(items);
                            }else{
                            itemList.add(items);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }


    // Delete the document
    private void DeleteData(String documentId) {
        DocumentReference docRef = db.collection("Dummy Group").document(documentId);
        docRef.delete();
    }


    //Listening to the changes in realtime
    @Override
    protected void onStart() {
        super.onStart();

        //Clear the items so that it is not added twice
        itemList.clear();

        ReadAllData();

    }

//    private void ReadDataFromFirebase() {
//        fireRef.get()
//                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                    @Override
//                    public void onSuccess(DocumentSnapshot documentSnapshot) {
//                        if(documentSnapshot.exists()) {
//                            String fitemname = documentSnapshot.getString("name");
//
//                            String fitemprice = documentSnapshot.getString("price");
//                            Date d = documentSnapshot.getDate("date");
//                            Date cur = new Date();
//                            getNoOfDays(d);
//
//                            itemName.setText(fitemname);
//                            itemPrice.setText(fitemprice);
//                            noOfDAys.setText("Duration of the item is on the market for- \n" + daysBetween);
//                        }
//
//                    }
//                });
//    }


    //Calculate the no. of days
    private void getNoOfDays(String d) {
        Date c = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String todaysDate = dateFormat.format(c);

        String dateStr1 = d;
        String dateStr2 = todaysDate;

        try {
            Date date1 = dateFormat.parse(dateStr1);
            Date date2 = dateFormat.parse(dateStr2);

            long diffInMilliseconds = date2.getTime() - date1.getTime();
            long diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMilliseconds);

            daysBetween =  diffInDays;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





}