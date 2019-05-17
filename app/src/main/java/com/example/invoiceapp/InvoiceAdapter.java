package com.example.invoiceapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.invoiceapp.Invoice;
import com.example.invoiceapp.Invoice;

import java.util.ArrayList;

public class InvoiceAdapter extends ArrayAdapter<Invoice> {

    public InvoiceAdapter(@NonNull Context context, ArrayList<Invoice> invoices) {
        super(context, 0, invoices);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;

        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.invoice_list_item, parent, false);
        }

        Invoice currentInvoice = getItem(position);

        if (currentInvoice != null) {
            //setting buyer name
            TextView buyerName = listItemView.findViewById(R.id.buyer_name_text);
            buyerName.setText(currentInvoice.getBuyerName());

            //setting invoice date
            TextView invoiceDate = listItemView.findViewById(R.id.invoice_date_text);
            invoiceDate.setText(currentInvoice.getDate());

        }

        return listItemView;
    }

}
