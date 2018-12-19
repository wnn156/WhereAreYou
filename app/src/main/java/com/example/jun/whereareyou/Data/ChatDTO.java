package com.example.jun.whereareyou.Data;

public class ChatDTO {

    private String userName;
    private String message;
    private String firebaseKey;
    private long time;

    public ChatDTO() {}
    public ChatDTO(String userName, String message) {
        this.userName = userName;
        this.message = message;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserName() {
        return userName;
    }

    public String getMessage() {
        return message;
    }

    public String getFirebaseKey() {
        return firebaseKey;
    }

    public void setFirebaseKey(String firebaseKey) {
        this.firebaseKey = firebaseKey;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}