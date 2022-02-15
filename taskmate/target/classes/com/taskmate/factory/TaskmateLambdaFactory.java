package com.taskmate.factory;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskmate.model.RequestContext;
import com.taskmate.service.TaskmateService;

public class TaskmateLambdaFactory {

	//private static LambdaLogger log;
	private DynamoDBMapper dynamoDBMapper;
	private static String CLASS_NAME = "com.taskmate.service.impl.";
	private static Map<String, TaskmateService> serviceMap = new HashMap<>();

	@Inject
	public TaskmateLambdaFactory(DynamoDBMapper dynamoDBMapper) {
		this.dynamoDBMapper = dynamoDBMapper;
	}

	public TaskmateService getTaskmateLamdaService(ObjectMapper mapper, RequestContext context, String className) throws RuntimeException {
		//log.log("Inside getTaskmateLamdaService \n");
		CLASS_NAME = CLASS_NAME + className;
		if (!serviceMap.containsKey(CLASS_NAME)) {
			try {
				Class<?> clazz = Class.forName(CLASS_NAME);

				TaskmateService service = buildJsonParserLambdaService(clazz, mapper, dynamoDBMapper, context);
				serviceMap.put(CLASS_NAME, service);
				if (null == service) {
					throw new RuntimeException("null object returned for name: " + CLASS_NAME);
				}
			} catch (ClassNotFoundException e) {
				throw new RuntimeException("null object returned for name: " + CLASS_NAME);
			}
		}
		return serviceMap.get(CLASS_NAME);
	}

	private TaskmateService buildJsonParserLambdaService(Class<?> clazz, ObjectMapper mapper, DynamoDBMapper db,
			RequestContext context) throws RuntimeException {

		TaskmateService service = null;
		try {
			service = (TaskmateService) clazz.getConstructor(ObjectMapper.class, DynamoDBMapper.class).newInstance(mapper, db);

		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			throw new RuntimeException(e);
		}

		return service;
	}

}
