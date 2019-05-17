package com.example.invoiceapp;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;
import com.example.invoiceapp.InvoiceInDb;

import java.io.IOException;
import java.sql.SQLException;

//scans source files and checks which one have OrmLite annotations and generates conf files for them
public class DbConfigUtils extends OrmLiteConfigUtil{

    //classes to scan
    private static final Class<?>[] classes = new Class[]{InvoiceInDb.class};

    public static void main(String[] args) throws IOException, SQLException {
        //generate config file
        writeConfigFile("ormlite_config.txt", classes);
    }
}
