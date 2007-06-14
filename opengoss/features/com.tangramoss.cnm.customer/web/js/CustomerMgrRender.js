//***********************业务功能*****************
	//*******************修改客户*****************
	function modifyCustomerRender(result){
		//mgr.ftable hasn't store property????
		var ftable = dojo.widget.byId("table");
		var data = ftable.store.getDataByKey(result.id);
		var newData = transform(result);
		ftable.store.removeData(data);
		ftable.store.addData(newData,newData.key);
		mgr.modifyDlg.hide();

	}
	function loadCustomerRender(result){
		var binder = new FormBinder("name");
		 binder.resolvers["ss"]= new Select2Resolver(); //对select 修改 
		binder.bindDataToUI("modifyDialog",result);
		var time = dojo.widget.byId("time_modify");
		
		time.setDate(result.time.time);
	}
	
	
	//*******************查找客户*****************
	function findCustomerRender(list){
		displayOnTable(list);
	}


	
	//*******************删除客户*****************
	
	//*******************创建客户*****************
	function createCustomerRender(customer){
		var data = transform(customer);
		var ftable = dojo.widget.byId("table");
			ftable.store.addData(data,customer.id);
		mgr.addDlg.hide();
	}
	
//********************页面初始化******************
	function loadAllCustomersRender(customers){
		displayOnTable(customers);

	}
	
	function displayOnTable(customers){
		mgr.ftable = dojo.widget.byId("table");
		clearTable();
		for(var i=0; i<customers.length; i++) {
			var data = transform(customers[i]);
			insertRow(data);		
		}
	}
	
	function transform(customer){
		var data = {
				id:"<input type=\"checkbox\" />",
				name:customer.name,
				fullName:customer.fullName,
				time:customer.time.time,
				level:customer.level,
				email:customer.email,
				mobile:customer.mobile,
				phone:customer.phone,
				informed:customer.informed,
				message:customer.message,
				key:customer.id,
			}
		return data;	
	}
	function insertRow(data){
		
		mgr.ftable.store.addData(data,data.key);
	}
	function clearTable(){
		mgr.ftable.store.clearData();
	}
	