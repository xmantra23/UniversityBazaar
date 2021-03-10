package org.samir.universitybazaar.Models;

/**
 * @author Samir Shrestha
 * holds data about posts made by users within a club.
 */
public class ClubPost {
    private int _id;
    private int clubId;
    private String title;
    private String description;
    private String creatorName;
    private String creatorId;
    private String createdDate;


    private String adminId;

    public ClubPost(int _id, int clubId, String title, String description, String creatorName, String creatorId, String createdDate,String adminId) {
        this._id = _id;
        this.clubId = clubId;
        this.title = title;
        this.description = description;
        this.creatorName = creatorName;
        this.creatorId = creatorId;
        this.createdDate = createdDate;
        this.adminId = adminId;
    }

    public ClubPost(int clubId, String title, String description, String creatorName, String creatorId, String createdDate,String adminId) {
        this._id = -1;
        this.clubId = clubId;
        this.title = title;
        this.description = description;
        this.creatorName = creatorName;
        this.creatorId = creatorId;
        this.createdDate = createdDate;
        this.adminId = adminId;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }


    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getClubId() {
        return clubId;
    }

    public void setClubId(int clubId) {
        this.clubId = clubId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
}
