package com.itouch8.pump.util.ai;

/**
 * Copy Right Information : 深圳云途信息资讯有限公司 <br>
 * Project : 途途物流公共平台 <br>
 * Description : 身份证信息<br>
 * Author : kingdom<br>
 * Version : 1.0.0 <br>
 * Since : 1.0.0 <br>
 * Date : 2018-01-09<br>
 */
public class IDCard {

    private String name;

    private String nation;

    private String address;

    private String idNo;

    private String birthday;

    private String gender;

    private String invalidDate;

    private String pubDate;

    private String pubOrg;

    public String getInvalidDate() {
        return invalidDate;
    }

    public void setInvalidDate(String invalidDate) {
        this.invalidDate = invalidDate;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getPubOrg() {
        return pubOrg;
    }

    public void setPubOrg(String pubOrg) {
        this.pubOrg = pubOrg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
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

}
