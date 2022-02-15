package com.taskmate.module;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.annotation.processing.Generated;

@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class DynamoDBModule_ProvideDynamoDBMapperFactory implements Factory<DynamoDBMapper> {
  private static final DynamoDBModule_ProvideDynamoDBMapperFactory INSTANCE = new DynamoDBModule_ProvideDynamoDBMapperFactory();

  @Override
  public DynamoDBMapper get() {
    return provideDynamoDBMapper();
  }

  public static DynamoDBModule_ProvideDynamoDBMapperFactory create() {
    return INSTANCE;
  }

  public static DynamoDBMapper provideDynamoDBMapper() {
    return Preconditions.checkNotNull(DynamoDBModule.provideDynamoDBMapper(), "Cannot return null from a non-@Nullable @Provides method");
  }
}
