package com.taskmate.utils;

public class EnumUtils {

	private EnumUtils() {
		throw new IllegalStateException("Utility class");
	}

	public enum UserType {
		TASKMATE_ADMIN("taskmate_admin"), UNREGISTERED_USER("unregistered_user"), USER("user"),
		SUPPORT_EMPLOYEE("support_employee"); 

		private final String name;

		UserType(String name) {
			this.name = name;
		}

		public static UserType getByName(String name) {
			for (UserType value : values()) {
				if (value.getName().equals(name))
					return value;
			}
			return null;
		}

		public String getName() {
			return name;
		}
	}

	public enum TaskStatus {

		CREATED, COMPLETED, INPROGRESS, BLOCKED, INVALID

	}

}
