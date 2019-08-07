package org.xbib.systemd;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;

import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

@DisabledOnOs({OS.MAC, OS.WINDOWS})
class SystemdJournalReaderTest {

    private static final Logger logger = Logger.getLogger(SystemdJournalReaderTest.class.getName());

    @Test
    void testConsumer() throws InterruptedException {
        SystemdJournalConsumer consumer = new SystemdJournalConsumer("SYSLOG_IDENTIFIER=su",
                entry -> logger.log(Level.INFO, entry.toString()));
        Executors.newSingleThreadExecutor().submit(consumer);
        // exit after 1 minute
        Thread.sleep(60000L);
    }
}
