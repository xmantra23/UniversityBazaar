package org.samir.universitybazaar.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import org.samir.universitybazaar.Models.Comment;
import org.samir.universitybazaar.Models.Profile;

import java.util.ArrayList;
/**
 * @author Minyi Lu,Yifei Lu
 * @description This class define the operation that relate to comment database.
 */
public class CommentDAO {
    private DatabaseHelper dbHelper;
    public CommentDAO(Context context){
        dbHelper=new DatabaseHelper(context);
    }

    /**
     * @author Minyi Lu
     * @description This method adds a new comment to the comments table.
     */
    public boolean addComment(Comment comment){
        SQLiteDatabase db = null;
        try {
            db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("postId", comment.getPostId());
            values.put("commentText", comment.getCommentText());
            values.put("commentOwnerName", comment.getCommentOwnerName());
            values.put("commentOwnerId", comment.getCommentOwnerId());
            values.put("commentReceiverName", comment.getCommentReceiverName());
            values.put("commentReceiverId", comment.getCommentReceiverId());
            values.put("commentDate", comment.getCommentDate());

            long userId = db.insert("comments",null,values);
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
     * @author minyi lu
     * @discription this method gets comments by post_id
     */
    public ArrayList<Comment> getCommentsByPostId(int post_id){
        ArrayList<Comment> comments = new ArrayList<>();
        SQLiteDatabase db = null;
        try {
            db = dbHelper.getWritableDatabase();
            String[] columns = new String[]{
                    "_id",
                    "postId",
                    "commentText",
                    "commentOwnerName",
                    "commentOwnerId",
                    "commentReceiverName",
                    "commentReceiverId",
                    "commentDate"
            };
            String[] args = new String[]{
                    post_id + "" //doing this to convert int to string. selection arguments must be strings in the sql query.
            };
            //get all comments by post_id in comments table
            Cursor cursor = db.query("comments", columns, "postId=?", args, null, null, null);
            if (cursor != null){
                if(cursor.moveToFirst()){
                    boolean isLast = false;
                    while (!isLast){ //continue until travel all comments
                        Comment comment = new Comment();
                        int _id = cursor.getInt(cursor.getColumnIndex("_id"));
                        int postId = cursor.getInt(cursor.getColumnIndex("postId"));
                        String commentText = cursor.getString(cursor.getColumnIndex("commentText"));
                        String commentOwnerName = cursor.getString(cursor.getColumnIndex("commentOwnerName"));
                        String commentOwnerId = cursor.getString(cursor.getColumnIndex("commentOwnerId"));
                        String commentReceiverName = cursor.getString(cursor.getColumnIndex("commentReceiverName"));
                        String commentReceiverId = cursor.getString(cursor.getColumnIndex("commentReceiverId"));
                        String commentDate = cursor.getString(cursor.getColumnIndex("commentDate"));

                        comment.set_id(_id);
                        comment.setPostId(postId);
                        comment.setCommentText(commentText);
                        comment.setCommentOwnerName(commentOwnerName);
                        comment.setCommentOwnerId(commentOwnerId);
                        comment.setCommentReceiverName(commentReceiverName);
                        comment.setCommentReceiverId(commentReceiverId);
                        comment.setCommentDate(commentDate);

                        comments.add(comment);

                        if (cursor.isLast()){
                            isLast = true;
                        } else {
                            cursor.moveToNext();
                        }
                    }


                }
            } else {
                db.close();
                cursor.close();
                return null;
            }
        }catch (SQLException e){
            e.printStackTrace();
            db.close();
            return null;
        }

        return comments;
    }

    /**
     * @author Lu yifei
     * @discription this method get comment by comment id
     */
    public Comment getComment(int commentId) {
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            String[] columns = new String[]{
                    "_id",
                    "postId",
                    "commentText",
                    "commentOwnerName",
                    "commentOwnerId",
                    "commentReceiverName",
                    "commentReceiverId",
                    "commentDate"
            };

            String[] args = new String[]{
                    commentId + ""
            };

            Cursor cursor = db.query("comments", columns, "_id=?", args, null, null, null, null);
            if(cursor != null){

                Comment comment = new Comment();

                if(cursor.moveToFirst()){
                    int postId = cursor.getInt(cursor.getColumnIndex("postId"));
                    String commentText = cursor.getString(cursor.getColumnIndex("commentText"));
                    String commentOwnerName = cursor.getString(cursor.getColumnIndex("commentOwnerName"));
                    String commentOwnerId = cursor.getString(cursor.getColumnIndex("commentOwnerId"));
                    String commentReceiverName = cursor.getString(cursor.getColumnIndex("commentReceiverName"));
                    String commentReceiverId = cursor.getString(cursor.getColumnIndex("commentReceiverId"));
                    String commentDate = cursor.getString(cursor.getColumnIndex("commentDate"));

                    comment.set_id(commentId);
                    comment.setPostId(postId);
                    comment.setCommentText(commentText);
                    comment.setCommentOwnerName(commentOwnerName);
                    comment.setCommentOwnerId(commentOwnerId);
                    comment.setCommentReceiverName(commentReceiverName);
                    comment.setCommentReceiverId(commentReceiverId);
                    comment.setCommentDate(commentDate);

                    cursor.close();
                    db.close();
                    return comment;
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
     * @author Lu yifei
     * @discription this method delete comment by comment id
     */
    public Boolean deleteComment(int commentId) {
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            String[] args = new String[]{
                    commentId + ""
            };
            db.delete("comments","_id=?", args);
            db.close();
            return true;
        }catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
