package com.example.jun.whereareyou.Data;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private String email;

    public User(){
        email = null;
    }

    public User(Parcel in) {
        email = in.readString();
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
    }
}
