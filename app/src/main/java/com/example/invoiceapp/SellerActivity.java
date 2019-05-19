package com.example.invoiceapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.EditText;

public class SellerActivity extends AppCompatActivity {

    //shared preferences object
    private SharedPreferences sharedPrefs;

    private EditText sellerNameEditText;
    private EditText sellerNipEditText;
    private EditText sellerAddressEditText;

    private String sellerName;
    private String sellerNip;
    private String sellerAddress;

    private static final String DEFAULT_SELLER_NAME = "";
    private static final String DEFAULT_SELLER_NIP = "";
    private static final String DEFAULT_SELLER_ADDRESS = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sellerNameEditText = findViewById(R.id.name_edittext);
        sellerNipEditText = findViewById(R.id.nip_edittext);
        sellerAddressEditText = findViewById(R.id.address_edittext);

        sharedPrefs = getSharedPreferences("test_preferences", MODE_PRIVATE);
        sellerName = sharedPrefs.getString("seller_name", DEFAULT_SELLER_NAME);
        sellerNip = sharedPrefs.getString("seller_nip", DEFAULT_SELLER_NIP);
        sellerAddress = sharedPrefs.getString("seller_address", DEFAULT_SELLER_ADDRESS);

        sellerNameEditText.setText(sellerName);
        sellerNipEditText.setText(sellerNip);
        sellerAddressEditText.setText(sellerAddress);
    }

    //up button - show dialog
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //respond to the action bar's Up/Home button
            case android.R.id.home:
                //save seller data in sharedPreferences
                sellerName = sellerNameEditText.getText().toString();
                sellerNip = sellerNipEditText.getText().toString();
                sellerAddress = sellerAddressEditText.getText().toString();

                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString("seller_name", sellerName);
                editor.apply();
                editor.putString("seller_nip", sellerNip);
                editor.apply();
                editor.putString("seller_address", sellerAddress);
                editor.apply();

                if((sellerName != null  && !sellerName.isEmpty())
                        && (sellerNip != null && !sellerNip.isEmpty())
                        && (sellerAddress != null && !sellerAddress.isEmpty())) {
                    finish();
                }
                else {
                    DialogInterface.OnClickListener leaveButtonClickListener =
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //user clicked "Leave" button, so close the current activity
                                    finish();
                                }
                            };

                    //show dialog that there are unfilled fields
                    showBackButtonDialog(leaveButtonClickListener);

                    return true;
                }
        }
        return super.onOptionsItemSelected(item);
    }

    //helper method for showing dialog when BACK button is clicked
    private void showBackButtonDialog (DialogInterface.OnClickListener leaveButtonClickListener) {
        //create an AlertDialog.Builder and set the message, and click listeners
        //for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(SellerActivity.this);
        builder.setTitle("Wyjdź");
        builder.setMessage("Nie wprowadzono wszystkich wymaganych danych.");
        builder.setPositiveButton("Wyjdź", leaveButtonClickListener);
        builder.setNegativeButton("Zostań", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                //user clicked the "Stay" button, so dismiss the dialog
                //and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.negative));
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.positive));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
