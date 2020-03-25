package Objects;

public class PaymentHistory {
    String paymentId;
    double amount;
    String eventId;
    String parentId;
    String memberId;
    String group;
    String location;

    public PaymentHistory(String paymentId, double amount, String eventId, String parentId, String memberId, String group, String location) {
        this.paymentId = paymentId;
        this.amount = amount;
        this.eventId = eventId;
        this.parentId = parentId;
        this.memberId = memberId;
        this.group = group;
        this.location = location;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
