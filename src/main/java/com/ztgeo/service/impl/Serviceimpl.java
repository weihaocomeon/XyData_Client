package com.ztgeo.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ztgeo.dao.Dao;
import com.ztgeo.entity.reqMessage;
import com.ztgeo.service.DataService;
import com.ztgeo.util.FormateData;
import com.ztgeo.util.Jdbc;
import com.ztgeo.util.Page;
import com.ztgeo.util.Tojson;

@Service("service")
public class Serviceimpl implements DataService {
	@Resource(name = "dao")
	private Dao dao;
	
	
	private static String ServiceStr;//静态字符串 用来 判断返回的msggroup是否是同一个值

	//保存信息的方法
	@Override
	public void saveMessage(HttpServletRequest req) {
		//将返回的jsonstr 封装成返回结果
		
		String reqs = getMessage(req);
		reqMessage reqMess = saveWithReqMessage(reqs);
		reqs=null;
		//将类放入到数据库中进行保存
		saveToDB(reqMess);
		reqMess=null;
	}
	
	private void saveToDB(reqMessage reqMess) {
		//接口有可能推送三次 目前未知 返回什么 接口认为 推送成功!! 所以应该保存静态值 判断 防止保存三次
		Connection conn =Jdbc.getConn();
		//对参数进行判断性保存
		String[] params = new String[7];
		params[0] =("DELIVRD".equals(reqMess.getErrorCode()))?"1":"2";//status状态码
		params[1] =FormateData.sendStatus(reqMess.getReportStatus());//remarks目前的状态信息
		params[2] =reqMess.getReceiveDate();//marssendtime短信发送时间
		params[3] =reqMess.getMobile();//phonenumber电话号码
		params[4] =reqMess.getMsgGroup();//msggroup消息组分类
		params[5] =reqMess.getMobile();//phonenumber电话号码
		params[6] =reqMess.getMsgGroup();//msggroup消息组分类
		String sql =
				" update  sms_detailinfo t set(t.status,t.remarks,t.marssendcount,t.marssendtime)=\n" +
						"(select ?,?,(t.marssendcount+1), to_date(?,'YYYY-MM-DD HH24:MI:SS') from sms_detailinfo t where t.phonenumber =? and t.msggroup=?)\n" + 
						" where t.phonenumber =? and t.msggroup=?";
		
		//执行
		/*try {
			System.out.println("程序睡眠中.....");
			Thread.sleep(13000);
			System.out.println("程序睡眠结束.....");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		//判断是否一致 不一致才执行 避免重复保存
		if((reqMess.getMsgGroup()).equals(ServiceStr)){
			System.out.println("接口访问被重复访问,不予保存!!");
		}else{
			dao.doExecuteUpdate(sql, params);
			//如果成功 保存当前值
			ServiceStr = reqMess.getMsgGroup();
		}
		
		params=null;
		Jdbc.closeResource();
		Jdbc.closeConn();
	}
	
	public reqMessage saveWithReqMessage(String result){
		reqMessage req = new reqMessage();
		JSONObject jsonObj = JSONObject.parseObject(result);
		req.setErrorCode(jsonObj.getString("errorCode"));
		req.setMobile(jsonObj.getString("mobile"));
		req.setMsgGroup(jsonObj.getString("msgGroup"));
		req.setReceiveDate(jsonObj.getString("receiveDate"));
		req.setReportStatus(jsonObj.getString("reportStatus"));
		return req;
	}
	
	public String getMessage(HttpServletRequest req){
		String result="";
		BufferedReader in =null ;
		System.out.println("---------短信接口被访问!!");
		try {
			in = new BufferedReader(
					new InputStreamReader( req.getInputStream()));	
			String line;
			while ((line = in.readLine()) != null) {
				result += "\n" + line;
				System.out.println("接口返回结果:"+result);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}finally {
			try {
				if (in!= null) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	@Override
	public String getDataByParams(String page, String rows, String sort, String order, String reqStr, String sendStr,
			String beginTime, String endTime,boolean zt,String sendUser) {
		//获得初始值 和 结束值
		int star =Page.getStar(Page.parseInt(page), Page.parseInt(rows));
		int end = Page.getEnd(Page.parseInt(page), Page.parseInt(rows));
		//组装sql语句
		StringBuffer sb = new StringBuffer();
		StringBuffer sbCount = new StringBuffer();
		
		
		sb.append("select * from (select a1.*, rownum rn  from ( ");
		sb.append("select  t.id,t.phonenumber,t.content,t.status,t.sendcount,t.sendtime,t.effectivetime,t.senduser,t.marssendcount,t.marssendtime,t.reqinfo,t.remarks from  sms_detailinfo t where 1=1 ");
		
		sbCount.append("select count(1) from (");
		sbCount.append("select  t.id,t.phonenumber,t.content,t.status,t.sendcount,t.sendtime,t.effectivetime,t.senduser,t.marssendcount,t.marssendtime,t.reqinfo,t.remarks from  sms_detailinfo t where 1=1 ");
		
		//组装查询日期
		sb.append(" and (t.sendtime between to_date('"+beginTime+" 00:00:00','YYYY-MM-DD HH24:MI:SS') and to_date('"+endTime+" 23:59:59','YYYY-MM-DD HH24:MI:SS')) ");
		sbCount.append(" and (t.sendtime between to_date('"+beginTime+" 00:00:00','YYYY-MM-DD HH24:MI:SS') and to_date('"+endTime+" 23:59:59','YYYY-MM-DD HH24:MI:SS')) ");
		if(zt==true){ //按条件找
				//组装状态
				if("请求成功".equals(reqStr)){
					if("发送成功".equals(sendStr)){
						//请求成功 发送成功
						sb.append(" and t.status=1 ");
						sbCount.append(" and t.status=1 ");
					}else if("发送失败".equals(sendStr)){
						//请求成功 发送失败
						sb.append(" and t.status=2 ");
						sbCount.append(" and t.status=2 ");
					}else{//发送结果未知
						sb.append(" and t.status=0 ");
						sbCount.append(" and t.status=0 ");
					}		
				}else if("请求失败".equals(reqStr)){
					//请求失败 
					sb.append(" and substr(t.reqinfo,0,4)='请求失败'");
					sbCount.append(" and t.status=0 and remarks is not null");
				}else{
					//未请求
					sb.append(" and t.status=0 and remarks is null");
					sbCount.append(" and t.status=0 and remarks is null");
				}
				
		}
		//判断缮证人
		if(sendUser!=null&&(!"".equals(sendUser))){
			sb.append(" and t.senduser like '%"+sendUser+"%' ");
			sbCount.append(" and t.senduser like '%"+sendUser+"%' ");
			
		}
		//最后查看是否有排序--排序一定放最后
		if(order!=null&&!"".equals(order)){
			sb.append(" order by "+sort+" "+order+" ");
			sbCount.append(" order by "+sort+" "+order+" ");
		}
		sb.append(" ) a1 where rownum <= "+end +" ) where rn >= "+ star + " ");
		sbCount.append(" )");
		System.out.println("最终的查询语句:"+sb.toString());
		System.out.println("总条数:"+sbCount.toString());
		
		//链接数据库
		Jdbc.getConn();
		//查询条数
		int count = Jdbc.getCount(sbCount.toString());
		//查询
		ResultSet set = Jdbc.getData(sb.toString());
		net.sf.json.JSONArray jsonarray = Tojson.resultToJsonArray(set);
		String result =Tojson.getJsonData(jsonarray,count);
		//释放资源
		jsonarray = null;
		sb=null;
		sbCount=null;
		Jdbc.closeResource();
		Jdbc.closeConn();
		//处理set集合封装成json数据
		return result;
	}

	//更改状态值
	@Override
	public String changeRemarks(String iD) {
		String sql = 
				"update sms_detailinfo t set (t.remarks,t.sendcount,t.reqinfo,t.msggroup,t.status,t.marssendtime)=\n" +
						"\t\t\t\t\t\t  (select null,t.sendcount+1,null,null,0,null from  sms_detailinfo t  where t.id='"+iD+"') where t.id='"+iD+"'";
		//链接数据库
		Jdbc.getConn();
		int resultCount = Jdbc.doExecuteUpdate(sql, new String[0]);
		//关闭资源
		Jdbc.closeResource();
		Jdbc.closeConn();
		sql=null;
		return Tojson.msgTojson(resultCount);
	}
	
	
}
