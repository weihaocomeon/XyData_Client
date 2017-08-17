<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>

</head>

<body>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/top.css">

<%-- <%@include file="../../import.jsp" %> --%>


	<div class="fmDiv">
		<form id="fm" method="post" >
		<div >
		<span>
			<label>是否按状态查询:</label>
			 <input id='zt'>
		</span>
		<span id ='sp1'>
			<label>请求状态</label>
			<select id="reqStr" class="easyui-combobox" name="dept" style="width:200px;">   
			    <option value='请求成功'>请求成功</option>   
			    <option value='请求失败'>请求失败</option>
			    <option value='未请求'>未请求</option>     
			</select>  
	
			<label>发送状态</label>
			<select id="SendStr" class="easyui-combobox" name="dept" style="width:200px;">   
			    <option value='发送成功'>发送成功</option>   
			    <option value='发送失败'>发送失败</option>  
			    <option value='发送结果未知'>发送结果未知</option>  
			</select>  
		</span>
			
			 <label>发起始时间:</label>
			<input  id="beginTime"  type="text" class= "easyui-datebox" >   
		
			<label>结束时间:</label>
			<input  id="endTime"  type= "text" class= "easyui-datebox" >  
		 
			<label>缮证人:</label>
			<input  id="tb" type= "text" class= "easyui-text" style="width:150px">  
			 
		<input type="submit" value="开始查询" id='submitBtn'> 
		<a id="btnForReSet" >重置</a>  
		</div>
	</form>	
	</div>	
	
	
	
	
</body>
</html>	