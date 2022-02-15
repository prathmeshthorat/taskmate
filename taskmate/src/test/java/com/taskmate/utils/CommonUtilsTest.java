package com.taskmate.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.taskmate.exception.BusinessException;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CommonUtilsTest {

	@InjectMocks
	CommonUtils commonUtils = new CommonUtils();

	private String action = "register.user";
	private String pw = "password";

	@SuppressWarnings("static-access")
	@Test
	void getConfigTest() {
		try {
			commonUtils.getConfig(action);
			assertTrue(true);
		} catch (BusinessException e) {
			fail();
			e.printStackTrace();
		}
	}

	@Test
	@SuppressWarnings("static-access")
	void passwordTest() {

		String hash = commonUtils.hashpw(pw);
		assertTrue(commonUtils.checkpw(pw, hash));
		System.out.println(hash);
	}

}
