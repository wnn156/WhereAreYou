package com.example.jun.whereareyou.Data;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

public class User implements Parcelable {
    private String email;
    private String name;
    private String phone_number;
    private int score;
    private int count;
    private double latitude;
    private double longitude;

    public User(String email, String name, String phone_number, int score, int count,double latitude,double longitude) {
        this.email = email;
        this.name = name;
        this.phone_number = phone_number;
        this.score = score;
        this.count = count;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public User(){

    }

    public User(JSONObject o) {
        try {
        this.email = o.getString("memberId");
        this.name = o.getString("name");
        this.phone_number = o.getString("phone");
        this.score = o.getInt("score");
        this.count = o.getInt("creditCnt");
        this.latitude = o.getDouble("latitude");
        this.longitude = o.getDouble("longitude");
        } catch(Exception e){

        }
    }


    protected User(Parcel in) {
        email = in.readString();
        name = in.readString();
        phone_number = in.readString();
        score = in.readInt();
        count = in.readInt();
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public double getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(email);
        dest.writeString(name);
        dest.writeString(phone_number);
        dest.writeInt(score);
        dest.writeInt(count);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", phone_number='" + phone_number + '\'' +
                ", score=" + score +
                ", count=" + count +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

    public double get_credit() {
        return count > 0 ? score / count : 0.0;
    }




}
