package Objects;

public class Review {
    String reviewId;
    String parentId;
    String eventId;
    String title;
    String body;

    public Review() {
    }

    public Review(String reviewId, String parentId, String eventId, String title, String body) {
        this.reviewId = reviewId;
        this.parentId = parentId;
        this.eventId = eventId;
        this.title = title;
        this.body = body;
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
}
