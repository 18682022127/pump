package com.itouch8.pump.util.ai;

/**
 * Copy Right Information : 深圳云途信息资讯有限公司 <br>
 * Project : 途途物流公共平台 <br>
 * Description : 驾驶证信息<br>
 * Author : kingdom<br>
 * Version : 1.0.0 <br>
 * Since : 1.0.0 <br>
 * Date : 2018-01-09<br>
 */
public class DrivingLicense {

    /**
     * 证号
     */
    private String no;

    /**
     * 有效期限
     */
    private String timeLimit;

    /**
     * 准驾车型
     */
    private String allowCarType;

    /**
     * 有效起始日期
     */
    private String availAbleBeginDate;

    /**
     * 住址
     */
    private String address;

    /**
     * 姓名
     */
    private String name;

    /**
     * 国籍
     */
    private String nationality;

    /**
     * 出生日期
     */
    private String birthday;

    /**
     * 性别
     */
    private String gender;

    /**
     * 初次领证日期
     */
    private String fetchDate;

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(String timeLimit) {
        this.timeLimit = timeLimit;
    }

    public String getAllowCarType() {
        return allowCarType;
    }

    public void setAllowCarType(String allowCarType) {
        this.allowCarType = allowCarType;
    }

    public String getAvailAbleBeginDate() {
        return availAbleBeginDate;
    }

    public void setAvailAbleBeginDate(String availAbleBeginDate) {
        this.availAbleBeginDate = availAbleBeginDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFetchDate() {
        return fetchDate;
    }

    public void setFetchDate(String fetchDate) {
        this.fetchDate = fetchDate;
    }

}
