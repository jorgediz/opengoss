dojo.require("dojo.widget.Spinner");
dojo.require("dojo.widget.CommonTable");
      
dojo.hostenv.writeIncludes();
var OperationLog = {
	jsRpcClient:new JSONRpcClient("/Log/log"), //("./log");不行
	ftable:null,
}

	//****************查询日志*********************
	
	function findLogHandler(result , exception){
		if (exception != null){
			alert("search failed");
			alert(toJSON(exception));
		}else{
			findLogRender(result.list)
		}
	}
	function findLog(){
		
			var searchData = bindUItoData("searchForm");
			OperationLog.jsRpcClient.logService.findLogRecord(findLogHandler,"OperationLog",searchData);
	}
	/*
	 * 绑定页面的数据到对象，要求页面上的tag中添加fieldName标签，并且fieldName标签内容和要绑定的对象属性一致
	 * 局限性：只解析了一级级连的对象关联
	 */
	 function bindUItoData(formId) {
     	var formUI = document.getElementById(formId);
        var data = new Object();
     	var inputs = formUI.getElementsByTagName("input");
     	var newInputs = new Array(); //只添加type＝'text'
     	for(var index = 0; index <inputs.length; index ++) {
     		if(inputs[index].type == "text"){
     			newInputs[newInputs.length] = inputs[index];
     		}
     	}
		inputs  = newInputs  ;
     	for(var index=0; index<inputs.length; index++) {
     		var input = inputs[index];
     		var fieldName = input.getAttribute("fieldName");
     		if( fieldName != null ){
     			if( isNested( fieldName ) ){
     			    var nesteds = fieldName.split(".");
     			    var nestedObjectName = nesteds[0];
     			    var nestedFieldName = nesteds[1];
     			    
     			    if( data[nestedObjectName] ){
     			    	var nestedObject = data[nestedObjectName];
     			    	nestedObject[nestedFieldName]= input.value;
     			    }else{
     			    	var nestedObject = new Object();
     			    	nestedObject[nestedFieldName]= input.value;
     			    	data[nestedObjectName] = nestedObject;
     			    }
     			    
     		    }else{
     			    data[fieldName]=input.value;
     		    }
     		}
     	}
//     	alert(toJSON(data));
     	return data;
     }
     
     /*
      * 对象是否含有对象
      */
     function isNested(fieldName) {
              	return -1 != fieldName.indexOf(".");
     }
//     function testBind(){
//        var formUI = document.getElementById("searchForm");
//        var data = new Object();
//     	var inputs = formUI.getElementsByTagName("input");
//     
//     	var newInputs = new Array();
//     	for(var index = 0; index <inputs.length; index ++) {
//     		if(inputs[index].type == "text"){
//     			newInputs[newInputs.length] = inputs[index];
//     		}
//     	}
//     	for(var index=0; index<newInputs.length; index++) {
//	     	alert(newInputs[index].type);     		
//     	}
//
//     }
	
	//********************页面初始化**************
/*
 * 
 * 页面初始化 
 * 
 */
 	function logInitHandler(result , exception){
		if (exception != null){
			alert("search failed");
			alert(toJSON(exception));
		}else{
			initRender(result.list)
		}
	}
	
	function loadAllOperationLog(){

		OperationLog.jsRpcClient.logService.loadOperationLogRecord(logInitHandler);
		
	}		
		