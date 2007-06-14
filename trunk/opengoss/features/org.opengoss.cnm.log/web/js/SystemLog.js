dojo.require("dojo.widget.Spinner");
dojo.require("dojo.widget.*");
dojo.require("dojo.widget.CommonTable");
      
dojo.hostenv.writeIncludes();

var SystemLog = {
	jsRpcClient:new JSONRpcClient("/Log/log"),
	ftable:null,
}

	//**************查询日志**********************
	function findSysLogCallBack(result,exception){
		if (exception != null){
			alert("search failed");
			alert(toJSON(exception));
		}else{
//			alert(toJSON(result.list));
			findSysLogRender(result.list)
		}
	}
	function findLog(){
		try {
			var binder = new FormBinder();
			var searchData = binder.bindUIToData("searchForm");
			var time = dojo.widget.byId("time");
			searchData.time = time.getDate();
//			alert(toJSON(searchData));
			SystemLog.jsRpcClient.logService.findSysLogRecord(findSysLogCallBack,searchData);
		} catch (e) { 
			alert(e);
		}
	}
	
     //****************页面初始化***********************
   
 	function logInitHandler(result , exception){
		if (exception != null){
			alert("search failed");
			alert(toJSON(exception));
		}else{
			initRender(result.list);
		}
	}
	
	function loadAllSysLog(){
			
		SystemLog.jsRpcClient.logService.loadSysLogRecord(logInitHandler);
		
	}		