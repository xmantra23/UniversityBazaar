package org.samir.universitybazaar.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import org.samir.universitybazaar.Models.Loan;
import org.samir.universitybazaar.Models.Sell;

public class LoanDAO {

    private DatabaseHelper dbHelper;

    public LoanDAO(Context context){
        dbHelper = new DatabaseHelper(context);
    }

    public boolean addLoan(Loan loan){
        if(dbHelper != null){
            SQLiteDatabase db = null;
            try {
                db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("title", loan.getTitle());
                values.put("description", loan.getDescription());
                values.put("creatorName", loan.getCreatorName());
                values.put("creatorId", loan.getCreatorId());
                values.put("createdDate", loan.getCreatedDate());
                values.put("image", loan.getImage());
                values.put("price", loan.getPrice());
                values.put("status", "for sale");

                long rowId = db.insert("loan",null,values);
                db.close();

                if(rowId != -1){ //insert was successful
                    return true;
                }else{ //insert failed.
                    return false;
                }
            } catch (SQLException e) {
                //error inserting in database
                e.printStackTrace();
                db.close();
                return false;
            }finally {
                db.close();
            }
        }else{
            //couldn't connect to the database
            return false;
        }
    }

}
