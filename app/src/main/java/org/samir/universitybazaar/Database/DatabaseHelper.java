package org.samir.universitybazaar.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG  = "DatabaseHelper"; //tag for debugging.

    private static final String DB_NAME = "UniveristyBazaarDB"; // Database Name.
    private static final int DB_VERSION = 1; //Database Version.


    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG,"onCreate: started"); //just for logs.
        createUserTable(db); //creates user table in the database.

    }

    //only need to use this when updating the database version.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //creates user table in the database.
    private static void createUserTable(SQLiteDatabase db){
        String createUserTable = "CREATE TABLE users (_id INTEGER PRIMARY KEY AUTOINCREMENT,member_id TEXT NOT NULL UNIQUE" +
                " ,email TEXT NOT NULL UNIQUE, password TEXT NOT NULL UNIQUE, " +
                "first_security TEXT NOT NULL, second_security TEXT NOT NULL, third_security TEXT NOT NULL)";
        db.execSQL(createUserTable);
    }
}
