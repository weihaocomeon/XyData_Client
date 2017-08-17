package com.ztgeo.dao.impl;


import java.sql.ResultSet;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.ztgeo.dao.Dao;
import com.ztgeo.util.Jdbc;

@Repository("dao")
public class DaoImpl implements Dao {
	private ResultSet rs;
	
	
	
	@Override
	public ResultSet getData(String sql) {
	rs =Jdbc.getData(sql);
	return rs;
	}
	
	@Override
	public ResultSet getDataByParams(String baseSql,String[] params) {
		rs = Jdbc.getDataByParams(baseSql, params);
		return rs;
	}
	
	//测试用


	@Override
	public int getCount(String sql) {
		
		return Jdbc.getCount(sql);
	}

	@Override
	public int doExecuteUpdate(String sql, String[] strings) {
		
		return Jdbc.doExecuteUpdate(sql, strings);
	}

	@Override
	public int doExecuteUpdateNotAuto(String sql, String[] params) {
		return Jdbc.doExecuteUpdateNotAuto(sql, params);
		
	}

	





}
