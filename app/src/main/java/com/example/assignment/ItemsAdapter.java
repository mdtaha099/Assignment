package com.example.assignment;

import static java.lang.Math.abs;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.myViewHolder> {

    private ArrayList<Items> itemList;

    public ItemsAdapter (ArrayList<Items> items){
        this.itemList = items;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_card, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        Items items = itemList.get(position);
        String id = items.getItemId();

        holder.itemName.setText("  " + items.getItemName());
        holder.itemPrice.setText("  ₹ " + items.getItemPrice());
        holder.itemDate.setText("  Listed On: "+items.getDate());


        long noOfDays = getNoOfDays(items.getDate());
        if(noOfDays > -1) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), BuyActivity.class);
                    intent.putExtra("ID", id);
                    view.getContext().startActivity(intent);
                }
            });
        }
        else {
            long dy =  abs(noOfDays);
            holder.itemDate.setText("Available in "+ dy +"d");
        }
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


    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder{
        TextView itemName;
        TextView itemPrice;
        TextView itemDate;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            itemName = itemView.findViewById(R.id.item_name_tv);
            itemPrice = itemView.findViewById(R.id.item_price_tv);
            itemDate = itemView.findViewById(R.id.item_date_tv);
        }
    }




}
