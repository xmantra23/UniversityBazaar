package org.samir.universitybazaar.Models;

/**
 * @author Samir Shrestha
 * @description This is just a DTO (Data Transfer Object) class for holding information about the post.
 */
public class Post {
    private int _id;
    private String title;
    private String description;
    private String creatorId;
    private String creatorName;
    private String createdDate;
    private String category;

    public Post(int _id, String title, String description, String creatorId, String creatorName, String createdDate, String category) {
        this._id = _id;
        this.title = title;
        this.description = description;
        this.creatorId = creatorId;
        this.creatorName = creatorName;
        this.createdDate = createdDate;
        this.category = category;
    }

    public Post() {
    }

    public int getId() {
        return _id;
    }

    public void setId(int _id) {
        this._id = _id;
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

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Post{" +
                "_id=" + _id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", creatorId='" + creatorId + '\'' +
                ", creatorName='" + creatorName + '\'' +
                ", createdDate='" + createdDate + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}
