package org.samir.universitybazaar.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.samir.universitybazaar.Models.Club;
import org.samir.universitybazaar.Models.ClubNotice;

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

    //get all the notices for a given club with clubId
    public ArrayList<ClubNotice> getClubNotices(int clubId){
        ArrayList<ClubNotice> notices = new ArrayList<>();
        SQLiteDatabase db = null;
        try {
            db = databaseHelper.getReadableDatabase();

            String[] args = new String[]{
                    clubId + "" //doing this to convert int to string. selection arguments must be strings in the sql query.
            };

            //retrieve the notice from the club_notice table whose clubId is the provided clubId in the argument.
            Cursor cursor = db.query("club_notice", null, "clubId=?", args, null, null, null);
            if(cursor != null){
                if(cursor.moveToFirst()){
                    boolean isLast = false;
                    while(!isLast){ //continue until there are no more rows to process in the dataset.
                        int _id = cursor.getInt(cursor.getColumnIndex("_id"));
                        String title = cursor.getString(cursor.getColumnIndex("title"));
                        String description = cursor.getString(cursor.getColumnIndex("description"));
                        String creatorId = cursor.getString(cursor.getColumnIndex("creatorId"));
                        String createdDate = cursor.getString(cursor.getColumnIndex("createdDate"));

                        ClubNotice notice = new ClubNotice(_id,clubId,title,description,creatorId,createdDate);
                        notices.add(notice); // add notice to build the notices arraylist.

                        //stop if last row of data has been read else continue to the next row.
                        if(cursor.isLast()){
                            isLast = true;
                        }else{
                            cursor.moveToNext();
                        }
                    }
                }
                db.close();
                cursor.close();
                return notices;
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


    //add a notice to a club
    public boolean addNoticeInClub(ClubNotice notice){
        if(databaseHelper != null){
            SQLiteDatabase db = null;
            try {
                db = databaseHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("clubId",notice.getClubId());
                values.put("title", notice.getTitle());
                values.put("description", notice.getDescription());
                values.put("creatorId", notice.getCreatorId());
                values.put("createdDate", notice.getCreatedDate());

                long rowId = db.insert("club_notice",null,values);
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
    //retrieve all the subscribed clubs from clubs_subscriptions view
    public ArrayList<Club> getSubscribedClubsByMemberId(String memberId){
        ArrayList<Club> clubs = new ArrayList<>();
        SQLiteDatabase db = null;
        try {
            db = databaseHelper.getReadableDatabase();
            String[] columns = new String[]{
                    "clubId",
                    "title",
                    "shortDescription",
                    "longDescription",
                    "ownerName",
                    "ownerId",
                    "createdDate",
                    "memberCount"
            };
            String[] args = {memberId};

            //retrieve all the clubs that the member has subscribed by using the memberId from the clubs_subscriptions view
            Cursor cursor = db.query("club_subscriptions", columns, "memberId=?", args, null, null, null);
            if(cursor != null){
                if(cursor.moveToFirst()){
                    boolean isLast = false;
                    while(!isLast){ //continue until there are no more rows to process in the dataset.
                        Club club = new Club();
                        int clubId = cursor.getInt(cursor.getColumnIndex("clubId"));
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


    //retrieve all clubs created my a member with the given memberId
    public ArrayList<Club> getClubsByMemberId(String memberId){
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
            String[] args = {memberId};

            //retrieve all the clubs in the clubs table with ownerId == memberId
            Cursor cursor = db.query("clubs", columns, "ownerId=?", args, null, null, null);
            if(cursor != null){
                if(cursor.moveToFirst()){
                    boolean isLast = false;
                    while(!isLast){ //continue until there are no more rows to process in the dataset.

                        //get the each row of data about clubs from the database.
                        int clubId = cursor.getInt(cursor.getColumnIndex("_id"));
                        String title = cursor.getString(cursor.getColumnIndex("title"));
                        String shortDescription = cursor.getString(cursor.getColumnIndex("shortDescription"));
                        String longDescription = cursor.getString(cursor.getColumnIndex("longDescription"));
                        String ownerName = cursor.getString(cursor.getColumnIndex("ownerName"));
                        String ownerId = cursor.getString(cursor.getColumnIndex("ownerId"));
                        String createdDate = cursor.getString(cursor.getColumnIndex("createdDate"));
                        int memberCount = cursor.getInt(cursor.getColumnIndex("memberCount"));

                        //create a new club with all the above retrieved data.
                        Club club = new Club(clubId,title,shortDescription,longDescription,ownerName,ownerId,createdDate,memberCount);
                        clubs.add(club);//add club to the clubs arraylist.

                        //stop if last row of data has been read else continue to the next row.
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

    //add member to a club
    public boolean addMemberToClub(int clubId, String memberId){
        boolean status = false;
        if(databaseHelper != null){
            SQLiteDatabase db = null;
            try {
                db = databaseHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("clubId", clubId);
                values.put("memberId", memberId);

                Log.d("test", "values: " + clubId + " " + memberId);
                long tableId = db.insert("club_members",null,values); //add a new member in the club
                if(tableId != -1){
                    //insert was successful
                    status = true;
                }
            } catch (SQLException e) {
                //error inserting in database
                e.printStackTrace();
            }finally {
                db.close();
            }
        }
        Log.d("test", "values: " + clubId + " " + memberId);
        return status;

    }

    //add member to a club
    public boolean removeMemberFromClub(int clubId, String memberId){
        boolean status = false;
        if(databaseHelper != null){
            SQLiteDatabase db = null;
            try {
                db = databaseHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                String[] args = {String.valueOf(clubId),memberId};
                int affectedRows = db.delete("club_members","clubId=? and memberId=?",args);
                if(affectedRows > 0){
                    //delete successful
                    status = true;
                }
            } catch (SQLException e) {
                //error inserting in database
                e.printStackTrace();
            }finally {
                db.close();
            }
        }
        return status;

    }

    //increment memberCount in clubs tables
    public boolean incrementMemberCount(Club club){
        boolean status = false;
        if(databaseHelper != null){
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            try {
                ContentValues values = new ContentValues();
                if(club != null){
                    values.put("memberCount",club.getMemberCount() + 1); //increment memberCount by 1
                    String[] args = {club.get_id()+""}; //selection criteria for updating the row.
                    int count = db.update("clubs",values,"_id=?",args); //update memberCount in clubs table where _id == clubId
                    if(count > 0){
                        //update was successful.
                        status = true;
                    }
                }
            }catch(SQLException e){
                e.printStackTrace();
            }finally {
                db.close();
            }
        }
        return status;
    }

    //decrement memberCount in clubs tables
    public boolean decrementMemberCount(Club club){
        boolean status = false;
        if(databaseHelper != null){
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            try {
                ContentValues values = new ContentValues();
                if(club != null){
                    int memberCount = club.getMemberCount() - 1;//decrement member count by 1
                    if(memberCount < 0){
                        memberCount = 0;
                    }
                    values.put("memberCount",memberCount); //decrement memberCount by 1
                    String[] args = {club.get_id()+""}; //selection criteria for updating the row.
                    int count = db.update("clubs",values,"_id=?",args); //update memberCount in clubs table where _id == clubId
                    if(count > 0){
                        //update was successful.
                        status = true;
                    }
                }
            }catch(SQLException e){
                e.printStackTrace();
            }finally {
                db.close();
            }
        }
        return status;
    }

    //check if a user is a member of a club
    public boolean verifyMembership(int clubId,String memberId){
        SQLiteDatabase db = null;
        boolean result = false;
        try{
            db = databaseHelper.getReadableDatabase();
            String[] args = {String.valueOf(clubId),memberId};
            Cursor cursor = db.query("club_members",null,"clubId=? and memberId=?",args,null,null,null);
            if(cursor != null){
                if(cursor.moveToFirst()){
                    //the user with memberId is subscribed to the club with the clubId
                    result = true;
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            db.close();
            return result;
        }
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


    //get a club with the corresponding clubId in the club_members table.
    public Club getClubById(int clubId){
        SQLiteDatabase db = null;
        try {
            db = databaseHelper.getReadableDatabase();

            String[] args = new String[]{
                    clubId + "" //doing this to convert int to string. selection arguments must be strings in the sql query.
            };

            //retrieve the club from the clubs table whose _id == clubId
            Cursor cursor = db.query("clubs", null, "_id=?", args, null, null, null);
            if(cursor != null){
                if(cursor.moveToFirst()){
                    int _id = cursor.getInt(cursor.getColumnIndex("_id"));
                    String title = cursor.getString(cursor.getColumnIndex("title"));
                    String shortDescription = cursor.getString(cursor.getColumnIndex("shortDescription"));
                    String longDescription = cursor.getString(cursor.getColumnIndex("longDescription"));
                    String ownerName = cursor.getString(cursor.getColumnIndex("ownerName"));
                    String ownerId = cursor.getString(cursor.getColumnIndex("ownerId"));
                    String createdDate = cursor.getString(cursor.getColumnIndex("createdDate"));
                    int memberCount = cursor.getInt(cursor.getColumnIndex("memberCount"));

                    Club club = new Club(_id,title,shortDescription,longDescription,ownerName,ownerId,createdDate,memberCount);
                    return club;
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
