package org.samir.universitybazaar.Models;

public class Club {
    private int _id; //club id
    private String title; //club title
    private String shortDescription; //short description for the club
    private String longDescription; //long description for the club
    private String ownerName; //name of the person who created this club
    private String ownerId; //id of the person who created this club
    private String createdDate; //date this club was created.
    private int memberCount; //total number of members in this club. default is 0

    public Club(int _id, String title, String shortDescription, String longDescription, String ownerName, String ownerId, String createdDate, int memberCount) {
        this._id = _id;
        this.title = title;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
        this.ownerName = ownerName;
        this.ownerId = ownerId;
        this.createdDate = createdDate;
        this.memberCount = memberCount;
    }

    public Club(String title, String shortDescription, String longDescription, String ownerName, String ownerId, String createdDate, int memberCount) {
        this._id = -1;
        this.title = title;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
        this.ownerName = ownerName;
        this.ownerId = ownerId;
        this.createdDate = createdDate;
        this.memberCount = memberCount;
    }

    public Club(){

    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public int getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }

    @Override
    public String toString() {
        return "Club{" +
                "_id=" + _id +
                ", title='" + title + '\'' +
                ", shortDescription='" + shortDescription + '\'' +
                ", longDescription='" + longDescription + '\'' +
                ", ownerName='" + ownerName + '\'' +
                ", ownerId='" + ownerId + '\'' +
                ", createdDate='" + createdDate + '\'' +
                ", memberCount=" + memberCount +
                '}';
    }
}
