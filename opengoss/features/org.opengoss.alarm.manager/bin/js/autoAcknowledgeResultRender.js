	
	
	//********************全局变量存储区**************************
	//每创建一行，就此ID增加1；
	var rowID=0;
	
	var checkID=null;
	
	//false=没有选中;
	var check=false;
	
	var returnRuleId=null;
	//**********************************************************
//	/**
//	 *返回从数据库取到的规则的id
//	 */
//	function getRuleId(){
//		return returnRuleId;
//	}
//	/**
//	 * 返回空间块中被选择的ID
//	 */
//	function getChooseID(){
//		if(!check){
//			checkID=null;		
//		}
//		return checkID;
//		
//	}
//	
//	/**
//	 *当checkBox 被选择的时候，将customerID设置为选择的ID。
//	 */
//	function setChooseID(tag,id){
//		if(tag.checked){
//			checkID=id;
//		}
//		check=tag.checked;
//		
//	}
//	
//	function createCell(value){
//		var tdE=document.createElement("td");
//		var textValue = document.createTextNode(value);
//		tdE.appendChild(textValue);
//		
//		return tdE;
//	}
//	
//	function createRow(rowElement){
//		var trE= document.createElement("tr");
//		
//		//create checkBox
//		var inputTag = document.createElement("input");
//		inputTag.type="checkbox";
//		inputTag.id="input_"+rowID;
//		inputTag.onclick=function (){setChooseID(inputTag,rowElement.id);}
//		var tdEC=document.createElement("td");
//		tdEC.appendChild(inputTag);
//		
//		trE.appendChild(tdEC);
//		trE.appendChild(createCell(rowElement.name));
//		
//		return trE;
//	}
	
	/**
	 * 根据result填充表格中的数据
	 * rule--一条告警的详细记录
	 */
	function renderAutonAckTable(rule){
		var id=null;
		var hiden_ruleId = document.getElementById("hiden_DescInfoDialog");
		id = hiden_ruleId.value;
//		alert("id = "+id);
//		var ctable = dojo.widget.byId("alarmCommonTable");
		var ftable = dojo.widget.byId("table");
//		alert(ftable);
//		var objs = ctable.getSelectedObjs();
		/**如果是在修改某条规则，则先删除这条规则*/
//		for (var i=0; i < objs.length; i++){
			/**如果选择了checkBox,则默认是在修改此条告警规则*/
//			id = objs[i].key;
			if(id){
				var obj=ftable.store.getDataByKey(id);
				ftable.store.removeData(obj);				
			}
//		}
		var result = new Array();
		result[0]=rule;
//		alert(rule.name);
		renderAutonAckTableList(result);		
	}
	
	function renderAutonAckTableList(result){
		var ftable = dojo.widget.byId("table");
		for(var i = 0 ; i<result.length; i++){			
		var data = {
			"id":"<input type=\"checkbox\" >" ,
			"name":result[i].name,
			};
   		data.key=result[i].id;
   		ftable.store.addData(data , result[i].id);		
		}
	}
	
	/**
	 * 根据result填充对话框中要显示的内容
	 * 从数据库中取到的sql转化为dialog中的数据有困难,选择其它方式处理
	 */
	function renderAutonAckDialog(result){
		var domN = document.getElementById("ruleName");
		domN.value = result.name;
		returnRuleId = result.id;
		var domC = document.getElementById("customerS");
		var domA = document.getElementById("alarmLevelS");
		var sqlStr = result.ruleCondition;
//		alert(sqlStr);
		/**解析sql语句*/
		var ab = sqlStr.split("where");
		var bcd = ab[1].split("and");
		for(var i = 0 ; i <bcd.length ; i++){
			var b = bcd[i].split("=");
//			alert(b[1]);
			switch(i){
				case 0: domC.options[domC.selectedIndex].text=b[1];break;
				case 1: domA.options[domA.selectedIndex].text=b[1];
			}
		}
		var showautoAcknowledge = dojo.widget.byId("autoAcknowledgeDialog");
		var cancelBtn = document.getElementById("autoAcknowledgeDialog_cancel");
		showautoAcknowledge.setCloseControl(cancelBtn);
		showautoAcknowledge.show();
	}
