package org.samir.universitybazaar.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import org.samir.universitybazaar.Models.Post;
import org.samir.universitybazaar.Models.Profile;

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
}
