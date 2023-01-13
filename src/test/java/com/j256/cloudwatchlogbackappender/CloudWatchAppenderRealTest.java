package com.j256.cloudwatchlogbackappender;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Disabled("for integration testing")
public class CloudWatchAppenderRealTest {

	static {
		Ec2InstanceNameConverter.setInstanceName("localhost");
	}

	@Test
	void testStuff() throws InterruptedException {
		Logger logger = LoggerFactory.getLogger(getClass());
		logger.info("testing stuff");
		logger.error("Here's a throw", new RuntimeException(new Exception("test exception here")));
		Thread.sleep(1000);
		logger.info("more stuff");
		Thread.sleep(10000000);
	}
}
