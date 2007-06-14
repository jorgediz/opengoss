	//****************修改用户*******************
	function loadUserRender(data){
		data.password = ""; //暂时用这方法
		var binder = new FormBinder("name");
		if (data.roleInfo =="manager"){
			binder.resolvers["ms"]= new MultipleSelectResolver();
			if (userMgr.isManager != null && userMgr.isManager == "no"){
					dojo.lfx.html.wipeIn("rescModify", 300).play();					
			}
				userMgr.isManager = "yes";
		}else {
			if (data.roleInfo == "operator" ){
					if(	userMgr.isManager != null && userMgr.isManager == "yes"){
						dojo.lfx.html.wipeOut("rescModify", 300).play();
					}
				userMgr.isManager = "no";
			}
			
		}
        binder.resolvers["ss"]= new SelectResolver();
		binder.bindDataToUI("modifyDialog",data);

	}

	function modifyUserRender(userData){
		userMgr.modifyDlg.hide();
		userMgr.ftable = dojo.widget.byId("table");
		userMgr.ftable.store.removeData(userMgr.modRow);
		var data = transform(userData);
		insertRow(data);	
				  
	}
	//****************查询用户*******************
	function findUserRender(list){
		displayResultOnTable(list);

	}
	
	//****************创建用户*******************
	function createUserRender(userData){
		var data = transform(userData);
		userMgr.ftable = dojo.widget.byId("table");
		insertRow(data);
		userMgr.addDlg.hide();
	}
	
	//***************初始页面*******************
	
	function loadAllUserRender(users){
		displayResultOnTable(users);
	}
	
	function displayResultOnTable(result){
		userMgr.ftable = dojo.widget.byId("table");
		clearTable();
		for(var i=0; i<result.length; i++) {
			var data = transform(result[i]);
			insertRow(data);
		}
	}
	
	function clearTable(){
		userMgr.ftable.store.clearData();
	}
	function insertRow(data){
		userMgr.ftable.store.addData(data,data.key);
	}
	function transform(userData){
		var data = {
			id:"<input type=\"checkbox\"/>",
			name:userData.name,
			userName:userData.userName,
			mail:userData.mail,
			address:userData.address,
			mobile:userData.mobile,
			phone:userData.phone,
			company:userData.company,
			department:userData.department,
			key:userData.id
		}
		return data;	
	}
