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
    String sellerAddress;
    @DatabaseField
    String buyerName;
    @DatabaseField
    String buyerNip;
    @DatabaseField
    String buyerAddress;

    @DatabaseField
    String date;

    @DatabaseField
    double price;

    public InvoiceInDb() {
        //empty constructor - required by ormlite
    }

    public InvoiceInDb(String sellerName, String sellerNip, String sellerAddress, String buyerName,
                       String buyerNip, String buyerAddress, double price, String date) {
        this.sellerName = sellerName;
        this.sellerNip = sellerNip;
        this.sellerAddress = sellerAddress;
        this.buyerName = buyerName;
        this.buyerNip = buyerNip;
        this.buyerAddress = buyerAddress;
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

    public String getBuyerAddress() {
        return buyerAddress;
    }

    public String getSellerNip() {
        return sellerNip;
    }

    public String getSellerAddress() {
        return sellerAddress;
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
                ", sellerAddress='" + sellerAddress + '\'' +
                ", buyerName=" + buyerName +
                ", buyerNip='" + buyerNip + '\'' +
                ", buyerAddress='" + buyerAddress + '\'' +
                ", date=" + date + '\'' +
                ", price=" + price +
                '}';
    }
}
