package com.example.invoiceapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.example.invoiceapp.InvoiceInDb;
import com.example.invoiceapp.R;

import java.sql.SQLException;

public class DbHelper extends OrmLiteSqliteOpenHelper {
    //name and version of DB
    private static final String DATABASE_NAME ="invoices.db";
    private static final int DATABASE_VERSION = 1;

    //database access object - invoiceInDb
    private Dao<InvoiceInDb, Integer> invoiceInDbDao = null;
    //object for generating only one RUNTIME EXCEPTION - it wraps other exceptions into RuntimeException
    //because other way - a lot of try/catch blocks and code is unreadable
    private RuntimeExceptionDao<InvoiceInDb, Integer> invoiceInDBRuntimeDao = null;

    //DbHelper constructor
    public DbHelper (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
    }

    //create database
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, InvoiceInDb.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //drop old version of DB and upgrade new one
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, InvoiceInDb.class, true);
            onCreate(sqLiteDatabase, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //accessing database
    public Dao<InvoiceInDb, Integer> getInvoiceInDbDao() throws SQLException {
        if (invoiceInDbDao == null) {
            invoiceInDbDao = getDao(InvoiceInDb.class);
        }
        return invoiceInDbDao;
    }

    public RuntimeExceptionDao<InvoiceInDb, Integer> getInvoiceInDBRuntimeDao() {
        if (invoiceInDBRuntimeDao == null) {
            invoiceInDBRuntimeDao = getRuntimeExceptionDao(InvoiceInDb.class);
        }
        return invoiceInDBRuntimeDao;
    }

}
