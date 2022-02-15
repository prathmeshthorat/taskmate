package com.taskmate.factory;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import dagger.internal.Factory;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class TaskmateLambdaFactory_Factory implements Factory<TaskmateLambdaFactory> {
  private final Provider<DynamoDBMapper> dynamoDBMapperProvider;

  public TaskmateLambdaFactory_Factory(Provider<DynamoDBMapper> dynamoDBMapperProvider) {
    this.dynamoDBMapperProvider = dynamoDBMapperProvider;
  }

  @Override
  public TaskmateLambdaFactory get() {
    return new TaskmateLambdaFactory(dynamoDBMapperProvider.get());
  }

  public static TaskmateLambdaFactory_Factory create(
      Provider<DynamoDBMapper> dynamoDBMapperProvider) {
    return new TaskmateLambdaFactory_Factory(dynamoDBMapperProvider);
  }

  public static TaskmateLambdaFactory newInstance(DynamoDBMapper dynamoDBMapper) {
    return new TaskmateLambdaFactory(dynamoDBMapper);
  }
}
