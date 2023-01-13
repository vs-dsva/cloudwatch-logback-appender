package com.j256.cloudwatchlogbackappender;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class MiscUtilsTest {

	@Test
	void testIsBlank() {
		assertTrue(MiscUtils.isBlank(null));
		assertTrue(MiscUtils.isBlank(""));
		assertTrue(MiscUtils.isBlank(" "));
		assertFalse(MiscUtils.isBlank("s"));
		// coverage
		new MiscUtils();
	}
}
