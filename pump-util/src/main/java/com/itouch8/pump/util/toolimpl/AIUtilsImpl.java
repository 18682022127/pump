package com.itouch8.pump.util.toolimpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64.Decoder;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import com.baidu.aip.ocr.AipOcr;
import com.itouch8.pump.util.Tool;
import com.itouch8.pump.util.ai.BankCard;
import com.itouch8.pump.util.ai.BusinessLicense;
import com.itouch8.pump.util.ai.DrivingLicense;
import com.itouch8.pump.util.ai.IDCard;
import com.itouch8.pump.util.ai.VehicleLicense;

public abstract class AIUtilsImpl {

    public static final String APP_ID = "10633937";
    public static final String API_KEY = "k1YxcF3LSQYSS1WqbabQRRNI";
    public static final String SECRET_KEY = "Ei0iLNhTLToObzTuCtDv2wqcHoOQZdWX";

    private static final AIUtilsImpl instance = new AIUtilsImpl() {};

    private AIUtilsImpl() {}

    public static AIUtilsImpl getInstance() {
        return instance;
    }

    /**
     * 身份证识别
     * 
     * @param imageBase64 图像文件的base64编码
     * @param side front:正面 back:反面
     * @return
     */
    public IDCard idcard(String imageBase64, String side) {
        return idcard(this.decodeImage(imageBase64), side);
    }

    public IDCard idcard(InputStream file, String side) {
        IDCard ic = new IDCard();
        try {
            ic = idcard(IOUtils.toByteArray(file), side);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ic;
    }

    private IDCard idcard(byte[] b, String side) {
        JSONObject res = getClient().idcard(b, side, null);
        IDCard ic = new IDCard();
        if (res.has("words_result")) {
            JSONObject jo = res.getJSONObject("words_result");
            if (null != side && "front".equals(side)) {
                ic.setName(getKey(jo, "姓名"));
                ic.setNation(getKey(jo, "民族"));
                ic.setAddress(getKey(jo, "住址"));
                ic.setIdNo(getKey(jo, "公民身份号码"));
                ic.setBirthday(getKey(jo, "出生"));
                ic.setGender(getKey(jo, "性别"));
            } else {
                ic.setInvalidDate(getKey(jo, "失效日期"));
                ic.setPubOrg(getKey(jo, "签发机关"));
                ic.setPubDate(getKey(jo, "签发日期"));
            }
        }
        return ic;
    }

    /**
     * 银行卡识别
     * 
     * @param imageBase64
     * @return
     */
    public BankCard bankcard(String imageBase64) {
        return bankcard(this.decodeImage(imageBase64));
    }

    /**
     * 银行卡识别
     * 
     * @param file
     * @return
     */
    public BankCard bankcard(InputStream file) {
        BankCard bc = new BankCard();
        try {
            bc = bankcard(IOUtils.toByteArray(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bc;
    }

    private BankCard bankcard(byte[] b) {
        JSONObject res = getClient().bankcard(b, null);
        BankCard bc = new BankCard();
        if (res.has("result")) {
            JSONObject jo = res.getJSONObject("result");
            bc.setBankCardNumber(jo.getString("bank_card_number"));
            bc.setBankName(jo.getString("bank_name"));
            bc.setBankCardType(jo.getString("bank_card_type"));
        }
        return bc;
    }

    /**
     * 驾驶证识别
     * 
     * @param file
     * @return
     */
    public DrivingLicense drivingLicense(InputStream file) {
        DrivingLicense dl = new DrivingLicense();
        try {
            dl = drivingLicense(IOUtils.toByteArray(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dl;
    }

    /**
     * 驾驶证识别
     * 
     * @param imageBase64
     * @return
     */
    public DrivingLicense drivingLicense(String imageBase64) {
        return drivingLicense(this.decodeImage(imageBase64));
    }

    private DrivingLicense drivingLicense(byte[] b) {
        JSONObject res = getClient().drivingLicense(b, null);
        DrivingLicense dl = new DrivingLicense();
        if (res.has("words_result")) {
            JSONObject jo = res.getJSONObject("words_result");
            dl.setNo(getKey(jo, "证号"));
            dl.setTimeLimit(getKey(jo, "有效期限"));
            dl.setAllowCarType(getKey(jo, "准驾车型"));
            dl.setAvailAbleBeginDate(getKey(jo, "有效起始日期"));
            dl.setAddress(getKey(jo, "住址"));
            dl.setName(getKey(jo, "姓名"));
            dl.setNationality(getKey(jo, "国籍"));
            dl.setBirthday(getKey(jo, "出生日期"));
            dl.setGender(getKey(jo, "性别"));
            dl.setFetchDate(getKey(jo, "初次领证日期"));
        }
        return dl;
    }

    /**
     * 营业执照识别
     * 
     * @param file
     * @return
     */
    public BusinessLicense businessLicense(InputStream file) {
        BusinessLicense bl = new BusinessLicense();
        try {
            bl = businessLicense(IOUtils.toByteArray(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bl;
    }

    /**
     * 营业执照识别
     * 
     * @param imageBase64
     * @return
     */
    public BusinessLicense businessLicense(String imageBase64) {
        return businessLicense(this.decodeImage(imageBase64));
    }

    private BusinessLicense businessLicense(byte[] b) {
        JSONObject res = getClient().businessLicense(b, null);
        BusinessLicense bl = new BusinessLicense();
        if (res.has("words_result")) {
            JSONObject jo = res.getJSONObject("words_result");
            bl.setCompName(getKey(jo, "单位名称"));
            bl.setLegalPerson(getKey(jo, "法人"));
            bl.setAddress(getKey(jo, "地址"));
            bl.setAvailableDate(getKey(jo, "有效期"));
            bl.setNo(getKey(jo, "证件编号"));
            bl.setUscc(getKey(jo, "社会信用代码"));
        }
        return bl;
    }

    /**
     * 行驶证识别
     * 
     * @param file
     * @return
     */
    public VehicleLicense vehicleLicense(InputStream file) {
        VehicleLicense vl = new VehicleLicense();
        try {
            vl = vehicleLicense(IOUtils.toByteArray(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return vl;
    }

    /**
     * 行驶证识别
     * 
     * @param imageBase64
     * @return
     */
    public VehicleLicense vehicleLicense(String imageBase64) {
        return vehicleLicense(this.decodeImage(imageBase64));
    }

    private VehicleLicense vehicleLicense(byte[] b) {
        JSONObject res = getClient().vehicleLicense(b, null);
        VehicleLicense vl = new VehicleLicense();
        if (res.has("words_result")) {
            JSONObject jo = res.getJSONObject("words_result");
            vl.setBrand(getKey(jo, "品牌型号"));
            vl.setPubDate(getKey(jo, "发证日期"));
            vl.setUseKind(getKey(jo, "使用性质"));
            vl.setEngineNo(getKey(jo, "发动机号码"));
            vl.setKindNo(getKey(jo, "号牌号码"));
            vl.setOwner(getKey(jo, "所有人"));
            vl.setAddresss(getKey(jo, "住址"));
            vl.setRegisterDate(getKey(jo, "注册日期"));
            vl.setCarRecognitionNo(getKey(jo, "车辆识别代号"));
            vl.setCarType(getKey(jo, "车辆类型"));
        }
        return vl;
    }

    private byte[] decodeImage(String imageBase64) {
        Decoder decoder = java.util.Base64.getDecoder();
        byte[] bytes = decoder.decode(imageBase64);
        for (int i = 0; i < bytes.length; ++i) {
            if (bytes[i] < 0) {
                bytes[i] += 256;
            }
        }
        return bytes;
    }

    private AipOcr getClient() {
        return new AipOcr(APP_ID, API_KEY, SECRET_KEY);
    }

    private String getKey(JSONObject jo, String key) {
        if (jo.has(key)) {
            return jo.getJSONObject(key).getString("words");
        }
        return null;
    }

    public static void main(String[] args) {
        try {
            IDCard idcard = Tool.AI.idcard(new FileInputStream(new File("D:\\文档\\云途\\资料\\1.jpg")), "front");
            System.out.println(idcard.getAddress());
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
