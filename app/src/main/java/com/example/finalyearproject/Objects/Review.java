package com.example.finalyearproject.Objects;

public class Review {
    String reviewId;
    String parentId;
    String eventId;
    String createdBy;
    String title;
    String body;
    int rating;

    public Review() {
    }

    public Review(String reviewId, String parentId, String eventId, String createdBy, String title, String body, int rating) {
        this.reviewId = reviewId;
        this.parentId = parentId;
        this.eventId = eventId;
        this.createdBy = createdBy;
        this.title = title;
        this.body = body;
        this.rating = rating;
    }

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
