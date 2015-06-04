package com.src.playtime.thumb.bean;

/**
 * Created by wanfei on 2015/6/2.
 */
public class UserModel {

    /**用户名（也是手机号）*/
    private String user;
    /**拇机号码*/
    private String mujiphone;

    public void setUser(String user) {
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public void setMujiphone(String mujiphone) {
        this.mujiphone = mujiphone;
    }

    public String getMujiphone() {
        return mujiphone;
    }
}
