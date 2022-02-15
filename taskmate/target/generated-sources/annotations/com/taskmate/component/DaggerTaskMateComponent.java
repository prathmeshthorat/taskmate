package com.taskmate.component;

import com.taskmate.factory.TaskmateLambdaFactory;
import com.taskmate.module.DynamoDBModule;
import com.taskmate.module.DynamoDBModule_ProvideDynamoDBMapperFactory;
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
public final class DaggerTaskMateComponent implements TaskMateComponent {
  private DaggerTaskMateComponent() {

  }

  public static Builder builder() {
    return new Builder();
  }

  public static TaskMateComponent create() {
    return new Builder().build();
  }

  @Override
  public TaskmateLambdaFactory buildTaskmateLambdaFactory() {
    return new TaskmateLambdaFactory(DynamoDBModule_ProvideDynamoDBMapperFactory.provideDynamoDBMapper());}

  public static final class Builder {
    private Builder() {
    }

    /**
     * @deprecated This module is declared, but an instance is not used in the component. This method is a no-op. For more, see https://dagger.dev/unused-modules.
     */
    @Deprecated
    public Builder dynamoDBModule(DynamoDBModule dynamoDBModule) {
      Preconditions.checkNotNull(dynamoDBModule);
      return this;
    }

    public TaskMateComponent build() {
      return new DaggerTaskMateComponent();
    }
  }
}
