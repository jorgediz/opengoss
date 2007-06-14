/**
 * @author hurr
 */
function getForwardRuleInitCallBack(result,exception) {
	try {
		if(result){
			var filtertable = dojo.widget.byId("forwardTable");
			for(var i = 0 ; i<result.list.length; i++){
				var info = result.list[i];
				var data = {
					id:"<input type=\"checkbox\"/>",
					condition:info.ruleCondition,
					type:info.type,
					parameter:info.context,
					key:info.id
				}
				filtertable.store.addData(data,info.id);
			}
		}
	} catch(exception) {
		alert(exception);
	}
}
function addForwardRuleCallBack(result,exception) {
	try{
		if(result){
			var newDlg = dojo.widget.byId("newDialog");
			newDlg.hide();
			
			var filtertable = dojo.widget.byId("forwardTable");
			var data = {
				id:"<input type=\"checkbox\"/>",
				condition:result.ruleCondition,
				type:result.type,
				parameter:result.context,
				key:result.id
			}
			filtertable.store.addData(data,result.id);
		}
	} catch(exception) {
		alert(exception);
	}
}
function deleteForwardRuleCallBack(result,exception,ruleIds) {
	try {
		if(result){
			var ftable = dojo.widget.byId("forwardTable");
			for (var i=0; i < ruleIds.length; i++) {
				var obj = ftable.store.getDataByKey(ruleIds[i]);
				ftable.store.removeData(obj);
			}
			alert("delete " + ruleIds.length + " item(s) already");
		}
	} catch(exception) {
		alert(exception);
	} 	 
}
function fillForwardUpdateCallBack(result,exception) {
	try{
		if(result) {
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
			for (var i = 0; i < severitySelect.length; i++) {
				if (severitySelect.options[i].value == severity) {
					severitySelect.selectedIndex = i;
				}
			}
		
			var typeSelect = document.getElementById("updateType");
			for (var i = 0; i < typeSelect.length; i++) {
				if (typeSelect.options[i].value == result.type) {
					typeSelect.selectedIndex = i;
				}
			}
		
			var parameterInput = document.getElementById("updateParameter");
			parameterInput.value = result.context;
		
			var updateDlg = dojo.widget.byId("updateDialog"); 
			updateDlg.show();
		}
	} catch(exception) {
		alert(exception);
	}
}
function updateForwardRuleCallBack(result,exception,ruleId,severity,type,parameters) {
	try{
		if(result) {
			var updateDlg = dojo.widget.byId("updateDialog"); 
			updateDlg.hide();
			var ftable = dojo.widget.byId("forwardTable");
			var obj = ftable.store.getDataByKey(ruleId);
			var condition = "from Alarm alarm where where alarm.perceivedSeverity=" + severity;
			ftable.store.update(obj,"condition",condition);
			ftable.store.update(obj,"type",type);
			ftable.store.update(obj,"parameter",parameters);
		}
	} catch(exception) {
		alert(exception);
	}
}