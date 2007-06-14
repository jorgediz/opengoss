
	function findSysLogRender(logs){
		SystemLog.ftable = dojo.widget.byId("ftable");		
		clearTable();
		insertRows(logs);
	}
	
	function initRender(logs){
		SystemLog.ftable = dojo.widget.byId("ftable");
		insertRows(logs);
	}
	
	function findLogRender(logs){
		SystemLog.ftable = dojo.widget.byId("ftable");		
		clearTable();
		insertRows(logs);
	}
	
	function clearTable(){
		SystemLog.ftable.store.clearData();
	}	
	
	function insertRows(logs){
		for(var index=0; index<logs.length; index++) {
			var log = logs[index];
			var data ={
				level:log.level,
				time:log.time.time,
				result:log.result,
				description:log.description,
			}
			SystemLog.ftable.store.addData(data,log.id);
		}
	}