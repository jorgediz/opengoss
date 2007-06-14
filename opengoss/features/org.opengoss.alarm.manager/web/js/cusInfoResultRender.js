
	dojo.require("dojo.collections.*");
	dojo.require("dojo.fx.*");
	dojo.require("dojo.widget.Dialog");
	dojo.require("dojo.widget.Button");
	//********************全局变量存储区**************************
	//每创建一行，就此ID增加1；
	var rowID=0;
	
	var customerID=null;
	
	var cusName = null;
	//false=没有选中;
	var check=false;
	
	var plguinRpc = "/alarmmgr/JSON-RPC";
	//*********************************************************
	/**
	 *当checkBox 被选择的时候，将customerID设置为选择的ID。
	 */
	function setCusID(tag,id){
		if(tag.checked){
			customerID=id;
		}
		check=tag.checked;
		
	}
	
	function setCusName(tag , n){
		if(tag.checked){
			cusName=n;
		}
		check=tag.checked;
	}
	
	/*function createCell(value){
		var tdE=document.createElement("td");
		var textValue = document.createTextNode(value);
		tdE.appendChild(textValue);
		
		return tdE;
	}
	
	function createRow(rowElement){
		var trE= document.createElement("tr");
		
		//create checkBox
		var inputTag = document.createElement("input");
		inputTag.type="radio";
		inputTag.name="radiobutton";
		inputTag.value="radiobutton"; 
		inputTag.id=rowElement.id;
//		alert(inputTag.id);
		
		var tdEC=document.createElement("td");
		tdEC.appendChild(inputTag);
		
		trE.appendChild(tdEC);
		trE.appendChild(createCell(rowElement.name));
		trE.appendChild(createCell(rowElement.level));
		trE.appendChild(createCell(rowElement.desc));
		return trE;
	}*/

	/**
	 * 展现大客户的详细信息，初始展现单个属性：name , level ,desc
	 */
	function renderCusInfo(result,exception){
		var ftable = dojo.widget.byId("table");
		for(var i = 0 ; i<result.length; i++){
			var data = {
				choose:"<input type=\"checkbox\" id="+result[i].id+"\>" ,
				name:result[i].name,
				level:result[i].level,
				desc:result[i].description
				};
       		data.key=result[i].id;
	
       		ftable.store.addData(data , result[i].id);
		}
	}
	
	/**
	 * 返回空间块中被选择的ID
	 */
	function getCusID(){
		if(!check){
			customerID=null;		
		}
		return customerID;		
	}
	
	/**
	 * 获取当前选中的顾客姓名
	 */
	function getCusName(){
		if(!check){
			cusName=null;		
		}
		return cusName;
	}
	
	function setOneCusInfo(result){
		//放置用户的详细信息到table中
		var fullNameE = document.getElementById("fullName");
		fullNameE.innerHTML=result.fullname;
		var nameE=document.getElementById("cusName");
		nameE.innerHTML=result.name;
		var classE=document.getElementById("class");
		classE.innerHTML=result.customerClass;
		var operationTypeE=document.getElementById("operationType");
		operationTypeE.innerHTML=result.operationType;
		var timeE=document.getElementById("time");
		timeE.innerHTML=result.time;
		var levelE=document.getElementById("level");
		levelE.innerHTML=result.level;
		var phoneE = document.getElementById("phone");
		phoneE.innerHTML=result.phone;
		var mobileE=document.getElementById("mobile");
		mobileE.innerHTML=result.mobile;
		var corperatorE=document.getElementById("corperator");
		corperatorE.innerHTML=result.corperator;
		var addressE=document.getElementById("address");
		addressE.innerHTML=result.address;
		var informedE=document.getElementById("informed");
		informedE.innerHTML=result.informed;
		var messageE=document.getElementById("message");
		messageE.innerHTML=result.message;
		var emailE=document.getElementById("email");
		emailE.innerHTML=result.email;
		var regionE=document.getElementById("region");
		regionE.innerHTML=result.region;
		var circuitNoE=document.getElementById("circuitNo");
		circuitNoE.innerHTML=result.circuitNo;
		var circuitInfoE=document.getElementById("circuitInfo");
		circuitInfoE.innerHTML=result.circuitInfo;
		var faxE = document.getElementById("fax");
		faxE.innerHTML=result.fax;
		var descE=document.getElementById("desc");
		descE.innerHTML=result.description;
	}
	/**
	 * 展现某个大客户的详细信息
	 */
	function renderOneCusInfo(result,exception){
		setOneCusInfo(result);
		
		var cusInfoDialog = dojo.widget.byId("cusInfoDialog");
		var btn = document.getElementById("okButton");
		cusInfoDialog.setCloseControl(btn);
		cusInfoDialog.show();
		
	}
	
	//-----------------------------------------搜索功能-----------------------------------
	/**
	 * @取得大客户告警页面中的搜索栏输入的用户姓名
	 */
	function getSearchCusName(){
		var cusName = document.getElementById("searchBox_cusName");
		return cusName.value;
		
	}
	
	/**
	 * @取得大客户告警页面中的搜索栏输入的用户级别
	 */
	function getSearchCusLevel(){
		var cusLevel = document.getElementById("searchBox_cusLevel");
		return cusLevel.value;
		
	}
	
	//-------------------------展现搜索数据--------------------------------------------------------
	function setSearchResultOfCI(result){
		var searchNameE = document.getElementById("searchCusInfoDialog_cusName");
		searchNameE.innerHTML=result.name;
		var searchNameE = document.getElementById("searchCusInfoDialog_cusLevel");
		searchNameE.innerHTML=result.level;
		var searchNameE = document.getElementById("searchCusInfoDialog_cusAlarmInfo");
		searchNameE.innerHTML=result.desc;
		
	}
	
	
	function renderSearchResultOfCI(result,exception){
		if(result){
			var ctable = dojo.widget.byId("alarmCommonTable");
//			var objs = ctable.getSelectedObjs();
			/**清楚表单，但不等于删除数据*/
			ctable.__tableWidget.store.clearData();
			renderCusInfo(result);			
		}
			

		
		
//		if(result){
//			var size = result[0];
//			setSearchResultOfCI(result[1]);
//			if(size==0){
//				/**如果只有一条记录,则下一页的显示标签显示不能操作*/
//				var next = document.getElementById("searchCusInfo_next");
//				next.style.display="none";		
//				var tdE = document.getElementById("searchCusInfo_td");
//				tdE.colSpan="2";
//				tdE.align="center";
//			}
			
//			var showSearchResult = dojo.widget.byId("searchCusInfoDialog");
//			showSearchResult.show();
//			var okBtn = dojo.widget.byId("searchCusInfo_cancel");
//			showSearchResult.setCloseControl(okBtn);			
//		}
	}
	
	//--------------------------搜索数据下一页的操作------------------------
	
	function showCINextPage(){
		try {
			
			jsonrpc = new JSONRpcClient(plguinRpc);
			
			jsonrpc.ManagerService.getLeftCusInfo(renderLeftBinsAlarm);				
		
		} catch(e) {
			alert(e);
		}
	}
	
	/**
	 * @拿到后台的分页数据，然后展现
	 */
	function renderLeftBinsAlarm(result,exception){
		if(result){
			setLeftSearchResultOfCI(result);
			var searchCusInfoDialog = dojo.widget.byId("searchCusInfoDialog");
			searchCusInfoDialog.show();
		}
		
	}
	
	function setLeftSearchResultOfCI(result){
		var num = result[0];//alarm list 的长度;
		if(num==1){
			/**如果只有一条记录,则下一页的显示标签显示不能操作*/
			var next = document.getElementById("searchCusInfo_next");
			next.style.display="none";		
			var tdE = document.getElementById("searchCusInfo_td");
			tdE.colSpan="2";
			tdE.align="center";
		}
		var data = result[1];
		setSearchResultOfCI(data);
	}
	 