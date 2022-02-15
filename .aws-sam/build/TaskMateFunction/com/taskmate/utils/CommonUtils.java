package com.taskmate.utils;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.mindrot.jbcrypt.BCrypt;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskmate.domain.User;
import com.taskmate.exception.BusinessException;

public class CommonUtils {

	private static final String FILE_NAME = "taskmate_config.json";

	public static Map<String, String> getHeaders() {
		Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		headers.put("x-custom-header", "application/json");
		headers.put(Constants.X_ID_TOKEN, null);
		return headers;
	}

	public static String hashpw(String password) {
		return BCrypt.hashpw(password, BCrypt.gensalt());
	}

	public static boolean checkpw(String pw, String hpw) {
		return BCrypt.checkpw(pw, hpw);
	}

	public static Map<String, String> getConfig(String action) throws BusinessException {
		String fileContent = "";
		ObjectMapper mapper = new ObjectMapper();
		String value = "";
		Map<String, String> output = new HashMap<>();
		InputStream is = null;
		try {
			try {
			is = CommonUtils.class.getResourceAsStream(FILE_NAME);
			fileContent = new String(is.readAllBytes());
			}
			finally {
				if(is != null)
				is.close();
			}
			System.out.println("\n Loading configuration: ");
			JsonNode rootNode = mapper.readTree(fileContent);
			JsonNode configNode = rootNode.path("config");
			value = configNode.path(action).asText();
			String[] ar = action.split("\\.");
			output.put(Constants.SERVICE, value);
			output.put(Constants.ACTION, ar[1]);
			output.put(Constants.SUB_ACTION, ar[0]);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException("Exception while reading config. "+e.getMessage(), e);
		}
		
		return output;
	}

	public static void clearPassword(User user) {
		user.setPassword(null);
	}
	
	public static String getConfigByKey(String key){
		String fileContent = "";
		ObjectMapper mapper = new ObjectMapper();
		String value = "";
		InputStream is = null;
		try {
			try {
			is = CommonUtils.class.getResourceAsStream(FILE_NAME);
			fileContent = new String(is.readAllBytes());
			}
			finally {
				if(is != null)
				is.close();
			}
			JsonNode rootNode = mapper.readTree(fileContent);
			JsonNode configNode = rootNode.path("config");
			value = configNode.path(Constants.JWT_B64_KEY).asText();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return value;
	}

}
