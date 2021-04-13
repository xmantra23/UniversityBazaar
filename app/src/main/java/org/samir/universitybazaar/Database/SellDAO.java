package org.samir.universitybazaar.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import org.samir.universitybazaar.Models.Post;
import org.samir.universitybazaar.Models.Sell;

import java.util.ArrayList;

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
                values.put("status", "for sale");

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

    public Boolean updateSellStatus(Sell sell) {
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("status", sell.getStatus());
            db.update("sell", values, "_id = '"+sell.get_id()+"'", null );
            db.close();
            return true;
        }catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<Sell> getAllSells(){
        ArrayList<Sell> sells = new ArrayList<>();
        SQLiteDatabase db = null;
        try {
            db = dbHelper.getReadableDatabase();
            String[] columns = new String[]{
                    "_id",
                    "title",
                    "description",
                    "creatorId",
                    "creatorName",
                    "createdDate",
                    "image",
                    "price",
                    "status"
            };

            //retrieve all the sale in the sells table.
            Cursor cursor = db.query("sell", columns, null, null, null, null, null);
            if(cursor != null){
                if(cursor.moveToFirst()){
                    boolean isLast = false;
                    while(!isLast){ // continues until all the retrieved rows have been iterated.
                        Sell sell = new Sell();
                        int sellId = cursor.getInt(cursor.getColumnIndex("_id"));
                        String title = cursor.getString(cursor.getColumnIndex("title"));
                        String description = cursor.getString(cursor.getColumnIndex("description"));
                        String creatorId = cursor.getString(cursor.getColumnIndex("creatorId"));
                        String creatorName = cursor.getString(cursor.getColumnIndex("creatorName"));
                        String createdDate = cursor.getString(cursor.getColumnIndex("createdDate"));
                        String image = cursor.getString(cursor.getColumnIndex("image"));
                        String price = cursor.getString(cursor.getColumnIndex("price"));
                        String status = cursor.getString(cursor.getColumnIndex("status"));


                        sell.set_id(sellId);
                        sell.setTitle(title);
                        sell.setDescription(description);
                        sell.setCreatorId(creatorId);
                        sell.setCreatorName(creatorName);
                        sell.setCreatedDate(createdDate);
                         sell.setPrice(price);
                        sell.setStatus(status);

                        sells.add(sell); //add the sell to the sells arraylist.

                        if(cursor.isLast()){ // we are at the last row of the dataset. no need to continue anymore.
                            isLast = true;
                        }else{
                            cursor.moveToNext(); //move to the next row in the dataset.
                        }
                    }
                }
                db.close();
                cursor.close();
                return sells;
            }else{
                db.close();
                cursor.close();
                return null;
            }
        }catch(SQLException e){
            e.printStackTrace();
            db.close();
            return null;
        }
    }


    public Sell getSellById(int sellIdIn){
        SQLiteDatabase db = null;
        try {
            db = dbHelper.getReadableDatabase();
            String[] columns = new String[]{
                    "_id",
                    "title",
                    "description",
                    "creatorId",
                    "creatorName",
                    "createdDate",
                    "image",
                    "price",
                    "status"
            };

            String[] args = new String[]{
                    sellIdIn + "" //doing this to convert int to string. selection arguments must be strings in the sql query.
            };

            //retrieve the sell from the sells table whose _id == sellID
            Cursor cursor = db.query("sell", columns, "_id=?", args, null, null, null);
            if(cursor != null){
                if(cursor.moveToFirst()){
                    Sell sell = new Sell();
                    int sellId = cursor.getInt(cursor.getColumnIndex("_id"));
                    String title = cursor.getString(cursor.getColumnIndex("title"));
                    String description = cursor.getString(cursor.getColumnIndex("description"));
                    String creatorId = cursor.getString(cursor.getColumnIndex("creatorId"));
                    String creatorName = cursor.getString(cursor.getColumnIndex("creatorName"));
                    String createdDate = cursor.getString(cursor.getColumnIndex("createdDate"));
                    String image = cursor.getString(cursor.getColumnIndex("image"));
                    String price = cursor.getString(cursor.getColumnIndex("price"));
                    String status = cursor.getString(cursor.getColumnIndex("status"));


                    sell.set_id(sellId);
                    sell.setTitle(title);
                    sell.setDescription(description);
                    sell.setCreatorId(creatorId);
                    sell.setCreatorName(creatorName);
                    sell.setCreatedDate(createdDate);
                    sell.setImage(image);
                    sell.setPrice(price);
                    sell.setStatus(status);


                    return sell;
                }
                db.close();
                cursor.close();
                return null;
            }else{
                db.close();
                cursor.close();
                return null;
            }
        }catch(SQLException e){
            e.printStackTrace();
            db.close();
            return null;
        }
    }


    public ArrayList<Sell> getSellByMemberId(String memberId){
        ArrayList<Sell> sells = new ArrayList<>();
        SQLiteDatabase db = null;
        try {
            db = dbHelper.getReadableDatabase();
            String[] columns = new String[]{
                    "_id",
                    "title",
                    "description",
                    "creatorId",
                    "creatorName",
                    "createdDate",
                    "image",
                    "price",
                    "status"
            };

            String[] args = new String[]{
                    memberId
            };

            //retrieve all the sells from the sells table where creatorId == memberId
            Cursor cursor = db.query("sell", columns, "creatorId=?", args, null, null, null);
            if(cursor != null){
                if(cursor.moveToFirst()){
                    boolean isLast = false;
                    while(!isLast){ // continues until all the retrieved rows have been iterated.
                        Sell sell = new Sell();
                        int sellId = cursor.getInt(cursor.getColumnIndex("_id"));
                        String title = cursor.getString(cursor.getColumnIndex("title"));
                        String description = cursor.getString(cursor.getColumnIndex("description"));
                        String creatorId = cursor.getString(cursor.getColumnIndex("creatorId"));
                        String creatorName = cursor.getString(cursor.getColumnIndex("creatorName"));
                        String createdDate = cursor.getString(cursor.getColumnIndex("createdDate"));
                        String image = cursor.getString(cursor.getColumnIndex("image"));
                        String price = cursor.getString(cursor.getColumnIndex("price"));
                        String status = cursor.getString(cursor.getColumnIndex("status"));


                        sell.set_id(sellId);
                        sell.setTitle(title);
                        sell.setDescription(description);
                        sell.setCreatorId(creatorId);
                        sell.setCreatorName(creatorName);
                        sell.setCreatedDate(createdDate);
                        sell.setImage(image);
                        sell.setPrice(price);
                        sell.setStatus(status);

                        sells.add(sell); //add the sell to the sells arraylist.

                        if(cursor.isLast()){ // we are at the last row of the dataset. no need to continue anymore.
                            isLast = true;
                        }else{
                            cursor.moveToNext(); //move to the next row in the dataset.
                        }
                    }
                }
                db.close();
                cursor.close();
                return sells; //return all the sells we got for that memberId in the sells table.
            }else{
                db.close();
                cursor.close();
                return null;
            }
        }catch(SQLException e){
            e.printStackTrace();
            db.close();
            return null;
        }
    }

}
