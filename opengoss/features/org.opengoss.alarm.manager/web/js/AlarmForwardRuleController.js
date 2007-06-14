/**
 * @author hurr
 */
var AlarmForwardRuleController = {
	pluginRpc:new JSONRpcClient("/Alarmmgr/JSON-RPC"),
	ownerId:1
};

dojo.addOnLoad(init);

function init() {
	commonTableInit();
	getAllForwardRuleInfo();
}

function commonTableInit() {
	var form, button;
	form = dojo.widget.byId("newDialog");
	button = document.getElementById("addCancel");
	form.setCloseControl(button);
	form = dojo.widget.byId("updateDialog");
	button = document.getElementById("updateCancel");
	form.setCloseControl(button);
	var ctable = dojo.widget.byId("forwardCommonTable");
	//Delete
	ctable.setDelFunc(function(objs){
		if(objs.length <= 0){
			alert("fail, no selected row.");
			return false;
		}
		var checkIds = new Array();
		for(var i = 0; i < objs.length; i++){
			checkIds.push(objs[i].key);
		}
		deleteForwardRule(checkIds);
		return true;
	});
	// Update not yet
	ctable.setModifyFunc(function(objs){
		if (objs.length <= 0) {
			alert("fail, no selected row.");
			return false;
		} else if (objs.length != 1) {
			alert("fail! You should select only one row here.");
			return false;
		}
		var checkId = objs[0].key;
		fillForwardUpdate(checkId);
		return true;
	});
}
function getAllForwardRuleInfo() {
	try {
			jsonrpc = AlarmForwardRuleController.pluginRpc;			
			jsonrpc.AlarmRuleService.getAllForwardRule(getForwardRuleInitCallBack,AlarmForwardRuleController.ownerId);
		} catch(e) {
			alert(e);
		}
}
function fillForwardUpdate(ruleId) {
	try {
			jsonrpc = AlarmForwardRuleController.pluginRpc;	
			jsonrpc.AlarmRuleService.getForwardRule(fillForwardUpdateCallBack,ruleId);
		} catch(e) {
			alert(e);
		}
}
function addForwardRule() {
	try {
			var nameInput = document.getElementById("addName");
			var ruleName = nameInput.value;
			
			var operatorSelect = document.getElementById("addCustomer");
			var operator = operatorSelect.options[operatorSelect.selectedIndex].value;
			
			var severitySelect = document.getElementById("addSeverity");
			var severity = severitySelect.options[severitySelect.selectedIndex].value;
			
			var typeSelect = document.getElementById("addType");
			var type = typeSelect.options[typeSelect.selectedIndex].value;
			
			var parameterInput = document.getElementById("addParameter");
			var parameters = parameterInput.value;
		
			jsonrpc = AlarmForwardRuleController.pluginRpc;	
			jsonrpc.AlarmRuleService.addForwardRule(addForwardRuleCallBack,ruleName,operator,severity,type,parameters);
		} catch(e) {
			alert(e);
		}
}
function deleteForwardRule(ruleIds) {
	try {
			jsonrpc = AlarmForwardRuleController.pluginRpc;	
			jsonrpc.AlarmRuleService.deleteForwardRule(function(result,exception) {
				deleteForwardRuleCallBack(result,exception,ruleIds);
			},ruleIds);
		} catch(e) {
			alert(e);
		}
}
function updateForwardRule() {
	try {
			var nameInput = document.getElementById("updateName");
			var ruleId = nameInput.getAttribute("ruleId");
			var operatorSelect = document.getElementById("updateCustomer");
			var operator = operatorSelect.options[operatorSelect.selectedIndex].value;
			var severitySelect = document.getElementById("updateSeverity");
			var severity = severitySelect.options[severitySelect.selectedIndex].value;
			var typeSelect = document.getElementById("updateType");
			var type = typeSelect.options[typeSelect.selectedIndex].value;
			var parameterInput = document.getElementById("updateParameter");
			var parameters = parameterInput.value;	
			
			jsonrpc = AlarmForwardRuleController.pluginRpc;	
			jsonrpc.AlarmRuleService.updateForwardRule(function(result,exception){
				updateForwardRuleCallBack(result,exception,ruleId,severity,type,parameters);
			},ruleId,operator,severity,type,parameters);
		} catch(e) {
			alert(e);
		}
}