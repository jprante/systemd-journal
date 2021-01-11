package org.xbib.systemd.journal;

import com.sun.jna.Library;
import com.sun.jna.Pointer;
import com.sun.jna.StringArray;
import com.sun.jna.Structure;

public interface SystemdLibrary extends Library {

    int sd_journal_send(String format, Object... args);

    int sd_journal_open(SdJournal sdJournal, int flag);

    int sd_journal_get_fd(SdJournal sdJournal);

    int sd_journal_seek_tail(SdJournal sdJournal);

    int sd_journal_previous(SdJournal sdJournal);

    int sd_journal_next(SdJournal sdJournal);

    int sd_journal_get_cursor(SdJournal sdJournal, StringArray cursor);

    int sd_journal_add_match(SdJournal sdJournal, String match, int len);

    int sd_journal_wait(SdJournal sdJournal, long timeout);

    int sd_journal_get_data(SdJournal sdJournal, String field, Pointer data, Pointer length);

    int sd_journal_enumerate_data(SdJournal sdJournal, Pointer data, Pointer length);

    int sd_journal_restart_data(SdJournal sdJournal);

    class SdJournal extends Structure {
        public int top_level_fd;
        public String path;
        public String prefix;
        public String namespace;
    }
}
