	dojo.require("dojo.widget.Dialog");	
	dojo.require("dojo.widget.ComboBox");	
	dojo.require("dojo.widget.*");	
	dojo.require("dojo.lfx.*");
	dojo.require("dojo.widget.CommonTable");
	dojo.hostenv.writeIncludes();
	//*************全局变量*****************
var mgr = {
	delRow:null,  //修改时选中的row
	modRow:null,
	addDlg:null,
	modifyDlg:null,
	id:0,
	ftable:null,
	jsonRpcClient:new JSONRpcClient("/Customer/JSON-RPC"),
	
}

//*****************业务功能******************

	//*************修改客户******************
	var customerData ={
		'customerData.id':0,
		'name':'simpleName',
		'fullName':'fullName',
		'level':0,
		'time':344565,
		'mobile':'1323456',
		'phone':'234556',
		'informed':true,
		'message':'message',
		'address':'pudong',
		'email':'mail',
		
		'region':'domain',
		'customerClass':'operationType',
		'corperator':'corperator',
		'region':'shanghai',
		'circuitNo':'345',
		'circuitInfo':'circuitInfo',
		'fax':'3945',
		'description':"desc",
		'linkMan':'linkMan',
	}
	
	function modifyCustomerHandler(result,exception){
		if (exception != null){
			alert(toJSON(exception));
		}
		if (result != null){
			modifyCustomerRender(result);
		}else{
			alert("no result");
		}
	}
	
	function modifyCustomer(){
			var binder = new FormBinder("name");
			binder.resolvers["ss"]= new SelectResolver();
			var data = 	binder.bindUIToData("modifyDialog");
			var time = dojo.widget.byId("time_modify");
			data.time = time.getDate();
//			alert(toJSON(data));
			data.id = mgr.id ;
			mgr.jsonRpcClient.customerMgr.update(modifyCustomerHandler,data);
			
	}
	
	function loadCustomerHandler(result , exception ){
		if (exception != null){
			alert(toJSON(exception));
		}
		if (result != null){
			loadCustomerRender(result);
		}else{
			alert("no result");
		}
	}
	
	function loadCustomer(id){
		
			customerData.id = id;
			mgr.id = id; //modify的时候使用到 
			mgr.jsonRpcClient.customerMgr.load(loadCustomerHandler,id);
			
	}

	//*************查询客户******************
	function findCustomerHandler(result , exception){
		if (exception != null){
			alert(toJSON(exception));
		}
//		alert(toJSON(result));
		findCustomerRender(result.list);
	}
	
	function findCustomer(){
			var binder = new FormBinder("name");	
			var searchData = binder.bindUIToData("searchFade");
//						alert(toJSON(searchData));
			mgr.jsonRpcClient.customerMgr.findCustomer(findCustomerHandler,searchData);
			
	}
	
	function delCustomerHandler(result , exception){
		if (exception != null){
			alert(toJSON(exception));
		}else{	
		  for (var i = 0; i < mgr.delRow.length; i++) {
		        mgr.ftable.store.removeData(mgr.delRow[i]);
		    }
		}

	}
	//*************删除客户******************
	function deleteCustomer(){
				var ids = [];
			 for (var i = 0; i < mgr.delRow.length; i++) {
         			   ids.push(mgr.delRow[i].key);
		        }
//				var id = mgr.delRow[0].key;
//				try{
//				jsonrpc = new JSONRpcClient("/Customer/JSON-RPC");
//				jsonrpc.deleteCustomer(delCustomerHandler,id);
//				}catch(e){
//					toJSON(e);
//				}
				mgr.jsonRpcClient.customerMgr.delCustomers(delCustomerHandler,ids);
		}
	
	//*************创建客户*****************


	function createCustomerHandler(result,exception){
		if (exception != null){
			alert(toJSON(exception));
		}
		createCustomerRender(result);
	}
	function createCustomer(){
			var binder = new FormBinder("name");
	        binder.resolvers["ss"]= new SelectResolver();
			var data = 	binder.bindUIToData("addDialog");
			var time = dojo.widget.byId("time_add");
			data.time = time.getDate();
//		    alert(toJSON(data));
			mgr.jsonRpcClient.customerMgr.create(createCustomerHandler,data);
			
	}
	
     	
	//*************页面初始化****************
	function init() {
		initDialog();
		loadCustomers();
	}
	function loadCustomersHandler(result , exception){
		if (exception != null){
			alert(toJSON(exception));
		}
		if (result != null){
			loadAllCustomersRender(result.list);
		}else{
			alert("no result");
		}
	}
	function loadCustomers(){
		try{
			var jsonrpc = new JSONRpcClient("/Customer/mgr");
			jsonrpc.customerMgr.loadAllCustomers(loadCustomersHandler);
		}catch(e){
			alert(toJSON(e));
		}
//		mgr.jsonRpcClient = new JSONRpcClient("/Customer/JSON-RPC");
//		mgr.jsonRpcClient.customerMgr.loadAllCustomers(loadAllCustomersHandler);
	}
	function initDialog(){
		
		mgr.addDlg  = dojo.widget.byId("addDialog");
		var btn = document.getElementById("addCancel");
		mgr.addDlg.setCloseControl(btn);
		mgr.modifyDlg = dojo.widget.byId("modifyDialog");
		var mClose = document.getElementById("modifyCancel");
		mgr.modifyDlg.setCloseControl(mClose);
		var ctable = dojo.widget.byId("commontable");
		
		ctable.setDelFunc(function(objs) {
			   if (objs.length <= 0) {
                    alert("fail, no selected row.");
                    return false;
                }
            mgr.delRow = objs;
            mgr.ftable = this;
            deleteCustomer();
			return true;
        });
		
		ctable.setModifyFunc(function(objs){
                if(objs.length != 1){
                    alert("fail, no selected row.");
                    return false;
                }
			mgr.modRow = objs[0];
			mgr.ftable = this ;
			var id = objs[0].key;   				
           	loadCustomer(id);
            return true;
            });
	}
	
//	 dojo.addOnLoad(init);