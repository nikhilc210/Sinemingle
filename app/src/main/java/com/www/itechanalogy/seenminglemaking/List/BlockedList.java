package com.www.itechanalogy.seenminglemaking.List;

public class BlockedList {
    String myid,uid,uname,uimage,age,country,country_flag,prof,when;

    public BlockedList(String myid, String uid, String uname, String uimage, String age, String country, String country_flag, String prof, String when) {
        this.myid = myid;
        this.uid = uid;
        this.uname = uname;
        this.uimage = uimage;
        this.age = age;
        this.country = country;
        this.country_flag = country_flag;
        this.prof = prof;
        this.when = when;
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

    public String getUimage() {
        return uimage;
    }

    public void setUimage(String uimage) {
        this.uimage = uimage;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry_flag() {
        return country_flag;
    }

    public void setCountry_flag(String country_flag) {
        this.country_flag = country_flag;
    }

    public String getProf() {
        return prof;
    }

    public void setProf(String prof) {
        this.prof = prof;
    }

    public String getWhen() {
        return when;
    }

    public void setWhen(String when) {
        this.when = when;
    }
}
