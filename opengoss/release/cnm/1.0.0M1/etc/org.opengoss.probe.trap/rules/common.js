importPackage(java.util);
importPackage(java.lang);

//The main function that parses trap message.
function parseTrap(trapMsg, trapConfig, parseFunc) {
	var eventMsg = new HashMap();
	//Fields in trapConfig to eventMsg.
	for (var key in trapConfig) {
		eventMsg.put(key, trapConfig[key]); 
	}
	//Common fields in trapConfig to eventMsg.
	extractCommonFields(trapMsg, eventMsg);
	//Customize the parse process.
	parseFunc(trapMsg, eventMsg);
	return eventMsg;
}

function extractCommonFields(trapMsg, eventMsg) {
	eventMsg.put('trap_oid', trapMsg.get('trap_oid'));
	eventMsg.put('node_addr', trapMsg.get('node_addr'));
	eventMsg.put('received_time', trapMsg.get('received_time'));
	eventMsg.put('time_stamp', trapMsg.get('time_stamp'));
	eventMsg.put('trap_msg', trapMsg);
}
