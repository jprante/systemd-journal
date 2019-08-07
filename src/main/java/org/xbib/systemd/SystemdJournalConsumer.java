package org.xbib.systemd;

import org.bridj.Pointer;
import org.bridj.SizeT;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        Pointer<Pointer<SystemdJournalLibrary.sdJournal>> sdJournalPointer = Pointer.allocatePointer(SystemdJournalLibrary.sdJournal.class);
        int r = SystemdJournalLibrary.sd_journal_open(sdJournalPointer, SystemdJournalLibrary.SD_JOURNAL_LOCAL_ONLY);
        if (r < 0) {
            logger.log(Level.WARNING, "error opening journal for read: " + r);
        }
        Pointer<SystemdJournalLibrary.sdJournal> sdJournal = sdJournalPointer.getPointer(SystemdJournalLibrary.sdJournal.class);
        if (match != null) {
            r = SystemdJournalLibrary.sd_journal_add_match(sdJournal, Pointer.pointerToCString(match), match.length());
            if (r < 0) {
                logger.log(Level.WARNING, "error in add_match: " + r);
            }
        }
        SystemdJournalLibrary.sd_journal_get_fd(sdJournal);
        SystemdJournalLibrary.sd_journal_seek_tail(sdJournal);
        SystemdJournalLibrary.sd_journal_previous(sdJournal);
        SystemdJournalLibrary.sd_journal_next(sdJournal);
        Pointer<Pointer<Byte>> cursorPointer = Pointer.allocatePointer(Byte.TYPE);
        SystemdJournalLibrary.sd_journal_get_cursor(sdJournal, cursorPointer);
        Pointer<Byte> existingCursor = cursorPointer.get();
        while (true) {
            do {
                r = SystemdJournalLibrary.sd_journal_wait(sdJournal, -1);
            } while (r == 0); // NOP
            while (SystemdJournalLibrary.sd_journal_next(sdJournal) > 0) {
                if (field != null) {
                    Pointer<Pointer<?>> dataPointer = Pointer.allocatePointer();
                    Pointer<SizeT> sizePointer = Pointer.allocateSizeT();
                    r = SystemdJournalLibrary.sd_journal_get_data(sdJournal, Pointer.pointerToCString(field), dataPointer, sizePointer);
                    if (r < 0) {
                        throw new IOException("error in get_data: " + r);
                    }
                    Pointer<Byte> data = dataPointer.as(Byte.class);
                    if (listener != null) {
                        listener.handleEntry(makeEntry(Collections.singletonList(data.getPointer(Byte.class).getCString())));
                    }
                } else {
                    cursorPointer = Pointer.allocatePointer(Byte.TYPE);
                    SystemdJournalLibrary.sd_journal_get_cursor(sdJournal, cursorPointer);
                    Pointer<Byte> newCursor = cursorPointer.get();
                    if (!existingCursor.getCString().equals(newCursor.getCString())) {
                        existingCursor = newCursor;
                        Pointer<Pointer<?>> dataPointer = Pointer.allocatePointer();
                        Pointer<SizeT> sizePointer = Pointer.allocateSizeT();
                        List<String> list = new ArrayList<>();
                        while (SystemdJournalLibrary.sd_journal_enumerate_data(sdJournal, dataPointer, sizePointer) > 0) {
                            Pointer<Byte> data = dataPointer.as(Byte.class);
                            String line = data.getPointer(Byte.class).getCString();
                            list.add(line);
                        }
                        SystemdJournalLibrary.sd_journal_restart_data(sdJournal);
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
            logger.log(Level.INFO, "entry: " + string);
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
                continue;
            }

        }
        return journalEntry;
    }
}
