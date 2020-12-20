package com.www.itechanalogy.seenminglemaking.List;

public class CardsList {
    String myid,uid,uname,uimage,age,country,country_flag,proffesion,about,current_status,already_liked;

    public CardsList(String myid, String uid, String uname, String uimage, String age, String country, String country_flag, String proffesion, String about, String current_status, String already_liked) {
        this.myid = myid;
        this.uid = uid;
        this.uname = uname;
        this.uimage = uimage;
        this.age = age;
        this.country = country;
        this.country_flag = country_flag;
        this.proffesion = proffesion;
        this.about = about;
        this.current_status = current_status;
        this.already_liked = already_liked;
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

    public String getProffesion() {
        return proffesion;
    }

    public void setProffesion(String proffesion) {
        this.proffesion = proffesion;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getCurrent_status() {
        return current_status;
    }

    public void setCurrent_status(String current_status) {
        this.current_status = current_status;
    }

    public String getAlready_liked() {
        return already_liked;
    }

    public void setAlready_liked(String already_liked) {
        this.already_liked = already_liked;
    }

    public String getCountry_flag() {
        return country_flag;
    }

    public void setCountry_flag(String country_flag) {
        this.country_flag = country_flag;
    }
}
