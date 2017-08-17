package com.ztgeo.service;

import javax.servlet.http.HttpServletRequest;

public interface DataService {
	
	//保存信息
	public void saveMessage(HttpServletRequest req);
	
	//获取分页信息
	public String getDataByParams(String page, String rows, String sort, String order, String reqStr, String sendStr,
			String beginTime, String endTime, boolean zt, String sendUser);

	public String changeRemarks(String iD);
	
}
