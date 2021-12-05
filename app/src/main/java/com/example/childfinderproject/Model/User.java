package com.example.childfinderproject.Model;

public class User {

    String userFulName;
    String userName;
    String userPhone;
    String userPassword;

    public User(String userFulName, String userName, String userPhone, String userPassword) {
        this.userFulName = userFulName;
        this.userName = userName;
        this.userPhone = userPhone;
        this.userPassword = userPassword;
    }

    public User() {
    }


    public String getUserFulName() {
        return userFulName;
    }

    public void setUserFulName(String userFulName) {
        this.userFulName = userFulName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}
