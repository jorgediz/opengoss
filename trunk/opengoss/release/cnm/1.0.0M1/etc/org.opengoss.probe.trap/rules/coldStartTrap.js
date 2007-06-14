var ColdStartTrap = {
	alarm_severity: 5,
	alarm_type: 'EquipmentAlarm',
	probable_cause: 'Device is down...',
};

function parseColdStart(trapMsg, eventMsg) {
	print('begin to parse cold start trap...\r\n');
	eventMsg.put("alarm_key", trapMsg.get("node_addr") + "_ColdStart");
	return eventMsg;
}
