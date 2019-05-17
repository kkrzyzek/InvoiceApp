package com.example.invoiceapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.util.Calendar;

public class AddInvoiceActivity extends AppCompatActivity {

    //shared preferences object
    private SharedPreferences sharedPrefs;

    private TextView sellerNameTextview;
    private TextView sellerNipTextview;

    private EditText buyerNameEdittextview;
    private EditText buyerNipEdittextview;
    private EditText priceEdittextview;

    private FloatingActionButton addInvoiceButton;

    private String sellerName;
    private String sellerNip;
    private String buyerName;
    private String buyerNip;
    private double price;


    DbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_invoice);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addInvoiceButton = findViewById(R.id.fab_approve);
        sellerNameTextview = findViewById(R.id.seller_name_textview);
        sellerNipTextview = findViewById(R.id.seller_nip_textview);

        buyerNameEdittextview = findViewById(R.id.buyer_name_edittext);
        buyerNipEdittextview = findViewById(R.id.buyer_nip_edittext);
        priceEdittextview = findViewById(R.id.price_edittext);

        sharedPrefs = getSharedPreferences("test_preferences", MODE_PRIVATE);
        sellerName = sharedPrefs.getString("seller_name", null);
        sellerNip = sharedPrefs.getString("seller_nip", null);

        if ((sellerName != null  && !sellerName.isEmpty()) && (sellerNip != null && !sellerNip.isEmpty())) {
            sellerNameTextview.setText("Imię i nazwisko sprzedawcy: " + sellerName);
            sellerNipTextview.setText("NIP sprzedawcy: " + sellerNip);
        } else {
            Toast.makeText(this, "Nie wprowadzono wymaganych danych o sprzedawcy.",
                    Toast.LENGTH_LONG).show();
                    finish();
        }

        addInvoiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sellerName = sellerNameTextview.getText().toString();
                sellerNip = sellerNipTextview.getText().toString();
                buyerName = buyerNameEdittextview.getText().toString();
                buyerNip = buyerNipEdittextview.getText().toString();
                price = Integer.valueOf(priceEdittextview.getText().toString());

                saveInvoiceInDb(sellerName, sellerNip, buyerName, buyerNip, price);
                Toast.makeText(AddInvoiceActivity.this, "Pomyslnie wystawiono fakture.",
                        Toast.LENGTH_LONG).show();
                finish();
            }
        });

    }

    //helper method - save calibration to DB
    private void saveInvoiceInDb(String sellerName, String sellerNip, String buyerName, String buyerNip, double price) {
        mDbHelper = OpenHelperManager.getHelper(this, DbHelper.class);
        RuntimeExceptionDao<InvoiceInDb, Integer> invoiceDao = mDbHelper.getInvoiceInDBRuntimeDao();

        //create invoice record in DB
        invoiceDao.createOrUpdate(new InvoiceInDb(
                sellerName,
                sellerNip,
                buyerName,
                buyerNip,
                price,
                Calendar.getInstance().getTime().toString()
        ));

        OpenHelperManager.releaseHelper();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
