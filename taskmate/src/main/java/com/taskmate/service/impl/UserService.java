package com.taskmate.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskmate.domain.User;
import com.taskmate.model.GatewayProxyRequest;
import com.taskmate.model.GatewayResponse;
import com.taskmate.model.RequestContext;
import com.taskmate.service.TaskmateService;
import com.taskmate.utils.CommonUtils;
import com.taskmate.utils.Constants;
import com.taskmate.utils.EnumUtils.UserType;
import com.taskmate.utils.jwt.JwtProvider;

public class UserService implements TaskmateService {

	private ObjectMapper mapper;
	private DynamoDBMapper dbMapper;
	// private static final String FUNCTION_NAME = "UserService";
	private static final String REGISTER = "register";
	private static final String LOGIN = "login";
	private static final String REMOVE = "remove";
	private RequestContext ctx = null;

	public UserService(ObjectMapper mapper, DynamoDBMapper dbMapper) {
		this.dbMapper = dbMapper;
		this.mapper = mapper;
	}

	@Override
	public GatewayResponse process(GatewayProxyRequest request, RequestContext context) {
		GatewayResponse response = null;
		Map<String, String> headers = CommonUtils.getHeaders();
		this.ctx = context;
		String subAction = context.getSubAction();
		switch (subAction) {
		case REGISTER:
			response = register(request);
			break;
		case LOGIN:
			response = login(request);
			break;
		case REMOVE:
			response = remove(request);
			break;
		default:
			response = new GatewayResponse("Invalid subaction.", headers, 200);
			break;
		}

		return response;
	}

	private GatewayResponse remove(GatewayProxyRequest request) {
		String body = null;
		if (ctx.getUserType() != null
				&& (ctx.getUserType() == UserType.TASKMATE_ADMIN || ctx.getUserType() == UserType.SUPPORT_EMPLOYEE)) {
			try {
				User user = mapper.readValue(request.getBody(), User.class);
				if (user.getEmail() != null) {
					User removeUser = fetchUserByEmailId(user.getEmail());
					dbMapper.delete(removeUser);
					body = String.format("%s removed from user table.", user.getEmail());
				}
			} catch (Exception e) {
				System.out.println("Printing error:: " + e.toString());
				body = e.getMessage();
			}

		}
		return new GatewayResponse(body, CommonUtils.getHeaders(), 200);
	}

	private GatewayResponse login(GatewayProxyRequest request) {
		String body = null;
		Map<String, String> headers = CommonUtils.getHeaders();
		try {
			User user = mapper.readValue(request.getBody(), User.class);
			User registeredUser = fetchUserByEmailId(user.getEmail());
			if (registeredUser != null) {
				if (CommonUtils.checkpw(user.getPassword(), registeredUser.getPassword())) {
					System.out.println("User login successful  :: " + registeredUser.getUserId());
					String token = JwtProvider.generateToken(registeredUser);
					headers.put(Constants.X_ID_TOKEN, token);
					body = "Login successful.";
				} else {
					body = "Email or Password is incorrect.";
				}
				CommonUtils.clearPassword(registeredUser);

			} else {
				body = String.format("%s email id is not Registered. Please register to use Taskmate.",
						user.getEmail());
			}
		} catch (Exception e) {
			System.out.println("Printing error:: " + e.toString());
			body = e.getMessage();
		}
		return new GatewayResponse(body, headers, 200);
	}

	private GatewayResponse register(GatewayProxyRequest request) {
		String body;
		Map<String, String> headers = CommonUtils.getHeaders();
		try {
			User user = mapper.readValue(request.getBody(), User.class);
			if (null == fetchUserByEmailId(user.getEmail())) {
				user.setUserId(UUID.randomUUID().toString());
				user.setPassword(CommonUtils.hashpw(user.getPassword()));
				user.setAction(null);
				System.out.println("Registering user with userId :: " + user.getUserId());
				dbMapper.save(user);
				CommonUtils.clearPassword(user);
				body = mapper.writeValueAsString(user);
				String token = JwtProvider.generateToken(user);
				headers.put(Constants.X_ID_TOKEN, token);
			} else {
				body = String.format("%s email id already Registered.", user.getEmail());
			}
		} catch (Exception e) {
			System.out.println("Printing error:: " + e.toString());
			body = e.getMessage();
		}
		return new GatewayResponse(body, headers, 200);
	}

	private User fetchUserByEmailId(String email) {
		User user = null;
		Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
		eav.put(":val1", new AttributeValue().withS(email));
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression().withFilterExpression("email = :val1")
				.withExpressionAttributeValues(eav);

		List<User> users = dbMapper.scan(User.class, scanExpression);
		if (users.size() > 0) {
			user = users.get(0);
			// System.out.println("User from DB:: " + user.toString());
		}
		return user;
	}

}
