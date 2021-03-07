package org.samir.universitybazaar.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import org.samir.universitybazaar.Models.Club;

public class ClubDAO {
    private DatabaseHelper databaseHelper;
    private Context context;

    public ClubDAO(Context context){
        this.context = context;
        databaseHelper = new DatabaseHelper(context);
    }

    public boolean addClub(Club club){
        if(databaseHelper != null){
            SQLiteDatabase db = null;
            try {
                db = databaseHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("title", club.getTitle());
                values.put("shortDescription", club.getShortDescription());
                values.put("longDescription", club.getLongDescription());
                values.put("ownerName", club.getOwnerName());
                values.put("ownerId", club.getOwnerId());
                values.put("createdDate", club.getCreatedDate());
                values.put("memberCount",club.getMemberCount());

                long clubId = db.insert("clubs",null,values);
                db.close();
                if(clubId != -1){ //insert was successful
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
