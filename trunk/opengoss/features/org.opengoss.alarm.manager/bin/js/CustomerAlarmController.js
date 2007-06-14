
dojo.require("dojo.widget.*" );
dojo.require("dojo.event.*");

dojo.require("dojo.widget.FloatingPane" );
dojo.require("dojo.widget.Dialog");

dojo.hostenv.writeIncludes(); 
dojo.require();

var TableModule={
	jsonrpc : new JSONRpcClient("/alarmmgr/JSON-RPC")
}

var jsonRpc=TableModule.jsonrpc;

//--------------------全局变量区------------------------------------
	//计数器
	var count=0;
//------------------------------------------------------------------

//***********************3 层***************************************
	/**
	 * 修改备注信息dialog中的
	 * ok button
	 */
	function descInfoOkButton(){		
		var aa= document.getElementById("descText");
		descValue=aa.value;
		try{
			var hiden_alarmId = document.getElementById("hiden_DescInfoDialog");
			var customerID = hiden_alarmId.value;
			alert("您要修改的用户ID是："+customerID);
			if(customerID){				
				/**将修改的备注信息存放到后台*/
				jsonRpc.ManagerService.modifyCustomerInfo(doNothingCallBack , customerID, descValue);			
			}
		} catch(e) {
			alert(e);
		}
	}
	
	function showCusInfoCallBack(result,exception){
		if(exception){
			alert("大客户告警页面在从后台取数据的时候发生异常："+exception);			
		}
		try{
//			renderCusInfo(result);
			if(result){
				//放置用户的详细信息到table中
				var fullNameE = document.getElementById("fullName");
				fullNameE.innerHTML=result.fullName;
				var nameE=document.getElementById("name");
				nameE.innerHTML=result.name;
				var classE=document.getElementById("class");
				classE.innerHTML=result.customerClass;
				var operationTypeE=document.getElementById("operationType");
				operationTypeE.innerHTML=result.operationType;
				var timeE=document.getElementById("time");
				timeE.innerHTML=result.time;
				var levelE=document.getElementById("level");
				levelE.innerHTML=result.level;
				var phoneE = document.getElementById("phone");
				phoneE.innerHTML=result.phone;
				var mobileE=document.getElementById("mobile");
				mobileE.innerHTML=result.mobile;
				var corperatorE=document.getElementById("corperator");
				corperatorE.innerHTML=result.corperator;
				var addressE=document.getElementById("address");
				addressE.innerHTML=result.address;
				var informedE=document.getElementById("informed");
				informedE.innerHTML=result.informed;
				var messageE=document.getElementById("message");
				messageE.innerHTML=result.message;
				var emailE=document.getElementById("email");
				emailE.innerHTML=result.email;
				var regionE=document.getElementById("region");
				regionE.innerHTML=result.region;
				var circuitNoE=document.getElementById("circuitNo");
				circuitNoE.innerHTML=result.circuitNo;
				var circuitInfoE=document.getElementById("circuitInfo");
				circuitInfoE.innerHTML=result.circuitInfo;
				var faxE = document.getElementById("fax");
				faxE.innerHTML=result.fax;
				var descE=document.getElementById("desc");
				descE.innerHTML=result.description;
				
				var cusInfoDialog = dojo.widget.byId("cusInfoDialog");
				//alert(cusInfoDialog.id);
				var btn = document.getElementById("hider0");
				cusInfoDialog.setCloseControl(btn);
				cusInfoDialog.show();				
			}
		}catch(e){
			alert(e);
		}
	}
	
	/**
	 * 修改大客户备注信息 
	 */
	function cusInfoModifyCallBack(result,exception){
		if(exception){
			alert("大客户告警页面在从后台取数据的时候发生异常："+exception);			
		}
		try{
			/**
			 * 取出customer信息，将必要信息添加到dialog中
			 * 必要的复用
			 */
			renderModifyDialog(result);
			
			/**展现dialog*/
			var descInfoDialog = dojo.widget.byId("descInfoDialog");
			var descBtn = document.getElementById("descButton");
			descInfoDialog.setCloseControl(descBtn);
			descInfoDialog.show();
		}catch(e){
			alert(e);
		}
	}
	
	function cusWithAlaInfoCallBack(result,exception){
		if(exception){
			alert("大客户告警页面在从后台取数据的时候发生异常："+exception);			
		}
		try {
			/*result = toJSON(result);
			document.write(result);*/
			if(result){
				result = result.list;
				renderCusWithAlasResult(result);				
			}
		} catch(e) {
			alert(e);
		} 	
	}
	
	function doNothingCallBack(result,exception){
		if(exception){
			alert("大客户告警页面在从后台取数据的时候发生异常："+exception);				
		}
		
	}
	
	/**
	 * 展现某个大客户的业务告警
	 */
	function showBizAlarmCallBack(result,exception){
		if(exception){
			alert("大客户告警页面在从后台取数据的时候发生异常："+exception);			
		}
		try {
			result = result.list;
			/**定位到业务告警页面*/
			if(result){
				var resultA = new Array();
				for(var i = 0 ; i< result.length; i++){
					resultA[i]=result[i].id;
				}
				window.location="ActiveBusAlarm.html?result="+resultA+"";	
			}
		} catch(e) {
			alert(e);
		} 	
	}
	
	/**
 	 * @大客户告警页面
 	 * @展现搜索结果展现
	 */
	function showSearchResultCallBack(result,exception){
		if(exception){
			alert("大客户告警页面在从后台取数据的时候发生异常："+exception);			
		}
		try {
			/*result = toJSON(result);
			document.write(result);*/
			result = result.list;
			renderSearchResult(result,exception);
		} catch(e) {
			alert(e);
		}
	}
//***********************************2 层*******************************************
 	function customerAlarmsPreview(){
 		try {
			jsonRpc.ManagerService.getAlarmsCustomerList(cusWithAlaInfoCallBack , 0);//page 0
		} catch(e) {
			alert(e);
		} 		
 	}
 	
 	
 	
	/**
	 * 
	 */
	function exhibitCustomerList(){
		var radioE=document.getElementById("exhibit");
		try {
			if(count==0){
				/**选择展现所有的大客户信息*/
				radioE.src="img/radio_chk1.gif";
				count=(count+1)%2;
				jsonRpc.ManagerService.getAllCustomerList(cusWithAlaInfoCallBack , 0);
			}
			else if(count==1){
				/**不展现所有的大客户信息*/
				radioE.src="img/radio_chk0.gif";
				count=(count+1)%2;
				removeExtraInfo();
			}			
		} catch(e) {
			alert(e);
		}
		
	}
 	
 
 	/**
 	 * @显示某个大客户的详细信息
 	 */
 	function showCusInfo(rows){
		try {
			if(rows.length>1){
				alert("出错，您选择了多行！");
				return;
			}
			for(var i = 0; i < rows.length ; i++){
				var customerID = rows[i].key;
				alert("您想显示的大客户Id是 "+customerID);
				jsonRpc.ManagerService.getCustomerInfo(showCusInfoCallBack , customerID);			
			}
			
		} catch(e) {
			alert(e);
		}
 	}
 	
 	/**
 	 * 修该大备注信息
 	 */
 	function modifyCusInfo(rows){
 		try {
			if(rows.length>1){
				alert("出错，您选择了多行！");
				return;
			}
			
			for(var i = 0; i < rows.length; i++){
				var customerID = rows[i].key;
				var hiden_alarmId = document.getElementById("hiden_DescInfoDialog");
				hiden_alarmId.value=customerID;
				jsonRpc.ManagerService.getCustomerInfo(cusInfoModifyCallBack, customerID);		
			}
		} catch(e) {
			alert(e);
		}
 		
 	}
 	
 	function showBizAlarm(rows){
 		try {
//			jsonrpc = new JSONRpcClient(plguinRpc);
			
			if(rows.length>1){
				alert("出错，您选择了多行！");
				return;
			}
			
			for(var i = 0; i < rows.length ; i++){
				var customerID = rows[i].key;
//				var customerID = rows[i].cells[0].childNodes[0].id;
				alert("现在您正在操作id为: "+customerID+" 的业务告警");
				jsonRpc.ManagerService.getAlarm(showBizAlarmCallBack , customerID);				
			}
		} catch(e) {
			alert(e);
		}
 		
 	}
 	
 	/**
 	 * @大客户告警页面
 	 * @展现搜索结果展现
 	 */
 	function showSearchBinsAlarmResult(){
 		try {			
			/**搜集searchBox中的内容*/
			var cusName = document.getElementById("searchBox_cusName").value;
			var cusLevel = document.getElementById("searchBox_cusLevel").value;
			/**如果是展现所有的大客户信息*/
			if(count==0){
				var isAll=0;//表示全部
				jsonRpc.ManagerService.getSearchResult(showSearchResultCallBack , cusName, cusLevel,isAll);				
			}
			else{/**如果是展现有告警的大客户信息*/
				var isAll=1;//表示部分
				jsonRpc.ManagerService.getSearchResult(showSearchResultCallBack , cusName, cusLevel,isAll);	
			}
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
		 * 展现用户详细信息
		 */
		commnTable.addFunc("显示用户详细信息",showCusInfo);
		
		/**
		 * 修改客户备注信息
		 */
		commnTable.addFunc("修改大客户备注",modifyCusInfo);
		/**
		 * @展现某个大客户的业务告警列表
		 */
		commnTable.addFunc("显示用户业务高警",showBizAlarm);
	}
	
	