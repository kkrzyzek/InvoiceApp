package com.example.invoiceapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.util.ArrayList;

public class SingleInvoiceActivity extends AppCompatActivity {

    //invoice data
    private Invoice mInvoice;

    private DbHelper mDbHelper;

    //views
    private TextView dateTextView;
    private TextView sellerNameTextView;
    private TextView sellerNipTextView;
    private TextView buyerNameTextView;
    private TextView buyerNipTextView;
    private TextView priceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_invoice);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (intent.hasExtra("invoice_data")) {
            Bundle intentData = intent.getExtras();
            mInvoice = intentData.getParcelable("invoice_data");
        }

        dateTextView = findViewById(R.id.date_textview);
        sellerNameTextView = findViewById(R.id.seller_name_textview);
        sellerNipTextView = findViewById(R.id.seller_nip_textview);
        buyerNameTextView = findViewById(R.id.buyer_name_textview);
        buyerNipTextView = findViewById(R.id.buyer_nip_textview);
        priceTextView = findViewById(R.id.price_textview);

        dateTextView.setText("Data faktury: " + mInvoice.getDate());
        sellerNameTextView.setText(mInvoice.getSellerName());
        sellerNipTextView.setText(mInvoice.getSellerNip());
        buyerNameTextView.setText("Dane nabywcy: " + mInvoice.getBuyerName());
        buyerNipTextView.setText("NIP nabywcy: " + mInvoice.getBuyerNip());
        priceTextView.setText("Koszt: " + String.valueOf(mInvoice.getPrice()));

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}






