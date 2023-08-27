package com.example.assignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class AddItemActivity extends AppCompatActivity {

    EditText itemName, itemPrice;
    TextView dateSelected, errorName, errorPrice, date_today;
    Button addBtn;
    Button datepick;
    private Calendar calendar;
    String selectedDate ="";
    String todaysDate;

    private static final String KEY_NAME = "name";
    private static final String KEY_PRICE = "price";
    private static final String KEY_DATE = "date";


    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Dummy Group");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        addBtn = findViewById(R.id.add_btn);
        itemName = findViewById(R.id.item_name);
        itemPrice = findViewById(R.id.item_price);
        dateSelected = findViewById(R.id.date_tv);
        date_today = findViewById(R.id.date_today_tv);

        errorName = findViewById(R.id.error_name_tv);
        errorPrice = findViewById(R.id.error_price_tv);

        datepick = findViewById(R.id.datePick);


        calendar = Calendar.getInstance();

        datepick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePickerDialog();
            }
        });




        // Format date as "dd-MM-yyyy"
        Date c = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        todaysDate = dateFormat.format(c);
        date_today.setText("Today's Date: "+todaysDate);



        addBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick (View view){
            //Save Data as a new separate item
            SaveDataAsNewItem();

        }
    });


}

    private void openDatePickerDialog() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                        String formattedDate = sdf.format(calendar.getTime());
                        selectedDate = formattedDate;
                        long diff = getNoOfDays(selectedDate);
                        if(diff > 0){
                            Toast.makeText(AddItemActivity.this, "Previous date not allowed! 🚫", Toast.LENGTH_SHORT).show();
                            openDatePickerDialog();
                        }
                        if(diff == 0){
                            dateSelected.setText("Available from: Today");
                        }
                        else{
                            dateSelected.setText("Available from: "+formattedDate);
                        }


                    }
                },
                year, month, day);

        datePickerDialog.show();
    }


    private long getNoOfDays(String date) {
        Date c = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String todaysDate = dateFormat.format(c);

        String dateStr1 = date;
        String dateStr2 = todaysDate;
        long daysBetween = -1;
        try {
            Date date1 = dateFormat.parse(dateStr1);
            Date date2 = dateFormat.parse(dateStr2);

            long diffInMilliseconds = date2.getTime() - date1.getTime();
            long diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMilliseconds);

            daysBetween =  diffInDays;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return daysBetween;
    }


    private void SaveDataAsNewItem() {
        String name = itemName.getText().toString().trim();
        String price = itemPrice.getText().toString().trim();

        if(selectedDate.length() == 0){
            selectedDate = todaysDate;
        }

        if(name.length() != 0 && price.length() != 0) {
            Items items = new Items(name, price, selectedDate, "" + 0);

            collectionReference.add(items);

            Toast.makeText(AddItemActivity.this, "Item added successfully!", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getApplicationContext(), StoreiActivity.class);
            startActivity(intent);
        }
        else {
            if(name.length() == 0){
                errorName.setVisibility(View.VISIBLE);
            }
            if(price.length() == 0){
                errorPrice.setVisibility(View.VISIBLE);
            }
            Toast.makeText(this, "Enter all details!", Toast.LENGTH_SHORT).show();
        }

    }

//    private void SaveDataToFirestore() {
//        String name = itemName.getText().toString().trim();
//        String price = itemPrice.getText().toString().trim();
//        Date d = new Date();
//
//        Items i1 = new Items();
//        i1.setItemName(name);
//        i1.setItemPrice(price);
//        i1.setDate(d);
//
//
//
//        db.collection("Dummy Group")
//                .document("Market")
//                .set(i1)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void unused) {
//                        Toast.makeText(AddItemActivity.this, "Item added successfully!", Toast.LENGTH_SHORT).show();
//
//                        Intent intent = new Intent(getApplicationContext(), StoreiActivity.class);
//                        startActivity(intent);
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(AddItemActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }


}