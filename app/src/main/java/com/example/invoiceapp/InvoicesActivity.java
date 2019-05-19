package com.example.invoiceapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.util.ArrayList;
import java.util.List;

public class InvoicesActivity extends AppCompatActivity {

    private InvoiceAdapter invoiceAdapter;

    private DbHelper mDbHelper;

    private ArrayList<Invoice> invoices;
    private LinearLayout noInvoicesView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoices);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        noInvoicesView = findViewById(R.id.noinvoices_view);

        //TODO: read from DB invoices data and save it in invoices array!
        invoices = readAllInvoicesFromDb();

        if (!invoices.isEmpty()) {
            noInvoicesView.setVisibility(View.GONE);
        }

        //set adapter and populate ListView with data
        ListView invoiceListView = findViewById(R.id.invoices_list_view);
        invoiceAdapter = new InvoiceAdapter(this, new ArrayList<Invoice>());
        invoiceAdapter.addAll(invoices);

        invoiceListView.setAdapter(invoiceAdapter);

        //open new activity with specific invoice onitemclick
        invoiceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Intent intent = new Intent(InvoicesActivity.this, SingleInvoiceActivity.class);
                intent.putExtra("invoice_data", invoices.get(position));
                startActivity(intent);

                //Toast.makeText(ResultsActivity.this, "Clicked on: " + patients.get(position).getName(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    //helper method - read all invoices from DB
    private ArrayList<Invoice> readAllInvoicesFromDb() {
        mDbHelper = OpenHelperManager.getHelper(this, DbHelper.class);
        RuntimeExceptionDao<InvoiceInDb, Integer> invoiceDao = mDbHelper.getInvoiceInDBRuntimeDao();

        List<InvoiceInDb> allInvoicesDb = invoiceDao.queryForAll();
        ArrayList<Invoice> allInvoices = new ArrayList<>();
        Invoice tempInvoice;

        for(int i=0; i<allInvoicesDb.size(); i++) {
            tempInvoice = new Invoice(
                    allInvoicesDb.get(i).getSellerName(),
                    allInvoicesDb.get(i).getSellerNip(),
                    allInvoicesDb.get(i).getSellerAddress(),
                    allInvoicesDb.get(i).getBuyerName(),
                    allInvoicesDb.get(i).getBuyerNip(),
                    allInvoicesDb.get(i).getBuyerAddress(),
                    allInvoicesDb.get(i).getPrice(),
                    allInvoicesDb.get(i).getDate());
            tempInvoice.setId(allInvoicesDb.get(i).getId());
            //tempInvoice.setDate(allInvoicesDb.get(i).getDate());

            allInvoices.add(tempInvoice);
        }

        OpenHelperManager.releaseHelper();

        return allInvoices;
    }

}


