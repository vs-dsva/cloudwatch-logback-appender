package com.j256.cloudwatchlogbackappender;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.getCurrentArguments;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.easymock.IAnswer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import com.amazonaws.services.logs.AWSLogsClient;
import com.amazonaws.services.logs.model.PutLogEventsRequest;
import com.amazonaws.services.logs.model.PutLogEventsResult;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.LoggingEvent;

public class HostNameConverterTest extends BaseConverterTest {

	@Test
	@Timeout(5000)
	void testStuff() throws InterruptedException, UnknownHostException {

		String hostAddr = InetAddress.getLocalHost().getHostName();

		AWSLogsClient awsLogClient = createMock(AWSLogsClient.class);
		appender.setAwsLogsClient(awsLogClient);

		String prefix = "logstream-";
		appender.setLogStream(prefix + "%hostName");
		final String expectedLogStream = prefix + hostAddr;
		PatternLayout layout = new PatternLayout();
		layout.setPattern("%msg");
		layout.setContext(LOGGER_CONTEXT);
		layout.start();
		appender.setLayout(layout);

		LoggingEvent event = new LoggingEvent();
		event.setTimeStamp(System.currentTimeMillis());
		event.setLoggerName("name");
		event.setLevel(Level.DEBUG);
		event.setMessage("message");

		final PutLogEventsResult result = new PutLogEventsResult();
		result.setNextSequenceToken("ewopjfewfj");
		expect(awsLogClient.putLogEvents(isA(PutLogEventsRequest.class))).andAnswer(new IAnswer<PutLogEventsResult>() {
			@Override
			public PutLogEventsResult answer() {
				PutLogEventsRequest request = (PutLogEventsRequest) getCurrentArguments()[0];
				assertEquals(LOG_GROUP, request.getLogGroupName());
				assertEquals(expectedLogStream, request.getLogStreamName());
				return result;
			}
		});
		awsLogClient.shutdown();

		// =====================================

		replay(awsLogClient);
		appender.start();
		appender.append(event);
		while (appender.getEventsWrittenCount() < 1) {
			Thread.sleep(10);
		}
		appender.stop();
		verify(awsLogClient);
	}
}
