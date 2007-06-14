	dojo.require("dojo.widget.Dialog");

//********************全局变量存储区**************************
	//每创建一行，就此ID增加1；
	var rowID=0;
	
	var alarmIDList = new Array();
	//false=没有选中;
	var checkList = new Array();

	var plguinRpc = "/alarmmgr/JSON-RPC";
//*********************************************************

	function doNothing(result , exception){
		
	}
/**==============================对CheckBox的操作====================*/
	/**
	 * 返回选中的条目id链表
	 * 获取已经选择的告警ID
	 */
	function getAlarmID(){
		/**如果没有选中的条目，则=====>*/
		if(checkList){
			return null;
		}
		
		/**如果有选中的条目，则====>*/
		var arrayReturn = new Array();
		var alarmIDListTemp = alarmIDList;
		/**id与check一个个对比*/
		var temp = alarmIDListTemp.pop();
		alert("第一次: "+temp);
		while(temp){
			if(checkList[temp]){
				arrayReturn.push(temp);	
				alert("第二次:"+temp);
			}
			temp=alarmIDListTemp.pop();
		}
		
		return arrayReturn;
	}
	
	
	function setAlarmID(tag,id){
		if(tag.checked){//如果此条目被选中
			if(alarmIDList){
				alarmIDList = new Array()
			}
			if(checkList){
				checkList=new Array();
			}
			alarmIDList[id]=id;
			checkList[id]=true;//表示此ID的状态为‘checked’;
		}
		else{
			checkList[id]=false;			
		}
	}
	
/**==================================================================================over*/
	function setAcAlarmForm(data){
		var nameE = document.getElementById("alarmName");
		nameE.value = data.name;
		var alarmIdE = document.getElementById("alarmId");
		alarmIdE.value = data.alarmId;
		var vendorAlarmIdE = document.getElementById("vendorAlarmId");
		vendorAlarmIdE.value = data.vendorAlarmIdE;
		var vendorAlarmNameE = document.getElementById("vendorAlarmName");
		vendorAlarmNameE.value = data.vendorAlarmName;
		var alarmTypeE = document.getElementById("alarmType");
		alarmTypeE.value = data.alarmType;
		var probleCauseE = document.getElementById("probleCause");
		probleCauseE.value = data.probleCause;
		var specialProblemE = document.getElementById("specialProblem");
		specialProblemE.value = data.specialProblem;
		var perceivedSeverityE = document.getElementById("perceivedSeverity");
		perceivedSeverityE.value = data.perceivedSeverityE;
		var raisedTimeE = document.getElementById("raisedTime");
		raisedTimeE.value = data.raisedTime;
		var ackUserIdE=document.getElementById("ackUserId");
		ackUserIdE.value = data.ackUserId;
		var ackTimeE = document.getElementById("ackTime");
		ackTimeE.value = data.ackTime;
		var alarmSourceE = document.getElementById("alarmSource");
		alarmSourceE.value = data.alarmSource;
		var additionalInfoE = document.getElementById("additionalInfo");
		additionalInfoE.value = data.additionalInfo;
		var rescInfoE = document.getElementById("rescInfo");
		rescInfoE.value = data.rescInfo;
		var slaInfoE = document.getElementById("slaInfo");
		slaInfoE.value = data.slaInfo;
		var memoE = document.getElementById("memo");
		memoE.value = data.memo;
		var locationE = document.getElementById("location");
		locationE.value = data.location;
		var recordedTimeE = document.getElementById("recordedTime");
		recordedTimeE.value = data.recordedTime;
	}


//-----------------------------------------------------------------------------------------------
	function setmodifyDialogForm(data){
		var alarmIdE = document.getElementById("modify_alarmId");
		alarmIdE.innerHTML = data.name;
		var memoE = document.getElementById("modify_memo");
		memoE.value = data.memo;
	}





//	function createRow(rowElement){
//		var trE= document.createElement("tr");
//		
//		//create checkBox
//		var inputTag = document.createElement("input");
//		inputTag.type="checkbox";
//		inputTag.id=rowElement.id;
////		alert(inputTag.id);
//		//点击checkBox,选择它所指代的行号；
//		//inputTag.onclick=function (){setAlarmID(inputTag,rowElement.alarmId);}
//		
//		var tdEC=document.createElement("td");
//		tdEC.appendChild(inputTag);
//		
//		trE.appendChild(tdEC);
//		trE.appendChild(createCell(rowElement.name));
//		trE.appendChild(createCell(rowElement.alarmType));
//		var perceivedSeverityValue;
//		switch(rowElement.perceivedSeverity){
//			case 0: perceivedSeverityValue="Cleared";trE.style.backgroundColor="#00FF00";break;
//			case 1: perceivedSeverityValue="Indeterminate";trE.style.backgroundColor="#C0C0C0";break;
//			case 2: perceivedSeverityValue="Warning";trE.style.backgroundColor="#00FFFF";break;
//			case 3: perceivedSeverityValue="Minor";trE.style.backgroundColor="#FF7F00";break;
//			case 4: perceivedSeverityValue="Major";trE.style.backgroundColor="#FFFF00";break;
//			case 5: perceivedSeverityValue="Critical";trE.style.backgroundColor="#FF0000";
//		}
//		
//		trE.appendChild(createCell(perceivedSeverityValue));
//		trE.appendChild(createCell(rowElement.vendorAlarmId));
//		trE.appendChild(createCell(rowElement.alarmSource));
//		trE.appendChild(createCell(rowElement.memo));
//		
//		return trE;
//	}


//------------活动业务告警的初始展现-----------------------------------
	function renderbusAlarmsPreResult(result,exception){
		if(result){
			
			var ftable = dojo.widget.byId("table");
			for(var i = 0 ; i<result.length; i++){
				var ps;
				switch(result[i].perceivedSeverity){
					case 5 : ps="Critical";break;
					case 4 : ps="Major"; break;
					case 3 : ps="Minor"; break;
					case 2 : ps="Warning";break;
					case 1 : ps="Indeterminate";break;
					case 0 : ps="Cleared";break;
					default: alert("在做告警级别转换的时候出现错误！");
				}
				
				var data = {
					id:"<input type=\"checkbox\"\>" ,
					alarmName:result[i].name,
					alarmType:result[i].alarmType,
					perceivedSeverity:ps,
					vendorAlarmId:result[i].vendorAlarmId,
					alarmSource:result[i].alarmSource,
					memo:result[i].memo
					};
	       		data.key=result[i].id;
		
	       		ftable.store.addData(data , result[i].id);
			}		
		}
	}
	
	function renderTableByOneData(result,exception){
		if(result){
			var resultArray = new Array();
			resultArray[0]=result;
			renderbusAlarmsPreResult(resultArray , exception);
//			var tableBody = document.getElementById("tableBody");
//			var rowTR=createRow(result);
//			//每次创建完一行，就将行ID表识加一
//			rowID++;
//			tableBody.appendChild(rowTR);
		}
	}
	
	
	/**
	 * 搜集修改的告警信息,目前只支持修改告警的备注信息
	 */
	function collectAndSendInfo(){
		/**设置OK退出的按钮的时候出了点小问题*/
		var modifyBADialog = dojo.widget.byId("modifyBusAlarmDialog");
		var btnOK = document.getElementById("modifyBusAlarm_OK");
		modifyBADialog.setCloseControl(btnOK);
		/************************************/
		
		var memoE = document.getElementById("modify_memo");
		var memo = memoE.value;/**搜集到的告警信息*/


		/**送到后台*/
		try {
			jsonrpc = new JSONRpcClient(plguinRpc);
			var ctable = dojo.widget.byId("alarmCommonTable");
//			filtertable.store.addData(data,info.id);
			var table = dojo.widget.byId("table");
			/**如果您选择了checkBox,则...*/
			var objs = ctable.getSelectedObjs();
			if(objs.length>1){
				alert("出错!您选择了多行!");
				return;
			}
//			for (var i=0; i < objs.length; i++) {
				var hiden_alarmId = document.getElementById("hiden_DescInfoDialog");
				var alarmId = hiden_alarmId.value;
//				alert("The alarm id is "+alarmId);
//				var alarmId = objs[i].key;
				jsonrpc.ManagerService.modifyAlarmInfo(doNothing, alarmId , memo);
				obj=table.store.getDataByKey(alarmId);
//				alert("obj.name:"+obj.alarmName);
				/**先移去此行*/
				ctable.__tableWidget.store.removeData(obj);
				/**在用取得的值重新渲染表格*/
				var ps;
				
				var data = {
					id:"<input type=\"checkbox\"\>" ,
					alarmName:obj.alarmName,
					alarmType:obj.alarmType,
					perceivedSeverity:obj.perceivedSeverity,
					vendorAlarmId:obj.vendorAlarmId,
					alarmSource:obj.alarmSource,
					memo:memo
				};
				data.key=obj.key;
				var ftable = dojo.widget.byId("table");
				ftable.store.addData(data , obj.key);
//			}
			
		} catch(e) {
			alert(e);
		}
	}
	
	//--------------搜集searchBox中的数据-------------------------------------------------
	function getSearchCusName(){
		var name = document.getElementById("searchBox_cusName");
		return name.value;
		
	}
	
	function getSearchCusLevel(){
		var type = document.getElementById("searchBox_cusType");
		return type.value;
	}
	