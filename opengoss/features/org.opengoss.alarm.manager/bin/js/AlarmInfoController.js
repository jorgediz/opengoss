/**
 * @author hurr
 */

var AlarmInfoController = {
	pluginRpc:new JSONRpcClient("/Alarmmgr/JSON-RPC")
};

//dep
var Alarm = {
	'id':0,
	'name':"alarm",
	'alarmKey':"key",
	'vendorAlarmId':"",
	'vendorAlarmName':"",
	'alarmType':"CommunicationAlarm",
	'probableCause':"",
	'specialProblem':"",
	'perceivedSeverity':"Major",
	'alarmRaisedTime':45,
	'alarmClearedTime':465,	
	'ackTime':45,	
	'ackUserId':45,
	'location':"",
	'alarmSource':"",
	'additionalInfo':"",
	'firstOccurence':"",
	'lastOccurence':"",
	'repeatNumbers':5,
	'memo':"",
	'recordedTime':321
};

dojo.addOnLoad(init);

function init() {
	commonTableInit();
	getAllAlarmInfo();
	//getPageSum();
}

function commonTableInit() {
	var form, button;
	form = dojo.widget.byId("modifyForm");
	var detailForm = dojo.widget.byId("detailForm");
	button = document.getElementById("updateCancel");
	var detailButton = document.getElementById("detailOk");
	form.setCloseControl(button);
	detailForm.setCloseControl(detailButton);
	var ctable = dojo.widget.byId("alarmCommonTable");
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
		deleteAlarm(checkIds);
	});
	// Update
	ctable.setModifyFunc(function(objs){
		if(objs.length <= 0){
			alert("fail, no selected row.");
			return false;
		} else	if(objs.length != 1){
			alert("fail! You should select only one row here.");
			return false;
		}
		var input = this.domNode.getElementsByTagName("input")[0];
		var memoCell = objs[0].memo;
		input.value = memoCell;
		input.id = objs[0].key;
		return true;
	});
	// Acknowledge
	ctable.addFunc("Acknowledge",acknowledgeAlarm);
	// Unacknowledge
	ctable.addFunc("Unacknowledge",unAcknowledgeAlarm);
	// Show Alarm detail 
	ctable.addFunc("Detail",detailAlarm);
}
/***
 * Acknowledge the Alarm,set the ackUserId
 * @param {Object} selectedRows
 */
function acknowledgeAlarm(selectedRows) {
	if(selectedRows.length <= 0){
			alert("fail, no selected row.");
			return false;
	}
	var alarmIds = new Array();
	for(var i = 0; i < selectedRows.length; i++){
		alarmIds.push(selectedRows[i].key);
	}
	try {
			jsonrpc = AlarmInfoController.pluginRpc;
			jsonrpc.AlarmInfoService.setAckUserId(setAckUserIdCallBack,alarmIds);
		} catch(e) {
			alert(e);
		}
}
/***
 * Unacknowledge the Alarm,set the ackUserId to default id
 * @param {Object} selectedRows
 */
function unAcknowledgeAlarm(selectedRows) {
	if(selectedRows.length <= 0){
			alert("fail, no selected row.");
			return false;
	}
	var alarmIds = new Array();
	for(var i = 0; i < selectedRows.length; i++){
		alarmIds.push(selectedRows[i].key);
	}
	try {
		jsonrpc = AlarmInfoController.pluginRpc;			
		jsonrpc.AlarmInfoService.setAckUserIdToDefault(setAckUserIdToDefaultCallBack,alarmIds);
		} catch(e) {
			alert(e);
		}
}
/***
 * Show the alarm's detail
 */
function detailAlarm(selectedRows) {
	if(selectedRows.length <= 0){
			alert("fail, no selected row.");
			return false;
	} else	if(selectedRows.length != 1){
		alert("fail! You should select only one row here.");
		return false;
	}
	var alarmId = selectedRows[0].key;
	try {
			jsonrpc = AlarmInfoController.pluginRpc;			
			jsonrpc.AlarmInfoService.getAlarmDetail(getAlarmDetailCallBack,alarmId);
	} catch(e) {
		alert(e);
	}
}

/***
 * initialize the table,to get all the Alarm.
 * addAlarmCallBack to render the result to show in the list
 * 0 is the first page
 * 5 is the table max list capacity
 *
 */
function getAllAlarmInfo() {
	try {
			jsonrpc = AlarmInfoController.pluginRpc;			
			jsonrpc.AlarmInfoService.getAlarmInfo(getAlarmInitPageCallBack);
		} catch(e) {
			alert(e);
		}
}
/**
 * to show the result max pageNum
 * getAlarmPagesCallBack return the max pageNum
 * 5 is the table max list capacity
 */
function getPageSum(){

			try {
					jsonrpc = AlarmInfoController.pluginRpc;
					jsonrpc.AlarmInfoService.pageCountSum(getPageMaxNumCallBack,5);
				} catch(e) {
					alert(e);
				}
}

/**
 * to delete the Alarm
 * deleteAlarmCallBack is render the result
 */
function deleteAlarm(alarmIds) {
	try {
			jsonrpc = AlarmInfoController.pluginRpc;	
			jsonrpc.AlarmInfoService.deleteAlarm(function(result,exception){
				deleteAlarmCallBack(result,exception,alarmIds);
			},alarmIds);
		} catch(e) {
			alert(e);
		}
}

/**
 * to update the Alarm
 * updateAlarmCallBack is render the result
 */
function updateAlarm() {
	var updateDialog = document.getElementById("modifyForm");
	var memoInput = updateDialog.getElementsByTagName("input")[0];
	var alarmId = memoInput.id;
	var alarmMemo = memoInput.value;
	try {
			jsonrpc = AlarmInfoController.pluginRpc;
			jsonrpc.AlarmInfoService.updateAlarmMemo(function(result,exception){
				updateAlarmCallBack(result,exception,alarmId,alarmMemo);
			},alarmId,alarmMemo);
		} catch(e) {
			alert(e);
		}
}

/**
 * to query the Alarms by AlarmType
 * queryAlarmCallBack is render the result
 * not yet sure the requirements
 */
function queryAlarmByType() {
	var queryDialog = document.getElementById("searchForm");
	var queryTypeInput = queryDialog.getElementsByTagName("input")[0];
	var alarmType = queryTypeInput.value;
	try {
			jsonrpc = AlarmInfoController.pluginRpc;
			jsonrpc.AlarmInfoService.queryAlarmByType(queryAlarmTypeCallBack,alarmType);
		} catch(e) {
			alert(e);
		}
}


