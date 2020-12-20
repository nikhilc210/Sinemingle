package com.www.itechanalogy.seenminglemaking.List;

public class ChatBoxList {
    String myid,uid,uname,prof,message,time,uimage,num;

    public ChatBoxList(String myid, String uid, String uname, String prof, String message, String time, String uimage, String num) {
        this.myid = myid;
        this.uid = uid;
        this.uname = uname;
        this.prof = prof;
        this.message = message;
        this.time = time;
        this.uimage = uimage;
        this.num = num;
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

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getProf() {
        return prof;
    }

    public void setProf(String prof) {
        this.prof = prof;
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

    public String getUimage() {
        return uimage;
    }

    public void setUimage(String uimage) {
        this.uimage = uimage;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
}
