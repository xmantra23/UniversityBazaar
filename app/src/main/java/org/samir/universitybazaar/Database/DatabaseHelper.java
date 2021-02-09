package org.samir.universitybazaar.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import org.samir.universitybazaar.Models.User;

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
                " ,email TEXT NOT NULL UNIQUE, password TEXT NOT NULL, " +
                "first_security TEXT NOT NULL, second_security TEXT NOT NULL, third_security TEXT NOT NULL)";
        db.execSQL(createUserTable);
    }

    public boolean doesUserExist(User user){
        try{
            SQLiteDatabase db = this.getReadableDatabase();
            String [] columns = {"member_id","email"};
            String [] args = {user.getMemberId(),user.getEmail()};
            Cursor cursor = db.query("users",columns,"member_id=? or email=?",args,null,null,null);
            if(cursor != null){
                if(cursor.moveToFirst()){
                    if(cursor.getString(cursor.getColumnIndex("email")).equals(user.getEmail()) ||
                    cursor.getString(cursor.getColumnIndex("member_id")).equals(user.getMemberId())){
                        cursor.close();
                        db.close();
                        return true;
                    }else{
                        cursor.close();
                        db.close();
                        return false;
                    }
                }else{
                    cursor.close();
                    db.close();
                    return false;
                }
            }else{
                db.close();
                return true;
            }
        }catch(SQLException e){
            e.printStackTrace();
            return true;
        }

    }

    public boolean registerUser(User user){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("member_id",user.getMemberId());
            values.put("email",user.getEmail());
            values.put("password",user.getPassword());
            values.put("first_security",user.getFirstSecurityQuestion());
            values.put("second_security",user.getSecondSecurityQuestion());
            values.put("third_security",user.getThirdSecurityQuestion());

            long userId = db.insert("users",null,values);
            return true;
        }catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
