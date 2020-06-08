package org.xbib.systemd.journal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

@EnabledOnOs({OS.LINUX})
class SystemdJournalReaderTest {

    private static final Logger logger = Logger.getLogger(SystemdJournalReaderTest.class.getName());

    @Test
    void testConsumer() throws InterruptedException {
        SystemdJournalConsumer consumer = new SystemdJournalConsumer("SYSLOG_IDENTIFIER=su",
                entry -> logger.log(Level.INFO, entry.toString()));
        Executors.newSingleThreadExecutor().submit(consumer);
        //  consuming for some seconds
        Thread.sleep(10000L);
    }
}
