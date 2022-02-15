package com.taskmate.domain;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@DynamoDBDocument
public class Task{

	@DynamoDBHashKey
	@DynamoDBAutoGeneratedKey
	private Long taskId;
	@DynamoDBAttribute
	private String taskName;
	@DynamoDBAttribute
	private LocalDateTime startTime;
	@DynamoDBAttribute
	private LocalDateTime endTime;
	@DynamoDBAttribute
	private String status;
	@DynamoDBAttribute
	private Boolean isScheduled;

	@DynamoDBAttribute
	private User assignedTo;

	@DynamoDBAttribute
	private User createdBy;
	@DynamoDBAttribute
	private OffsetDateTime dateCreated;
	@DynamoDBAttribute
	private OffsetDateTime lastUpdated;

//    @PrePersist
//    public void prePersist() {
//        dateCreated = OffsetDateTime.now();
//        lastUpdated = dateCreated;
//    }
//
//    @PreUpdate
//    public void preUpdate() {
//        lastUpdated = OffsetDateTime.now();
//    }

}
