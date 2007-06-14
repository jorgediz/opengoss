	dojo.require("dojo.widget.Spinner");
	dojo.require("dojo.widget.Dialog");	
	dojo.require("dojo.lfx.*");
	dojo.require("dojo.widget.CommonTable");
	dojo.hostenv.writeIncludes();

//*************全局变量*****************
var userMgr = {
	delRow:null,  //删除时选中的row
	modRow:null, //修改时选中的row
	addDlg:null, //创建用户的Dialog
	modifyDlg:null, //修改用户的Dialog
	ftable:null,//filtingTable
	isManager:null,
	jsonRpcClient:new JSONRpcClient("/Security/JSON-RPC"),
}
	//*********************业务功能*******************
		
		
		//*****************查询用户***************
	function findUserHandler(result , exception){
		if (exception != null){
			alert("search failed");
			alert(toJSON(exception));
		}else{
//			alert(toJSON(result.list));
			findUserRender(result.list)	;
		}
	}
	function findUser(){
		try {
			var binder = new FormBinder("name");
			var searchData = binder.bindUIToData("searchForm");
			userMgr.jsonRpcClient.userService.findUser(findUserHandler,searchData);
		} catch (e) { 
			alert(e);
		}
	}

     
		//*****************删除用户***************
		function deleteUserHandler(result,exception){
			if (exception != null){
				alert(toJSON(exception));
		}else{	
			  for (var i = 0; i < userMgr.delRow.length; i++) {
                    userMgr.ftable.store.removeData(userMgr.delRow[i]);
                }
			}
		}
		function deleteUser(){

			try{
			var ids = [];
			 for (var i = 0; i < userMgr.delRow.length; i++) {
         			   ids.push(userMgr.delRow[i].key);
		        }
				userMgr.jsonRpcClient.userService.delUsers(deleteUserHandler ,  ids );
			}catch(e){
				alert(e);
			}
		}
		
		//*****************创建用户***************
		var userData = { //默认用户数据
		id:0,
		name:'zou',
		password:'aaaaa',
		userName:'zouxc',
		mail:'alleninfo.com',
		address:'shanghai',
		mobile:'5900l1',
		phone:'02583455',
		company:'alleninfo',
		department:'software',
		userGroupInfo:'manager',
		selCustomers:{
						'javaClass':'java.util.ArrayList',
						'list':['Customer1','Customer2'],
			}
		};
		
	function createUserHandler(result,exception){
		if(exception != null){
			alert("has exception,ceate user failed");
			return ;
		}
		if (result ==null){
			alert("no result");
			return ;
		}else{
			createUserRender(result);
		}
	}
	function createUser(){

		var binder = new FormBinder("name");
		var selWidget = document.getElementById("selectrole");
		var options = selWidget.options;
		if(options[0].selected){ //manager selected
			binder.resolvers["ms"]= new MultipleSelectResolver();				
		}

        binder.resolvers["ss"]= new SelectResolver();
	    var data =   binder.bindUIToData("addDialog");
//	    alert(toJSON(data));
	    userMgr.jsonRpcClient.userService.create(createUserHandler,data);

	}
	
	
		//*****************修改用户***************
	var modifyData = {
		id:0,
		name:null,
		password:'aaaaa',
		userName:'zouxc',
		mail:'alleninfo.com',
		address:'shanghai',
		mobile:'15900l1',
		phone:'02583455',
		company:'alleninfo',
		department:'software',
		userGroupInfo:null,
		selCustomers:{
						'javaClass':'java.util.ArrayList',
						'list':['a','b'],
		}
	};
		function loadUserHandler(result , exception){
			if(exception != null){
			alert("has exception,load user failed");
				return ;
			}
			if (result ==null){
				alert("load user no result");
				return ;
				
			}else{
				loadUserRender(result);
			}
		}
		
		function loadUser(id){
			try{
			modifyData.id = id;
			userMgr.jsonRpcClient.userService.load(loadUserHandler,id);
			}catch(e){
				alert(e);
			}
		}
	
		function modifyUserHandler(result,exception){
			if(exception != null){
				alert("has exception");
			}else{
				modifyUserRender(result);
			}
		}
			
		function modifyUser(){
			var binder = new FormBinder("name");
			binder.resolvers["ms"]= new MultipleSelectResolver();
	        binder.resolvers["ss"]= new SelectResolver();
	        var data = binder.bindUIToData("modifyDialog");
			data.id = userMgr.modRow.key;
		userMgr.jsonRpcClient.userService.modify(modifyUserHandler,data);		
		}
		
	
			
	
	
	//*********************页面初始化*****************
	
		
		//***************加载用户*******************
		function loadUsersHandler(result, exception){
			if(exception != null){
			alert("has exception,load users failed");
				return ;
			}
				loadAllUserRender(result.list);
		}
		function loadUsers (){
			userMgr.jsonRpcClient.userService.loadAllUser(loadUsersHandler);
		}
		
		
		//***************加载资源*******************	
		
		function loadRescs(){
			var rescs  = userMgr.jsonRpcClient.userService.loadResources();
			return rescs.list;

		}	
		
		function init(){
			initDialog();
			loadUsers();
		}
		function initDialog(){
			userMgr.modifyDlg = dojo.widget.byId("modifyDialog");
			userMgr.modifyDlg.__modifyShow = userMgr.modifyDlg.show;
			userMgr.modifyDlg.show = function(){
				initResource("modifyDialog"); //读取后台customer信息
				this.__modifyShow();
				
			}
			var btn = document.getElementById("modifyCancel");
			userMgr.modifyDlg.setCloseControl(btn);
			userMgr.addDlg = dojo.widget.byId("addDialog");
			var addClose = document.getElementById("addCancel");
			userMgr.addDlg.setCloseControl(addClose);
			userMgr.addDlg.__modifyShow = userMgr.addDlg.show;
			
			userMgr.addDlg.show  = function (){
				initResource("addDialog");			
				this.__modifyShow();
			}
			
			var ctable = dojo.widget.byId("commontable");
			ctable.setDelFunc(function(objs) {
                if (objs.length <= 0) {
                    alert("fail, no selected row.");
                    return false;
                }
                
                userMgr.delRow  = objs;
                userMgr.ftable = this;
                deleteUser(	);
            });
			
			ctable.setModifyFunc(function(objs){
                if(objs.length != 1){
                    alert("fail, no selected row.");
                    return false;
                }
   				userMgr.modRow = objs[0];
   				userMgr.ftable = this ;
				var id = objs[0].key;   				
               	loadUser(id);
                return true;
            });
		}
	
	dojo.addOnLoad(init);
		
	//****************页面功能***********************
	function roleChange(selWidget){
		var options = selWidget.options;
		for(var i=0; i<options.length; i++) {
			var option = options[i];
			if(option.selected){
				if (option.value =="manager"){
					dojo.lfx.html.wipeIn("resc", 300).play();
				}else{
					dojo.lfx.html.wipeOut("resc", 300).play();
				}
				
			}
		}
	}	
	
	function initResource(which){//which is expected  "addDialog" or "modifyDialog"

		var  allResc = [];
		var rescs = loadRescs();
		for(var i=0; i<rescs.length; i++) {
			allResc.push(new Option(rescs[i],rescs[i],false,false));
		}
		
		var rescLength = allResc.length;
		
		var dialog = document.getElementById(which);
		var sel = dialog.getElementsByTagName("select");
		var all = sel[sel.length -2];//all rescs
		var sel = sel[sel.length -1 ];//selected rescs
		all.options.length = 0 ;
		sel.options.length = 0 ; //set selectedRescs null
		for(var index=0; index<rescLength; index++) {
			all.options[all.options.length] = allResc[index];
		}
	}
		
	function addSelected(which){ //which is expected  "addDialog" or "modifyDialog"
		var dialog = document.getElementById(which);
		var sel = dialog.getElementsByTagName("select");
		var all = sel[sel.length -2];//all customers
		var sel = sel[sel.length -1 ];//selected customers
		for(var index=0; index<all.options.length; index++) { //
			var option = all.options[index];
			if(option.selected){ 
				index --;
				option.selected = false;
				sel.options[sel.options.length] = option;
			}
		}
	}
 
 	function removeSelected(where){//which is expected "addDialog"  or "modifyDialog"
 		var dialog = document.getElementById(where);
		var sel = dialog.getElementsByTagName("select");
		var all = sel[sel.length - 2]; //all customers
		var sel = sel[sel.length - 1]; //selected customers
		for(var index=0; index<sel.options.length; index++) { //
			var option = sel.options[index];
			if(option.selected){ 
				index--;
				option.selected = false;
				all.options[all.options.length] = option;
			}
		}
 	}
 	
 	
 	