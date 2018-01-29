package com.itouch8.pump.util.ai;

/**
 * Copy Right Information : 深圳云途信息资讯有限公司 <br>
 * Project : 途途物流公共平台 <br>
 * Description : 营业执照<br>
 * Author : kingdom<br>
 * Version : 1.0.0 <br>
 * Since : 1.0.0 <br>
 * Date : 2018-01-09<br>
 */
public class BusinessLicense {

    /**
     * 企业名称
     */
    private String compName;

    /**
     * 法人
     */
    private String legalPerson;

    /**
     * 地址
     */
    private String address;

    /**
     * 有效期
     */
    private String availableDate;

    /**
     * 证件编号
     */
    private String no;

    /**
     * 统一信用代码
     */
    private String uscc;

    public String getCompName() {
        return compName;
    }

    public void setCompName(String compName) {
        this.compName = compName;
    }

    public String getLegalPerson() {
        return legalPerson;
    }

    public void setLegalPerson(String legalPerson) {
        this.legalPerson = legalPerson;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAvailableDate() {
        return availableDate;
    }

    public void setAvailableDate(String availableDate) {
        this.availableDate = availableDate;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getUscc() {
        return uscc;
    }

    public void setUscc(String uscc) {
        this.uscc = uscc;
    }

}
