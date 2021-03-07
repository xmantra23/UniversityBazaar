package org.samir.universitybazaar.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import org.samir.universitybazaar.Models.Club;
import org.samir.universitybazaar.Models.Post;

import java.util.ArrayList;

/**
 * @author samir shrestha
 * @description class for interacting with everything related to clubs in the database.
 */
public class ClubDAO {
    private DatabaseHelper databaseHelper;
    private Context context;

    public ClubDAO(Context context){
        this.context = context;
        databaseHelper = new DatabaseHelper(context);
    }

    //adds a club to the clubs table
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

    /**
     * @author Samir Shrestha
     * @description This method returns all the clubs in the system.
     */
    public ArrayList<Club> getAllClubs(){
        ArrayList<Club> clubs = new ArrayList<>();
        SQLiteDatabase db = null;
        try {
            db = databaseHelper.getReadableDatabase();
            String[] columns = new String[]{
                    "_id",
                    "title",
                    "shortDescription",
                    "longDescription",
                    "ownerName",
                    "ownerId",
                    "createdDate",
                    "memberCount"
            };

            //retrieve all the clubs in the clubs table.
            Cursor cursor = db.query("clubs", columns, null, null, null, null, null);
            if(cursor != null){
                if(cursor.moveToFirst()){
                    boolean isLast = false;
                    while(!isLast){ //continue until there are no more rows to process in the dataset.
                        Club club = new Club();
                        int clubId = cursor.getInt(cursor.getColumnIndex("_id"));
                        String title = cursor.getString(cursor.getColumnIndex("title"));
                        String shortDescription = cursor.getString(cursor.getColumnIndex("shortDescription"));
                        String longDescription = cursor.getString(cursor.getColumnIndex("longDescription"));
                        String ownerName = cursor.getString(cursor.getColumnIndex("ownerName"));
                        String ownerId = cursor.getString(cursor.getColumnIndex("ownerId"));
                        String createdDate = cursor.getString(cursor.getColumnIndex("createdDate"));
                        int memberCount = cursor.getInt(cursor.getColumnIndex("memberCount"));

                        club.set_id(clubId);
                        club.setTitle(title);
                        club.setShortDescription(shortDescription);
                        club.setLongDescription(longDescription);
                        club.setOwnerName(ownerName);
                        club.setOwnerId(ownerId);
                        club.setCreatedDate(createdDate);
                        club.setMemberCount(memberCount);

                        clubs.add(club);
                        if(cursor.isLast()){
                            isLast = true;
                        }else{
                            cursor.moveToNext();
                        }
                    }
                }
                db.close();
                cursor.close();
                return clubs; //return all the clubs
            }else{
                db.close();
                cursor.close();
                return null; //error return null
            }
        }catch(SQLException e){
            e.printStackTrace();
            db.close();
            return null; //error return null
        }
    }
}
