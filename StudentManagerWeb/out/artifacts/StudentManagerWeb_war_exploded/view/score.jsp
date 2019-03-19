<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta charset="UTF-8">
	<title>学生列表</title>
	<link rel="stylesheet" type="text/css" href="easyui/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="easyui/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="easyui/css/demo.css">
	<script type="text/javascript" src="easyui/jquery.min.js"></script>
	<script type="text/javascript" src="easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="easyui/js/validateExtends.js"></script>
	<script type="text/javascript">
	$(function() {	
		//datagrid初始化 
	    $('#dataList').datagrid({ 
	        title:'学生成绩列表', 
	        iconCls:'icon-more',//图标 
	        border: true, 
	        collapsible:false,//是否可折叠的 
	        fit: true,//自动大小 
	        method: "post",
	        url:"ScoreServlet?method=ScoreList&t="+new Date().getTime(),
	        idField:'id', 
	        singleSelect:false,//是否单选 
	        pagination:true,//分页控件 
	        rownumbers:true,//行号 
	        sortName:'id',
	        sortOrder:'asc', 
	        remoteSort: false,
	        columns: [[  
				{field:'chk',checkbox: true,width:50},
 		        {field:'id',title:'ID',width:50, sortable: true},    
 		        {field:'studentId',title:'学号',width:200, sortable: true},    
 		        {field:'studentName',title:'姓名',width:200},
 		        {field:'courseId',title:'课程',width:150},
 		       	{field:'clazz_id',title:'班级',width:150, 
 		        	formatter: function(value,row,index){
 						if (row.clazzId){
 							var clazzList = $("#clazzList").combobox("getData");
 							for(var i=0;i<clazzList.length;i++ ){
 								//console.log(clazzList[i]);
 								if(row.clazzId == clazzList[i].id)return clazzList[i].name;
 							}
 							return row.clazzId;
 						} else {
 							return 'not found';
 						}
 					}
				 },
				{field:'score',title:'成绩',width:150},
 		        
	 		]], 
	        toolbar: "#toolbar",
	        onBeforeLoad : function(){
	        	try{
	        		$("#courseList").combobox("getData")
	        	}catch(err){
	        		preLoadClazz();
	        	}
	        }
	    
	    }); 
	    //设置分页控件 
	    var p = $('#dataList').datagrid('getPager'); 
	    $(p).pagination({ 
	    	autoRowHeight:false,
	        pageSize: 10,//每页显示的记录条数，默认为10 
	        pageList: [10,20,30,50,100],//可以设置每页记录条数的列表 
	        beforePageText: '第',//页数文本框前显示的汉字 
	        afterPageText: '页    共 {pages} 页', 
	        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录', 
	    }); 
	    
	    //设置工具类按钮
	    $("#add").click(function(){
	    	$("#addDialog").dialog("open");
	    });
	    
	    //修改
	    $("#edit").click(function(){
	    	var selectRows = $("#dataList").datagrid("getSelections");
        	if(selectRows.length != 1){
            	$.messager.alert("消息提醒", "请选择一条数据进行操作!", "warning");
            } else{
		    	$("#editDialog").dialog("open");
            }
	    });
	    
	    //删除
	    $("#delete").click(function(){
	    	var selectRows = $("#dataList").datagrid("getSelections");
        	var selectLength = selectRows.length;
        	if(selectLength == 0){
            	$.messager.alert("消息提醒", "请选择数据进行删除!", "warning");
            } else{
            	var numbers = [];
            	$(selectRows).each(function(i, row){
            		numbers[i] = row.sn;
            	});
            	var ids = [];
            	$(selectRows).each(function(i, row){
            		ids[i] = row.id;
            	});
            	$.messager.confirm("消息提醒", "将删除该学生的对应成绩，确认继续？", function(r){
            		if(r){
            			$.ajax({
							type: "post",
							url: "ScoreServlet?method=DeleteScore",
							data: {sns: numbers, ids: ids},
							success: function(msg){
								if(msg == "success"){
									$.messager.alert("消息提醒","删除成功!","info");
									//刷新表格
									$("#dataList").datagrid("reload");
									$("#dataList").datagrid("uncheckAll");
								} else{
									$.messager.alert("消息提醒","删除失败!","warning");
									return;
								}
							}
						});
            		}
            	});
            }
	    });
	    
	  	
	    


	  	
	  	function preLoadClazz(){
	  		$("#clazzList").combobox({
		  		width: "150",
		  		height: "25",
		  		valueField: "id",
		  		textField: "name",
		  		multiple: false, //可多选
		  		editable: false, //不可编辑
		  		method: "post",
		  		url: "ClazzServlet?method=getClazzList&t="+new Date().getTime()+"&from=combox",
		  		onChange: function(newValue, oldValue){
		  			//加载班级下的学生
		  			//$('#dataList').datagrid("options").queryParams = {clazzid: newValue};
		  			//$('#dataList').datagrid("reload");
		  		}
		  	});
	  	}
	  	//下拉框通用属性
	  	$("#add_clazzList, #edit_clazzList").combobox({
	  		width: "200",
	  		height: "30",
	  		valueField: "id",
	  		textField: "name",
	  		multiple: false, //可多选
	  		editable: false, //不可编辑
	  		method: "post",
	  	});
	
	  	$("#add_clazzList").combobox({
	  		url: "ClazzServlet?method=getClazzList&t="+new Date().getTime()+"&from=combox",
	  		onLoadSuccess: function(){
		  		//默认选择第一条数据
				var data = $(this).combobox("getData");;
				$(this).combobox("setValue", data[0].id);
	  		}
	  	});
	  	
	  	
	  	
	  	$("#edit_clazzList").combobox({
	  		url: "ClazzServlet?method=getClazzList&t="+new Date().getTime()+"&from=combox",
			onLoadSuccess: function(){
				//默认选择第一条数据
				var data = $(this).combobox("getData");
				$(this).combobox("setValue", data[0].id);
	  		}
	  	});
	  	
	  	//设置添加学生窗口
	    $("#addDialog").dialog({
	    	title: "添加学生成绩信息",
	    	width: 650,
	    	height: 460,
	    	iconCls: "icon-add",
	    	modal: true,
	    	collapsible: false,
	    	minimizable: false,
	    	maximizable: false,
	    	draggable: true,
	    	closed: true,
	    	buttons: [
	    		{
					text:'添加',
					plain: true,
					iconCls:'icon-user_add',
					handler:function(){
						var validate = $("#addForm").form("validate");
						if(!validate){
							$.messager.alert("消息提醒","请检查你输入的数据!","warning");
							return;
						} else{
							var clazzid = $("#add_clazzList").combobox("getValue");
							$.ajax({
								type: "post",
								url: "ScoreServlet?method=AddScore",
								data: $("#addForm").serialize(),
								success: function(msg){
									if(msg == "success"){
										$.messager.alert("消息提醒","添加成功!","info");
										//关闭窗口
										$("#addDialog").dialog("close");
										//清空原表格数据
										$("#add_studentId").textbox('setValue', "");
										$("#add_name").textbox('setValue', "");
										$("#add_courseId").textbox('setValue', "");
										$("#add_clazzId").textbox('setValue', "");
										$("#add_score").textbox('setValue', "");
									
										//重新刷新页面数据
										$('#dataList').datagrid("options").queryParams = {clazzid: clazzid};
							  			$('#dataList').datagrid("reload");
							  			setTimeout(function(){
											$("#clazzList").combobox('setValue', clazzid);
										}, 100);
										
									} else{
										$.messager.alert("消息提醒","添加失败!","warning");
										return;
									}
								}
							});
						}
					}
				},
				{
					text:'重置',
					plain: true,
					iconCls:'icon-reload',
					handler:function(){
						$("#add_studentId").textbox('setValue', "");
						$("#add_name").textbox('setValue', "");
						$("#add_courseId").textbox('setValue', "");
						$("#add_clazzId").textbox('setValue', "");
						$("#add_score").textbox('setValue', "");
						//重新加载年级
						$("#add_gradeList").combobox("clear");
						$("#add_gradeList").combobox("reload");
					}
				},
			]
	    });
	  	
	  	//设置编辑学生窗口
	    $("#editDialog").dialog({
	    	title: "修改学生信息",
	    	width: 650,
	    	height: 460,
	    	iconCls: "icon-edit",
	    	modal: true,
	    	collapsible: false,
	    	minimizable: false,
	    	maximizable: false,
	    	draggable: true,
	    	closed: true,
	    	buttons: [
	    		{
					text:'提交',
					plain: true,
					iconCls:'icon-user_add',
					handler:function(){
						var validate = $("#editForm").form("validate");
						var clazzid = $("#edit_clazzList").combobox("getValue");
						if(!validate){
							$.messager.alert("消息提醒","请检查你输入的数据!","warning");
							return;
						} else{
							$.ajax({
								type: "post",
								url: "ScoreServlet?method=EditScore&t="+new Date().getTime(),
								data: $("#editForm").serialize(),
								success: function(msg){
									if(msg == "success"){
										$.messager.alert("消息提醒","更新成功!","info");
										//关闭窗口
										$("#editDialog").dialog("close");
										//刷新表格
										$('#dataList').datagrid("options").queryParams = {clazzid: clazzid};
										$("#dataList").datagrid("reload");
										$("#dataList").datagrid("uncheckAll");
										
							  			setTimeout(function(){
											$("#clazzList").combobox('setValue', clazzid);
										}, 100);
							  			
									} else{
										$.messager.alert("消息提醒","更新失败!","warning");
										return;
									}
								}
							});
						}
					}
				},
				{
					text:'重置',
					plain: true,
					iconCls:'icon-reload',
					handler:function(){
						//清空表单
						$("#edit_studentId").textbox('setValue', "");
						$("#edit_name").textbox('setValue', "");
						$("#edit_course").textbox('setValue', "");
						$("#edit_score").textbox('setValue', "");
						$("#edit_gradeList").combobox("clear");
						$("#edit_gradeList").combobox("reload");
					}
				}
			],
			onBeforeOpen: function(){
				var selectRow = $("#dataList").datagrid("getSelected");
				//设置值
				$("#edit_studentId").textbox('setValue', selectRow.studentId);
				$("#edit_name").textbox('setValue', selectRow.studentName);
				$("#edit_courseId").textbox('setValue', selectRow.courseId);
				$("#edit_score").val('setValue',selectRow.score);
				$("#edit-id").val(selectRow.id);
				var clazzid = selectRow.clazzId;
				setTimeout(function(){
					$("#edit_clazzList").combobox('setValue', clazzid);
				}, 100);
				
			}
	    });
	  //搜索按钮监听事件
	  	$("#search-btn").click(function(){
	  		$('#dataList').datagrid('load',{
	  			studentName: $('#search_student_name').val(),
	  			clazzid: $("#clazzList").combobox('getValue') == '' ? 0 : $("#clazzList").combobox('getValue')
	  		});
	  	});
	});
 
	
    $("#download").click(function(){
     	$.ajax({
			type: "post",
			url: "ScoreServlet?method=exportAccountData",
			data: {sns: numbers, ids: ids},
			success: function(msg){
				if(msg == "success"){
					$.messager.alert("消息提醒","删除成功!","info");
					
				} else{
					$.messager.alert("消息提醒","删除失败!","warning");
					return;
				}
			}
		});
    });

	</script>
</head>
<body>
	<!-- 学生列表 -->
	<table id="dataList" cellspacing="0" cellpadding="0"> 
	</table> 

	<!-- 工具栏 -->
	<div id="toolbar" style="height:26px;">
		<c:if test="${userType == 1 || userType == 3}">
		<div style="float: left;"><a id="add" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加</a></div>
			<div style="float: left;" class="datagrid-btn-separator"></div>
		</c:if>
		<div style="float: left;"><a id="edit" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改</a></div>
			<div style="float: left;" class="datagrid-btn-separator"></div>
		<c:if test="${userType == 1 || userType == 3}">
		<div style="float: left;"><a id="delete" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-some-delete',plain:true">删除</a></div>
		</c:if>
		<div style="float: left;margin-top:4px;" class="datagrid-btn-separator" >&nbsp;&nbsp;姓名：<input id="search_student_name" class="easyui-textbox" name="search_student_name" /></div>
		<div style="float:left; margin-left: 10px;margin-top:4px;" >班级：<input id="clazzList" class="easyui-textbox" name="clazz" />
			<a id="search-btn" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true">搜索</a>
		</div>
		<div  style="float: left;">
			<!-- 导入 -->
			<form action="/StudentManagerWeb/UploadExcelServlet" enctype="multipart/form-data"method="post">		
			<!-- /StudentManagerWeb/ScoreServlet?method=excelImport -->
		
			     <a href="javascript:void();" class=" easyui-linkbutton" onclick="document.getElementById('headImage').click()">导入成绩</a>
				 <input  type='text'  id='textfield'style="border:none;display:none;text-align:center;"class='txt' />
				 <input type="file" name="headImage"id="headImage" class="el-upload__input" style="display:none;;filter:alpha(opacity:0);opacity: 0;width:0px ;size:28;" onchange="document.getElementById('textfield').value=this.value;document.getElementById('subExcel').click();"accept="XML/XSL"/>
				 <input style="display:none;" type="submit" id="subExcel" name="subExel">
			</form>
		</div>
		<div style="float: left;" >
			<form action="/StudentManagerWeb/ScoreServlet?method=exportAccountData" method="post">
			 <a href="javascript:void();" class=" easyui-linkbutton" onclick="document.getElementById('download').click()">导出成绩</a>
			<input type="submit"  id="download" style="float: left;display:none;margin-top:4px;" class="easyui-linkbutton">
			</form>	
		</div>

	</div>
	
	<!-- 添加学生窗口 -->
	<div id="addDialog" style="padding: 10px">  
    	<form id="addForm" method="post">
	    	<table cellpadding="8" >
	    		<tr>	
	    			<td>学号:</td>
	    			<td><input id="add_studentId" style="width: 200px; height: 30px;" class="easyui-textbox" type="text" name="studentId" data-options="required:true, missingMessage:'请填写学号'" /></td>
	    		</tr>
	    		<tr>
	    			<td>姓名:</td>
	    			<td>
	    				<input id="add_name"  class="easyui-textbox" style="width: 200px; height: 30px;" type="text" name="name" data-options="required:true, missingMessage:'请输入学生姓名'" />
	    			</td>
	    		</tr>
	    		<tr>
	    			<td>课程:</td>
	    			<td><input id="add_courseId" style="width: 200px; height: 30px;" class="easyui-textbox" type="text" placeholder="请输入课程ID"name="courseId" validType="number" /></td>
	    		</tr>
	    		<tr>
	    			<td>班级:</td>
	    			<td><input id="add_clazzList" style="width: 200px; height: 30px;" class="easyui-textbox" name="clazzid" /></td>
	    		</tr>
	    		<tr>
	    			<td>成绩:</td>
	    			<td><input id="add_score" style="width: 200px; height: 30px;" class="easyui-textbox" type="text" name="score" validType="number" /></td>
	    		</tr>
	    	</table>
	    </form>
	</div>
	
	<!-- 修改学生窗口 -->
	<div id="editDialog" style="padding: 10px">
    	<form id="editForm" method="post">
	    	<input type="hidden" name="id" id="edit-id">
	    	<table cellpadding="8" >
	    		<tr>
	    			<td>学号:</td>
	    			<td><input id="edit_studentId" style="width: 200px; height: 30px;" class="easyui-textbox" type="text" name="studentId" data-options="required:true, missingMessage:'请填写学号'" /></td>
	    		</tr>

	    		<tr>
	    			<td>姓名:</td>
	    			<td><input id="edit_name" style="width: 200px; height: 30px;" class="easyui-textbox" type="text" name="name" data-options="required:true, missingMessage:'请填写姓名'" /></td>
	    		</tr>
	    		<tr>
	    			<td>课程:</td>
	    			<td><input id="edit_courseId" style="width: 200px; height: 30px;" class="easyui-textbox" type="text" name="courseId" validType="number" /></td>
	    		</tr>
	    		
	    		<tr>
	    			<td>班级:</td>
	    			<td><input id="edit_clazzList" style="width: 200px; height: 30px;" class="easyui-textbox" name="clazzid" /></td>
	    		</tr>
	    		
	    		<tr>
	    			<td>成绩:</td>
	    			<td><input id="edit_score" style="width: 200px; height: 30px;" class="easyui-textbox" type="text" name="score" validType="number" /></td>
	    		</tr>
	    	</table>
	    </form>
	</div>
<!-- 提交表单处理iframe框架 -->
	<iframe id="photo_target" name="photo_target"></iframe>  
	
	

</body>
	<script type="text/javascript">
	/*
	// 导出成绩为excel表格
	// 使用outerHTML属性获取整个table元素的HTML代码（包括<table>标签），然后包装成一个完整的HTML文档，设置charset为urf-8以防止中文乱码
    var html = "<html><head><meta charset='utf-8' /></head><body>" + document.getElementById("dataList").outerHTML + "</body></html>";
    // 实例化一个Blob对象，其构造函数的第一个参数是包含文件内容的数组，第二个参数是包含文件类型属性的对象
    var blob = new Blob([html], { type: "application/vnd.ms-excel" });
    var a = document.getElementById("download");
    // 利用URL.createObjectURL()方法为a元素生成blob URL
    a.href = URL.createObjectURL(blob);
    // 设置文件名
    a.download = "学生成绩表.xls";
	
    */
    
    
    
    
    
    function getPath(obj) {
		 if (obj) {
		  if (window.navigator.userAgent.indexOf("MSIE") >= 1) {
			   obj.select();
			   return document.selection.createRange().text;
		  } else if (window.navigator.userAgent.indexOf("Firefox") >= 1) {
			   if (obj.files) {
			    return obj.files.item(0).getAsDataURL();
			   }
			   return obj.value;
		  }
		  	return obj.value;
		 }
		}

    
    
    function change(){

    	document.getElementById('textfield').value=this.value;
    //	alert(this.value);
    	//document.getElementById('subExcel').click();
    	
    }
    
    
    
    
    
    
    
    
    function base64 (content) {
        return window.btoa(unescape(encodeURIComponent(content)));         
     }
     /*
     *@tableId: table的Id
     *@fileName: 要生成excel文件的名字（不包括后缀，可随意填写）
     */
     function tableToExcel(tableID,fileName){
       var table = document.getElementById(tableID);
       console.log(table);
       var excelContent = table.innerHTML;
       var excelFile = "<html xmlns:o='urn:schemas-microsoft-com:office:office' xmlns:x='urn:schemas-microsoft-com:office:excel' xmlns='http://www.w3.org/TR/REC-html40'>";
       excelFile += "<head><!--[if gte mso 9]><xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet><x:Name>{worksheet}</x:Name><x:WorksheetOptions><x:DisplayGridlines/></x:WorksheetOptions></x:ExcelWorksheet></x:ExcelWorksheets></x:ExcelWorkbook></xml><![endif]--></head>";
       excelFile += "<body><table>";
       excelFile += excelContent;
       excelFile += "</table></body>";
       excelFile += "</html>";
       var link = "data:application/vnd.ms-excel;base64," + base64(excelFile);
       var a = document.createElement("a");
       a.download = fileName+".xls";
       a.href = link;
       a.click();
     }

     

     
    


	</script>
	


</html>