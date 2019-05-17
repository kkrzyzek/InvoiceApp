package com.example.invoiceapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //hide actionbar
        getSupportActionBar().hide();

        Button addInvoiceButton = findViewById(R.id.add_invoice_button);
        Button invoicesButton = findViewById(R.id.invoices_button);
        Button sellerButton = findViewById(R.id.seller_button);

        addInvoiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddInvoiceActivity.class);
                startActivity(intent);
            }
        });

        invoicesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, com.example.invoiceapp.InvoicesActivity.class);
                startActivity(intent);
            }
        });

        sellerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, com.example.invoiceapp.SellerActivity.class);
                startActivity(intent);
            }
        });
    }
}
