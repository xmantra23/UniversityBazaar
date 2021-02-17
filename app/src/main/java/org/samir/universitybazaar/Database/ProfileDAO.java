package org.samir.universitybazaar.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Button;

import org.samir.universitybazaar.Models.Profile;
import org.samir.universitybazaar.Models.User;

public class ProfileDAO {
    private DatabaseHelper dbHelper;
    public ProfileDAO(Context context){
        dbHelper=new DatabaseHelper(context);
    }


    // update the profile with provide data(find this profile with memberId)
    public Boolean updateProfile(Profile profile) {
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("full_name",profile.getFullName());
            values.put("gender",profile.getGender());
            values.put("address",profile.getAddress());
            values.put("phone",profile.getPhone());
            values.put("dob",profile.getDob());
            db.update("user_profiles", values, "member_id = '"+profile.getMemberId()+"'", null );
            db.close();
            return true;
        }catch(SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    //find a profile with provided memberId
    public Profile getProfile(String member_id) {
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            String[] columns = new String[]{
                    "member_id",
                    "email",
                    "full_name",
                    "gender",
                    "address",
                    "phone",
                    "dob",
                    "avatar",
            };
            Cursor cursor = db.query("user_profiles", columns, "member_id = '"+member_id+"'", null, null, null, null);
            if(cursor != null){

                Profile profile = new Profile();

                if(cursor.moveToFirst()){
                    String memberId = cursor.getString(cursor.getColumnIndex("member_id"));
                    String email = cursor.getString(cursor.getColumnIndex("email"));
                    String fullName = cursor.getString(cursor.getColumnIndex("full_name"));
                    String gender = cursor.getString(cursor.getColumnIndex("gender"));
                    String address = cursor.getString(cursor.getColumnIndex("address"));
                    String phone = cursor.getString(cursor.getColumnIndex("phone"));
                    String dob = cursor.getString(cursor.getColumnIndex("dob"));
                    String avatar = cursor.getString(cursor.getColumnIndex("avatar"));


                    profile.setMemberId(memberId);
                    profile.setEmail(email);
                    profile.setFullName(fullName);
                    profile.setGender(gender);
                    profile.setAddress(address);
                    profile.setPhone(phone);
                    profile.setDob(dob);
                    profile.setAvatar(avatar);

                    cursor.close();
                    db.close();
                    return profile;
                }else{
                    cursor.close();
                    db.close();
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public String getAvatar(String member_id){
        try{
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            String[] columns = new String[]{
                    "member_id",
                    "avatar",
            };
            Cursor cursor = db.query("user_profiles", columns, "member_id = '"+member_id+"'", null, null, null, null);

            if(cursor != null){
                if(cursor.moveToFirst()){
                    String avatar = cursor.getString(cursor.getColumnIndex("avatar"));
                    cursor.close();
                    db.close();
                    return avatar;
                }else{
                    cursor.close();
                    db.close();
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
            return null;
        }
        return null;

    }

    //save avatar to database
    public Boolean updateAvatar(String member_id, String avatar){
        try{
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("avatar",avatar);
            db.update("user_profiles",values,"member_id = '"+member_id+"'",null);
            db.close();
            return true;
        }catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }
}
