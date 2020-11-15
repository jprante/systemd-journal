package org.xbib.systemd.journal;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.StringArray;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.xbib.systemd.journal.SystemdLibraryAPI.SD_JOURNAL_LOCAL_ONLY;

public class SystemdJournalConsumer implements Runnable {

    private static final Logger logger = Logger.getLogger(SystemdJournalConsumer.class.getName());

    private final String match;

    private final String field;

    private final SystemdJournalListener listener;

    public SystemdJournalConsumer(String match, SystemdJournalListener listener) {
        this(match, null, listener);
    }

    public SystemdJournalConsumer(String match, String field, SystemdJournalListener listener) {
        this.match = match;
        this.field = field;
        this.listener = listener;
    }

    @Override
    public void run() {
        try {
            loop();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    private void loop() throws IOException {
        SystemdLibraryAPI api = SystemdLibraryAPI.getInstance();
        SdJournal sdJournal = new SdJournal();
        logger.log(Level.INFO, "opening");
        int rc = api.sd_journal_open(sdJournal, SD_JOURNAL_LOCAL_ONLY);
        logger.log(Level.INFO, "open: " + rc);
        if (rc != 0) {
            logger.log(Level.WARNING, "error opening journal for read: " + rc);
            return;
        }
        if (match != null) {
            rc = api.sd_journal_add_match(sdJournal, match, match.length());
            logger.log(Level.INFO, "add_match: " + rc);
            if (rc != 0) {
                logger.log(Level.WARNING, "error in add_match: " + rc);
                return;
            }
        }
        rc = api.sd_journal_get_fd(sdJournal);
        logger.log(Level.INFO, "get_fd: " + rc);
        rc = api.sd_journal_seek_tail(sdJournal);
        logger.log(Level.INFO, "seek_tail: " + rc);
        rc = api.sd_journal_previous(sdJournal);
        logger.log(Level.INFO, "previous: " + rc);
        rc = api.sd_journal_next(sdJournal);
        logger.log(Level.INFO, "next: " + rc);
        String[] strings = new String[1];
        StringArray cursor = new StringArray(strings);
        rc = api.sd_journal_get_cursor(sdJournal, cursor);
        logger.log(Level.INFO, "get_cursor: " + rc);
        while (true) {
            do {
                rc = api.sd_journal_wait(sdJournal, -1);
                logger.log(Level.INFO, "wait: " + rc);
            } while (rc == 0); // NOP
            while ((rc = api.sd_journal_next(sdJournal)) > 0) {
                logger.log(Level.INFO, "next: " + rc);
                if (field != null) {
                    Pointer dataPointer = new Memory(Native.POINTER_SIZE);
                    Pointer sizePointer = new Memory(Native.POINTER_SIZE);
                    rc = api.sd_journal_get_data(sdJournal, field, dataPointer, sizePointer);
                    logger.log(Level.INFO, "get_data: " + rc);
                    if (rc != 0) {
                        throw new IOException("error in get_data: " + rc);
                    }
                    int size = sizePointer.getInt(0);
                    byte[] b = dataPointer.getByteArray(0, size);
                    String s = new String(b, StandardCharsets.UTF_8);
                    if (listener != null) {
                        listener.handleEntry(makeEntry(Collections.singletonList(s)));
                    }
                } else {
                    String[] strings2 = new String[1];
                    StringArray nextCursor = new StringArray(strings2);
                    rc = api.sd_journal_get_cursor(sdJournal, nextCursor);
                    logger.log(Level.INFO, "get_cursor: " + rc);
                    if (!cursor.getString(0).equals(nextCursor.getString(0))) {
                        cursor = nextCursor;
                        Pointer dataPointer = new Memory(Native.POINTER_SIZE);
                        Pointer sizePointer = new Memory(Native.POINTER_SIZE);
                        List<String> list = new ArrayList<>();
                        while ((rc = api.sd_journal_enumerate_data(sdJournal, dataPointer, sizePointer)) > 0) {
                            logger.log(Level.INFO, "enumerate_data: " + rc);
                            int size = sizePointer.getInt(0);
                            byte[] b = dataPointer.getByteArray(0, size);
                            String s = new String(b, StandardCharsets.UTF_8);
                            list.add(s);
                        }
                        rc = api.sd_journal_restart_data(sdJournal);
                        logger.log(Level.INFO, "restart_data: " + rc);
                        if (listener != null) {
                            listener.handleEntry(makeEntry(list));
                        }
                    }
                }
            }
        }
    }

    private JournalEntry makeEntry(List<String> list) {
        DefaultJournalEntry journalEntry = new DefaultJournalEntry();
        for (String string : list) {
            if (string.startsWith("MESSAGE=")) {
                journalEntry.setMessage(string.substring(8));
                continue;
            }
            if (string.startsWith("MESSAGE_ID=")) {
                journalEntry.setMessageId(string.substring(11));
                continue;
            }
            if (string.startsWith("PRIORITY=")) {
                journalEntry.setPriority(Integer.parseInt(string.substring(9)));
                continue;
            }
            if (string.startsWith("CODE_FILE=")) {
                journalEntry.setCodeFile(string.substring(10));
                continue;
            }
            if (string.startsWith("CODE_LINE=")) {
                journalEntry.setCodeLine(string.substring(10));
                continue;
            }
            if (string.startsWith("CODE_FUNC=")) {
                journalEntry.setCodeFunc(string.substring(10));
                continue;
            }
            if (string.startsWith("ERRNO=")) {
                journalEntry.setErrno(string.substring(6));
                continue;
            }
            if (string.startsWith("SYSLOG_FACILITY=")) {
                journalEntry.setSyslogFacility(string.substring(16));
                continue;
            }
            if (string.startsWith("SYSLOG_IDENTIFIER=")) {
                journalEntry.setSyslogIdentifier(string.substring(18));
                continue;
            }
            if (string.startsWith("SYSLOG_PID=")) {
                journalEntry.setSyslogPid(string.substring(11));
                continue;
            }
            if (string.startsWith("SYSLOG_TIMESTAMP=")) {
                journalEntry.setSyslogTimestamp(string.substring(17));
                continue;
            }
            if (string.startsWith("SYSLOG_RAW=")) {
                journalEntry.setSyslogRaw(string.substring(11));
                continue;
            }
            if (string.startsWith("_PID=")) {
                journalEntry.setPid(string.substring(5));
                continue;
            }
            if (string.startsWith("_UID=")) {
                journalEntry.setUid(string.substring(5));
                continue;
            }
            if (string.startsWith("_GID=")) {
                journalEntry.setGid(string.substring(5));
                continue;
            }
            if (string.startsWith("_COMM=")) {
                journalEntry.setComm(string.substring(6));
                continue;
            }
            if (string.startsWith("_EXE=")) {
                journalEntry.setExe(string.substring(5));
                continue;
            }
            if (string.startsWith("_CMDLINE=")) {
                journalEntry.setCmdLine(string.substring(9));
                continue;
            }
            if (string.startsWith("_CAP_EFFECTIVE=")) {
                journalEntry.setCapEffective(string.substring(15));
                continue;
            }
            if (string.startsWith("_TRANSPORT=")) {
                journalEntry.setTransport(string.substring(11));
                continue;
            }
            if (string.startsWith("_SYSTEMD_OWNER_UID=")) {
                journalEntry.setSystemdOwnerUid(string.substring(19));
                continue;
            }
            if (string.startsWith("_SYSTEMD_UNIT=")) {
                journalEntry.setSystemdUnit(string.substring(13));
                continue;
            }
            if (string.startsWith("_SYSTEMD_USER_SLICE=")) {
                journalEntry.setSystemdUserSlice(string.substring(19));
                continue;
            }
            if (string.startsWith("_SYSTEMD_USER_UNIT=")) {
                journalEntry.setSystemdUserUnit(string.substring(18));
            }
        }
        return journalEntry;
    }
}
