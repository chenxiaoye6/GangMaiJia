package com.example.administrator.gangmaijia.Model;

/**
 * Created by Administrator on 2016/10/12.
 */

public class User {


    private String UserId;
    private String UserType;
    private String UserPhone;
    private String isLogin;
    private String UserBell;
    private String balance;

    @Override
    public String toString() {
        return "User{" +
                "UserId='" + UserId + '\'' +
                ", UserType='" + UserType + '\'' +
                ", UserPhone='" + UserPhone + '\'' +
                ", isLogin='" + isLogin + '\'' +
                ", UserBell='" + UserBell + '\'' +
                ", balance='" + balance + '\'' +
                '}';
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getUserType() {
        return UserType;
    }

    public void setUserType(String userType) {
        UserType = userType;
    }

    public String getUserPhone() {
        return UserPhone;
    }

    public void setUserPhone(String userPhone) {
        UserPhone = userPhone;
    }

    public String getIsLogin() {
        return isLogin;
    }

    public void setIsLogin(String isLogin) {
        this.isLogin = isLogin;
    }

    public String getUserBell() {
        return UserBell;
    }

    public void setUserBell(String userBell) {
        UserBell = userBell;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}
