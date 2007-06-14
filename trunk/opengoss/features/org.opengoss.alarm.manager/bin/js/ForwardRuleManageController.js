dojo.require("dojo.event.*");
dojo.require("dojo.widget.Dialog");


//********************全局变量存储区**************************
var plguinRpc = "/alarmmgr/JSON-RPC";

//******************************3 层************************
	function doNothing(result ,exception){
		alert("后台出错!Exception:"+exception);
	}

	/**
	 * 初始展现所有数据
	 */
	function previewForwardRuleCallBack(result ,exception){
		try{
			/*result = toJSON(result);
			document.write(result);*/
			result = result.list;
			if(result){
				renderForwardTableList(result);
			}
		}catch(e){
			alert("后台出错!Exception:"+exception);
		}
	}


	function modifyForwardRuleCallBack(result ,exception){
		try{
			/*result = toJSON(result);
			document.write(result);*/
			if(result){
				renderForwardDialog(result);
			}
		}catch(e){
			alert("后台出错!Exception:"+exception);
		}
	}
	
		
	function renderForwardRuleCallBack(result ,exception){
		if(exception){
			alert("exception : "+exception);			
		}
		try{
			/*result = toJSON(result);
			document.write(result);*/
			var re = new Array();
			re[0] = result;
			if(re){
				renderForwardTableList(re);
			}
		}catch(e){
			alert("后台出错!Exception:"+exception);
		}
	}
	
	function deleteForwardRuleCallBack(result ,exception){
		try{
			var rowBody = document.getElementById("tableBody");
			for(var i=rowBody.rows.length-1 ; i>0; i--){				
				var rIndex = rowBody.rows[i];
				if(rIndex.cells[0].childNodes[0].checked){
						alert("Now you are in the : "+i+" LINE");
						rowBody.deleteRow(i);
				}
			}
		}catch(e){
			alert("后台出错!Exception:"+exception);
		}
	}
//******************************2 层*********************************************
	/**
	 *  规则初始展现
	 */
	function previewRules(){
		try {
			/**写入数据库*/
			var jsonrpc = new JSONRpcClient(plguinRpc);
			jsonrpc.ManagerService.getAllForwordRules(previewForwardRuleCallBack, 0);//page 0
		} catch(e) {
			alert(e);
		}
	}
	
	/**
	 * 新建一条前转规则数据
	 */
	function newForwardRule(){
		/**每次新建的时候将隐藏的属性_value设置为null*/
		var hiden_ruleId = document.getElementById("hiden_DescInfoDialog");
		hiden_ruleId.value=null;
		var showForwardD = dojo.widget.byId("forwardDialog");
		var cancelBtn = document.getElementById("forwardDialog_cancel");
		showForwardD.setCloseControl(cancelBtn);
		showForwardD.show();	
	}

	
	
		/**
	 * 搜集新建规则对话框中的数据，然后发往后台整理为SQL语句
	 */
	function okButtonProcess(){
		var domN = document.getElementById("ruleName");
		var ruleNameStr = domN.value;

		var domC = document.getElementById("customer_name");
		var customerNameStr = domC.options[domC.selectedIndex].text;

		var domA = document.getElementById("bizAlarm_pSeverity");
		var alarmSeverityStr = domA.options[domA.selectedIndex].text;
		
		var domT = document.getElementById("ruleType");
		var ruleTypeStr = domT.options[domT.selectedIndex].text;
		
		var domP = document.getElementById("ruleContext");
		var ruleContextStr = domP.options[domP.selectedIndex].text;
		
		/**
		 * private Long id;
	
			private String name;//规则名称
			
			private String type;//规则类型,如：mail前转规则、短信前转规则等 $实际代码中就两种类型：自动确认规则和前转规则
			
			private String condition;//规则匹配条件,面向数据库的SQL语句
			
			private String context;//规则动作�的上下文,如短信前转规则需要的短信号码
			
			private String desc;//规则说明				 
		*/
		var ctable = dojo.widget.byId("alarmCommonTable");
		var objs = ctable.getSelectedObjs();
		var ruleId=null;/**如果是在修改某条规则，则id值不为0，如果是在新建某条规则则id为0*/
//		for (var i=0; i < objs.length; i++){
			/**如果选择了checkBox,则默认是在修改此条告警规则*/
			var hiden_ruleId = document.getElementById("hiden_DescInfoDialog");
			ruleId = hiden_ruleId.value;
//			ruleId = objs[i].key;
//		}
		
		if(ruleId==null){
			ruleId=0;
		}	
		
		var objectArray = new Object();
//		alert("1:"+ruleId+";2:"+ruleNameStr+";3:"+customerNameStr+";4:"+alarmSeverityStr+";5:"+ruleTypeStr+";6:"+ruleContextStr);
		objectArray["id"] = ruleId;//id
		objectArray["ruleName"] = ruleNameStr;//规则名称
		objectArray["customerName"] = customerNameStr;//客户名称
		objectArray["alarmSerceivedSeverity"] = alarmSeverityStr;//告警级别
		objectArray["ruleType"] = ruleTypeStr;//Email前转规则
		objectArray["ruleContext"] = ruleContextStr;//短信号码
		
//		var array = toJSON(objectArray);
		try {
			/**写入数据库*/
			var jsonrpc = new JSONRpcClient(plguinRpc);
//			jsonrpc.ManagerService.addForwardRule(renderForwardRuleCallBack, ruleId, ruleNameStr ,customerNameStr ,alarmSeverityStr,ruleTypeStr,ruleContextStr);
			jsonrpc.ManagerService.addForwardRule(renderForwardRuleCallBack, objectArray);
		} catch(e) {
			alert(e);
		}
	
	}

	function modifyForwardRule(objs){
		try {
			/**写入数据库*/
			var jsonrpc = new JSONRpcClient(plguinRpc);
			if(objs.length>1){
				alert("出错!您选择了多行修改");
				return;
			}
			for(var i = 0; i < objs.length ; i++){
				var ruleId = objs[i].key;
				var hiden_ruleId = document.getElementById("hiden_DescInfoDialog");
				hiden_ruleId.value=ruleId;
				jsonrpc.ManagerService.getForwordRuleById(modifyForwardRuleCallBack, ruleId);
			}
			
		} catch(e) {
			alert(e);
		}
	}
	
	/**
	 * 
	 */
	function deleteForwardRule(objs){
		try {
			/**写入数据库*/
			var jsonrpc = new JSONRpcClient(plguinRpc);
			for(var i = objs.length-1; i >=0; i--){
				var ruleId = objs[i].key;
				jsonrpc.ManagerService.deleteForwordRuleById(doNothing, ruleId);
				this.store.removeData(objs[i]);
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
		 * 新建前转规则规则
		 */
		commnTable.addFunc("新建",newForwardRule);
		
		/**
		 * 修改前转规则
		 */
		commnTable.setModifyFunc(modifyForwardRule , "修改");
		
		/**
		 * 删除某条前转规则规则
		 */
		commnTable.addFunc("删除",deleteForwardRule);
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