package com.example.jun.whereareyou.Data;

import android.os.Parcel;
import android.os.Parcelable;

public class FriendProfile implements Parcelable{
    private String name;
    private String image;

    public FriendProfile(String name, String image) {
        this.name = name;
        this.image = image;
    }

    public FriendProfile(Parcel in) {
        this.name = in.readString();
        this.image = in.readString();
    }


    public static final Creator<FriendProfile> CREATOR = new Creator<FriendProfile>() {
        @Override
        public FriendProfile createFromParcel(Parcel in) {
            return new FriendProfile(in);
        }

        @Override
        public FriendProfile[] newArray(int size) {
            return new FriendProfile[size];
        }
    };

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return this.name;
    }

    public String getImage() {
        return this.image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.name);
        parcel.writeString(this.image);
    }
}
