dojo.require("dojo.collections.*");
dojo.require("dojo.fx.*");

dojo.require("dojo.widget.Button");

	//********************全局变量存储区**************************
	//每创建一行，就此ID增加1；
	var rowID=0;
	
	var customerID=null;
	
	//false=没有选中;
	var check=false;
	
	//修改客户备注信息用的临时存储变量
	var descValue=null;
	//*********************************************************
	
	/**
	 * 返回空间块中被选择的ID
	 */
	function getCusID(){
		return customerID;
		
	}
	
	/**
	 *当checkBox 被选择的时候，将customerID设置为选择的ID。
	 */
	function setCusID(id){	
		customerID=id;		
	}
	
	/**
	 * 数据协议: name;level;critical,major,minor,warning;
	 */
	/* 
	function createCell(value){
		var tdE=document.createElement("td");
		var textValue = document.createTextNode(value);
		tdE.appendChild(textValue);
		
		return tdE;
	}
	
	function createRow(rowElement){
		var trE= document.createElement("tr");
		
		//create checkBox
		var inputTag = document.createElement("input");
		inputTag.type="checkbox";
		inputTag.id=rowElement.ID;
//		alert("input id "+rowElement.ID);
		//点击checkBox,选择它所指代的行号；
//		inputTag.onclick=function (){setCusID(inputTag,rowElement.ID);}
		
		var tdEC=document.createElement("td");
		tdEC.appendChild(inputTag);
		
		trE.appendChild(tdEC);
		trE.appendChild(createCell(rowElement.name));
		trE.appendChild(createCell(rowElement.level));
		trE.appendChild(createCell(rowElement.critical));
		trE.appendChild(createCell(rowElement.major));
		trE.appendChild(createCell(rowElement.minor));
		trE.appendChild(createCell(rowElement.warning));
		
		return trE;
	}*/
	/**
	 * 
	 */
	function renderCusWithAlasResult(result){
		var ftable = dojo.widget.byId("table");
		for(var i = 0 ; i<result.length; i++){			
			var data = {
				"choose":"<input type=\"checkbox\" >" ,
				"name":result[i].name,
				"level":result[i].level,
				"critical":result[i].critical,
				"majory":result[i].major,
				"minial":result[i].minor,
				"warning":result[i].warning
				};
       		data.key=result[i].ID;
       		ftable.store.addData(data , result[i].ID);
			
//			var rowTR=createRow(result[i]);
//			//每次创建完一行，就将行ID表识加一
//			tableBody.appendChild(rowTR);
		}
	}
	
	
	/**
	 * @大客户告警: 展现全部大客户列表==>删除展现的大客户列表
	 */
	function removeExtraInfo(){
		var ftable = dojo.widget.byId("table");
		var objs = ftable.store.get();
		for(var i = objs.length-1 ; i>=0; i--){
			if(objs[i].src.critical==0&&objs[i].src.majory==0&&objs[i].src.minial==0&&objs[i].src.warning==0){
				ftable.store.removeData(objs[i].src);
			}			
		}
	}
	
	/**
	 * 
	 */
	 function renderModifyDialog(result){
	 	if(result){
	 		var name = document.getElementById("modifyDesc_cusName");
	 		name.innerHTML = result.name;
	 		var desc = document.getElementById("descText");
	 		desc.value = result.description;
	 	}
	 }
	 
//	/**
//	 * 展现修改备注信息的对话框
//	 */
//	function showDescModifyDialog(){
//		/**将customerID放到一个共享区域*/
//		var descInfoDialog = dojo.widget.byId("descInfoDialog");
//		var descBtn = document.getElementById("descButton");
//		descInfoDialog.setCloseControl(descBtn);
//		descInfoDialog.show();
//	}
	

	
	/**
	 * 单纯的为form添加数值
	 */
	function setBAForm(result){
		var num = result[0];//alarm list 的长度;
		alert(num);
		var data = result[1];
		var nameE = document.getElementById("alarmName");
		nameE.innerHTML = data.name;
		var alarmIdE = document.getElementById("alarmId");
		alarmIdE.innerHTML = data.alarmId;
		var vendorAlarmIdE = document.getElementById("vendorAlarmId");
		vendorAlarmIdE.innerHTML = data.vendorAlarmIdE;
		var vendorAlarmNameE = document.getElementById("vendorAlarmName");
		vendorAlarmNameE.innerHTML = data.vendorAlarmName;
		var alarmTypeE = document.getElementById("alarmType");
		alarmTypeE.innerHTML = data.alarmType;
		var probleCauseE = document.getElementById("probleCause");
		probleCauseE.innerHTML = data.probleCause;
		var specialProblemE = document.getElementById("specialProblem");
		specialProblemE.innerHTML = data.specialProblem;
		var perceivedSeverityE = document.getElementById("perceivedSeverity");
		perceivedSeverityE.innerHTML = data.perceivedSeverityE;
		var raisedTimeE = document.getElementById("raisedTime");
		raisedTimeE.innerHTML = data.raisedTime;
		var ackUserIdE=document.getElementById("ackUserId");
		ackUserIdE.innerHTML = data.ackUserId;
		var ackTimeE = document.getElementById("ackTime");
		ackTimeE.innerHTML = data.ackTime;
		var alarmSourceE = document.getElementById("alarmSource");
		alarmSourceE.innerHTML = data.alarmSource;
		var additionalInfoE = document.getElementById("additionalInfo");
		additionalInfoE.innerHTML = data.additionalInfo;
		var customerInfoE = document.getElementById("customerInfo");
		var idValue="大客户的ID有";
		for(var i = 0 ; i <data.customerInfo.list.length; i++){
			idValue= idValue+","+data.customerInfo.list[i]
		}
		customerInfoE.innerHTML = idValue;
		var rescInfoE = document.getElementById("rescInfo");
		rescInfoE.innerHTML = data.rescInfo;
		var slaInfoE = document.getElementById("slaInfo");
		slaInfoE.innerHTML = data.slaInfo;
		var memoE = document.getElementById("memo");
		memoE.innerHTML = data.memo;
		var locationE = document.getElementById("location");
		locationE.innerHTML = data.location;
		var recordedTimeE = document.getElementById("recordedTime");
		recordedTimeE.innerHTML = data.recordedTime;
		
		/**如果只有一条记录,则下一页的显示标签显示不能操作*/
		var next = document.getElementById("BinsAlarmButton_next");
		if(num==1){
			next.disabled="false"; 
			next.hidefocus="false"
		}
	}
	
	//----------------------------------点击下一页的操作------------------------------
	/**
	 * 
	 */
	function renderLeftBinsAlarm(result,exception){
		if(result){
			setBAForm(result);
			var BinsAlarmDialog = dojo.widget.byId("binsAlarmInfoDialog");
			BinsAlarmDialog.show();
			
		}
	}

//-----------------------------------------搜索功能-----------------------------------
	/**
	 * @取得大客户告警页面中的搜索栏输入的用户姓名
	 */
//	function getCusName(){
//		var cusName = document.getElementById("searchBox_cusName");
//		return cusName.value;
//		
//	}
	
	/**
	 * @取得大客户告警页面中的搜索栏输入的用户级别
	 */
//	function getCusLevel(){
//		var cusLevel = document.getElementById("searchBox_cusLevel");
//		return cusLevel.value;
//		
//	}
	
	
	function setSearchResult(result){
//		var searchNameE = document.getElementById("searchName");
//		searchNameE.innerHTML=result.name;
//		var searchLevelE = document.getElementById("searchLevel");
//		searchLevelE.innerHTML=result.level;
//		var searchCriticalE = document.getElementById("searchCritical");
//		searchCriticalE.innerHTML=result.critical;
//		var searchMajorE = document.getElementById("searchMajor");
//		searchMajorE.innerHTML=result.major;
//		var searchMinorE = document.getElementById("searchMinor");
//		searchMinorE.innerHTML=result.minor;
//		var searchWarningE = document.getElementById("searchWarning");
//		searchWarningE.innerHTML=result.warning;

		var ctable = dojo.widget.byId("alarmCommonTable");
		var objs = ctable.getSelectedObjs();
		/**清楚表单，但不等于删除数据*/
		ctable.__tableWidget.store.clearData();
		renderCusWithAlasResult(result);
		
	}
	
	
	function renderSearchResult(result , exception){
		//alert(result);
		if(result){
			
			/**此处要做的业务是清楚table中的所有数据，然后将搜索到的数据添加到数据模型中*/
			setSearchResult(result);				
			
//			var showSearchResult = dojo.widget.byId("showSearchResult");
//			showSearchResult.show();
//			var okBtn = dojo.widget.byId("showSearchResult_ok");
//			showSearchResult.setCloseControl(okBtn);			
		}		
	}
	