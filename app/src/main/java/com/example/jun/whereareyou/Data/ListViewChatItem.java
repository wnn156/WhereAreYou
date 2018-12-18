package com.example.jun.whereareyou.Data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class ListViewChatItem implements Parcelable {
    private String chat_name;
    private String place;
    private String time;

    //private ArrayList<User> users;

    public ListViewChatItem(String chat_name, String place, String time) {
        this.chat_name = chat_name;
        this.place = place;
        this.time = time;
    }

    protected ListViewChatItem(Parcel in) {
        chat_name = in.readString();
        place = in.readString();
        time = in.readString();
    }

    public static final Creator<ListViewChatItem> CREATOR = new Creator<ListViewChatItem>() {
        @Override
        public ListViewChatItem createFromParcel(Parcel in) {
            return new ListViewChatItem(in);
        }

        @Override
        public ListViewChatItem[] newArray(int size) {
            return new ListViewChatItem[size];
        }
    };


    public String getChat_name() {
        return chat_name;
    }

    public void setChat_name(String chat_name) {
        this.chat_name = chat_name;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(chat_name);
        dest.writeString(place);
        dest.writeString(time);
    }
}
