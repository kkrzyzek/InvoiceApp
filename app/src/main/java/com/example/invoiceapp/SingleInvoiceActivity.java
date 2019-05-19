package com.example.invoiceapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class SingleInvoiceActivity extends AppCompatActivity {

    //invoice data
    private Invoice mInvoice;

    private DbHelper mDbHelper;

    //views
    private TextView dateTextView;
    private TextView invoiceNumbTextView;

    private TextView sellerNameTextView;
    private TextView sellerAddressTextView;
    private TextView sellerNipTextView;

    private TextView buyerNameTextView;
    private TextView buyerNipTextView;
    private TextView buyerAddressTextView;

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
        invoiceNumbTextView = findViewById(R.id.invoice_numb_textview);
        sellerNameTextView = findViewById(R.id.seller_name_textview);
        sellerNipTextView = findViewById(R.id.seller_nip_textview);
        sellerAddressTextView = findViewById(R.id.seller_address_textview);

        buyerNameTextView = findViewById(R.id.buyer_name_textview);
        buyerNipTextView = findViewById(R.id.buyer_nip_textview);
        buyerAddressTextView = findViewById(R.id.buyer_address_textview);

        priceTextView = findViewById(R.id.price_textview);

        dateTextView.setText("Data faktury: " + mInvoice.getDate());
        invoiceNumbTextView.setText("FAKTURA NR " + mInvoice.getId());
        sellerNameTextView.setText(mInvoice.getSellerName());
        sellerNipTextView.setText("NIP: " + mInvoice.getSellerNip());
        sellerAddressTextView.setText(mInvoice.getSellerAddress());

        buyerNameTextView.setText(mInvoice.getBuyerName());
        buyerNipTextView.setText("NIP: " + mInvoice.getBuyerNip());
        buyerAddressTextView.setText(mInvoice.getBuyerAddress());

        priceTextView.setText("Cena usługi: " + String.valueOf(mInvoice.getPrice()));
    }

    //define action - saving/exporting invoice
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id) {
            case R.id.action_export:
                savePdf();
                break;
            case R.id.action_delete:
                DialogInterface.OnClickListener deleteButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //user clicked "Delete" button, so close the current activity
                                deleteInvoiceFromDb();
                                Toast.makeText(SingleInvoiceActivity.this, "Pomyślnie usunięto fakturę.", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(SingleInvoiceActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        };

                //show dialog
                showDeleteButtonDialog(deleteButtonClickListener);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //helper method for showing dialog when DELETE button is clicked
    private void showDeleteButtonDialog (DialogInterface.OnClickListener deleteButtonClickListener) {
        //create an AlertDialog.Builder and set the message, and click listeners
        //for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(SingleInvoiceActivity.this);
        builder.setTitle("Usuń fakturę");
        builder.setMessage("Czy na pewno chcesz usunąć fakturę?");
        builder.setPositiveButton("Usuń", deleteButtonClickListener);
        builder.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                //user clicked the "Cancel" button, so dismiss the dialog
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

    //helper method - delete Invoice from DB
    private void deleteInvoiceFromDb() {
        mDbHelper = OpenHelperManager.getHelper(this, DbHelper.class);
        RuntimeExceptionDao<InvoiceInDb, Integer> invoiceDao = mDbHelper.getInvoiceInDBRuntimeDao();

        invoiceDao.deleteById(mInvoice.getId());

        OpenHelperManager.releaseHelper();
    }

    //generate pdf
    private void savePdf() {

        Document mDoc = new Document();
        String date = android.text.format.DateFormat.format("dd-MM-yyyy_HHmmss",
                new java.util.Date()).toString();
        //pdf file
        String mFileName = "faktura-"+ date;
        String mFilePath = Environment.getExternalStorageDirectory() + "/" + mFileName + ".pdf";

        try {
            PdfWriter.getInstance(mDoc, new FileOutputStream(mFilePath));
            Font bold = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
            Font title = new Font(Font.FontFamily.HELVETICA, 24, Font.BOLD);
            Font small = new Font(Font.FontFamily.HELVETICA, 8, Font.ITALIC);

            mDoc.open();

            // load image
            try {
                // get input stream
                InputStream ims = getAssets().open("icon_taxi.png");
                Bitmap bmp = BitmapFactory.decodeStream(ims);
                //Bitmap bmpEdited = Bitmap.createScaledBitmap(bmp, 30, 30, false);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                Image image = Image.getInstance(stream.toByteArray());
                //mDoc.add(image);

                PdfPTable table = new PdfPTable(2);
                table.setWidthPercentage(100);
                table.setWidths(new int[]{1, 10});
                table.addCell(createImageCell(image));
                table.addCell(createTextCell("Wygenerowano przy pomocy aplikacji - Faktury dla TAXI"));
                mDoc.add(table);
            }
            catch(IOException ex)
            {
                return;
            }

            mDoc.addAuthor(mInvoice.getSellerName());

            //mDoc.add(new Paragraph("Wygenerowano przy pomocy aplikacji Faktury dla TAXI", small));

            Paragraph mainPar = new Paragraph("Faktura Nr " + mInvoice.getId() + "/" + mInvoice.getDate(), title);
            mainPar.setAlignment(Element.ALIGN_CENTER);
            mDoc.add(mainPar);
            mDoc.add(new Paragraph(""));

            LineSeparator ls = new LineSeparator();
            mDoc.add(new Chunk(ls));

           mDoc.add(new Paragraph("Sprzedawca", bold));
            mDoc.add(new Paragraph("Imie i nazwisko/Nazwa firmy: " + mInvoice.getBuyerName()));
            mDoc.add(new Paragraph("Adres: " + mInvoice.getBuyerAddress()));
            mDoc.add(new Paragraph("NIP: " + mInvoice.getBuyerNip()));

            mDoc.add(new Chunk(ls));

            mDoc.add(new Paragraph("Nabywca", bold));
            mDoc.add(new Paragraph("Imię i nazwisko/ Nazwa firmy: " + mInvoice.getSellerName()));
            mDoc.add(new Paragraph("Adres: " + mInvoice.getSellerAddress()));
            mDoc.add(new Paragraph(mInvoice.getSellerNip()));

            mDoc.add(new Chunk(ls));

            mDoc.add(new Paragraph("Usluga", bold));
            mDoc.add(new Paragraph("Nazwa uslugi: Przewoz osob"));
            mDoc.add(new Paragraph("Cena uslugi: " + mInvoice.getPrice()));

            mDoc.close();
            Toast.makeText(this, "Pomyślnie wygenerowano plik .pdf", Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            e.printStackTrace();
        }

//        File file = new File(Environment.getExternalStorageDirectory() + "/" + mFileName + ".pdf");
//        // create new Intent
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//
//        // set flag to give temporary permission to external app to use your FileProvider
//        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//
//        // generate URI, I defined authority as the application ID in the Manifest, the last param is file I want to open
//        Uri uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID, file);
//
//        // I am opening a PDF file so I give it a valid MIME type
//        intent.setDataAndType(uri, "application/pdf");
//
//        // validate that the device can open your File!
//        PackageManager pm = this.getPackageManager();
//        if (intent.resolveActivity(pm) != null) {
//            startActivity(intent);
//        }

    }

    //helper method itext - table
    public static PdfPCell createImageCell(Image img) throws DocumentException, IOException {
        //Image img = Image.getInstance(path);
        PdfPCell cell = new PdfPCell(img, true);
        return cell;
    }
    public static PdfPCell createTextCell(String text) throws DocumentException, IOException {
        PdfPCell cell = new PdfPCell();
        Paragraph p = new Paragraph(text);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.single_invoice_menu, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}






