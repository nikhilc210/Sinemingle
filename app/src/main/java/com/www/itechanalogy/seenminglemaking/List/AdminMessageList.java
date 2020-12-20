package com.www.itechanalogy.seenminglemaking.List;

public class AdminMessageList {
    String myid,mid,message,time;

    public AdminMessageList(String myid, String mid, String message, String time) {
        this.myid = myid;
        this.mid = mid;
        this.message = message;
        this.time = time;
    }

    public String getMyid() {
        return myid;
    }

    public void setMyid(String myid) {
        this.myid = myid;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
