		function getAlarmInitPageCallBack(result,exception) {
			try {
				if(result){
					var filtertable = dojo.widget.byId("alarmTable");
					for(var i = 0 ; i<result.list.length; i++){
						var info = result.list[i];
						var data = {
							id:"<input type=\"checkbox\"/>",
							name:info.name,
							alarmType:info.alarmType,
							perceivedSeverity:info.perceivedSeverity,
							vendorAlarmId:info.vendorAlarmId,
							alarmSource:info.alarmSource,
							memo:info.memo,
							key:info.id
						};
						filtertable.store.addData(data,info.id);
					}
				}
			} catch(exception) {
				alert(exception);
			} 	 
		}
		function getPageMaxNumCallBack(result,exception) {
			try {
				var pageNums = toJSON(result);
				if((pageNums) != null){
					var spinner = document.getElementById("pageNum");
					spinner.max = pageNums;
				}
				} catch(exception) {
					alert(exception);
				} 	 
		}
		function deleteAlarmCallBack(result,exception,keys) {
			try {
				if(result){
					var ftable = dojo.widget.byId("alarmTable");
					for (var i=0; i < keys.length; i++) {
						var obj = ftable.store.getDataByKey(keys[i]);
						ftable.store.removeData(obj);
					}
					alert("delete " + keys.length + " items already");
				}
				} catch(exception) {
					alert(exception);
				} 	 
		}
		function updateAlarmCallBack(result,exception,AlarmId,AlarmMemo) {
			try {
				if (result) {
					var dia = dojo.widget.byId("modifyForm");
					dia.hide();
					
					var ftable = dojo.widget.byId("alarmTable");
					var obj = ftable.store.getDataByKey(AlarmId);
					ftable.store.update(obj,"memo",AlarmMemo);
				}
				} catch(exception) {
					alert(exception);
				} 	 
		}
		function queryAlarmTypeCallBack(result,exception) {
			try{
				if(result){
					var filtertable = dojo.widget.byId("alarmTable");
					filtertable.store.clearData();
					for (var i = 0; i<result.list.length; i++){
						var info = result.list[i];
						var data = {
							id:"<input type=\"checkbox\"/>",
							name:info.name,
							alarmType:info.alarmType,
							perceivedSeverity:info.perceivedSeverity,
							vendorAlarmId:info.vendorAlarmId,
							alarmSource:info.alarmSource,
							memo:info.memo,
							key:info.id
						};
						filtertable.store.addData(data,info.id);
					}
				}
			} catch(exception) {
				alert(exception);
			}
		}
		function setAckUserIdCallBack(result,exception) {
			try {
				if (result) {
					alert("Acknowledge successful!");
				}
				} catch(exception) {
					alert(exception);
				} 	 
		}
		function setAckUserIdToDefaultCallBack(result,exception) {
			try {
				if (result) {
					alert("Unacknowledge successful!");
				}
				} catch(exception) {
					alert(exception);
				} 	 
		}
		function getAlarmDetailCallBack(result,exception) {
			try {
				if (result) {
					var detailDlg = dojo.widget.byId("detailForm");
					var lables = document.getElementsByTagName("label");
					
					for(var index = 0; index < lables.length; index++) {
						var alarmLabel = lables[index];
						var labelId = alarmLabel.getAttribute("id");
						if( labelId != null && result[labelId]!= null){
							alarmLabel.innerHTML = result[labelId];
						}
					}
					
					detailDlg.show();
				}
			} catch(exception) {
					alert(exception);
			} 
		} 
