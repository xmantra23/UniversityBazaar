package org.samir.universitybazaar.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import org.samir.universitybazaar.Models.Club;
import org.samir.universitybazaar.Models.ClubPost;
import org.samir.universitybazaar.Models.Member;
import org.samir.universitybazaar.Models.Message;

import java.util.ArrayList;

/**
 * @author samir shrestha
 * @description class for interacting with everything related to the messages functionality in the database.
 */
public class MessageDAO {
    private DatabaseHelper databaseHelper;
    private Context context;

    public MessageDAO(Context context){
        this.context = context;
        databaseHelper = new DatabaseHelper(context);
    }

    public ArrayList<Member> getMembersByName(String name){
        ArrayList<Member> members = new ArrayList<>();
        SQLiteDatabase db = null;
        try {
            db = databaseHelper.getReadableDatabase();

            String[] args = new String[]{name+"%"};
            String[] cols = new String[]{"member_id","full_name"};
            //retrieve the members from the profile table whose name matches the search arguments.
            Cursor cursor = db.query("user_profiles", cols, "full_name LIKE ?", args, null, null, null);
            if(cursor != null){
                if(cursor.moveToFirst()){
                    boolean isLast = false;
                    while(!isLast){ //continue until there are no more rows to process in the dataset.
                        String member_id = cursor.getString(cursor.getColumnIndex("member_id"));
                        String full_name = cursor.getString(cursor.getColumnIndex("full_name"));

                        Member member = new Member(member_id,full_name);
                        members.add(member); // add member to build the members array list containing all the members that match the given name

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
                return members;
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

    //adds a message to the messages table
    public boolean addSingleMessage(Message message){
        if(databaseHelper != null){
            SQLiteDatabase db = null;
            try {
                db = databaseHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("subject",message.getSubject());
                values.put("message",message.getMessage());
                values.put("senderId",message.getSenderId());
                values.put("senderName",message.getSenderName());
                values.put("receiverId", message.getReceiverId());
                values.put("receiverName", message.getReceiverName());
                values.put("messageDate",message.getMessageDate());
                values.put("readStatus",message.getReadStatus());

                long messageId = db.insert("messages",null,values);
                db.close();

                if(messageId != -1){ //insert was successful
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
