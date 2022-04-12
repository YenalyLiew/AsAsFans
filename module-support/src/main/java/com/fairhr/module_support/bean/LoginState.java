package com.fairhr.module_support.bean;

public enum LoginState {
    // 登入
    LOGIN_IN(1),
    // 登出
    LOGIN_OUT(2),
    //取消登录
    LOGIN_CANCLE(3);
    private final int state;

    LoginState(int status) {
        this.state = status;
    }

    public static LoginState create(int value) {
        LoginState result;
        switch (value) {
            case 1:
                result = LOGIN_IN;
                break;
            case 2:
                result = LOGIN_OUT;
                break;
            case 3:
                result = LOGIN_CANCLE;
                break;
            default:
                result = LOGIN_OUT;
                break;
        }
        return result;
    }

    public int value() {
        return state;
    }
}
