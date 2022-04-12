package com.fairhr.module_support.bean;


import com.fairhr.module_support.tools.inter.IUserInfoProvide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Description:
 */
public class UserInfoProvideIml implements IUserInfoProvide {

    private UserInfo mUserInfo;
    private String mUserAccount;

    public UserInfoProvideIml(UserInfo userInfo, String userAccount) {
        mUserInfo = userInfo;
        mUserAccount = userAccount;
    }


    @Override
    public String userAccount() {
        return mUserAccount;
    }

    @Override
    public String userID() {
        return mUserInfo.getUserID();
    }

    @Override
    public String userType() {
        return mUserInfo.getUserType();
    }

    @Override
    public String expiration() {
        return mUserInfo.getExpiration();
    }

    @Override
    public String getToken() {
        return mUserInfo.getToken();
    }

    @Override
    public List<UserInfo.CompanyInfo> companyList() {
        return mUserInfo.getCompanyList();
    }

    @Override
    public UserInfo.UserBean userBean() {
        return mUserInfo.getPrivateUserInfo();
    }

    @Override
    public String companyName() {
        return companyList().size() == 0 ? "" : companyList().get(0).getCompanyName();
    }

    @Override
    public String email() {
        return companyList().size() == 0 ? "" : companyList().get(0).getEmail();
    }

    @Override
    public void setEmail(String email) {

    }

    @Override
    public String isAuth() {
        return companyList().size() == 0 ? "" : companyList().get(0).getIsAuth();
    }

    @Override
    public String lastLoginTime() {
        return companyList().size() == 0 ? "" : companyList().get(0).getLastLoginTime();
    }

    @Override
    public String mobile() {
        return companyList().size() == 0 ? "" : companyList().get(0).getMobile();
    }

    @Override
    public String shortCompanyName() {
        return companyList().size() == 0 ? "" : companyList().get(0).getShortCompanyName();
    }

    @Override
    public String userName() {
        return companyList().size() == 0 ? "" : companyList().get(0).getUserName();

    }

    @Override
    public String privateMobile() {
        return userBean() == null ? "" : userBean().getMobile();
    }

    @Override
    public String nickName() {
        return userBean() == null ? "" : userBean().getNickName();
    }

    @Override
    public String privateEmail() {
        return userBean() == null ? "" : userBean().getPrivateEmail();
    }

    @Override
    public void setPrivateEmail(String email) {
        userBean().setPrivateEmail(email);
    }

    @Override
    public String privatePhotoUrl() {
        return userBean() == null ? "" : userBean().getPrivatePhotoUrl();
    }

    @Override
    public String privateUserId() {
        return userBean() == null ? "" : userBean().getPrivateUserId();
    }

    @Override
    public String sex() {
        return userBean() == null ? "" : userBean().getSex();
    }

    @Override
    public void setAvatar(String avatarUrl) {
        userBean().setPrivatePhotoUrl(avatarUrl);
    }

    @Override
    public void setNickName(String nickName) {
        userBean().setNickName(nickName);
    }

    @Override
    public void setSex(String sex) {
        userBean().setSex(sex);
    }


}
