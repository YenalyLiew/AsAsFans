package com.fairhr.module_support.tools.inter;

import com.fairhr.module_support.bean.UserInfo;

import java.util.List;


public interface IUserInfoProvide {

    String userAccount();

    String userID();

    String userType();

    String expiration();

    String getToken();

    List<UserInfo.CompanyInfo> companyList();

    UserInfo.UserBean userBean();

    String companyName();

    String email();

    String isAuth();

    String lastLoginTime();

    String mobile();

    String shortCompanyName();

    String userName();

    String privateMobile();

    String nickName();

    String privateEmail();

    String privatePhotoUrl();

    String privateUserId();

    String sex();

    void setAvatar(String avatarUrl);

    void setNickName(String nickName);

    void setSex(String sex);

    void setEmail(String email);

    void setPrivateEmail(String privateEmail);
}
