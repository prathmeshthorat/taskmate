package com.taskmate.handler;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskmate.exception.BusinessException;
import com.taskmate.model.GatewayProxyRequest;
import com.taskmate.model.GatewayResponse;
import com.taskmate.model.RequestContext;
import com.taskmate.utils.Constants;
import com.taskmate.utils.EnumUtils.UserType;
import com.taskmate.utils.jwt.JwtProvider;

import software.amazon.awssdk.utils.StringUtils;

public abstract class LambdaFunctionProcessor implements RequestHandler<GatewayProxyRequest, GatewayResponse> {

	protected static Map<String, Map<String, String>> configMap;
	protected static final ObjectMapper mapper;

	protected UserType userType;
	protected String applicationId;
	protected String countryCode;
	protected String requestId;

	static {
		mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);

	}

	protected abstract GatewayResponse processRequest(GatewayProxyRequest request, RequestContext context)
			throws BusinessException;

	@Override
	public GatewayResponse handleRequest(GatewayProxyRequest request, Context context) {
		String s = new StringBuilder("## ").append(this.getClass().getSimpleName()).append(".").append("handleRequest")
				.toString();
		LambdaLogger logger = context.getLogger();
		logger.log("\n " + s + " Entrypoint ## "+request.toString());
		getUserInfo(request, context);
		RequestContext requestContext = request.getRequestContext();
		requestContext.setRequestId(context.getAwsRequestId());
		if (userType != null)
			requestContext.setUserType(userType);
		GatewayResponse response = null;

		if (isValid(request)) {
			getUserInfo(request, context);
			requestContext.setApplicationId(applicationId);
			requestContext.setCountryCode(countryCode);
			logger.log("\n Request valid. " + request.toString());
			try {
				response = processRequest(request, requestContext);
			} catch (Exception e) {
				logger.log(e.getMessage());
				response = new GatewayResponse("Internal Server Error. " + e.getMessage(), getHeaders(), 500);
			}
			if (200 == response.getStatusCode()) {
				// set headers
			}

		} else {
			response = new GatewayResponse("Invalid-Malformed Request.", getHeaders(), 400);
		}
		return response;
	}

	private void getUserInfo(GatewayProxyRequest request, Context context) {
		requestId = context.getAwsRequestId();
		applicationId = request.getHeaders().get(Constants.X_APPLICATION_ID);
		countryCode = request.getHeaders().get(Constants.X_COUNTRY_CODE);
		String jwtToken = request.getHeaders().get(Constants.X_ID_TOKEN);
		if (jwtToken != null) {
			userType = JwtProvider.getUserType(jwtToken);
		}
	}

	private boolean isValid(GatewayProxyRequest request) {
		return request.isValid() || StringUtils.isNotBlank(request.getInvoker());
	}

	protected Map<String, String> getHeaders() {
		Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		headers.put("X-Custom-Header", "application/json");
		headers.put(Constants.X_APPLICATION_ID, applicationId);
		headers.put(Constants.X_COUNTRY_CODE, countryCode);
		headers.put(Constants.X_REQUEST_ID, requestId);
		return headers;
	}

}
