package com.example.jun.whereareyou.Data;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import java.util.ArrayList;

public class ListViewChatItem implements Parcelable {
    private String chat_name; // 약속 이름
    private String place;
    private String time;
    private String key; // 채팅방 이름
    private ArrayList<User> users;
    private int promise_id;
    private double latitude;
    private double longitude;

    //private ArrayList<User> users;

    public ListViewChatItem(String chat_name, String place, String time, ArrayList<User> users, int promise_id,double latitude,double longitude) {
        this.chat_name = chat_name;
        this.place = place;
        this.time = time;
        this.users = users;
        this.promise_id=promise_id;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public ListViewChatItem(JSONObject o) {
        try {
            this.chat_name = o.getString("name");
            this.place = o.getString("place");
            this.time = o.getString("time");
            this.users = new ArrayList<>();
            this.promise_id = o.getInt("id");
            this.latitude = o.getDouble("x");
            this.longitude = o.getDouble("y");
        }catch (Exception e) {


        }
    }

    protected ListViewChatItem(Parcel in) {
        chat_name = in.readString();
        place = in.readString();
        time = in.readString();
        key = in.readString();
        users = in.readArrayList(User.class.getClassLoader());
        promise_id = in.readInt();
        latitude = in.readDouble();
        longitude = in.readDouble();
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

    public int getPromise_id() {
        return promise_id;
    }

    public void setPromise_id(int promise_id) {
        this.promise_id = promise_id;
    }

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

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

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
        dest.writeString(key);
        dest.writeList(users);
        dest.writeInt(promise_id);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }

    @Override
    public String toString() {
        return "ListViewChatItem{" +
                "chat_name='" + chat_name + '\'' +
                ", place='" + place + '\'' +
                ", time='" + time + '\'' +
                ", key='" + key + '\'' +
                ", users=" + users +
                '}';
    }
}
