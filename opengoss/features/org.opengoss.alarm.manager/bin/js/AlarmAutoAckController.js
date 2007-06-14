/**
 * @author hurr
 */
var AlarmAutoAckController = {
	pluginRpc:new JSONRpcClient("/Alarmmgr/JSON-RPC"),
	ownerId:1
}

dojo.addOnLoad(init);

function init() {
	commonTableInit();
	getAllAckRuleInfo();
}
function commonTableInit() {
	var form, button;
	form = dojo.widget.byId("newDialog");
	button = document.getElementById("addCancel");
	form.setCloseControl(button);
	form = dojo.widget.byId("updateDialog");
	button = document.getElementById("updateCancel");
	form.setCloseControl(button);
	var ctable = dojo.widget.byId("AckCommonTable");
	//Delete
	ctable.setDelFunc(function(objs){
		if(objs.length <= 0){
			alert("fail, no selected row.");
			return false;
		}
		var checkIds = new Array();
//		alert(objs.length);
		for(var i = 0; i < objs.length; i++){
			checkIds.push(objs[i].key);
		}
		deleteAckRule(checkIds);
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
		var ruleId = objs[0].key;
		fillAckUpdate(ruleId);
		return true;
	});
}
function getAllAckRuleInfo() {
	try {
			jsonrpc = AlarmAutoAckController.pluginRpc;			
			jsonrpc.AlarmRuleService.getAllAckRule(getAckRuleInitCallBack,AlarmAutoAckController.ownerId);
		} catch(e) {
			alert(e);
		}
}
function fillAckUpdate(ruleId) {
	try {
			jsonrpc = AlarmAutoAckController.pluginRpc;	
			jsonrpc.AlarmRuleService.getAckRule(fillAckUpdateCallBack,ruleId);
		} catch(e) {
			alert(e);
		}
}
function addAckRule() {
	try {
			var nameInput = document.getElementById("addName");
			var ruleName = nameInput.value;
			var operatorSelect = document.getElementById("addCustomer");
			var operator = operatorSelect.options[operatorSelect.selectedIndex].value;
			var severitySelect = document.getElementById("addSeverity");
			var severity = severitySelect.options[severitySelect.selectedIndex].value;
			
			jsonrpc = AlarmAutoAckController.pluginRpc;	
			jsonrpc.AlarmRuleService.addAckRule(addAckRuleCallBack,ruleName,operator,severity);
		} catch(e) {
			alert(e);
		}
}
function deleteAckRule(ruleIds) {
	try {
			jsonrpc = AlarmAutoAckController.pluginRpc;	
			jsonrpc.AlarmRuleService.deleteAckRule(function(result,exception) {
				deleteAckRuleCallBack(result,exception,ruleIds);
			},ruleIds);
		} catch(e) {
			alert(e);
		}
}
function updateAckRule() {
	try {
			var nameLabel = document.getElementById("updateName");
			var ruleId = nameLabel.getAttribute("ruleId");
			var operatorSelect = document.getElementById("updateCustomer");
			var operator = operatorSelect.options[operatorSelect.selectedIndex].value;
			var severitySelect = document.getElementById("updateSeverity");
			var severity = severitySelect.options[severitySelect.selectedIndex].value;
			
			jsonrpc = AlarmAutoAckController.pluginRpc;	
			jsonrpc.AlarmRuleService.updateAckRule(updateAckRuleCallBack,ruleId,operator,severity);
		} catch(e) {
			alert(e);
		}
}