package org.samir.universitybazaar.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.strictmode.SqliteObjectLeakedViolation;
import android.util.Log;

import androidx.annotation.Nullable;

import org.samir.universitybazaar.Models.Comment;
import org.samir.universitybazaar.Models.Post;
import org.samir.universitybazaar.Models.Profile;
import org.samir.universitybazaar.Models.User;

import java.util.ArrayList;

/**
 * @author Samir Shrestha
 * @description This is the main class responsible for all database operations.
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
        createPostTable(db);//create a post table in the database;
        createCommentsTable(db); //create a comments table for user posts in the database.
    }

    //only need to use this when updating the database version.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * @author samir shrestha
     * @description this method creates a posts table in the database
     */
    private  static void createPostTable(SQLiteDatabase db){
        //creatorID is the memberID of the current logged in user.
        String createPostsTable = "CREATE TABLE posts (_id INTEGER PRIMARY KEY AUTOINCREMENT,title TEXT NOT NULL" +
                " ,description TEXT NOT NULL, creatorId TEXT NOT NULL, " +
                "creatorName TEXT NOT NULL, createdDate TEXT NOT NULL, category TEXT NOT NULL)";
        db.execSQL(createPostsTable);

    }

    /**
     * @author Samir Shrestha
     * @description this creates users table in the database.
     */
    private static void createUserTable(SQLiteDatabase db) {
        String createUserTable = "CREATE TABLE users (_id INTEGER PRIMARY KEY AUTOINCREMENT,member_id TEXT NOT NULL UNIQUE" +
                " ,email TEXT NOT NULL UNIQUE, password TEXT NOT NULL, " +
                "first_security TEXT NOT NULL, second_security TEXT NOT NULL, third_security TEXT NOT NULL)";
        db.execSQL(createUserTable);
    }

    /**
     * @author Samir Shrestha
     * @description this creates comments table for user posts in the database.
     */
    private static void createCommentsTable(SQLiteDatabase db) {
        String createCommentsTable = "CREATE TABLE comments (_id INTEGER PRIMARY KEY AUTOINCREMENT,postId INTEGER NOT NULL" +
                " ,commentText TEXT NOT NULL, commentOwnerName TEXT NOT NULL, " +
                "commentOwnerId TEXT NOT NULL, commentReceiverName TEXT, commentReceiverId TEXT, commentDate TEXT NOT NULL)";
        db.execSQL(createCommentsTable);
    }



    /**
     * @author Samir Shrestha
     * @description checks if a user already exists in the database by looking at the member id and email.
     */
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

    /**
     * @author Samir Shrestha
     * @description Inserts a new user into the users table.
     */
    public boolean registerUser(User user) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
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
            db.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            db.close();
            return false;
        }
    }

    /**
     * @author Samir Shrestha
     * @description logs user into the system and sets up session data to be used throughout the login session.
     */
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

    /**
     * @author samir shrestha
     * @description This method adds a new post to the posts table.
     */
    public boolean addPost(Post post){
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("title", post.getTitle());
            values.put("description", post.getDescription());
            values.put("creatorId", post.getCreatorId());
            values.put("creatorName", post.getCreatorName());
            values.put("createdDate", post.getCreatedDate());
            values.put("category", post.getCategory());

            long userId = db.insert("posts",null,values);
            db.close();
            if(userId != -1){ //insert was successful
                return true;
            }else{ //insert failed.
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            db.close();
            return false;
        }finally {
            db.close();
        }
    }

    /**
     * @author Samir Shrestha
     * @description This method returns all the post objects for a given memberId from the posts table.
     */
    public ArrayList<Post> getPostByMemberId(String memberId){
        ArrayList<Post> posts = new ArrayList<>();
        SQLiteDatabase db = null;
        try {
            db = this.getReadableDatabase();
            String[] columns = new String[]{
                    "_id",
                    "title",
                    "description",
                    "creatorId",
                    "creatorName",
                    "createdDate",
                    "category",
            };

            String[] args = new String[]{
                    memberId
            };

            //retrieve all the posts from the posts table where creatorId == memberId
            Cursor cursor = db.query("posts", columns, "creatorId=?", args, null, null, null);
            if(cursor != null){
                if(cursor.moveToFirst()){
                    boolean isLast = false;
                    while(!isLast){ // continues until all the retrieved rows have been iterated.
                        Post post = new Post();
                        int postId = cursor.getInt(cursor.getColumnIndex("_id"));
                        String title = cursor.getString(cursor.getColumnIndex("title"));
                        String description = cursor.getString(cursor.getColumnIndex("description"));
                        String creatorId = cursor.getString(cursor.getColumnIndex("creatorId"));
                        String creatorName = cursor.getString(cursor.getColumnIndex("creatorName"));
                        String createdDate = cursor.getString(cursor.getColumnIndex("createdDate"));
                        String category = cursor.getString(cursor.getColumnIndex("category"));

                        post.setId(postId);
                        post.setTitle(title);
                        post.setDescription(description);
                        post.setCreatorId(creatorId);
                        post.setCreatorName(creatorName);
                        post.setCreatedDate(createdDate);
                        post.setCategory(category);

                        posts.add(post); //add the post to the posts arraylist.

                        if(cursor.isLast()){ // we are at the last row of the dataset. no need to continue anymore.
                            isLast = true;
                        }else{
                            cursor.moveToNext(); //move to the next row in the dataset.
                        }
                    }
                }
                db.close();
                cursor.close();
                return posts; //return all the posts we got for that memberId in the posts table.
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
     * @description This method returns a post object for a given postId
     */
    public Post getPostById(int postId){
        SQLiteDatabase db = null;
        try {
            db = this.getReadableDatabase();
            String[] columns = new String[]{
                    "_id",
                    "title",
                    "description",
                    "creatorId",
                    "creatorName",
                    "createdDate",
                    "category",
            };

            String[] args = new String[]{
                    postId + "" //doing this to convert int to string. selection arguments must be strings in the sql query.
            };

            //retrieve the post from the posts table whose _id == postID
            Cursor cursor = db.query("posts", columns, "_id=?", args, null, null, null);
            if(cursor != null){
                if(cursor.moveToFirst()){
                    Post post = new Post();
                    String title = cursor.getString(cursor.getColumnIndex("title"));
                    String description = cursor.getString(cursor.getColumnIndex("description"));
                    String creatorId = cursor.getString(cursor.getColumnIndex("creatorId"));
                    String creatorName = cursor.getString(cursor.getColumnIndex("creatorName"));
                    String createdDate = cursor.getString(cursor.getColumnIndex("createdDate"));
                    String category = cursor.getString(cursor.getColumnIndex("category"));

                    post.setId(postId);
                    post.setTitle(title);
                    post.setDescription(description);
                    post.setCreatorId(creatorId);
                    post.setCreatorName(creatorName);
                    post.setCreatedDate(createdDate);
                    post.setCategory(category);

                    return post;
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
     * @Description find a profile with provided memberId
     */
    public Profile getProfile(String member_id) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
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


    /**
     * @author Samir Shrestha
     * @description This method returns all the posts from all users.
     */
    public ArrayList<Post> getAllPosts(){
        ArrayList<Post> posts = new ArrayList<>();
        SQLiteDatabase db = null;
        try {
            db = this.getReadableDatabase();
            String[] columns = new String[]{
                    "_id",
                    "title",
                    "description",
                    "creatorId",
                    "creatorName",
                    "createdDate",
                    "category",
            };

            //retrieve all the post in the posts table.
            Cursor cursor = db.query("posts", columns, null, null, null, null, null);
            if(cursor != null){
                if(cursor.moveToFirst()){
                    boolean isLast = false;
                    while(!isLast){ //continue until there are no more rows to process in the dataset.
                        Post post = new Post();
                        int postId = cursor.getInt(cursor.getColumnIndex("_id"));
                        String title = cursor.getString(cursor.getColumnIndex("title"));
                        String description = cursor.getString(cursor.getColumnIndex("description"));
                        String creatorId = cursor.getString(cursor.getColumnIndex("creatorId"));
                        String creatorName = cursor.getString(cursor.getColumnIndex("creatorName"));
                        String createdDate = cursor.getString(cursor.getColumnIndex("createdDate"));
                        String category = cursor.getString(cursor.getColumnIndex("category"));

                        post.setId(postId);
                        post.setTitle(title);
                        post.setDescription(description);
                        post.setCreatorId(creatorId);
                        post.setCreatorName(creatorName);
                        post.setCreatedDate(createdDate);
                        post.setCategory(category);

                        posts.add(post);
                        if(cursor.isLast()){
                            isLast = true;
                        }else{
                            cursor.moveToNext();
                        }
                    }
                }
                db.close();
                cursor.close();
                return posts;
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







    //--------------------------------------------------------------------------------------------------------------------------------------

    /**
     * Other team members please add your methods below this line.
     * I want to separate all the database methods that I wrote and the ones that you wrote
     * and also comment your name at the beginning of the method so that I know who wrote that method.
     *
     */

    //---------------------------------------------------------------------------------------------------------------------------------------

    /**
     * @author Lu Yifei
     * creates userprofile table in the database.
     */
    private static void createUserProfileTable(SQLiteDatabase db){
        String createUserProfileTable = "CREATE TABLE user_profiles (_id INTEGER PRIMARY KEY AUTOINCREMENT, member_id TEXT NOT NULL UNIQUE" +
                " ,email TEXT NOT NULL UNIQUE, full_name TXET, gender TEXT, address TEXT , phone TEXT , dob TEXT, avatar TEXT)";
        db.execSQL(createUserProfileTable);
    }

    /**
     * @author Lu Yifei
     * @param memberId
     * @return
     */
    public User getUserByMemberId(String memberId) {
        try (SQLiteDatabase db = getReadableDatabase();
             Cursor cursor = db.query("users", null, "member_id=?", new String[]{memberId}, null, null, null)) {
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

    /**
     * @author Lu Yifei
     * @param user
     */
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
