//require statements
dojo.require("dojo.widget.*" );
dojo.require("dojo.event.*");
dojo.require("dojo.widget.Dialog");

//all dojo.require above this line
dojo.hostenv.writeIncludes(); 
dojo.require();	

//********************全局变量存储区**************************
var plguinRpc = "/alarmmgr/JSON-RPC";

//******************************3 层************************
	/**
	 * 初始展现所有数据
	 */
	function previewAutonAckCallBack(result ,exception){
		try{
			/*result = toJSON(result);
			document.write(result);*/
			result = result.list;
			if(result){
				renderAutonAckTableList(result);
			}
		}catch(e){
			alert("后台出错!Exception:"+exception);
		}
	}

	function renderAutonAckCallBack(result ,exception){
		try{
			/*result = toJSON(result);
			document.write(result);*/
			if(result){
				renderAutonAckTable(result);
			}
		}catch(e){
			alert("后台出错!Exception:"+exception);
		}
		
	}
	
	function modifyRuleCallBack(result ,exception){
		try{
			/*result = toJSON(result);
			document.write(result);*/
			if(result){
				renderAutonAckDialog(result);
			}
		}catch(e){
			alert("后台出错!Exception:"+exception);
		}
	}
	
	/**
	 * 在界面的table中根据是否被选中判断是否应该删除
	 */
	function deleteRuleCallBack(result ,exception){
		try{			
			var rowBody = document.getElementById("tableBody");
			for(var i=rowBody.rows.length-1 ;i>=0; i--){
				var rIndex = rowBody.rows[i];
				if(rIndex.cells[0].childNodes[0].checked){
						rowBody.deleteRow(i);
				}
			}	
		}catch(e){
			alert("后台出错!Exception:"+exception);
		}
	}

//******************************2 层*********************************************
	function previewRules(){
		try {
			/**写入数据库*/
			var jsonrpc = new JSONRpcClient(plguinRpc);
			jsonrpc.ManagerService.getAllAutoRules(previewAutonAckCallBack, 0);//page 0
		} catch(e) {
			alert(e);
		}
	}
	
	function newAutoAcknowledge(){
		/**每次新建的时候将隐藏的属性_value设置为null*/
		var hiden_ruleId = document.getElementById("hiden_DescInfoDialog");
		hiden_ruleId.value=null;
		var showautoAcknowledge = dojo.widget.byId("autoAcknowledgeDialog");
		var cancelBtn = document.getElementById("autoAcknowledgeDialog_cancel");
		showautoAcknowledge.setCloseControl(cancelBtn);
		showautoAcknowledge.show();
	}
		
	/**
	 * 搜集新建规则对话框中的数据，然后发往后台整理为SQL语句
	 */
	function okProcess(){
		var domN = document.getElementById("ruleName");
		var ruleNameStr = domN.value;
		var domC = document.getElementById("customerS");
		var customerStr = domC.options[domC.selectedIndex].text;
		var domA = document.getElementById("alarmLevelS");
		var alarmStr = domA.options[domA.selectedIndex].text;

		try {
			/**写入数据库*/
			var jsonrpc = new JSONRpcClient(plguinRpc);
			var ctable = dojo.widget.byId("alarmCommonTable");
			var objs = ctable.getSelectedObjs();
			if(objs.length>1){
				alert("出错!您选择了多行!");
				return;
			}
			var id=null;
//			for (var i=0; i < objs.length; i++){
				/**如果选择了checkBox,则默认是在修改此条告警规则*/
				var hiden_ruleId = document.getElementById("hiden_DescInfoDialog");
				var id = hiden_ruleId.value;
//				alert("The id is "+id);
//				id = objs[i].key;
//			}
			jsonrpc.ManagerService.setAutoAcknowledgeAlarms(renderAutonAckCallBack,id ,ruleNameStr,customerStr,alarmStr);				
		} catch(e) {
			alert(e);
		}	
	}
	
	/**
	 * 修改自动确认规则
	 */
	function modifyAutoAcknow(rows){
		try {
			var jsonrpc = new JSONRpcClient(plguinRpc);
			if(rows.length>1){
				alert("出错!您选择了多行修改");
				return;
			}
			for(var i = 0; i < rows.length ; i++){
				var ruleId = rows[i].key;
//				alert("11 : "+ruleId);
				var hiden_ruleId = document.getElementById("hiden_DescInfoDialog");
				hiden_ruleId.value=ruleId;
				jsonrpc.ManagerService.getAutoAlarmRule(modifyRuleCallBack,ruleId);
			}
		} catch(e) {
			alert(e);
		}
	}
	
	function deleteAutoAcknow(rows){
		try {
			var jsonrpc = new JSONRpcClient(plguinRpc);		
			for(var i=0; i<rows.length  ; i++){
				var ruleId = rows[i].key;
				jsonrpc.ManagerService.deleteAutoAlarmRule(deleteRuleCallBack,ruleId);
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
		 * 修改自动确认规则
		 */
		commnTable.setModifyFunc(modifyAutoAcknow , "修改");
		
		/**
		 * 新建自动确认规则
		 */
		commnTable.addFunc("新建",newAutoAcknowledge);
		
		/**
		 * 删除某条自动确认规则
		 */
		commnTable.addFunc("删除",deleteAutoAcknow);
	}
	

	function init()
	{
		LoadViewWidget();
		/**
		 * 初始展现数据库中的所有规则
		 */
		previewRules();
	}
	
	dojo.addOnLoad(init);