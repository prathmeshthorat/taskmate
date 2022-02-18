package com.taskmate.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskmate.component.DaggerTaskMateComponent;
import com.taskmate.exception.BusinessException;
import com.taskmate.factory.TaskmateLambdaFactory;
import com.taskmate.model.GatewayProxyRequest;
import com.taskmate.model.GatewayResponse;
import com.taskmate.model.RequestContext;
import com.taskmate.service.TaskmateService;
import com.taskmate.utils.CommonUtils;
import com.taskmate.utils.Constants;

public class TaskMateRequestHandler extends LambdaFunctionProcessor {

	private static TaskmateLambdaFactory factory;
	private static final ObjectMapper mapper;
	private static Map<String, String> config;

	static {
		mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		factory = DaggerTaskMateComponent.create().buildTaskmateLambdaFactory();
	}

	@Override
	protected GatewayResponse processRequest(GatewayProxyRequest request, RequestContext context)
			throws BusinessException {
		Map<String, String> headers = getHeaders();
		GatewayResponse response = null;
		try {
			getConfiguration(request, context);

			System.out.println(String.format("\n %s processing GatewayRequest.", this.getClass().getName()));
			String action = config.get(Constants.ACTION);
			context.setSubAction(config.get(Constants.SUB_ACTION));
			switch (action) {
			case "user":
				TaskmateService service = factory.getTaskmateLamdaService(mapper, context,
						config.get(Constants.SERVICE));
				response = service.process(request, context);
				break;

			default:
				response = new GatewayResponse("Action is not set in header.", headers, 400);
				break;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return response;
	}

	public void getConfiguration(GatewayProxyRequest request, RequestContext context)
			throws BusinessException, JsonMappingException, JsonProcessingException {
		if (null != request.getBody()) {
			config = CommonUtils.getConfig(mapper.readTree(request.getBody()).get(Constants.ACTION).asText());
		}
	}

	@SuppressWarnings("unused")
	private String getPageContents(String address) throws IOException {
		URL url = new URL(address);
		try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
			return br.lines().collect(Collectors.joining(System.lineSeparator()));
		}
	}

}
