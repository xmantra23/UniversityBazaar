package org.samir.universitybazaar.Models;

/**
 * @author samir shrestha
 * class for holding details about comments made within a notice inside a club.
 */
public class ClubNoticeComment {
    private int _id;
    private int noticeId;
    private String commentText;
    private String commentOwnerId;
    private String commentOwnerName;
    private String commentDate;
    private String adminId;

    public ClubNoticeComment(){

    }

    public ClubNoticeComment(int _id, int noticeId, String commentText, String commentOwnerId, String commentOwnerName, String commentDate,String adminId) {
        this._id = _id;
        this.noticeId = noticeId;
        this.commentText = commentText;
        this.commentOwnerName = commentOwnerName;
        this.commentOwnerId = commentOwnerId;
        this.commentDate = commentDate;
        this.adminId = adminId;
    }

    public ClubNoticeComment(int noticeId, String commentText, String commentOwnerId, String commentOwnerName, String commentDate, String adminId) {
        this._id = -1;
        this.noticeId = noticeId;
        this.commentText = commentText;
        this.commentOwnerId = commentOwnerId;
        this.commentOwnerName = commentOwnerName;
        this.commentDate = commentDate;
        this.adminId = adminId;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(int noticeId) {
        this.noticeId = noticeId;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public String getCommentOwnerId() {
        return commentOwnerId;
    }

    public void setCommentOwnerId(String commentOwnerId) {
        this.commentOwnerId = commentOwnerId;
    }

    public String getCommentOwnerName() {
        return commentOwnerName;
    }

    public void setCommentOwnerName(String commentOwnerName) {
        this.commentOwnerName = commentOwnerName;
    }

    public String getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(String commentDate) {
        this.commentDate = commentDate;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    @Override
    public String toString() {
        return "ClubNoticeComment{" +
                "_id=" + _id +
                ", noticeId=" + noticeId +
                ", commentText='" + commentText + '\'' +
                ", commentOwnerId='" + commentOwnerId + '\'' +
                ", commentOwnerName='" + commentOwnerName + '\'' +
                ", commentDate='" + commentDate + '\'' +
                ", adminId='" + adminId + '\'' +
                '}';
    }
}
