package com.example.dream11.PropertyClasses;

public class User {

    public String email;
    public String username;
    public String profile_pic;
    public String mobile_no;
    public String age;

    public User() {}

    public User(String email, String username, String profile_pic, String mobile_no, String age) {
        this.email = email;
        this.username = username;
        this.profile_pic = profile_pic;
        this.mobile_no = mobile_no;
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
