package com.fairhr.module_support;

import android.text.TextUtils;


import com.fairhr.module_support.bean.LoginEvent;
import com.fairhr.module_support.bean.LoginState;
import com.fairhr.module_support.bean.UserInfo;
import com.fairhr.module_support.constants.LiveEventKeys;
import com.fairhr.module_support.tools.inter.IUserInfoProvide;
import com.fairhr.module_support.utils.RxBus;
import com.jeremyliao.liveeventbus.LiveEventBus;

import org.json.JSONObject;

import java.util.List;


public class UserInfoManager implements IUserInfoProvide {
    private static UserInfoManager sUserInfoManager;
    private IUserInfoProvide mIUserInfoProvide;

    public IUserInfoProvide getIUserInfoProvide() {
        return mIUserInfoProvide;
    }

    public void setIUserInfoProvide(IUserInfoProvide IUserInfoProvide, boolean sendEvent) {
        mIUserInfoProvide = IUserInfoProvide;
        if (sendEvent && mIUserInfoProvide != null && !TextUtils.isEmpty(mIUserInfoProvide.userID())) {
            RxBus.getDefault().post(new LoginEvent((LoginState.LOGIN_IN)));
//            LiveEventBus.get(LiveEventKeys.ModuleLogin.LOGOUT_MOBILE).post(new LoginEvent((LoginState.LOGIN_IN)));
        }
    }

    private UserInfoManager() {
    }

    public static UserInfoManager getInstance() {
        if (sUserInfoManager == null) {
            synchronized (UserInfoManager.class) {
                if (sUserInfoManager == null) {
                    sUserInfoManager = new UserInfoManager();
                }
            }
        }
        return sUserInfoManager;
    }

    public void loginOut(boolean senEvent) {
        mIUserInfoProvide = null;
        if (senEvent) {
            RxBus.getDefault().post(new LoginEvent((LoginState.LOGIN_OUT)));
//            LiveEventBus.get(LiveEventKeys.ModuleLogin.LOGOUT_MOBILE).post(new LoginEvent((LoginState.LOGIN_OUT)));
        }
    }

    @Override
    public String userAccount() {
        return mIUserInfoProvide == null ? "" : mIUserInfoProvide.userAccount();
    }

    @Override
    public String userID() {
        return mIUserInfoProvide == null ? "" : mIUserInfoProvide.userID();
    }

    @Override
    public String userType() {
        return mIUserInfoProvide == null ? "" : mIUserInfoProvide.userType();
    }

    @Override
    public String expiration() {
        return mIUserInfoProvide == null ? "" : mIUserInfoProvide.expiration();
    }

    @Override
    public String getToken() {
        return mIUserInfoProvide == null ? "" : mIUserInfoProvide.getToken();
    }

    @Override
    public List<UserInfo.CompanyInfo> companyList() {
        return mIUserInfoProvide == null ? null : mIUserInfoProvide.companyList();
    }

    @Override
    public UserInfo.UserBean userBean() {
        return mIUserInfoProvide == null ? null : mIUserInfoProvide.userBean();
    }

    @Override
    public String companyName() {
        return mIUserInfoProvide == null ? "" : mIUserInfoProvide.companyName();
    }

    @Override
    public String email() {
        return mIUserInfoProvide == null ? "" : mIUserInfoProvide.email();
    }

    @Override
    public String isAuth() {
        return mIUserInfoProvide == null ? "" : mIUserInfoProvide.isAuth();
    }

    @Override
    public String lastLoginTime() {
        return mIUserInfoProvide == null ? "" : mIUserInfoProvide.lastLoginTime();
    }

    @Override
    public String mobile() {
        return mIUserInfoProvide == null ? "" : mIUserInfoProvide.mobile();
    }

    @Override
    public String shortCompanyName() {
        return mIUserInfoProvide == null ? "" : mIUserInfoProvide.shortCompanyName();
    }

    @Override
    public String userName() {
        return mIUserInfoProvide == null ? "" : mIUserInfoProvide.userName();
    }

    @Override
    public String privateMobile() {
        return mIUserInfoProvide == null ? "" : mIUserInfoProvide.privateMobile();
    }

    @Override
    public String nickName() {
        return mIUserInfoProvide == null ? "" : mIUserInfoProvide.nickName();
    }

    @Override
    public String privateEmail() {
        return mIUserInfoProvide == null ? "" : mIUserInfoProvide.privateEmail();
    }

    @Override
    public String privatePhotoUrl() {
        return mIUserInfoProvide == null ? "" : mIUserInfoProvide.privatePhotoUrl();
    }

    @Override
    public String privateUserId() {
        return mIUserInfoProvide == null ? "" : mIUserInfoProvide.privateUserId();
    }

    @Override
    public String sex() {
        return mIUserInfoProvide == null ? "" : mIUserInfoProvide.sex();
    }

    @Override
    public void setAvatar(String avatarUrl) {
        mIUserInfoProvide.setAvatar(avatarUrl);
    }

    @Override
    public void setNickName(String nickName) {
        mIUserInfoProvide.setNickName(nickName);
    }

    @Override
    public void setSex(String sex) {
        mIUserInfoProvide.setSex(sex);
    }

    @Override
    public void setEmail(String email) {
        mIUserInfoProvide.setEmail(email);
    }

    @Override
    public void setPrivateEmail(String privateEmail) {
        mIUserInfoProvide.setPrivateEmail(privateEmail);
    }


    /**
     * 是否登录
     *
     * @return
     */
    public boolean isLogin() {
        return mIUserInfoProvide != null;
    }
}
