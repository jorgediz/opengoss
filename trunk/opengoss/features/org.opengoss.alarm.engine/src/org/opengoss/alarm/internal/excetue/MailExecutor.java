package org.opengoss.alarm.internal.excetue;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.opengoss.alarm.core.AlarmRule;
import org.opengoss.alarm.core.BizAlarm;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * @author zhufan mail前转的规则
 */
public class MailExecutor implements RuleExecutor {
	private Log log = LogFactory.getLog(MailExecutor.class);

	private MailSender mailSender;

	private String mailFrom;

	private String subject = " Alarm Information";
	
	private String host;
	private String userName;
	private String pwd;
	
	private void createMailSender(){
		
		if(mailSender == null){
			Properties props = new Properties();
			props.put("mail.smtp.host",host);
			props.put("mail.smtp.auth","true");
			JavaMailSenderImpl sender = new JavaMailSenderImpl();
			sender.setJavaMailProperties(props);
			sender.setPassword(this.pwd);
			sender.setUsername(this.userName);
			
			this.mailSender = sender;
		}
	}

	public void execute(BizAlarm alarm, AlarmRule rule) {
		
		createMailSender();

		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(rule.getContext());
		message.setFrom(this.mailFrom);
		message.setSubject(this.subject);

		String text = getContentText(alarm);
		message.setText(text);

		mailSender.send(message);

		if (log.isDebugEnabled()) {
			
			log.debug("send mail to " + rule.getContext());
			log.debug(text);
		}

	}

	private String getContentText(BizAlarm alarm) {
		StringBuffer sb = new StringBuffer();
		sb.append("==========================================");
		sb.append(" Name : " + alarm.getName() + "/r/n");
		sb.append(" Vendor Alarm Id : " + alarm.getVendorAlarmId() + "/r/n");
		sb.append(" Vendor Alarm Name : " + alarm.getVendorAlarmName() + "/r/n");
		sb.append(" Alarm Type : " + alarm.getAlarmType() + "/r/n");
		sb.append(" ProbableCause : " + alarm.getProbableCause() + "/r/n");
		sb.append(" Special Problem : " + alarm.getSpecialProblem() + "/r/n");
		sb.append(" Perceived Severity : " + alarm.getPerceivedSeverity()
				+ "/r/n");
		sb.append(" Alarm Raised Time : " + alarm.getAlarmRaisedTime() + "/r/n");
		sb.append(" Additional Information : " + alarm.getAdditionalInfo() + "/r/n");
		sb.append(" Customer Name : " + alarm.getCustomerName() + "/r/n");
		sb.append(" Resource Information : " + alarm.getRescInfo() + "/r/n");
		sb.append(" SLA Information : " + alarm.getSlaInfo() + "/r/n");
		sb.append(" Location : " + alarm.getLocation() + "/r/n");
		sb.append("==========================================");
		return sb.toString();
	}

	public String getMailFrom() {
		return mailFrom;
	}

	public void setMailFrom(String mailFrom) {
		this.mailFrom = mailFrom;
	}


	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
