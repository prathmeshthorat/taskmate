package com.taskmate.module;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

import dagger.Module;
import dagger.Provides;

@Module
public class DynamoDBModule {

	private static final String DYNAMODB_END_POINT = "http://192.168.96.1:8010/";
	private static final String AWS_REGION = "localhost";
	private static final String AWS_ACCESS_KEY_ID = "0xp0w";
	private static final String AWS_SECRET_ACCESS_KEY = "hlas6k";
	
	@Provides
	public static DynamoDBMapper provideDynamoDBMapper() {
		return new DynamoDBMapper(buildAmazonDynamoDB());
	}

	private static AmazonDynamoDB buildAmazonDynamoDB() {
		return AmazonDynamoDBClientBuilder.standard()
				.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(DYNAMODB_END_POINT, AWS_REGION))
				.withCredentials(new AWSStaticCredentialsProvider(
						new BasicAWSCredentials(AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY)))
				.build();
	}

}
