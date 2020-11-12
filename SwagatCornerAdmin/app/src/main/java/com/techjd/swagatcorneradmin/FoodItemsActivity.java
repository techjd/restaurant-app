package com.techjd.swagatcorneradmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.techjd.swagatcorneradmin.models.OrderItem;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class FoodItemsActivity extends AppCompatActivity {
    private String listString;
    private String itemName;
    private String itemQuanity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_items);
        listString = getIntent().getStringExtra("FOODITEMS");

        itemName = getIntent().getStringExtra("ITEM NAME");
        itemQuanity = getIntent().getStringExtra("ITEM QUANTITY");

        Log.d("LIST", listString);
        Log.d("NAMES", itemName);
        Log.d("QUANTITY", itemQuanity);






    }
}