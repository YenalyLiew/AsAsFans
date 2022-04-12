package com.fairhr.module_support.bean;


/**
 * Author:kingstar
 * Time:2019-10-21
 * PackageName:com.kingstar.ksfoudation.entity
 * Description:登录成功发送
 */
public class LoginEvent {

    private LoginState loginState;

    public LoginEvent(LoginState loginState) {
        this.loginState = loginState;
    }

    public LoginState getLoginState() {
        return loginState;
    }

    public void setLoginState(LoginState loginState) {
        this.loginState = loginState;
    }
}
