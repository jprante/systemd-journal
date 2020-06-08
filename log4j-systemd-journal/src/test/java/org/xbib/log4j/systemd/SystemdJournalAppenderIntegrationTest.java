package org.xbib.log4j.systemd;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

@EnabledOnOs({OS.LINUX})
class SystemdJournalAppenderIntegrationTest {

    private static final Logger logger = LogManager.getLogger(SystemdJournalAppenderIntegrationTest.class.getName());

    @BeforeEach
    void clearMdc() {
        ThreadContext.clearAll();
    }

    @Test
    void testMessages() {
        logger.trace("this is a test message with level TRACE");
        logger.debug("this is a test message with level DEBUG");
        logger.info("this is a test message with level INFO");
        logger.warn("this is a test message with level WARN");
        logger.error("this is a test message with level ERROR");
    }

    @Test
    void testMessageWithUnicode() {
        logger.info("this is a test message with unicode: →←üöß");
    }

    @Test
    void testMessageWithMDC() {
        ThreadContext.put("some key1", "some value %d");
        ThreadContext.put("some key2", "some other value with unicode: →←üöß");
        logger.info("this is a test message with a MDC");
    }

    @Test
    void testMessageWithPlaceholders() {
        ThreadContext.put("some key1%s", "%1$");
        ThreadContext.put("%1$", "%1$");
        logger.info("this is a test message with special placeholder characters: %1$");
    }

    @Test
    void testMessageWithStacktrace() {
        logger.info("this is a test message with an exception", new RuntimeException("some exception"));
    }

}
