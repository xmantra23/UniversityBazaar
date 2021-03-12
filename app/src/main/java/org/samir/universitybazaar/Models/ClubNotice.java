package org.samir.universitybazaar.Models;

/**
 * @author samir shrestha
 * @decription this class will be used to hold data about an announcement within a club.
 */
public class ClubNotice {
    private int _id;
    private int clubId;
    private String title;
    private String description;
    private String creatorId;
    private String createdDate;

    public ClubNotice(int _id, int clubId, String title, String description, String creatorId, String createdDate) {
        this._id = _id;
        this.clubId = clubId;
        this.title = title;
        this.description = description;
        this.creatorId = creatorId;
        this.createdDate = createdDate;
    }

    public ClubNotice(int clubId, String title, String description, String creatorId, String createdDate) {
        this._id = -1;
        this.clubId = clubId;
        this.title = title;
        this.description = description;
        this.creatorId = creatorId;
        this.createdDate = createdDate;
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
