package com.taskmate.component;

import javax.inject.Singleton;

import com.taskmate.factory.TaskmateLambdaFactory;
import com.taskmate.module.DynamoDBModule;

import dagger.Component;

@Component(modules = DynamoDBModule.class)
@Singleton
public interface TaskMateComponent {
	
	TaskmateLambdaFactory buildTaskmateLambdaFactory();

}
