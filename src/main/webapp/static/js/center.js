
//下拉框1请求状态
$('#reqStr').combobox({  
	editable:false,
	onChange:validateValue 	
});  
//下拉框2发送状态
$('#SendStr').combobox({  
	editable:false,
	onChange:validateValue 	
}); 
//时间控件
$('#beginTime').datebox({    
    required:true,  
    editable:false,
    onSelect:valDate
}); 

$('#endTime').datebox({    
    required:true,
    editable:false,
    onSelect:valDate
});  

//文本框
$('#tb').textbox({   
	prompt:'姓名全拼或首字母(小写)',
    iconCls:'icon-man', 
    iconAlign:'left'       
})
//初始值
$('#sp1').css('display','none');  
date= (new Date()).toLocaleString();
$('#beginTime').datebox('setValue',date);
$('#endTime').datebox('setValue',date);
//状态按钮
$("#zt").switchbutton({
	onText:'是',
	offText:'否',
	onChange:function(checked){
		//如果为false 则隐藏条件检索
		if(checked==false){
			$('#sp1').css('display','none');  
		}else{
			$('#sp1').css('display','inline'); 
		}
		//否则显示
	}
});

//搜索按钮
$("#searchBtn").linkbutton({    
    iconCls:"icon-search", 
    text:'搜索',
    iconAlign:'right',
});

//重置按钮
$('#btnForReSet').linkbutton({  

    iconCls:"icon-edit", 
    iconAlign:'right',
    onClick:reSet
});


//提交
$('#fm').form({
    onSubmit: function(){  
    	/*var isValid = $(this).form('validate');
    	if(!isValid){
			
		}
    	return false;*/
    },    
    success:function(data){    
    	//重组参数 刷新表格数据
    	//参数
    	beginTime= ($('#beginTime').combo('getValue'));	
        endTime= ($('#endTime').combo('getValue'));	
        reqStr = $('#reqStr').combobox('getValue');
        sendStr = $('#SendStr').combobox('getValue');
        sendUser = $('#tb').textbox('getText');
        //是否剔除条件检索
        zt = $('#zt').switchbutton('options').checked;
        $('#did').datagrid('load',{
        	beginTime:beginTime,
			endTime:endTime,
			reqStr:reqStr,
			sendStr:sendStr,
			zt:zt,
			sendUser:sendUser,
		});
    },
   
});   

//验证是否选择了请求成功
function validateValue(){
	//查看目前选择值	
	var reqStr = $('#reqStr').combobox('getValue');
	if(reqStr=="请求失败"||(reqStr=="未请求")){
		//值2设置值
		 $('#SendStr').combobox('select','发送结果未知');
		 //$('#SendStr').combo('setValue','发送失败');
		 $('#SendStr').combobox('disable',true);
	}else{
		 $('#SendStr').combobox('enable',true);
	}
	}

//验证输入时间
function valDate(){
	var beginTime= ($('#beginTime').combo('getValue')).replace('-',"").replace('-',"");	
	var endTime= ($('#endTime').combo('getValue')).replace('-',"").replace('-',"");	
	if((endTime<beginTime)&&(endTime!="")&&(beginTime!="")){
		showMsg('友情提示','开始时间必须小于结束时间','warning');
		//限制不可提交
		$('#submitBtn').linkbutton('disable');
		$('#submitBtn').attr({"disabled":"disabled"});	
	}else{
		//恢复状态
		$('#submitBtn').linkbutton('enable');
		$('#submitBtn').removeAttr("disabled");
	}
}
//条件重置
function reSet(){
	$('#zt').switchbutton('reset');
	//$('#fm').form('reset');
	 $('#SendStr').combobox('select','发送成功');
	 $('#reqStr').combobox('select','请求成功');
	 //时间框
	 date= getNowDate();
	 $('#beginTime').datebox('setValue',date);
	 $('#endTime').datebox('setValue',date);
	 $('#SendStr').combo('enable',true);
	 //输入框
	 $('#tb').textbox('reset');
}


//控件显示
$("#did").datagrid({
	url:'/XyData_Client/getData',	
	pagePosition:'bottom',
	height: '100%',
	fit:true ,
	rownumbers:true,
	title:"查询结果",
	loadMsg:'数据加载中......',
	iconCls:'icon-list',
	fitColumns:false,
	striped:true,
	autoRowHeight:false,
	pageSize:100,
	pageList:[20,50,100,150],
	nowrap: true,	
	remoteSort:true,//服务器排序
	multiSort:false,
   // selectOnCheck:false	,
  
	checkOnSelect:true,
	singleSelect:true,
	pagination:true,
	onResizeColumn:function(){
		//列宽为非自动
		//$('#did').datagrid('options').nowrap=false;
		$("#did").datagrid({
			nowrap:false
		});
		//console.log($('#did').datagrid('options'));
		//reloadDatagrid('#did');
		
	},
	onLoadSuccess:function(){
		$('.btnSend').linkbutton({    
		    iconCls: 'icon-send'  ,
		    size:'small',
		   // plain:true,
		    
		});  

	},
	
	columns: [columZ],
	

})



//刷新表格
function reloadDatagrid(id){
	$(id).datagrid('reload');
}

//获得选择行
function getAllselect(id){
	var rows = '';
	rows = $(id).datagrid('getSelections');
	return rows;
}

//显示alert表格
function showMsg(msgTitle,msg,icon){
	$.messager.alert(msgTitle,msg,icon);
}

//获取当前日期
function getNowDate(){
	return (new Date()).toLocaleString();
}

/*})*///我是jquery的下括号补全


