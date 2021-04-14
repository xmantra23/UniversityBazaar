package org.samir.universitybazaar.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import org.samir.universitybazaar.Models.Exchange;
import org.samir.universitybazaar.Models.Sell;

public class ExchangeDAO {
    private DatabaseHelper dbHelper;

    public ExchangeDAO(Context context){
        dbHelper = new DatabaseHelper(context);
    }

    public boolean addExchange(Exchange exchange){
        if(dbHelper != null){
            SQLiteDatabase db = null;
            try {
                db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("sellerId",exchange.getSellerId());
                values.put("customerId",exchange.getCustomerId());
                values.put("itemId",exchange.getItemId());
                values.put("type",exchange.getType());
                values.put("price",exchange.getPrice());
                values.put("exchangeDate",exchange.getExchangeDate());

                long rowId = db.insert("exchange",null,values);
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
