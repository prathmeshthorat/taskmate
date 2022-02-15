package com.taskmate.service;

import com.taskmate.model.GatewayProxyRequest;
import com.taskmate.model.GatewayResponse;
import com.taskmate.model.RequestContext;

public interface TaskmateService {
	
	public GatewayResponse process(GatewayProxyRequest request,RequestContext context);
}
