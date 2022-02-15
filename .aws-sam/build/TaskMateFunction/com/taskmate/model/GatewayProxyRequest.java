package com.taskmate.model;

import java.util.Map;

import com.taskmate.utils.Constants;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import software.amazon.awssdk.utils.StringUtils;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class GatewayProxyRequest {
	
	private String resourse;
	private String path;
	private String httpMethod;
	private Map<String, String> headers;
	private Map<String, String> pathParameters;
	private Map<String, String> queryParameters;
	private String body;
	private String methodArn;
	private String invoker;
	private RequestContext requestContext;
	private String action;
	
	
	public boolean isValid() {
		return StringUtils.isNotBlank(this.getHeaders().get(Constants.X_APPLICATION_ID)) 
				&& StringUtils.isNotBlank(this.getHeaders().get(Constants.X_COUNTRY_CODE));
	}
	
	
}
