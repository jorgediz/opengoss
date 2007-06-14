	
	function initRender(logs){
		OperationLog.ftable = dojo.widget.byId("ftable");
		insertRows(logs);
	}
	
	function findLogRender(logs){
		OperationLog.ftable = dojo.widget.byId("ftable");		
		clearTable();
		insertRows(logs);
	}
	
	function clearTable(){
		OperationLog.ftable.store.clearData();
	}	
	
	function insertRows(logs){
		for(var index=0; index<logs.length; index++) {
			var log = logs[index];
			var data ={
				userName:log.userName,
				userIP:log.userIP,
				action:log.action,
				result:log.result,
				description:log.description,
			}
			OperationLog.ftable.store.addData(data,log.id);
		}
	}
	
