package com.example.assignment;

import java.util.Calendar;
import java.util.Date;

public class Items {

    String itemId;
    String itemName;
    String itemPrice;
    String date_str;

    public Items() {
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public Items(String itemName, String itemPrice, String date_str, String itemId) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.date_str = date_str;
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getDate() {
        return date_str;
    }

    public void setDate(String date_str) {
        this.date_str = date_str;
    }
}
