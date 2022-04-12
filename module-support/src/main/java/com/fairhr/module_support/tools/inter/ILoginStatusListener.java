package com.fairhr.module_support.tools.inter;


import com.fairhr.module_support.bean.LoginState;

public interface ILoginStatusListener {

    void onStatus(LoginState loginState);

}
