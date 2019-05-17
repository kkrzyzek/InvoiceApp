package com.example.invoiceapp;

import android.os.Parcel;
import android.os.Parcelable;

public class Invoice implements Parcelable {

    private String mSellerName;
    private String mSellerNip;
    private String mBuyerName;
    private String mBuyerNip;
    private double mPrice;

    private String mDate;

    private int mId;

    public Invoice(String sellerName, String sellerNip, String buyerName, String buyerNip, double price, String date) {
        this.mSellerName = sellerName;
        this.mSellerNip = sellerNip;
        this.mBuyerName = buyerName;
        this.mBuyerNip = buyerNip;
        this.mPrice = price;
        this.mDate = date;
    }

//    public String getSellerName() {
//        return mSellerName;
//    }
//
//    public void setSellerName(String name) {
//        this.mSellerName = name;
//    }

    public String getSellerName() {
        return mSellerName;
    }
    public String getSellerNip() {
        return mSellerNip;
    }

    public String getBuyerName() {
        return mBuyerName;
    }
    public String getBuyerNip() {
        return mBuyerNip;
    }

    public double getPrice() {
        return mPrice;
    }

    public String getDate() {
        return mDate;
    }



    public void setDate(String date) {
        this.mDate = date;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }


    //PARCELABLE IMPLEMENTATION
    //creator
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Invoice createFromParcel(Parcel in) {
            return new Invoice(in);
        }

        public Invoice[] newArray(int size) {
            return new Invoice[size];
        }
    };

    //parcelling part
    public Invoice(Parcel in){
        this.mSellerName = in.readString();
        this.mSellerNip = in.readString();
        this.mBuyerName = in.readString();
        this.mBuyerNip = in.readString();
        this.mDate = in.readString();
        this.mPrice = in.readDouble();
        this.mId = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mSellerName);
        dest.writeString(this.mSellerNip);
        dest.writeString(this.mBuyerName);
        dest.writeString(this.mBuyerNip);
        dest.writeString(this.mDate);
        dest.writeDouble(this.mPrice);
        dest.writeInt(this.mId);

    }
}
