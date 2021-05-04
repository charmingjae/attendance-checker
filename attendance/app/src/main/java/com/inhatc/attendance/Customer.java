package com.inhatc.attendance;

public class Customer {

    public String userId;
    public String userName;
    public String userPhone;

    public Customer() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Customer(String userId, String userName, String userPhone) {
        this.userId = userId;
        this.userName = userName;
        this.userPhone = userPhone;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {return userName;}

    public void setUserName(String userName) {this.userName = userName;}

    public String getUserPhone() {return userPhone;}

    public void setUserPhone(String userPhone) {this.userPhone = userPhone;}

    @Override
    public String toString() {
        return "User{" +
                "userNumber='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
