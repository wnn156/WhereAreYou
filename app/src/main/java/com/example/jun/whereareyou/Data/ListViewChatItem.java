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
    private ArrayList<User> participant;
    private int promise_id;
    private double latitude;
    private double longitude;

    //private ArrayList<User> users;

    public ListViewChatItem(String chat_name, String place, String time, int promise_id,double latitude,double longitude, String key) {
        this.chat_name = chat_name;
        this.place = place;
        this.time = time;
        this.promise_id=promise_id;
        this.participant = new ArrayList<>();
        this.latitude = latitude;
        this.longitude = longitude;
        this.key = key;
    }

    public ListViewChatItem(JSONObject o) {
        try {
            this.participant = new ArrayList<>();
            this.chat_name = o.getString("name");
            this.place = o.getString("place");
            this.time = o.getString("time");
            this.promise_id = o.getInt("id");
            this.latitude = o.getDouble("x");
            this.longitude = o.getDouble("y");
            this.key = o.getString("chatKey");
        }catch (Exception e) {

        }
    }

    public void addParcicipant(JSONObject o) {
        participant.add(new User(o));
    }

    protected ListViewChatItem(Parcel in) {
        chat_name = in.readString();
        place = in.readString();
        time = in.readString();
        key = in.readString();
        participant = in.readArrayList(User.class.getClassLoader());
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

    public ArrayList<User> getparticipant() {
        return participant;
    }

    public void setParticipant(ArrayList<User> participant) {
        this.participant = participant;
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
        dest.writeList(participant);
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
                ", users=" + participant +
                '}';
    }
}
