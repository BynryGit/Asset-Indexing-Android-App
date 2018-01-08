package com.aia.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserProfileModel implements Serializable {
    @SerializedName("city")
    public String city;


    @SerializedName("user_id")
    public String user_id;

    @SerializedName("email_id")
    public String email_id;

    @SerializedName("profile_image")
    public String profile_image;

    @SerializedName("emp_type")
    public String emp_type;

    @SerializedName("contact_no")
    public String contact_no;

    @SerializedName("address")
    public String address;

    @SerializedName("emp_id")
    public String emp_id;

    @SerializedName("user_name")
    public String user_name;

    @SerializedName("token")
    public String token;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getEmp_type() {
        return emp_type;
    }

    public void setEmp_type(String emp_type) {
        this.emp_type = emp_type;
    }

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(String emp_id) {
        this.emp_id = emp_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }



}


