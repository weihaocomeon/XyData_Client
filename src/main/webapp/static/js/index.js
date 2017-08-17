
//工具条列
var columZ = [
	
      {
          field: 'PHONENUMBER',
          title: '电话号码',
          width: '10%',
          align:'center',
         
         
       },
       {
           field: 'CONTENT',
           title: '短信内容',
           width: '15%',
         
        },
        {
            field: 'SENDUSER',
            title: '缮证人',
            width: '10%',
            sortable :true,
          },
        {
            field: 'REQINFO',
            title: '请求状态',
            width: '8%',
            //格式化 状态信息
            formatter: function(value,row,index){
            	//alert(value+row+index);
				if (value=="请求成功success"){
					return "请求成功";
				} else if(value==null||(value=="")||value==undefined) {
					return "等待发送请求";
				}else{//请求失败
					return value;
				}
			},	
			styler: function(value,row,index){
				//	console.log(value);
				if (value=="请求成功success"){
					return 'background-color:#66FF66;color:black;';
				}else if(value==null||(value=="")||value==undefined) {
					return  'background-color:#FFFF8F;color:red;';
				}else{//请求失败
					return   'background-color:#FF6666;color:black;';
				}
          },     
        },
       {
         field: 'SENDCOUNT',
         title: '请求次数',
         width: '8%',
         sortable :true,
       },
 
     {
         field: 'SENDTIME',
         title: '请求发送时间',
         width: '10%',
         sortable :true,
     },
  
       {
           field: 'MARSSENDCOUNT',
           title: '发送次数',
           width: '8%',
       },  
       {
           field: 'REMARKS',
           title: '发送状态',
           width: '10%',
           formatter: function(value,row,index){
           	//console.log(row);
				if (row.STATUS==0){
					return "状态未反馈";
				} else if (row.STATUS==2){
					return "发送失败"+value;
				} 
				else{
					return value;
				}
			},
			styler: function(value,row,index){
				if (row.STATUS==0){
					return 'background-color:#FFFF8F;color:red;';
				}else if (row.STATUS==2){
					return 'background-color:#FF6666;color:black;';
				} 
				else{
					return 'background-color:#66FF66;color:black;';//66FF66
				}
			}

       }, 
       {
           field: 'MARSSENDTIME',
           title: '发送时间',
           width: '10%',
           sortable :true,
           align:'center',
       }, 
       {
           field: 'aaasssss',
           title: '操作',
           width: '10%',
           align:'center',
           formatter: function(value,row,index){
        	   //发送失败  请求成功 但无反馈
        	// console.log(row.REQINFO.substr(0,4));
        	 //发送失败可以重新发   //请求失败可以重新发  没有接到回执不给发
        	 if((row.STATUS==2)||(row.REMARKS=="请求已发送"&&row.REQINFO.substr(0,4)=="请求失败")){ 
        		
              return '<a class="btnSend" onclick="reSend(\''+row.ID+'\')">再次请求</a>'; 
        	 }	
   			}
       },  
       
       
    
];



function reSend(ID){

		//console.log(ID);
		//执行ajax到后台
		//ajax进行删除
		//alert(ID);
		$.ajax({
			url:'/XyData_Client/changeRemarks',
			beforeSend:showLoad(),
			type:'post',
			async: false,//同步请求  请求结束 才能进行下步工作 防止后台混乱(后台后期加入锁)
			dataType:'json',
			data:{
				ID:ID
			},
			success:function(data){
				hideLoad();
				if(data.msg==1){
					//刷新表格
					$('#did').datagrid('reload');
				//	$('#busDialog').dialog('close');
					//提醒
					showMsg("信息提醒","已重新发送请求,请稍后刷新查看请求及发送结果!!",'info');
				}else{
					showMsg('错误','请求发送失败!','warning')
				}
				
			},
			error:function(data){
				alert('失败');
			}
		})
	
};

//显示加载信息
function showLoad(){
	$.messager.progress({text:'数据加载中....'}); 
};

//隐藏加载信息
function hideLoad(){
	$.messager.progress('close'); 
}
