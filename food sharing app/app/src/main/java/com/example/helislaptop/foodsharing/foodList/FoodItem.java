package com.example.helislaptop.foodsharing.foodList;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.example.helislaptop.foodsharing.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

@Entity(tableName = "foodItem")
public class FoodItem implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int itemId;

    public String user;

    public String time;
    public String capacity;
    public String description;
    public String foodImage;
    public String postOrRequest;
    public double longitude;
    public double latitude;
    public String phoneNumber;
    public String address;
    public String category;
    public String expiredTime;

    public FoodItem(String user, String description, String postOrRequest, String phoneNumber, String address, double longitude, double latitude, String category, String capacity, String expiredTime, String foodImage) {

        try {

        } catch (Exception e){
            e.printStackTrace();
        }
        this.user = user;
        this.description = description;
        this.time = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        this.postOrRequest = postOrRequest;
        this.category = category;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.latitude = latitude;
        this.longitude = longitude;
        this.capacity = capacity;
        this.expiredTime = expiredTime;
        this.foodImage = foodImage;
    }
    public int parseIntWithDefault(String s, int defaultValue) {
        try {
            return Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return defaultValue;
        }
    }


    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getFoodImage() {
        return foodImage;
    }

    public void setFoodImage(String foodImage) {
        this.foodImage = foodImage;
    }

    public String getPostOrRequest() {
        return postOrRequest;
    }

    public void setPostOrRequest(String postOrRequest) {
        this.postOrRequest = postOrRequest;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(String expiredTime) {
        this.expiredTime = expiredTime;
    }





    protected FoodItem(Parcel in) {
        itemId = in.readInt();
        user = in.readString();
        time = in.readString();
        capacity = in.readString();
        description = in.readString();
        foodImage = in.readString();
        postOrRequest = in.readString();
        longitude = in.readDouble();
        latitude = in.readDouble();
        phoneNumber = in.readString();
        address = in.readString();
        category = in.readString();
        expiredTime = in.readString();
    }

    public static final Creator<FoodItem> CREATOR = new Creator<FoodItem>() {
        @Override
        public FoodItem createFromParcel(Parcel in) {
            return new FoodItem(in);
        }

        @Override
        public FoodItem[] newArray(int size) {
            return new FoodItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
    public FoodItem() {

    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(itemId);
        dest.writeString(user);
        dest.writeString(capacity);
        dest.writeString(description);
        dest.writeString(foodImage);
        dest.writeString(time);
        dest.writeString(postOrRequest);
    }
}
