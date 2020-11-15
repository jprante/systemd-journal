package org.xbib.systemd.journal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

@EnabledOnOs({OS.LINUX})
@ExtendWith(MockitoExtension.class)
public class SystemdJournalReaderTest {

    private static final Logger logger = Logger.getLogger(SystemdJournalReaderTest.class.getName());

    @Test
    void testConsumer() throws InterruptedException {
        SystemdJournalConsumer consumer = new SystemdJournalConsumer(null,
                entry -> logger.log(Level.INFO, entry.toString()));
        Executors.newSingleThreadExecutor().submit(consumer);
        //  consuming for some seconds
        Thread.sleep(60000L);
    }
}
