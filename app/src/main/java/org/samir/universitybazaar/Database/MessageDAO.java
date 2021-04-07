package org.samir.universitybazaar.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import org.samir.universitybazaar.Models.Club;
import org.samir.universitybazaar.Models.ClubNotice;
import org.samir.universitybazaar.Models.ClubPost;
import org.samir.universitybazaar.Models.Member;
import org.samir.universitybazaar.Models.Message;
import org.samir.universitybazaar.Models.User;
import org.samir.universitybazaar.Utility.Constants;
import org.samir.universitybazaar.Utility.Utils;

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


    //retrieve all the messages from the messages table whose receiver id matches the provided argument receiverId.
    public ArrayList<Message> getInboxMessages(String receiverId){
        ArrayList<Message> messages = new ArrayList<>();
        SQLiteDatabase db = null;
        try {
            db = databaseHelper.getReadableDatabase();

            String[] args = new String[]{receiverId};

            //retrieve the messages from the messages table whose receiverId is the provided receiverId  in the argument.
            Cursor cursor = db.query("messages", null, "receiverId=?", args, null, null, null);
            if(cursor != null){
                if(cursor.moveToFirst()){
                    boolean isLast = false;
                    while(!isLast){ //continue until there are no more rows to process in the dataset.
                        int _id = cursor.getInt(cursor.getColumnIndex("_id"));
                        String subject = cursor.getString(cursor.getColumnIndex("subject"));
                        String message = cursor.getString(cursor.getColumnIndex("message"));
                        String senderId = cursor.getString(cursor.getColumnIndex("senderId"));
                        String senderName = cursor.getString(cursor.getColumnIndex("senderName"));
                        String receiverName = cursor.getString(cursor.getColumnIndex("receiverName"));
                        String messageDate = cursor.getString(cursor.getColumnIndex("messageDate"));
                        int readStatus = cursor.getInt(cursor.getColumnIndex("readStatus"));

                        Message inboxMessage = new Message(_id,subject,message,senderId,senderName,receiverId,receiverName,messageDate,readStatus);
                        messages.add(inboxMessage); // collecting all the messages.

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
                return messages;
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

    //adds a message to the sent_messages table
    public boolean addToSentMessages(Message message,String messageType){
        if(databaseHelper != null){
            SQLiteDatabase db = null;
            try {
                db = databaseHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("subject",message.getSubject());
                values.put("message",message.getMessage());
                values.put("senderId",message.getSenderId());
                values.put("senderName",message.getSenderName());

                //only a single message or club message should have a receiverId. Incase of single it is the memberId of the receiving
                //user. Incase of clubs it is the clubId.
                if(messageType.equals(Constants.SINGLE_MESSAGE) || messageType.equals(Constants.CLUB_MESSAGE)){
                    values.put("receiverId", message.getReceiverId());
                }else{
                    values.put("receiverId","none");
                }
                values.put("receiverName", message.getReceiverName());
                values.put("messageDate",message.getMessageDate());

                long messageId = db.insert("sent_messages",null,values);
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

    //retrieve all the messages from the sent_messages table whose receiver id matches the provided argument receiverId.
    public ArrayList<Message> getSentMessages(String senderId){
        ArrayList<Message> messages = new ArrayList<>();
        SQLiteDatabase db = null;
        try {
            db = databaseHelper.getReadableDatabase();

            String[] args = new String[]{senderId};

            //retrieve the messages from the messages table whose receiverId is the provided receiverId  in the argument.
            Cursor cursor = db.query("sent_messages", null, "senderId=?", args, null, null, null);
            if(cursor != null){
                if(cursor.moveToFirst()){
                    boolean isLast = false;
                    while(!isLast){ //continue until there are no more rows to process in the dataset.
                        int _id = cursor.getInt(cursor.getColumnIndex("_id"));
                        String subject = cursor.getString(cursor.getColumnIndex("subject"));
                        String message = cursor.getString(cursor.getColumnIndex("message"));
                        String senderName = cursor.getString(cursor.getColumnIndex("senderName"));
                        String receiverId = cursor.getString(cursor.getColumnIndex("receiverId"));
                        String receiverName = cursor.getString(cursor.getColumnIndex("receiverName"));
                        String messageDate = cursor.getString(cursor.getColumnIndex("messageDate"));

                        Message sentMessage = new Message(_id,subject,message,senderId,senderName,receiverId,receiverName,messageDate,0);
                        messages.add(sentMessage); // collecting all the messages.

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
                return messages;
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

    public int getUnreadMessagesCount(String receiverId){
        int count = 0;
        SQLiteDatabase db = null;
        try {
            db = databaseHelper.getReadableDatabase();

            String[] args = new String[]{receiverId,"0"};

            //retrieve the messages from the messages table whose receiverId is the provided receiverId  and readStatus = 0 (not read)
            Cursor cursor = db.query("messages", null, "receiverId=? AND readStatus=?", args, null, null, null);
            if(cursor != null){
                count = cursor.getCount();
                db.close();
                cursor.close();
            }else{
                db.close();
                cursor.close();
            }
        }catch(SQLException e){
            e.printStackTrace();
            db.close();
        }
        return count;
    }

    public boolean markMessageRead(int messageId){
        boolean status = false;
        if(databaseHelper != null){
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            try {
                ContentValues values = new ContentValues();
                if(messageId != -1){
                    values.put("readStatus",1); //change readStatus to 1 (message has been read)
                    String[] args = {messageId+""}; //selection criteria for updating the row.
                    int count = db.update("messages",values,"_id=?",args); //update readStatus in messages table where _id == messageId
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

    public ArrayList<String> getAllUsersId(String senderId){
        ArrayList<String> memberIds = new ArrayList<>();
        SQLiteDatabase db = null;
        try {
            db = databaseHelper.getReadableDatabase();
            String[] args = new String[]{senderId};
            String[] cols = new String[]{"member_id"};
            //retrieve the member id from the users table whose member id doesn't  match the search arguments.
            Cursor cursor = db.query("users", cols, "member_id != ?", args, null, null, null);
            if(cursor != null){
                if(cursor.moveToFirst()){
                    boolean isLast = false;
                    while(!isLast){ //continue until there are no more rows to process in the dataset.
                        String member_id = cursor.getString(cursor.getColumnIndex("member_id"));
                        memberIds.add(member_id); // add memberId to the memberIds array list

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
                return memberIds;
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
