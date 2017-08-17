package com.ztgeo.util;


public class FormateData {
	
	
	//获得错误码
	public static String sendStatus(String str){
		if("CM:0000".equals(str)){
			return "发送成功";
		}else if ("ID:0012".equals(str)){
			return "异地号码";
		}else if ("ID:0120".equals(str)){
			return "用户停机";
		}else if ("ID:0020".equals(str)){
			return "用户停机";
		}else{
			return "其他错误"+str;
		}
	}
}
