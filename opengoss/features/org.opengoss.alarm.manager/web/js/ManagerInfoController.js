
	pluginRpc:"/alarmmgr/JSON-RPC";
	
	function showResultInfo(result , exception){
		try{
			
		}catch(e){
			alert("出错！Exception:"+exception);
		}
	}
	
	function modifyInfo(){
		/**搜集修改信息*/
			var userName = document.getElementById("userName");
			var userNameValue = userName.value;
//			var oldPwd = document.getElementById("oldPwd");
//			var oldPwdValue = oldPwd.value;
//			var newPwd = document.getElementById("newPwd");
//			var newPwdValue = newPwd.value;
			var affirmPwd = document.getElementById("affirmPwd");
			var affirmPwdValue = affirmPwd.value;
			var conpany = document.getElementById("conpany");
			var conpanyValue = conpany.value;
			var partment = document.getElementById("partment");
			var partmentValue = partment.value;
			var cellPhone = document.getElementById("cellPhone");
			var cellPhoneValue = cellPhone.value;
			var telPhone = document.getElementById("telPhone");
			var telPhoneValue = telPhone.value;
			var address = document.getElementById("address");
			var addressValue = address.value;
			
			var obj = new Object();
			obj["name"] = userNameValue;
			obj["password"] = affirmPwdValue;
			obj["company"] = conpanyValue ;
			obj["mobile"] = cellPhoneValue ;
			obj["phone"] = telPhoneValue ;
			obj["department"] = partmentValue;
			
			try {
				jsonrpc = new JSONRpcClient(plguinRpc);				
				
				jsonrpc.PersonalInfoService.modifyInfo(showResultInfo , obj);				

			} catch(e) {
				alert(e);
			}
			
	}
	
	
	function init()
	{
		 var modifyE=document.getElementById("modify_ok");
		 dojo.event.connect(modifyE, "onClick","modifyInfo");
		 
	}
	
	dojo.addOnLoad(init);