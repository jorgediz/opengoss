/**
 * @author hurr
 */
function getAckRuleInitCallBack(result,exception) {
	try {
		if(result){
			var filtertable = dojo.widget.byId("AckTable");
			for(var i = 0 ; i<result.list.length; i++){
				var info = result.list[i];
				var data = {
					id:"<input type=\"checkbox\"/>",
					name:info.name,
					key:info.id
				}
				filtertable.store.addData(data,info.id);
			}
		}
	} catch(exception) {
		alert(exception);
	} 	 
}
function addAckRuleCallBack(result,exception) {
	try{
		if(result){
			var addDlg = dojo.widget.byId("newDialog");
			addDlg.hide();
			
			var filtertable = dojo.widget.byId("AckTable");
			var data = {
				id:"<input type=\"checkbox\"/>",
				name:result.name,
				key:result.id
			}
			filtertable.store.addData(data,result.id);
		}
	} catch(exception) {
		alert(exception);
	}
}
function deleteAckRuleCallBack(result,exception,ruleIds) {
	try {
		if(result){
			var ftable = dojo.widget.byId("AckTable");
			for (var i=0; i < ruleIds.length; i++) {
				var obj = ftable.store.getDataByKey(ruleIds[i]);
				ftable.store.removeData(obj);
			}
			alert("delete " + ruleIds.length + "item(s) already");
		}
		} catch(exception) {
			alert(exception);
		} 	 
}
function fillAckUpdateCallBack(result,exception) {
	try{
		if (result) {
			var nameLabel = document.getElementById("updateName");
			nameLabel.innerHTML = result.name;
			nameLabel.setAttribute("ruleId",result.id);
		
			var operatorSelect = document.getElementById("updateCustomer");
			for (var i = 0; i < operatorSelect.length; i++) {
				if (operatorSelect.options[i].value == result.ownerId) {
					operatorSelect.selectedIndex = i;
				}
			}
		
			var sql = result.ruleCondition;
			var toExact = sql.split("where");// where clause only one condition
			var severityConditions = toExact[1].split("=");//
			var severity = severityConditions[1];	
			var severitySelect = document.getElementById("updateSeverity");
			for (var i = 0; i < operatorSelect.length; i++) {
				if (severitySelect.options[i].value == severity) {
					severitySelect.selectedIndex = i;
				}
			}
		
			var updateDlg = dojo.widget.byId("updateDialog"); 
			updateDlg.show();
		}
	} catch(exception) {
		alert(exception);
	}
}
function updateAckRuleCallBack(result,exception) {
	try{
		if(result) {
			var updateDlg = dojo.widget.byId("updateDialog"); 
			updateDlg.hide();
		}
	} catch(exception) {
		alert(exception);
	}
}