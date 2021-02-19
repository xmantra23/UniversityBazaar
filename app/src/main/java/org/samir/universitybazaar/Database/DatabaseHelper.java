package org.samir.universitybazaar.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import org.samir.universitybazaar.Models.User;

/**
 * @author Samir Shrestha
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper"; //tag for debugging.

    private static final String DB_NAME = "UniversityBazaarDB"; // Database Name.
    private static final int DB_VERSION = 1; //Database Version.
    private Context context; //application context used for identifying activities during program execution.

    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate: started"); //just for logs.
        createUserTable(db); //creates users table in the database.
        createUserProfileTable(db); //creates user_profiles table in database.
    }

    //only need to use this when updating the database version.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //creates user table in the database.
    private static void createUserTable(SQLiteDatabase db) {
        String createUserTable = "CREATE TABLE users (_id INTEGER PRIMARY KEY AUTOINCREMENT,member_id TEXT NOT NULL UNIQUE" +
                " ,email TEXT NOT NULL UNIQUE, password TEXT NOT NULL, " +
                "first_security TEXT NOT NULL, second_security TEXT NOT NULL, third_security TEXT NOT NULL)";
        db.execSQL(createUserTable);
    }

    //creates userprofile table in the database.
    private static void createUserProfileTable(SQLiteDatabase db){
        String createUserProfileTable = "CREATE TABLE user_profiles (_id INTEGER PRIMARY KEY AUTOINCREMENT, member_id TEXT NOT NULL UNIQUE" +
                " ,email TEXT NOT NULL UNIQUE, full_name TXET, gender TEXT, address TEXT , phone TEXT , dob TEXT, avatar TEXT)";
        db.execSQL(createUserProfileTable);
    }

    //checks if a user already exists in the database by looking at the member id and email.
    public boolean doesUserExist(User user) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String[] columns = {"member_id", "email"};  // retrieve these columns
            String[] args = {user.getMemberId(), user.getEmail()};  // get records matching the member id and email.
            Cursor cursor = db.query("users", columns, "member_id=? or email=?", args, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) { // move to the first result set in the table.
                    if (cursor.getString(cursor.getColumnIndex("email")).equals(user.getEmail()) ||
                            cursor.getString(cursor.getColumnIndex("member_id")).equals(user.getMemberId())) {
                        //user already exists in the database
                        cursor.close();
                        db.close();
                        return true;
                    } else {
                        //user doesn't exist in the database
                        cursor.close();
                        db.close();
                        return false;
                    }
                } else {
                    //user doesn't exist in the database or there was an error querying the database.
                    cursor.close();
                    db.close();
                    return false;
                }
            } else {
                //no result set was obtained after performing the sql query. user doesn't exist in the database.
                db.close();
                return true;
            }
        } catch (SQLException e) {
            // couldn't execute the query properly. Return true as a default behaviour indicating user could not be found.
            e.printStackTrace();
            return true;
        }

    }

    //Inserts a new user into the database.
    public boolean registerUser(User user) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("member_id", user.getMemberId());
            values.put("email", user.getEmail());
            values.put("password", user.getPassword());
            values.put("first_security", user.getFirstSecurityQuestion());
            values.put("second_security", user.getSecondSecurityQuestion());
            values.put("third_security", user.getThirdSecurityQuestion());

            long userId = db.insert("users",null,values);

            //create a user_profile with this user when register
            ContentValues profile_values = new ContentValues();
            profile_values.put("member_id",user.getMemberId());
            profile_values.put("email",user.getEmail());

            long profileId = db.insert("user_profiles",null,profile_values);
            userId = db.insert("users", null, values);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //logs user into the system and sets up session data to be used throughout the login session.
    public boolean loginUser(String memberId, String password) {
        try {
            SQLiteDatabase db = getReadableDatabase();
            String[] args = {memberId, password};
            Cursor cursor = db.query("users", null, "member_id=? AND password=?", args, null, null, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    //found a user record with matching username and password.
                    //constructing the found user from the database.
                    User user = new User();
                    user.setId(cursor.getInt(cursor.getColumnIndex("_id")));
                    user.setMemberId(cursor.getString(cursor.getColumnIndex("member_id")));
                    user.setEmail(cursor.getString(cursor.getColumnIndex("email")));
                    user.setPassword(cursor.getString(cursor.getColumnIndex("password")));
                    cursor.close();
                    db.close();
                    //Saving the data for creating a login session.
                    UserSession loginSession = new UserSession(context);
                    //creates a new login session.
                    if (loginSession.createUserSession(user)) {
                        return true;
                    } else {
                        //user session could not be created.
                        return false;
                    }
                } else {
                    //user details could not be obtained from the database so return false.
                    cursor.close();
                    db.close();
                    return false;
                }
            } else {
                //no user record with the provided username or password was found so return false.
                db.close();
                return false;
            }
        } catch (SQLException e) {
            //encountered a fatal error while executing the database query. return false to indicate login failed.
            e.printStackTrace();
            return false;
        }
    }

    public User getUserByMemberId(String memberId) {
        try (SQLiteDatabase db = getReadableDatabase(); Cursor cursor = db.query("users", null, "member_id=?", new String[]{memberId}, null, null, null)) {
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    User user = new User();
                    user.setId(cursor.getInt(cursor.getColumnIndex("_id")));
                    user.setMemberId(cursor.getString(cursor.getColumnIndex("member_id")));
                    user.setEmail(cursor.getString(cursor.getColumnIndex("email")));
                    user.setPassword(cursor.getString(cursor.getColumnIndex("password")));
                    user.setFirstSecurityQuestion(cursor.getString(cursor.getColumnIndex("first_security")));
                    user.setSecondSecurityQuestion(cursor.getString(cursor.getColumnIndex("second_security")));
                    user.setThirdSecurityQuestion(cursor.getString(cursor.getColumnIndex("third_security")));
                    return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateUser(User user) {
        try (SQLiteDatabase db = getReadableDatabase()) {
            ContentValues values = new ContentValues();
            values.put("member_id", user.getMemberId());
            if (user.getEmail() != null) {
                values.put("email", user.getEmail());
            }
            if (user.getPassword() != null) {
                values.put("password", user.getPassword());
            }
            if (user.getFirstSecurityQuestion() != null) {
                values.put("first_security", user.getFirstSecurityQuestion());
            }
            if (user.getSecondSecurityQuestion() != null) {
                values.put("second_security", user.getSecondSecurityQuestion());
            }
            if (user.getThirdSecurityQuestion() != null) {
                values.put("third_security", user.getThirdSecurityQuestion());
            }
            db.update("users", values, "member_id=?", new String[]{user.getMemberId()});
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
