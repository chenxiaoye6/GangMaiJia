package com.example.administrator.gangmaijia.DAO;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.administrator.gangmaijia.DBHelper;
import com.example.administrator.gangmaijia.Model.User;

/**
 * Created by Administrator on 2016/10/12.
 */

public class UserDAO {

    private String UserId;
    private String UserType;
    private String UserPhone;
    private String isLogin;
    private String UserBell;

    private Context context;


    public UserDAO(Context context){
        this.context = context;

    }

    //添加新用户
    private String ADDUSER = "insert into User(UserId,UserType,UserPhone,isLogin)values(?,?,?,?)";
    public boolean addUser(User user){
        boolean flag = false;
        SQLiteDatabase db = DBHelper.getInstance(context).getWritableDatabase();
        try {
            db.execSQL(ADDUSER,new String[]{
                    user.getUserId(),
                    user.getUserType(),
                    user.getUserPhone(),
                    user.getIsLogin()
            });
            flag = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return flag;
    }

    //修改用户
    private String UPDATEUSER = "update User set UserType = ?,UserPhone = ?,isLogin = ?,UserBell = ? where UserId = ?";
    public boolean updateUser(User user){
        boolean flag = false;
        SQLiteDatabase db = DBHelper.getInstance(context).getWritableDatabase();
        try {
            db.execSQL(UPDATEUSER,new String[]{
                    user.getUserType(),
                    user.getUserPhone(),
                    user.getIsLogin(),
                    user.getUserBell(),
                    user.getUserId()
            });
            flag = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }


    //根据id查询用户信息
    private String GETUSERBYID = "select * from User where UserId = ?";
    public User getUserById(String id){
        User user = new User();
        SQLiteDatabase db = DBHelper.getInstance(context).getReadableDatabase();
        Cursor cursor = db.rawQuery(GETUSERBYID, new String[]{id});
        for (cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()) {
            user.setUserId(id);
            try {
                UserType = cursor.getString(cursor.getColumnIndex("UserType"));
                user.setUserType(UserType);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                UserPhone = cursor.getString(cursor.getColumnIndex("UserPhone"));
                user.setUserPhone(UserPhone);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                isLogin = cursor.getString(cursor.getColumnIndex("isLogin"));
                user.setIsLogin(isLogin);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (cursor.getCount() == 0) {
            return null;
        }

        return user;
    }


    //获取登录用户信息
    private String GETUSERINFO = "select * from User where isLogin = 1;";
    public User getUserInfo(){
        User user = new User();
        SQLiteDatabase db = DBHelper.getInstance(context).getReadableDatabase();
        Cursor cursor = db.rawQuery(GETUSERINFO, null);
        for (cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()) {
            try {
                UserId = cursor.getString(cursor.getColumnIndex("UserId"));
                user.setUserId(UserId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                UserType = cursor.getString(cursor.getColumnIndex("UserType"));
                user.setUserType(UserType);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                UserPhone = cursor.getString(cursor.getColumnIndex("UserPhone"));
                user.setUserPhone(UserPhone);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                isLogin = cursor.getString(cursor.getColumnIndex("isLogin"));
                user.setIsLogin(isLogin);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (cursor.getCount() == 0) {
            return null;
        }

        return user;
    }

}
