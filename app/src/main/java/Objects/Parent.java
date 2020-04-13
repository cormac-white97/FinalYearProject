package Objects;

public class Parent {
    String parentId;
    String firstName;
    String password;
    String phone;
    String email;
    String childId;
    String group;
    String fcmToken;

    public Parent() {
    }

    public Parent(String parentId, String firstName, String phone, String email, String childId, String group, String fcmToken) {
        this.parentId = parentId;
        this.firstName = firstName;
        this.phone = phone;
        this.email = email;
        this.childId = childId;
        this.group = group;
        this.fcmToken = fcmToken;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return firstName;
    }

    public void setName(String firstName) {
        this.firstName = firstName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getChildId() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}
