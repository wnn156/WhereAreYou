package com.example.jun.whereareyou.Data;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private String email;
    private String name;
    private String phone_number;
    private double score;
    private int count;


    public User(){
        email = null;
    }

    public User(String email){
        this.email = email;
    }
    public User(String name, double score){
        this.name = name;
        this.score = score;
    }

    protected User(Parcel in) {
        email = in.readString();
        name = in.readString();
        phone_number = in.readString();
        score = in.readDouble();
        count = in.readInt();
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

    public void setScore(double score) {
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
        dest.writeDouble(score);
        dest.writeInt(count);
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", phone_number='" + phone_number + '\'' +
                ", score=" + score +
                ", count=" + count +
                '}';
    }
}
