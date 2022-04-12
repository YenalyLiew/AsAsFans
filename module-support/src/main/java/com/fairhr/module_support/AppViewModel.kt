package com.fairhr.module_support

import com.fairhr.module_support.viewmodel.BaseViewModel


/**
 */

class AppViewModel : BaseViewModel() {



    /**
     * 当前是否已经登录手机号
     */
    @kotlin.jvm.JvmField
    val isMobileLogin = BooleanLiveData(UserInfoManager.getInstance().isLogin)

}