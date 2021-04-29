package com.inhatc.attendance;

public class User {

    public String userNumber;
    public String userAttendance;
    public String userAttendTime;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String userNumber, String userAttendance, String userAttendTime) {
        this.userNumber = userNumber;
        this.userAttendance = userAttendance;
        this.userAttendTime = userAttendTime;
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

    @Override
    public String toString() {
        return "User{" +
                "userNumber='" + userNumber + '\'' +
                ", userAttendance='" + userAttendance + '\'' +
                '}';
    }
}
