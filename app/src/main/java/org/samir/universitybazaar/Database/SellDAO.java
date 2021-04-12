package org.samir.universitybazaar.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import org.samir.universitybazaar.Models.ClubPost;
import org.samir.universitybazaar.Models.Sell;

public class SellDAO {
    private DatabaseHelper dbHelper;

    public SellDAO(Context context){
        dbHelper = new DatabaseHelper(context);
    }

    public boolean addSell(Sell sell){
        if(dbHelper != null){
            SQLiteDatabase db = null;
            try {
                db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("title", sell.getTitle());
                values.put("description", sell.getDescription());
                values.put("creatorName", sell.getCreatorName());
                values.put("creatorId", sell.getCreatorId());
                values.put("createdDate", sell.getCreatedDate());
                values.put("image", sell.getImage());
                values.put("price", sell.getPrice());

                long rowId = db.insert("sell",null,values);
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
