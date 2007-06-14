var WarmStartTrap = {
	alarm_severity: 5,
	alarm_type: 'EquipmentAlarm',
	probable_cause: 'Device is maybe restarted...',
}

function parseWarmStart(trapMsg, eventMsg) {
	print('begin to parse warm start trap...\r\n');
	eventMsg.put("alarm_key", trapMsg.get("node_addr") + "_WarmStart");
	return eventMsg;
}
