package com.taskmate.model;

import com.taskmate.utils.EnumUtils.UserType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class RequestContext {
	
	private String requestId;
	private String applicationId;
	private String preferredLanguage;
	private String topicArn;
	private String xIdToken;
	private String countryCode;
	private String path;
	private UserType userType;
	private String awsTraceId;
	private String emailId;
	private String httpMethod;
	private String accountId;
	private String subAction;
	
}
