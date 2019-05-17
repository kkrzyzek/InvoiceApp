package com.example.invoiceapp;

import com.j256.ormlite.field.DatabaseField;

public class InvoiceInDb {
    @DatabaseField(generatedId = true)
    int id;

    @DatabaseField
    String sellerName;
    @DatabaseField
    String sellerNip;
    @DatabaseField
    String buyerName;
    @DatabaseField
    String buyerNip;

    @DatabaseField
    String date;

    @DatabaseField
    double price;

    public InvoiceInDb() {
        //empty constructor - required by ormlite
    }

    public InvoiceInDb(String sellerName, String sellerNip, String buyerName, String buyerNip, double price, String date) {
        this.sellerName = sellerName;
        this.sellerNip = sellerNip;
        this.buyerName = buyerName;
        this.buyerNip = buyerNip;
        this.price = price;
        this.date = date;
    }

    public String getSellerName() {
        return sellerName;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public String getBuyerNip() {
        return buyerNip;
    }

    public String getSellerNip() {
        return sellerNip;
    }

    public double getPrice() {
        return price;
    }

    public String getDate() {
        return date;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "InvoiceInDb{" +
                "id=" + id +
                ", sellerName='" + sellerName + '\'' +
                ", sellerNip='" + sellerNip + '\'' +
                ", buyerName=" + buyerName +
                ", buyerNip='" + buyerNip + '\'' +
                ", date=" + date + '\'' +
                ", price=" + price +
                '}';
    }
}
