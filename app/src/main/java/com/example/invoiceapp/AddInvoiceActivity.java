package com.example.invoiceapp;

import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
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
    private TextView sellerAddressTextview;
    private TextView dateTextview;
    private TextView invoiceNumbTextview;

    private EditText buyerNameEdittextview;
    private EditText buyerNipEdittextview;
    private EditText buyerAddressEdittextview;
    private EditText priceEdittextview;

    private FloatingActionButton addInvoiceButton;

    private String sellerName;
    private String sellerNip;
    private String sellerAddress;
    private String buyerName;
    private String buyerAddress;
    private String buyerNip;
    private double price;
    private String date;
    private int invoiceNumb;


    DbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_invoice);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        FloatingActionButton fab = findViewById(R.id.fab_approve);
        fab.setEnabled(false);
        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));

        sharedPrefs = getSharedPreferences("test_preferences", MODE_PRIVATE);
        invoiceNumb = sharedPrefs.getInt("invoice_numb",1);
        invoiceNumbTextview = findViewById(R.id.invoice_numb_textview);
        invoiceNumbTextview.setText("Faktura Nr " + invoiceNumb);

        dateTextview = findViewById(R.id.date_textview);
        date = android.text.format.DateFormat.format("dd-MM-yyyy",
                new java.util.Date()).toString();
        dateTextview.setText(date);

        addInvoiceButton = findViewById(R.id.fab_approve);
        sellerNameTextview = findViewById(R.id.seller_name_textview);
        sellerNipTextview = findViewById(R.id.seller_nip_textview);
        sellerAddressTextview = findViewById(R.id.seller_address_textview);

        buyerNameEdittextview = findViewById(R.id.buyer_name_edittext);
        buyerNipEdittextview = findViewById(R.id.buyer_nip_edittext);
        buyerAddressEdittextview = findViewById(R.id.buyer_address_edittext);
        priceEdittextview = findViewById(R.id.price_edittext);

        sharedPrefs = getSharedPreferences("test_preferences", MODE_PRIVATE);
        sellerName = sharedPrefs.getString("seller_name", null);
        sellerNip = sharedPrefs.getString("seller_nip", null);
        sellerAddress = sharedPrefs.getString("seller_address", null);

        if ((sellerName != null  && !sellerName.isEmpty())
                && (sellerNip != null && !sellerNip.isEmpty())
                && (sellerAddress != null && !sellerAddress.isEmpty())) {
            sellerNameTextview.setText(sellerName);
            sellerAddressTextview.setText(sellerAddress);
            sellerNipTextview.setText("NIP: " + sellerNip);
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
                sellerAddress = sellerAddressTextview.getText().toString();

                buyerName = buyerNameEdittextview.getText().toString();
                buyerNip = buyerNipEdittextview.getText().toString();
                buyerAddress = buyerAddressEdittextview.getText().toString();

                price = Double.valueOf(priceEdittextview.getText().toString());

                saveInvoiceInDb(sellerName, sellerNip, sellerAddress, buyerName, buyerNip, buyerAddress, price, date);
                Toast.makeText(AddInvoiceActivity.this, "Pomyślnie wystawiono fakturę!",
                        Toast.LENGTH_LONG).show();

                invoiceNumb+=1;
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putInt("invoice_numb", invoiceNumb);
                editor.apply();

                finish();
            }
        });

        buyerNameEdittextview.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
                enableSubmitIfReady();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        buyerNipEdittextview.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
                enableSubmitIfReady();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        buyerAddressEdittextview.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
                enableSubmitIfReady();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        priceEdittextview.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
                enableSubmitIfReady();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

    }

    //helper method - save calibration to DB
    private void saveInvoiceInDb(String sellerName, String sellerNip, String sellerAddress,
                                 String buyerName, String buyerNip, String buyerAddress, double price, String date) {
        mDbHelper = OpenHelperManager.getHelper(this, DbHelper.class);
        RuntimeExceptionDao<InvoiceInDb, Integer> invoiceDao = mDbHelper.getInvoiceInDBRuntimeDao();

        //create invoice record in DB
        invoiceDao.createOrUpdate(new InvoiceInDb(
                sellerName,
                sellerNip,
                sellerAddress,
                buyerName,
                buyerNip,
                buyerAddress,
                price,
                date
        ));

        OpenHelperManager.releaseHelper();
    }

    private void enableSubmitIfReady() {
        FloatingActionButton fab = findViewById(R.id.fab_approve);

        boolean isReady = (buyerNipEdittextview.getText().toString().length() > 0
                && buyerAddressEdittextview.getText().toString().length() > 0
                && buyerNameEdittextview.getText().toString().length() > 0
                && priceEdittextview.getText().toString().length() > 0);

        if (isReady){
            fab.setEnabled(true);
            fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
        } else {
            fab.setEnabled(false);
            fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
