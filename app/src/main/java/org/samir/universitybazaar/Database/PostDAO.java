package org.samir.universitybazaar.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import org.samir.universitybazaar.Models.Post;
import org.samir.universitybazaar.Models.Profile;

import java.util.ArrayList;

public class PostDAO {
    private DatabaseHelper dbHelper;
    public PostDAO(Context context){
        dbHelper=new DatabaseHelper(context);
    }

    /**
     * @author Jingwen Ma
     * @param post
     * @description Update the post information by postId
     */
    public Boolean updatePost(Post post) {
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("title",post.getTitle());
            values.put("description",post.getDescription());
            values.put("category",post.getCategory());
            db.update("posts", values, "_id = '"+post.getId()+"'", null );
            db.close();
            return true;
        }catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @author Jingwen Ma
     * @param post_id
     * @description Delete the post by postId
     */
    public Boolean deletePost(int post_id) {
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.delete("posts", "_id = '"+post_id+"'", null);
            db.close();
            return true;
        }catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @author Jingwen Ma
     * @param post_id
     * @description Delete the comment by postId
     */
    public Boolean deleteComment(int post_id) {
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.delete("comments", "postId = '"+post_id+"'", null);
            db.close();
            return true;
        }catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * @author Samir Shrestha
     */
    public ArrayList<Post> getPostByTitle(String title){
        ArrayList<Post> posts = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try {
            String[] columns = new String[]{
                    "_id",
                    "title",
                    "description",
                    "creatorId",
                    "creatorName",
                    "createdDate",
                    "category",
            };

            String[] args = new String[]{title+"%"};

            //retrieve all the posts from the posts table where title == provided title in the argument
            Cursor cursor = db.query("posts", columns, "title LIKE ?", args, null, null, null);
            if(cursor != null){
                if(cursor.moveToFirst()){
                    boolean isLast = false;
                    while(!isLast){ // continues until all the retrieved rows have been iterated.
                        Post post = new Post();
                        int postId = cursor.getInt(cursor.getColumnIndex("_id"));
                        String description = cursor.getString(cursor.getColumnIndex("description"));
                        String creatorId = cursor.getString(cursor.getColumnIndex("creatorId"));
                        String creatorName = cursor.getString(cursor.getColumnIndex("creatorName"));
                        String createdDate = cursor.getString(cursor.getColumnIndex("createdDate"));
                        String category = cursor.getString(cursor.getColumnIndex("category"));
                        title = cursor.getString(cursor.getColumnIndex("title"));

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
     * @description This method returns all the posts from all users.
     */
    public ArrayList<Post> getAllPosts(){
        ArrayList<Post> posts = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try {
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
}
