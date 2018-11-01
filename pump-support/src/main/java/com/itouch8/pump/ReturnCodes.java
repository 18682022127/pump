package com.itouch8.pump;

/**
 * Copy Right Information :  <br>
 * Project :  <br>
 * Description : 平台异常枚举类<br>
 * Author : Huangzhong<br>
 * Version : 1.0.0 <br>
 * Since : 1.0.0 <br>
 * Date : 2018-10-30<br>
 */
public enum ReturnCodes {
	
	
	SUCCESS("000000","系统异常"),
	
	/**
	 * 系统异常
	 */
	SYSTEM_ERROR("999999","系统异常");
	
	public final String code;
	
	//优先从资源文件获取，获取不到直接抛出msg的内容
	public final String msg;
	
	ReturnCodes(String code, String msg) {
	    this.code = code;
	    this.msg = msg;
	}
	
	public static ReturnCodes getResultEnumByCode(String code) {
	    for (ReturnCodes resultEnum : ReturnCodes.values()) {
	        if (resultEnum.code.equals(code)) {
	            return resultEnum;
	        }
	    }
	    return null;
	}

}
