	//********************全局变量存储区**************************
	//每创建一行，就此ID增加1；
	var rowID=0;
	
	var checkID=null;
	
	//false=没有选中;
	var check=false;
	
	var returnRuleId=null;
	//**********************************************************
	/**
	 *返回从数据库取到的规则的id
	 */
	/*function getRuleId(){
		return returnRuleId;
	}
	function getChooseID(){
		if(!check){
			checkID=null;		
		}
		return checkID;
		
	}*/
	
	/**
	 *当checkBox 被选择的时候，将customerID设置为选择的ID。
	 */
/*	function setChooseID(tag,id){
		if(tag.checked){
			checkID=id;
		}
		check=tag.checked;
		
	}
	
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
		
		inputTag.id = rowElement.id;
		
//		alert(inputTag.getAttribute("field"));
//		inputTag.onclick=function (){setChooseID(inputTag,rowElement.id);}
		var tdEC=document.createElement("td");
		tdEC.appendChild(inputTag);
		
		trE.appendChild(tdEC);
		trE.appendChild(createCell(rowElement.name));
		trE.appendChild(createCell(rowElement.type));
		trE.appendChild(createCell(rowElement.context));
		
		return trE;
	}*/
	
	//TODO...
	 function renderForwardTableList(result){
	 	var ctable = dojo.widget.byId("alarmCommonTable");
		var ftable = dojo.widget.byId("table");
		var objs = ctable.getSelectedObjs();
		/**如果是在修改某条规则，则先删除这条规则*/
		for (var i=0; i < objs.length; i++){
			/**如果选择了checkBox,则默认是在修改此条告警规则*/
			id = objs[i].key;
			ftable.store.removeData(objs[i]);			
		}
		
//        var ftable = dojo.widget.byId("table");
        for(var i = 0; i<result.length ; i++){
			var data = {
				"id":"<input type=\"checkbox\" >" ,
				"condition":result[i].name,
				"type":result[i].type,
				"parameter":result[i].context
				};
       		data.key=result[i].id;
       		ftable.store.addData(data , result[i].id);		
		}        
    }
	
	/**
	 * 初始化时候根据从后台传来的数据渲染整个table
	 */
	/*function renderForwardTableList(result){
		var tableBody = document.getElementById("tableBody");
		for(var i = 0; i<result.length ; i++){
			var rowTR=createRow(result[i]);

			tableBody.appendChild(rowTR);			
		}
	}*/
	
	/**
	 * 由从后台add完毕取到的AlarmRule去填充dialog
	 * @param AlarmRule的一个实际例
	 */
	function renderForwardDialog(result){
		var domN = document.getElementById("ruleName");
		domN.value = result.name;
		returnRuleId = result.id;
		var domC = document.getElementById("customer_name");
		var domA = document.getElementById("bizAlarm_pSeverity");
		var sqlStr = result.ruleCondition;
//		alert("sql:"+sqlStr);
		var ab = sqlStr.split("where");
		var bcd = ab[1].split("and");
		for(var i = 0 ; i <bcd.length ; i++){
			var b = bcd[i].split("=");
			switch(i){
				case 0: domC.options[domC.selectedIndex].text=b[1];break;
				case 1: domA.options[domA.selectedIndex].text=b[1];
			}
		}
		var typeE = document.getElementById("ruleType");
		if(result.type.match("mail前转规则")){
			typeE.options[typeE.selectedIndex].text = "Email";
			alert("前转类型为Email");
		}
		else if(result.type.match("短信前转规则")){
			typeE.options[typeE.selectedIndex].text = "短信";
			alert("前转类型为短信");
		}
		else{
			alert("There is wrong here 108 lines");
			return ;
		}
		
		var paraE = document.getElementById("ruleContext");
		paraE.options[paraE.selectedIndex].text = result.context;
		
		var showforwardDialog = dojo.widget.byId("forwardDialog");
		var cancelBtn = document.getElementById("forwardDialog_cancel");
		showforwardDialog.setCloseControl(cancelBtn);
		showforwardDialog.show();	
	}
	
	