package com.inhatc.attendance;

public class Customer {

    public String userId;
    public String userName;
    public String userPosition;

    public Customer() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Customer(String userId, String userName, String userPosition) {
        this.userId = userId;
        this.userName = userName;
        this.userPosition = userPosition;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {return userName;}

    public void setUserName(String userName) {this.userName = userName;}

    public String getUserPosition() {return userPosition;}

    public void setUserPosition(String userPosition) {this.userPosition = userPosition;}


    @Override
    public String toString() {
        return "User{" +
                "userNumber='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
