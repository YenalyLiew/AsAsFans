package com.fairhr.module_support.bean;

import java.util.List;

public class UserInfo {

    private List<CompanyInfo> companyList;
    private UserBean privateUserInfo;

    private String token;
    private String userID;
    private String userType;
    private String expiration;

    public List<CompanyInfo> getCompanyList() {
        return companyList;
    }

    public void setCompanyList(List<CompanyInfo> companyList) {
        this.companyList = companyList;
    }

    public UserBean getPrivateUserInfo() {
        return privateUserInfo;
    }

    public void setPrivateUserInfo(UserBean privateUserInfo) {
        this.privateUserInfo = privateUserInfo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public class CompanyInfo{

        private String companyName;
        private String email;
        private String isAuth;
        private String lastLoginTime;
        private String mobile;
        private String shortCompanyName;
        private String userAvatarUrl;
        private String userID;
        private String userName;

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getIsAuth() {
            return isAuth;
        }

        public void setIsAuth(String isAuth) {
            this.isAuth = isAuth;
        }

        public String getLastLoginTime() {
            return lastLoginTime;
        }

        public void setLastLoginTime(String lastLoginTime) {
            this.lastLoginTime = lastLoginTime;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getShortCompanyName() {
            return shortCompanyName;
        }

        public void setShortCompanyName(String shortCompanyName) {
            this.shortCompanyName = shortCompanyName;
        }

        public String getUserAvatarUrl() {
            return userAvatarUrl;
        }

        public void setUserAvatarUrl(String userAvatarUrl) {
            this.userAvatarUrl = userAvatarUrl;
        }

        public String getUserID() {
            return userID;
        }

        public void setUserID(String userID) {
            this.userID = userID;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        @Override
        public String toString() {
            return "CompanyInfo{" +
                    "companyName='" + companyName + '\'' +
                    ", email='" + email + '\'' +
                    ", isAuth='" + isAuth + '\'' +
                    ", lastLoginTime='" + lastLoginTime + '\'' +
                    ", mobile='" + mobile + '\'' +
                    ", shortCompanyName='" + shortCompanyName + '\'' +
                    ", userAvatarUrl='" + userAvatarUrl + '\'' +
                    ", userID='" + userID + '\'' +
                    ", userName='" + userName + '\'' +
                    '}';
        }
    }

    public class UserBean{
        private String mobile;
        private String nickName;
        private String privateEmail;
        private String privatePhotoUrl;
        private String privateUserId;
        private String sex;

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getPrivateEmail() {
            return privateEmail;
        }

        public void setPrivateEmail(String privateEmail) {
            this.privateEmail = privateEmail;
        }

        public String getPrivatePhotoUrl() {
            return privatePhotoUrl;
        }

        public void setPrivatePhotoUrl(String privatePhotoUrl) {
            this.privatePhotoUrl = privatePhotoUrl;
        }

        public String getPrivateUserId() {
            return privateUserId;
        }

        public void setPrivateUserId(String privateUserId) {
            this.privateUserId = privateUserId;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        @Override
        public String toString() {
            return "UserBean{" +
                    "mobile='" + mobile + '\'' +
                    ", nickName='" + nickName + '\'' +
                    ", privateEmail='" + privateEmail + '\'' +
                    ", privatePhotoUrl='" + privatePhotoUrl + '\'' +
                    ", privateUserId='" + privateUserId + '\'' +
                    ", sex='" + sex + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "companyList=" + companyList +
                ", privateUserInfo=" + privateUserInfo +
                ", token='" + token + '\'' +
                ", userID='" + userID + '\'' +
                ", userType='" + userType + '\'' +
                '}';
    }
}
