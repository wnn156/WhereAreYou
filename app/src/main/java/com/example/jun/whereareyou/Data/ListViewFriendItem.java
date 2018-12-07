package com.example.jun.whereareyou.Data;

public class ListViewFriendItem {
    private String user_id;
    private String credit_rate;

    public ListViewFriendItem(String user_id, String credit_rate) {
        this.user_id = user_id;
        this.credit_rate = credit_rate;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCredit_rate() {
        return credit_rate;
    }

    public void setCredit_rate(String credit_rate) {
        this.credit_rate = credit_rate;
    }
}
