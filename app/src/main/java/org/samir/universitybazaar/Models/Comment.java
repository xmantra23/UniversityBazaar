package org.samir.universitybazaar.Models;

/**
 * @author Samir Shrestha
 * @description A DTO (data transfer object) class that holds data about a comment for a user post.
 */
public class Comment {
    private int _id;
    private int postId;
    private String commentText;
    private String commentOwnerName;
    private String commentOwnerId;
    private String commentReceiverName;
    private String commentReceiverId;
    private String commentDate;

    public Comment(){

    }

    public Comment(int _id, int postId, String commentText, String commentOwnerName, String commentOwnerId,
                   String commentDate,String commentReceiverName, String commentReceiverId) {
        this._id = _id;
        this.postId = postId;
        this.commentText = commentText;
        this.commentOwnerName = commentOwnerName;
        this.commentOwnerId = commentOwnerId;
        this.commentReceiverName = commentReceiverName;
        this.commentReceiverId = commentReceiverId;
        this.commentDate = commentDate;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public String getCommentOwnerName() {
        return commentOwnerName;
    }

    public void setCommentOwnerName(String commentOwnerName) {
        this.commentOwnerName = commentOwnerName;
    }

    public String getCommentOwnerId() {
        return commentOwnerId;
    }

    public void setCommentOwnerId(String commentOwnerId) {
        this.commentOwnerId = commentOwnerId;
    }

    public String getCommentReceiverName() {
        return commentReceiverName;
    }

    public void setCommentReceiverName(String commentReceiverName) {
        this.commentReceiverName = commentReceiverName;
    }

    public String getCommentReceiverId() {
        return commentReceiverId;
    }

    public void setCommentReceiverId(String commentReceiverId) {
        this.commentReceiverId = commentReceiverId;
    }

    public String getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(String commentDate) {
        this.commentDate = commentDate;
    }
}
