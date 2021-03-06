package com.dheo.dheodeliverymerchantapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.appcompat.app.AlertDialog;

public class Helper {
    Context context;
    private String phone_number, blog_tittle;
    private String client_pass;
    private int clientId, click;
    private String photo_Url;
    SQLiteDatabase sqLiteDatabase, save_blog_to_database;

    public Helper(Context context) {
        this.context = context;
    }


    public void checkInternetConnection() {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();

        if (activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                //Toast.makeText(getApplicationContext(),"Wifi Enabled",Toast.LENGTH_SHORT).show();
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                //Toast.makeText(getApplicationContext(),"Mobile Data Enabled",Toast.LENGTH_SHORT).show();
            }
        } else {
            final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setIcon(R.drawable.ic_warning);
            dialog.setTitle("No Internet Connection");
            dialog.setMessage("It looks like your internet connection is off. Please turn it on and try again");
            dialog.setPositiveButton("Ok", null);
            dialog.show();
        }
    }
    public String getPhoto_Url() {
        //pin fetch from database
        sqLiteDatabase = context.openOrCreateDatabase("SQLite", Context.MODE_PRIVATE, null);
        Cursor query = sqLiteDatabase.rawQuery("SELECT * FROM ClientProfileInfo", null);
        if (query.moveToFirst()) {
            photo_Url = query.getString(2);
        }
        sqLiteDatabase.close();
        return photo_Url;
    }
    public String getPhone_number() {
        //pin fetch from database
        sqLiteDatabase = context.openOrCreateDatabase("SQLite", Context.MODE_PRIVATE, null);
        Cursor query = sqLiteDatabase.rawQuery("SELECT * FROM ClientProfileInfo", null);
        if (query.moveToFirst()) {
            phone_number = query.getString(1);
        }
        sqLiteDatabase.close();
        return phone_number;
    }
    public int getClientId() {
        //pin fetch from database
        sqLiteDatabase = context.openOrCreateDatabase("SQLite", Context.MODE_PRIVATE, null);
        Cursor query = sqLiteDatabase.rawQuery("SELECT * FROM ClientProfileInfo", null);
        if (query.moveToFirst()) {
            clientId = query.getInt(3);
        }
        sqLiteDatabase.close();
        return clientId;
    }
    public String getBlogTittle() {
        //pin fetch from database
        sqLiteDatabase = context.openOrCreateDatabase("SQLite", Context.MODE_PRIVATE, null);
        Cursor query = sqLiteDatabase.rawQuery("SELECT * FROM BlogInfo", null);
        if (query.moveToFirst()) {
            blog_tittle = query.getString(1);
        }
        sqLiteDatabase.close();
        return blog_tittle;
    }

    public int getClick() {
        //pin fetch from database
        sqLiteDatabase = context.openOrCreateDatabase("SQLite", Context.MODE_PRIVATE, null);
        Cursor query = sqLiteDatabase.rawQuery("SELECT * FROM BlogInfo", null);
        if (query.moveToFirst()) {
            click = query.getInt(2);
        }
        sqLiteDatabase.close();
        return click;
    }

}
