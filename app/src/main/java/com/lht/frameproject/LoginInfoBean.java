package com.lht.frameproject;

import com.google.gson.annotations.SerializedName;

public class LoginInfoBean {

    @SerializedName("errorCode")
    private int errorCode;

    @SerializedName("token")
    private String token;
    @SerializedName("username")
    private String userName;
    @SerializedName("sex")
    private int sex;
    @SerializedName("img")
    private String img;
    @SerializedName("status")
    private String status;
    @SerializedName("family_role_id")
    private int familyRoleId;
    @SerializedName("new")
    private int newUser;
    @SerializedName("family_id")
    private int familyId;
    @SerializedName("boxnum")
    private int boxNum;
    @SerializedName("face_img")
    private String faceImg;
    @SerializedName("rouse")
    private String rouse;
    @SerializedName("rouse_homonym")
    private String rouseHomonym;
    @SerializedName("familyname")
    private String familyName;
    @SerializedName("isAdmin")
    private int isAdmin;
    @SerializedName("boxCount")
    private int boxCount;
    @SerializedName("baby")
    private BabyInfoBean baby;
    @SerializedName("unionId")
    private String unionId;
    @SerializedName("nickName")
    private String nickName;
    @SerializedName("login")
    private String login;

    @SerializedName("rongcloud_token")
    private String rongToken;

    @SerializedName("service_time")
    private long serviceTime;

    @SerializedName("id")
    private int userId;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public long getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(long serviceTime) {
        this.serviceTime = serviceTime;
    }

    public String getRongToken() {
        return rongToken;
    }

    public void setRongToken(String rongToken) {
        this.rongToken = rongToken;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getImg() {
        return img;
    }

    public int getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(int isAdmin) {
        this.isAdmin = isAdmin;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getFamilyRoleId() {
        return familyRoleId;
    }

    public void setFamilyRoleId(int familyRoleId) {
        this.familyRoleId = familyRoleId;
    }

    public int getNewUser() {
        return newUser;
    }

    public void setNewUser(int newUser) {
        this.newUser = newUser;
    }

    public int getFamilyId() {
        return familyId;
    }

    public void setFamilyId(int familyId) {
        this.familyId = familyId;
    }

    public int getBoxNum() {
        return boxNum;
    }

    public void setBoxNum(int boxNum) {
        this.boxNum = boxNum;
    }

    public String getFaceImg() {
        return faceImg;
    }

    public void setFaceImg(String faceImg) {
        this.faceImg = faceImg;
    }

    public String getRouse() {
        return rouse;
    }

    public void setRouse(String rouse) {
        this.rouse = rouse;
    }

    public String getRouseHomonym() {
        return rouseHomonym;
    }

    public void setRouseHomonym(String rouseHomonym) {
        this.rouseHomonym = rouseHomonym;
    }

    public BabyInfoBean getBaby() {
        return baby;
    }

    public void setBaby(BabyInfoBean baby) {
        this.baby = baby;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public int getBoxCount() {
        return boxCount;
    }

    public void setBoxCount(int boxCount) {
        this.boxCount = boxCount;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public String toString() {
        return "LoginInfoBean{" +
                "errorCode=" + errorCode +
                ", token='" + token + '\'' +
                ", userName='" + userName + '\'' +
                ", sex=" + sex +
                ", img='" + img + '\'' +
                ", status='" + status + '\'' +
                ", familyRoleId=" + familyRoleId +
                ", newUser=" + newUser +
                ", familyId=" + familyId +
                ", boxNum=" + boxNum +
                ", faceImg='" + faceImg + '\'' +
                ", rouse='" + rouse + '\'' +
                ", rouseHomonym='" + rouseHomonym + '\'' +
                ", familyName='" + familyName + '\'' +
                ", isAdmin=" + isAdmin +
                ", boxCount=" + boxCount +
                ", baby=" + baby +
                ", unionId='" + unionId + '\'' +
                ", nickName='" + nickName + '\'' +
                ", login='" + login + '\'' +
                ", rongToken='" + rongToken + '\'' +
                ", serviceTime=" + serviceTime +
                ", userId=" + userId +
                '}';
    }

    public class BabyInfoBean {

        @SerializedName("baby_uid")
        private int babyUid;
        @SerializedName("baby_img")
        private String babyImg;
        @SerializedName("baby_name")
        private String babyName;
        @SerializedName("baby_birthday")
        private String babyBirthday;
        @SerializedName("age")
        private String age;
        @SerializedName("baby_sex")
        private int babySex;

        public int getBabyUid() {
            return babyUid;
        }

        public void setBabyUid(int babyUid) {
            this.babyUid = babyUid;
        }

        public String getBabyImg() {
            return babyImg;
        }

        public void setBabyImg(String babyImg) {
            this.babyImg = babyImg;
        }

        public String getBabyName() {
            return babyName;
        }

        public void setBabyName(String babyName) {
            this.babyName = babyName;
        }

        public String getBabyBirthday() {
            return babyBirthday;
        }

        public void setBabyBirthday(String babyBirthday) {
            this.babyBirthday = babyBirthday;
        }

        public int getBabySex() {
            return babySex;
        }

        public void setBabySex(int babySex) {
            this.babySex = babySex;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return "BabyInfoBean{" +
                    "babyUid=" + babyUid +
                    ", babyImg='" + babyImg + '\'' +
                    ", babyName='" + babyName + '\'' +
                    ", babyBirthday='" + babyBirthday + '\'' +
                    ", age='" + age + '\'' +
                    ", babySex=" + babySex +
                    '}';
        }

    }

}
