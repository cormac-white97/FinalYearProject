package Objects;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class EventObj implements Parcelable {
    String id;
    String type;
    String location;
    String date;
    String endDate;
    String group;
    double price;
    String createdBy;
    HashMap<String, String> eventLeaders;
    HashMap<String, String> parentReviews;
    ArrayList<String> paymentList;
    int availableSpaces;
    double lng;
    double lat;
    String approved;

    public EventObj(String id, String type, String location, String date, String endDate, String group, double price, String createdBy, HashMap<String, String> eventLeaders, HashMap<String, String> parentReviews, ArrayList<String> paymentList, int availableSpaces, double lng, double lat, String approved) {
        this.id = id;
        this.type = type;
        this.location = location;
        this.date = date;
        this.endDate = endDate;
        this.group = group;
        this.price = price;
        this.createdBy = createdBy;
        this.eventLeaders = eventLeaders;
        this.parentReviews = parentReviews;
        this.paymentList = paymentList;
        this.availableSpaces = availableSpaces;
        this.lng = lng;
        this.lat = lat;
        this.approved = approved;
    }

    public EventObj() {
    }

    protected EventObj(Parcel in) {
        id = in.readString();
        type = in.readString();
        location = in.readString();
        date = in.readString();
        endDate = in.readString();
        group = in.readString();
        price = in.readDouble();
        createdBy = in.readString();
        paymentList = in.createStringArrayList();
        availableSpaces = in.readInt();
        lng = in.readDouble();
        lat = in.readDouble();
        approved = in.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public HashMap<String, String> getEventLeaders() {
        return eventLeaders;
    }

    public void setEventLeaders(HashMap<String, String> eventLeaders) {
        this.eventLeaders = eventLeaders;
    }

    public HashMap<String, String> getParentReviews() {
        return parentReviews;
    }

    public void setParentReviews(HashMap<String, String> parentReviews) {
        this.parentReviews = parentReviews;
    }

    public ArrayList<String> getPaymentList() {
        return paymentList;
    }

    public void setPaymentList(ArrayList<String> paymentList) {
        this.paymentList = paymentList;
    }

    public int getAvailableSpaces() {
        return availableSpaces;
    }

    public void setAvailableSpaces(int availableSpaces) {
        this.availableSpaces = availableSpaces;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getApproved() {
        return approved;
    }

    public void setApproved(String approved) {
        this.approved = approved;
    }

    public static Creator<EventObj> getCREATOR() {
        return CREATOR;
    }

    public static final Creator<EventObj> CREATOR = new Creator<EventObj>() {
        @Override
        public EventObj createFromParcel(Parcel in) {
            return new EventObj(in);
        }

        @Override
        public EventObj[] newArray(int size) {
            return new EventObj[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }



    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(type);
        dest.writeString(location);
        dest.writeString(date);
        dest.writeString(endDate);
        dest.writeString(group);
        dest.writeDouble(price);
        dest.writeString(createdBy);
        dest.writeStringList(paymentList);
        dest.writeInt(availableSpaces);
        dest.writeDouble(lng);
        dest.writeDouble(lat);
        dest.writeString(approved);
    }
}
