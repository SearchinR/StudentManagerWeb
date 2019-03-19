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
    <script type="text/javascript" src="http://echarts.baidu.com/gallery/vendors/echarts/echarts.min.js"></script>
    <script type="text/javascript" src="http://echarts.baidu.com/gallery/vendors/echarts-gl/echarts-gl.min.js"></script>
    <script type="text/javascript" src="http://echarts.baidu.com/gallery/vendors/echarts-stat/ecStat.min.js"></script>
    <script type="text/javascript" src="http://echarts.baidu.com/gallery/vendors/echarts/extension/dataTool.min.js"></script>
    <script type="text/javascript" src="http://echarts.baidu.com/gallery/vendors/echarts/map/js/china.js"></script>
    <script type="text/javascript" src="http://echarts.baidu.com/gallery/vendors/echarts/map/js/world.js"></script>
    <script type="text/javascript" src="https://api.map.baidu.com/api?v=2.0&ak=xfhhaTThl11qYVrqLZii6w8qE5ggnhrY&__ec_v__=20190126"></script>
    <script type="text/javascript" src="http://echarts.baidu.com/gallery/vendors/echarts/extension/bmap.min.js"></script>
    <script type="text/javascript" src="http://echarts.baidu.com/gallery/vendors/simplex.js"></script>
	
</head>
<body>
	   <div id="container" style="height: 500px;weight:500px;"></div>
	   <%--<div id="container1"style="height: 500px;weight:500px;"></div>--%>
       <%--<div id="container3"style="height: 500px;weight:500px;"></div>--%>
       <div id="container2"style="height: 500px;weight:500px;"></div>
       
 <script type="text/javascript">
	var dom = document.getElementById("container");
	var myChart = echarts.init(dom);
	var app = {};
	option = null;
	app.title = '嵌套环形图';
	
	option = {
			title : {
				text: '统计分析',
				subtext: "",
				x:'center'
			},
	    tooltip: {
	        trigger: 'item',
	        formatter: "{a} <br/>{b}: {c} ({d}%)"
	    },
	    legend: {
	        orient: 'vertical',
	        x: 'left',
	        data:['专业课','选修课','公共课']
	    },
	    series: [
	        {
	            name:'来源',
	            type:'pie',
	            selectedMode: 'single',
	            radius: [0, '30%'],
	
	            label: {
	                normal: {
	                    position: 'inner'
	                }
	            },
	            labelLine: {
	                normal: {
	                    show: false
	                }
	            },
	            data:[

	                
	            ]
	        },
	        {
	            name:'来源',
	            type:'pie',
	            radius: ['40%', '55%'],
	            label: {
	                normal: {
	                    formatter: '{a|{a}}{abg|}\n{hr|}\n  {b|{b}：}{c}  {per|{d}%}  ',
	                    backgroundColor: '#eee',
	                    borderColor: '#aaa',
	                    borderWidth: 1,
	                    borderRadius: 4,
	                    // shadowBlur:3,
	                    // shadowOffsetX: 2,
	                    // shadowOffsetY: 2,
	                    // shadowColor: '#999',
	                    // padding: [0, 7],
	                    rich: {
	                        a: {
	                            color: '#999',
	                            lineHeight: 22,
	                            align: 'center'
	                        },
	                        // abg: {
	                        //     backgroundColor: '#333',
	                        //     width: '100%',
	                        //     align: 'right',
	                        //     height: 22,
	                        //     borderRadius: [4, 4, 0, 0]
	                        // },
	                        hr: {
	                            borderColor: '#aaa',
	                            width: '100%',
	                            borderWidth: 0.5,
	                            height: 0
	                        },
	                        b: {
	                            fontSize: 16,
	                            lineHeight: 33
	                        },
	                        per: {
	                            color: '#eee',
	                            backgroundColor: '#334455',
	                            padding: [2, 4],
	                            borderRadius: 2
	                        }
	                    }
	                }
	            },
	            data:[
	                {value:<%=request.getAttribute("MNum")%>, name:'专业课'},
	                {value:<%=request.getAttribute("PNum")%>, name:'公共课'},
	                {value:<%=request.getAttribute("ENum")%>, name:'选修课'},
	            ]
	        }
	    ]
	};;
	if (option && typeof option === "object") {
	    myChart.setOption(option, true);
	}
</script>


<script type="text/javascript">
	var dom = document.getElementById("container1");
	var myChart = echarts.init(dom);
	var app = {};
	option = null;
	option = {
			title : {
				text: '成绩与性别分析图',
				subtext: "",
				
			},
			 tooltip: {
			        trigger: 'axis'
			    },
		   legend: {},
		  
		   dataset: {
		       source: [
		           ['product', '专业课', '公共课', '选修课'],
		           ['男', <%=request.getAttribute("majorM")%>, <%=request.getAttribute("publicM")%>, <%=request.getAttribute("electiveM")%>],
		           ['女', <%=request.getAttribute("majorW")%>, <%=request.getAttribute("publicW")%>, <%=request.getAttribute("electiveW")%>],
		
		       ]
		   },
		   xAxis: {type: 'category'},
		   yAxis: {},
		   // Declare several bar series, each will be mapped
		   // to a column of dataset.source by default.
		   series: [
		       {type: 'bar'},
		       {type: 'bar'},
		       {type: 'bar'}
		   ]
	};
	;
	if (option && typeof option === "object") {
	    myChart.setOption(option, true);
	}
</script>



<script type="text/javascript">
var dom = document.getElementById("container2");
var myChart = echarts.init(dom);
var app = {};
option = null;
option = {
    title: {
        text: '成绩与课程类型关系分析图'
    },
    tooltip: {
        trigger: 'axis'
    },
    legend: {
        data:['各课程平均分','男生平均分','女生平均分']
    },
    grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
    },
    toolbox: {
        feature: {
            saveAsImage: {}
        }
    },
    xAxis: {
        type: 'category',
        boundaryGap: false,
        data: ['专业课','公共课','选修课']
    },
    yAxis: {
        type: 'value'
    },
    series: [
        {
            name:'各课程平均分',
            type:'line',
            stack: '总量',
            data:[<%=request.getAttribute("majorB")%>, <%=request.getAttribute("publicB")%>, <%=request.getAttribute("electiveB")%>]
        },
        {
            name:'男生平均分',
            type:'line',
            stack: '总量',
            data:[<%=request.getAttribute("majorM")%>, <%=request.getAttribute("publicM")%>, <%=request.getAttribute("electiveM")%>]
        },
        {
            name:'女生平均分',
            type:'line',
            stack: '总量',
            data:[<%=request.getAttribute("majorW")%>, <%=request.getAttribute("publicW")%>, <%=request.getAttribute("electiveW")%>]
        },
  
    ]
};
;
if (option && typeof option === "object") {
    myChart.setOption(option, true);
}
</script>

<script type="text/javascript">
var dom = document.getElementById("container3");
var myChart = echarts.init(dom);
var app = {};
option = null;

setTimeout(function () {

    option = {
    		title : {
				text: '成绩与教师学历关系分析图',
				subtext: "",
			},
	        legend: {},
	        tooltip: {
	            trigger: 'axis',
	            showContent: false
	        },
	        dataset: {
	            source: [
	                ['product', '本科生', '研究生', '博士生',],
	                ['男生平均分', <%=request.getAttribute("BSM")%>, <%=request.getAttribute("YSM")%>, <%=request.getAttribute("BoSM")%>],
	                ['女生平均分',<%=request.getAttribute("BSW")%>, <%=request.getAttribute("YSW")%>, <%=request.getAttribute("BoSW")%>],
	                ['总平均分', <%=request.getAttribute("BB")%>, <%=request.getAttribute("YB")%>, <%=request.getAttribute("BoB")%>],
	               
	            ]
	        },
	        xAxis: {type: 'category'},
	        yAxis: {gridIndex: 0},
	        grid: {top: '55%'},
	        series: [
	            {type: 'line', smooth: true, seriesLayoutBy: 'row'},
	            {type: 'line', smooth: true, seriesLayoutBy: 'row'},
	            {type: 'line', smooth: true, seriesLayoutBy: 'row'},
	            {type: 'line', smooth: true, seriesLayoutBy: 'row'},
	            {
	                type: 'pie',
	                id: 'pie',
	                radius: '30%',
	                center: ['50%', '25%'],
	                label: {
	                    formatter: '{b}: {@2012} ({d}%)'
	                },
	                encode: {
	                    itemName: 'product',
	                    value: '2012',
	                    tooltip: '2012'
	                }
	            }
	        ]
	    };
	
	    myChart.on('updateAxisPointer', function (event) {
	        var xAxisInfo = event.axesInfo[0];
	        if (xAxisInfo) {
	            var dimension = xAxisInfo.value + 1;
	            myChart.setOption({
	                series: {
	                    id: 'pie',
	                    label: {
	                        formatter: '{b}: {@[' + dimension + ']} ({d}%)'
	                    },
	                    encode: {
	                        value: dimension,
	                        tooltip: dimension
	                    }
	                }
	            });
	        }
	    });
	
	    myChart.setOption(option);
	
	});;
	if (option && typeof option === "object") {
	    myChart.setOption(option, true);
	}
 </script>

</body>

	


</html>