package com.example.assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BuyActivity extends AppCompatActivity {


    TextView  item_name, item_price, item_date, toolbar_title;
    Button buy_btn;


    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);


        toolbar_title = findViewById(R.id.item_name_toolbar);
        item_name = findViewById(R.id.item_name_tv);
        item_price = findViewById(R.id.item_price_tv);
        item_date = findViewById(R.id.item_date_tv);
        
        String id = getIntent().getStringExtra("ID");

        // Read the documents
        ReadItem(id);


        //Buying the item will delete it from the database
        buy_btn = findViewById(R.id.buyBtn);
        buy_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteData(id);
            }
        });


    }



    private void ReadItem(String id) {
        // Reference to a specific collection and document
        DocumentReference docRef = db.collection("Dummy Group").document(id);

        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    toolbar_title.setText(document.getString("itemName"));
                    item_name.setText(""+ document.getString("itemName"));
                    item_price.setText("Price: ₹ "+ document.getString("itemPrice"));
                    item_date.setText("Listed On: "+ document.getString("date"));

                } else {
                    Toast.makeText(this, "Document not found!!!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Error retrieving item!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void DeleteData(String documentId) {
        DocumentReference docRef = db.collection("Dummy Group").document(documentId);
        docRef.delete();

        Intent intent = new Intent(getApplicationContext(), StoreiActivity.class);
        startActivity(intent);
    }
}