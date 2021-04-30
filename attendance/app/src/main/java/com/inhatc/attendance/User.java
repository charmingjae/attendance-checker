package com.inhatc.attendance;

public class User {

    public String userNumber;
    public String userAttendance;
    public String userAttendTime;
    public String userPosition;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String userNumber, String userAttendance, String userAttendTime, String userPosition) {
        this.userNumber = userNumber;
        this.userAttendance = userAttendance;
        this.userAttendTime = userAttendTime;
        this.userPosition = userPosition;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public String getUserAttendance() {
        return userAttendance;
    }

    public void setUserAttendance(String userAttendance) {
        this.userAttendance = userAttendance;
    }

    public String getUserAttendTime(){
        return userAttendTime;
    }

    public void setUserAttendTime(String userAttendTime){
        this.userAttendTime = userAttendTime;
    }

    public String getUserPosition() { return userPosition; }

    public void setUserPosition(String userPosition) {this.userPosition = userPosition;}

    @Override
    public String toString() {
        return "User{" +
                "userNumber='" + userNumber + '\'' +
                ", userAttendance='" + userAttendance + '\'' +
                '}';
    }
}
