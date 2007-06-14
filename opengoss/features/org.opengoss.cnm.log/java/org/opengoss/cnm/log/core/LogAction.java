package org.opengoss.cnm.log.core;

public enum LogAction {
	
	GetLogger, FindAllLogger, FindSystemLog, FindOperationLog, FindLogRecord, DeleteLogRecord,
    ModifyLoggerInstance,
    // SM Actions
    Authenticate, HasPermission, 
    CreateUserGroup, ModifyUserGroup, 
    CreateUser, ModifyUser, DeleteUser, FindUsers, 
    ChangePwd, ResetPwd,
    UserLogin,UserLogout,
    
    
}
