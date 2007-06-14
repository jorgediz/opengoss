dojo.require("dojo.widget.*" );
dojo.require("dojo.event.*");

dojo.require("dojo.widget.FloatingPane" );
dojo.require("dojo.widget.Dialog");

dojo.hostenv.writeIncludes(); 
dojo.require();	

var plguinRpc = "/alarmmgr/JSON-RPC";
//***********************************3 层*******************************************
	function customerInfoPreviewCallBack(result , exception){
		try{
			if(exception){				
				alert("后台出错！Exception："+exception);
				return;
			}
			/*result = toJSON(result);
			document.write(result);*/
			result = result.list;
			renderCusInfo(result,exception);
		}catch(e){
			alert(e);
		}
	}

	function showCusInfoCallBack(result,exception){
		try{
			if(exception){				
				alert("后台出错！Exception："+exception);
				return;
			}
			/*result = toJSON(result);
			document.write(result);*/
			renderOneCusInfo(result,exception);
		}catch(e){
			alert(e);
		}
	}
	
	/**
	 * 空函数
	 */
	function doNothing(result,exception){
		if(exception){				
			alert("后台出错！Exception："+exception);
			return;
		}
	}
	
	/**
	 * 因为大客户的姓名是唯一的，所以可以以大客户的姓名在界面上判断删除
	 * 删除选中的某一行
	 */
	function removeRow(result , exception){
		if(exception){				
			alert("后台出错！Exception："+exception);
			return;
		}
		cName = result.name;
		var rowBody = document.getElementById("tableBody");
		for(var i=0 ; i<rowBody.rows.length; i++){
			var rIndex = rowBody.rows[i];
			if(rIndex.childNodes[1].innerHTML.match(cName)){
					rowBody.deleteRow(i);
			}
		}
	}
	
	/**
 	 * @大客户告警页面
 	 * @展现搜索结果展现
	 */
	function showSearchResultCallBack(result,exception){
		try {
			/*result = toJSON(result);
			document.write(result);*/
			if(exception){				
				alert("后台出错！Exception："+exception);
				return;
			}
			result = result.list;
			renderSearchResultOfCI(result,exception);
		} catch(e) {
			alert(e);
		}
	}

//***********************************2 层*******************************************
 	function customerInfoPreview(){
 		try {
			jsonrpc = new JSONRpcClient(plguinRpc);
			jsonrpc.ManagerService.getAllCustomerInfo(customerInfoPreviewCallBack , 0);//page 0
		} catch(e) {
			alert(e);
		} 		
 	}
 	
 	/**
 	 * 点击展现用户详细信息展现
 	 */
 	function showCustomerInfo(rows){
 		try {
			jsonrpc = new JSONRpcClient(plguinRpc);
			
			if(rows.length>1){
				alert("出错！您选择了多行！");
				return;
			}
			
			for(var i = 0; i < rows.length ; i++){
				var customerID = rows[i].key;
//				alert(rows[i].index+"--"+customerID);
//				var customerID = rows[i].cells[0].childNodes[0].id;
				jsonrpc.ManagerService.getCustomerInfo(showCusInfoCallBack , customerID);			
			}
		} catch(e) {
			alert(e);
		}
 	}
 	
 	/**
 	 * @删除某条客户记录
 	 */
 	function deleteCusInfo(rows){
 		jsonrpc = new JSONRpcClient(plguinRpc);
 		for(var i = 0; i < rows.length ; i++){
			var customerID = rows[i].key;
 			/**界面上删除选中的某一行*/
 			jsonrpc.ManagerService.getCustomerInfo(removeRow , customerID);
 			/**数据库中删除选中的某一行*/
 			jsonrpc.ManagerService.deleteCustomer(doNothing , customerID);
		}
 	}
 	
 	
 	/**
 	 * @大客户告警页面
 	 * @展现搜索结果展现
 	 */
 	function showSearchBinsAlarmResult(){
 		try {
			jsonrpc = new JSONRpcClient(plguinRpc);
			
			/**搜集searchBox中的内容*/
			var cusName = getSearchCusName();
			var cusLevel = getSearchCusLevel();			
			jsonrpc.ManagerService.getSearchResultOfCusInfo(showSearchResultCallBack , cusName, cusLevel);				
			
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
		 * 修改客户备注信息
		 */
		commnTable.addFunc("删除",deleteCusInfo);
		
		/**
		 * 展现用户详细信息
		 */
		commnTable.addFunc("显示用户详细信息",showCustomerInfo);
	}


	function init()
	{
		LoadViewWidget();
		/**
		 * 大客户详细信息初始界面的展现
		 */
		customerInfoPreview();
		
		var searchBinsAlarmButton=dojo.widget.byId("searchBox_search");
		dojo.event.connect(searchBinsAlarmButton, "onClick", "showSearchBinsAlarmResult");
	}
	
	dojo.addOnLoad(init);
	
