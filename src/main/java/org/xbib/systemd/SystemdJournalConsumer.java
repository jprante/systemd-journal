package org.xbib.systemd;

import org.bridj.Pointer;
import org.bridj.SizeT;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SystemdJournalConsumer implements Runnable {

    private static final Logger logger = Logger.getLogger(SystemdJournalConsumer.class.getName());

    private final String match;

    private final String field;

    private final SystemdJournalListener listener;

    public SystemdJournalConsumer(String match, String field, SystemdJournalListener listener) {
        this.match = match;
        this.field = field;
        this.listener = listener;
    }

    @Override
    public void run() {
        loop();
    }

    private void loop() {
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
            List<String> list = new ArrayList<>();
            while (SystemdJournalLibrary.sd_journal_next(sdJournal) > 0) {
                if (field != null) {
                    Pointer<Pointer<?>> dataPointer = Pointer.allocatePointer();
                    Pointer<SizeT> sizePointer = Pointer.allocateSizeT();
                    r = SystemdJournalLibrary.sd_journal_get_data(sdJournal, Pointer.pointerToCString(field), dataPointer, sizePointer);
                    if (r < 0) {
                        logger.log(Level.WARNING, "error in get_data: " + r);
                    }
                    Pointer<Byte> data = dataPointer.as(Byte.class);
                    list.add(data.getPointer(Byte.class).getCString());
                } else {
                    cursorPointer = Pointer.allocatePointer(Byte.TYPE);
                    SystemdJournalLibrary.sd_journal_get_cursor(sdJournal, cursorPointer);
                    Pointer<Byte> newCursor = cursorPointer.get();
                    if (!existingCursor.getCString().equals(newCursor.getCString())) {
                        existingCursor = newCursor;
                        Pointer<Pointer<?>> dataPointer = Pointer.allocatePointer();
                        Pointer<SizeT> sizePointer = Pointer.allocateSizeT();
                        while (SystemdJournalLibrary.sd_journal_enumerate_data(sdJournal, dataPointer, sizePointer) > 0) {
                            Pointer<Byte> data = dataPointer.as(Byte.class);
                            String line = data.getPointer(Byte.class).getCString();
                            list.add(line);
                        }
                        SystemdJournalLibrary.sd_journal_restart_data(sdJournal);
                    }
                }
            }
            if (listener != null) {
                for (String string : list) {
                    try {
                        listener.handleMessage(string);
                    } catch (IOException e) {
                        logger.log(Level.WARNING, e.getMessage(), e);
                    }
                }
            }
        }
    }
}
