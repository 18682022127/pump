package com.itouch8.pump.util.ai;

/**
 * Copy Right Information : 深圳云途信息资讯有限公司 <br>
 * Project : 途途物流公共平台 <br>
 * Description : 行驶证<br>
 * Author : kingdom<br>
 * Version : 1.0.0 <br>
 * Since : 1.0.0 <br>
 * Date : 2018-01-09<br>
 */
public class VehicleLicense {

    /**
     * 品牌型号
     */
    private String brand;

    /**
     * 发证日期
     */
    private String pubDate;

    /**
     * 使用性质
     */
    private String useKind;

    /**
     * 发动机号码
     */
    private String engineNo;

    /**
     * 品牌号码
     */
    private String kindNo;

    /**
     * 持有人
     */
    private String owner;

    /**
     * 住址
     */
    private String addresss;

    /**
     * 注册日期
     */
    private String registerDate;

    /**
     * 车辆识别代号
     */
    private String carRecognitionNo;

    /**
     * 车辆类型
     */
    private String carType;

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getUseKind() {
        return useKind;
    }

    public void setUseKind(String useKind) {
        this.useKind = useKind;
    }

    public String getEngineNo() {
        return engineNo;
    }

    public void setEngineNo(String engineNo) {
        this.engineNo = engineNo;
    }

    public String getKindNo() {
        return kindNo;
    }

    public void setKindNo(String kindNo) {
        this.kindNo = kindNo;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getAddresss() {
        return addresss;
    }

    public void setAddresss(String addresss) {
        this.addresss = addresss;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

    public String getCarRecognitionNo() {
        return carRecognitionNo;
    }

    public void setCarRecognitionNo(String carRecognitionNo) {
        this.carRecognitionNo = carRecognitionNo;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

}
