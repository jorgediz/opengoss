
dojo.require("dojo.widget.*" );
dojo.require("dojo.event.*");

dojo.hostenv.writeIncludes(); 
dojo.require();	

//********************全局变量存储区**************************
var plguinRpc = "/alarmmgr/JSON-RPC";
//*********************************************************
//***********************************3 层*******************************************
	
	function renderTableByOutsideDataCallBack(result , exception){
		try {
			renderTableByOneData(result,exception);
		} catch(e) {
			alert("后台出错!Exception:"+exception);
		}
	}

	/**
	 * 
	 */
	function busAlarmsPreviewCallBack(result , exception){
		if(exception){
			alert("后台出错!Exception:"+exception);			
		}
		try {
			/*result = toJSON(result);
			document.write(result);*/
			result = result.list;
			renderbusAlarmsPreResult(result,exception);
		} catch(e) {
			alert("前台出错!Exception:"+e);
		}
	}
	
	
	function modiBusAlarmInfoCallBack(result , exception){
		try{
			if(result){
				setmodifyDialogForm(result);
				var modifyBADialog = dojo.widget.byId("modifyBusAlarmDialog");
				var btnCancel = document.getElementById("modifyBusAlarm_cancel");
				modifyBADialog.setCloseControl(btnCancel);
				modifyBADialog.show();
			}
		}catch(e){
			alert("后台出错!Exception:"+exception);
		}
	}
	
	/**
	 *展现某条业务告警的详细信息
	 */
	function previewBusAlarmInfoCallBack(result , exception){
		try{
			if(result){
				setAcAlarmForm(result);
				var previewBADialog = dojo.widget.byId("detailBusAlarmDialog");
				var btnOK = document.getElementById("detailBusAlarm_OK");
				previewBADialog.setCloseControl(btnOK);
				previewBADialog.show();
			}			
		}catch(e){
			alert("后台出错!Exception:"+exception);
		}
	}
	
	/**根据alarmId，删除table中的这条*/
	function removeRow(aID){
		var rowBody = document.getElementById("tableBody");
		for(var i=0 ; i<rowBody.rows.length; i++){
			var rIndex = rowBody.rows[i];
			//=======================>>>>>>>>>>>>>>>>>>>>>>>>此处存在重大商业隐患,'rIndex.cells[0].childNodes[0].id'始终为1,有待修改
			alert(rIndex.cells[0].childNodes[0].id+"-^-"+aID);
			if(rIndex.cells[0].childNodes[0].id==aID){
					alert("Now you will the : "+i+" LINE");
					rowBody.deleteRow(i);
			}
		}
//		aID=aID+"";
//		for(var i=0 ; i<rowBody.rows.length; i++){
//			var rIndex = rowBody.rows[i];
//			if(rIndex.childNodes[1].innerHTML.match(aID)){
//					rowBody.deleteRow(i);
//					alert("Has Been delete : "+i);
//			}
//		}
	}
	
	function searchResultCallBack(result , exception){
		try{
			if(result){
				/*result = toJSON(result);
				document.write(result);*/
				/**清除table中的数据，但不删除后台的数据等待渲染搜索来的数据*/
				var ctable = dojo.widget.byId("alarmCommonTable");
	//			var objs = ctable.getSelectedObjs();
				ctable.__tableWidget.store.clearData();
				/**将搜索来的数据渲染到table中*/
				result = result.list;
				renderbusAlarmsPreResult(result,exception);
			}			
		}catch(e){
			alert("后台出错!Exception:"+exception);
		}
	}
	
//***********************************2 层*******************************************
	function doNothing(result,exception){
		
	}
	
	/**
	 * 
	 */
	function activeBusAlarmsPreview(){
		try {
			jsonrpc = new JSONRpcClient(plguinRpc);			
			jsonrpc.ManagerService.getAllBizAlrams(busAlarmsPreviewCallBack , 0);//page 0
		} catch(e) {
			alert(e);
		}
	}
	
	/**
	 * 修改某条业务告警信息
	 */
	function modiBusAlarmInfo(rows){
		try {
			jsonrpc = new JSONRpcClient(plguinRpc);
			if(rows.length>1){
				alert("出错！您选择了多行操作！");
				return;
			}	
			for(var i = 0; i < rows.length ; i++){
				var alarmId = rows[i].key;
				var hiden_alarmId = document.getElementById("hiden_DescInfoDialog");
				hiden_alarmId.value=alarmId;
				jsonrpc.ManagerService.getAlarmByAlarmID(modiBusAlarmInfoCallBack , alarmId);
			}
		} catch(e) {
			alert(e);
		}
	}

	/**
	 * 展现告警的详细信息
	 */
	function previewBusAlarm(rows){
		try {
			jsonrpc = new JSONRpcClient(plguinRpc);
			if(rows.length>1){
				alert("出错！您选择了多行操作！");
				return;
			}
			for(var i = 0; i < rows.length ; i++){
				var alarmId = rows[i].key;
//				alert("A->B:"+alarmId);
				jsonrpc.ManagerService.getAlarmByAlarmID(previewBusAlarmInfoCallBack , alarmId);//page 0
			}
		} catch(e) {
			alert(e);
		}
	}
	
	/**
	 *删除某条告警信息
	 */
	function deleteBusAlarm(rows){
		try {
			jsonrpc = new JSONRpcClient(plguinRpc);

			for(var i=rows.length-1; i>=0; i--){
				var alarmId = rows[i].key;
				/**先删前台的这条数据*/
				this.store.removeData(rows[i]);
				/**删除后台的数据*/
				jsonrpc.ManagerService.delBussinessAlarm(doNothing , alarmId);//page 0
			}		
		} catch(e) {
			alert(e);
		}
	}
	
	/**
	 * 告警确认
	 */
	function acknowledgeAlarm(rows){
		try {
			jsonrpc = new JSONRpcClient(plguinRpc);			
			
			for(var i = 0; i < rows.length ; i++){
				var alarmId = rows[i].key;
				jsonrpc.ManagerService.acknowledgeAlarm(doNothing,alarmId);//page 0
				alert("ID 为"+alarmId+"的告警已经确认.");
			}	
//			var list = getAlarmID();
//			var alarmId = list.pop();
//			while(alarmId){
//				
//				alarmId = list.pop();
//			}
		} catch(e) {
			alert(e);
		}
	}
	
	
	/**
	 * 告警反确认
	 */
	function unacknowledgeAlarm(rows){
		try {
			jsonrpc = new JSONRpcClient(plguinRpc);			
			for(var i = 0; i < rows.length ; i++){
				var alarmId = rows[i].key;
				jsonrpc.ManagerService.unacknowledgeAlarm(doNothing,alarmId);
				alert("ID 为"+alarmId+"的告警已经反确认.");
			}
//			var list = getAlarmID();
//			var alarmId = list.pop();
//			while(alarmId){
//				
//				alarmId = list.pop();
//			}
		} catch(e) {
			alert(e);
		}
	}
	
	/**
	 * 装载外部的URL
	 */
	function loadOutsideSource(){
		jsonrpc = new JSONRpcClient(plguinRpc);
		var aHref = location.href;		
		var result = aHref.split("?");
		if(!result[1]){
			return false;
		}
		alert("location redefined without query string.");
		var data = result[1].split("=");//result=result;
		//需要展现在告警列表中的告警ids.
		var idArray = data[1].split(",");//1,2,3
		for(var i = 0 ; i<idArray.length; i++){
			jsonrpc.ManagerService.getAlarmByAlarmID(renderTableByOutsideDataCallBack , idArray[i]);			
		}
		
		//如果'？'后面有参数则返回true
		return true;
	}
	
	 /**
 	 * @大客户告警页面
 	 * @展现搜索结果展现
 	 */
 	function showSearchBizAlarm(){
 		try {
			jsonrpc = new JSONRpcClient(plguinRpc);
			
			/**搜集searchBox中的内容*/
			var bizName = getSearchCusName();
			var bizType = getSearchCusLevel();
			
			jsonrpc.ManagerService.getSearchResultOfBizAlarm(searchResultCallBack , bizName, bizType);				
			
		} catch(e) {
			alert(e);
		}
 	}
	
	
//******************************1 层*********************************************
	/**
	 * 初始化界面展现
	 */
	function LoadViewWidget(){
		var commnTable = dojo.widget.byId("alarmCommonTable");
		
		/**
		 * 删除某条业务告警信息
		 */
		commnTable.setDelFunc(deleteBusAlarm);
		
		/**
		 * 展现某条业务告警信息
		 */
		commnTable.addFunc("详细信息",previewBusAlarm);
		
		/**
		 * 告警确认
		 */
		commnTable.addFunc("告警确认",acknowledgeAlarm);
		
		/**
		 * 告警反确认
		 */
		commnTable.addFunc("告警反确认",unacknowledgeAlarm);
		
		/**
		 * 修改某条业务告警信息
		 */
		commnTable.addFunc("告警备注信息修改",modiBusAlarmInfo);
	}
	
	function init()
	{
		/**
		 * 初始化界面展现
		 */
		LoadViewWidget();
		
		/**
		 * 装载外部的URL
		 */
		if(!loadOutsideSource()){//如果请求没有附加任何参数
			/**
			 * 大客户经理初始界面的展现
			 */
			activeBusAlarmsPreview(); 
		}
		
		/**
		 * 查询按钮
		 */
		var searchBinsAlarmButton=dojo.widget.byId("searchBox_search");
		dojo.event.connect(searchBinsAlarmButton, "onClick", "showSearchBizAlarm");
	}
	
	dojo.addOnLoad(init);
	