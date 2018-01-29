package com.itouch8.pump.util.ai;

/**
 * Copy Right Information : 深圳云途信息资讯有限公司 <br>
 * Project : 途途物流公共平台 <br>
 * Description : 银行卡信息<br>
 * Author : kingdom<br>
 * Version : 1.0.0 <br>
 * Since : 1.0.0 <br>
 * Date : 2018-01-09<br>
 */
public class BankCard {

    /**
     * 银行卡卡号
     */
    private String bankCardNumber;

    /**
     * 银行名，不能识别时为空
     */
    private String bankName;

    /**
     * 0:不能识别; 1: 借记卡; 2: 信用卡
     */
    private String bankCardType;

    public String getBankCardNumber() {
        return bankCardNumber;
    }

    public void setBankCardNumber(String bankCardNumber) {
        this.bankCardNumber = bankCardNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCardType() {
        return bankCardType;
    }

    public void setBankCardType(String bankCardType) {
        this.bankCardType = bankCardType;
    }

}
