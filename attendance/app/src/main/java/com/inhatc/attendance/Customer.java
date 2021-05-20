package com.inhatc.attendance;

public class Customer {

    public String userId;
    public String userName;

    public Customer() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Customer(String userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {return userName;}

    public void setUserName(String userName) {this.userName = userName;}


    @Override
    public String toString() {
        return "User{" +
                "userNumber='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
