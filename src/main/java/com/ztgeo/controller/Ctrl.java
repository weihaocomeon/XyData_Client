package com.ztgeo.controller;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ztgeo.service.DataService;
import com.ztgeo.util.SystemPrintln;

@Controller
public class Ctrl {
	
	SystemPrintln sy = new SystemPrintln();
	//装配属性
	@Resource(name="service")
	private DataService service;
	
	//线程保护锁 防止 多个ajax同时进入一个方法
	private Lock lock = new ReentrantLock();
	
	//目标文件转发
	//目标文件的转发
	@RequestMapping(value ="/restourl") 
	public String ResToUrl(String url) {
		return url;
	}

	//接受信息 分析json字符串
		@RequestMapping(value ="/saveMessage",method=RequestMethod.POST)//produces="application/json; charset=utf-8",
		@ResponseBody
		public String saveMessage(HttpServletRequest req ){
			lock.lock();
			System.out.println("-----接口回馈信息获得了锁");
			
			//接口的真实ip地址
			String url = req.getRequestURL()+req.getRequestURI()+req.getRemoteAddr()+req.getRemoteHost()+req.getLocalAddr()+req.getLocalName();
			System.out.println("真实ip地址:"+url);
			
			service.saveMessage(req);
			lock.unlock();
			System.out.println("-----接口回馈信息释放了锁");
			return null;
		}
	
	
	
	/*@RequestMapping(value ="/webservice")
	@ResponseBody
	public void webservice(){
		System.out.println("----进入webservice");
	}
	
	*/
	//获得数据
	@RequestMapping(value ="/getData",produces="application/json; charset=utf-8")
	@ResponseBody
	public Object getData(@RequestParam(value="page",required=false) String page//当前页
			,@RequestParam(value="rows", required=false) String rows//页面容量
			,@RequestParam(value="sort", required=false) String sort//排序字段
			,@RequestParam(value="order", required=false) String order//排序方式
			,@RequestParam(value="reqStr",required=false) String reqStr//请求状态
			,@RequestParam(value="sendStr",required=false) String sendStr//发送状态
			,@RequestParam(value="beginTime",required=false)String beginTime//开始时间
			,@RequestParam(value="endTime",required=false) String endTime//结束时间
			,@RequestParam(value="zt",required=false) boolean zt//结束时间
			,@RequestParam(value="sendUser",required=false) String sendUser//缮证人
			){
		lock.lock();
		System.out.println("-----获取信息获得了锁"+page+rows+sort+order+reqStr+sendStr+beginTime+endTime+zt+sendUser);
		//传入底层
		String result=null;
		if((!"".equals(reqStr)&&reqStr!=null)&&(!"".equals(beginTime)&&beginTime!=null)){
			 result= service.getDataByParams(page,rows,sort,order,reqStr,sendStr,beginTime,endTime,zt,sendUser);
		}else{
			System.out.println("----无用的重复请求,已过滤");
			result = "";
		}
		
		lock.unlock();
		System.out.println("-----获取信息释放了锁"+page+rows+sort+order+reqStr+sendStr+beginTime+endTime+zt);
		return result;
	}
	
	
	//更改状态值
	@RequestMapping(value ="/changeRemarks",produces="application/json; charset=utf-8")
	@ResponseBody
	public Object changeRemarks(@RequestParam(value="ID") String ID) {
		lock.lock();
		System.out.println("-----更改状态获得了锁 ID:"+ID);
		String result = service.changeRemarks(ID);
		lock.unlock(); 
		System.out.println("-----更改状态返回了锁 ID:"+ID);
		return result;
	} 
	
	
}
