package com.www.itechanalogy.seenminglemaking.List;

public class MessageList {
    String myid,uid,message,sender_id,time;

    public MessageList(String myid, String uid, String message, String sender_id, String time) {
        this.myid = myid;
        this.uid = uid;
        this.message = message;
        this.sender_id = sender_id;
        this.time = time;
    }

    public String getMyid() {
        return myid;
    }

    public void setMyid(String myid) {
        this.myid = myid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
