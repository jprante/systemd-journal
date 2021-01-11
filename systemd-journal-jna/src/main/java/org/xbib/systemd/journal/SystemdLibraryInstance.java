package org.xbib.systemd.journal;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.StringArray;

import java.util.List;

/**
 * The systemd library API, loaded by Java Native Access (JNA).
 *
 * The native library is loaded only once, so this class is a singleton.
 */
public class SystemdLibraryInstance {

    private static final SystemdLibraryInstance instance = new SystemdLibraryInstance();

    private final SystemdLibrary systemdLibrary;

    private SystemdLibraryInstance() {
        this.systemdLibrary = loadLibrary();
    }

    public static SystemdLibraryInstance getInstance() {
        return instance;
    }

    public static final int SD_JOURNAL_LOCAL_ONLY = 1;

    public static final int SD_JOURNAL_RUNTIME_ONLY = 2;

    public static final int SD_JOURNAL_SYSTEM = 4;

    public static final int SD_JOURNAL_CURRENT_USER = 8;

    public static final int SD_JOURNAL_NOP = 0;

    public static final int SD_JOURNAL_APPEND = 1;

    public static final int SD_JOURNAL_INVALIDATE = 2;

    public static final String SD_ID128_FORMAT_STR = "%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x";

    public int sd_journal_send(String format, Object... args) {
        return systemdLibrary.sd_journal_send(format, args);
    }

    public int sd_journal_send(String format, List<Object> args) {
        return systemdLibrary.sd_journal_send(format, args.toArray());
    }

    public int sd_journal_open(SystemdLibrary.SdJournal sdJournal, int flag) {
        return systemdLibrary.sd_journal_open(sdJournal, flag);
    }

    public int sd_journal_get_fd(SystemdLibrary.SdJournal sdJournal) {
        return systemdLibrary.sd_journal_get_fd(sdJournal);
    }

    public int sd_journal_seek_tail(SystemdLibrary.SdJournal sdJournal) {
        return systemdLibrary.sd_journal_seek_tail(sdJournal);
    }

    public int sd_journal_previous(SystemdLibrary.SdJournal sdJournal) {
        return systemdLibrary.sd_journal_previous(sdJournal);
    }

    public int sd_journal_next(SystemdLibrary.SdJournal sdJournal) {
        return systemdLibrary.sd_journal_next(sdJournal);
    }

    public int sd_journal_get_cursor(SystemdLibrary.SdJournal sdJournal, StringArray cursor) {
        return systemdLibrary.sd_journal_get_cursor(sdJournal, cursor);
    }

    public int sd_journal_add_match(SystemdLibrary.SdJournal sdJournal, String match, int len) {
        return systemdLibrary.sd_journal_add_match(sdJournal, match, len);
    }

    public int sd_journal_wait(SystemdLibrary.SdJournal sdJournal, long timeout_musec) {
        return systemdLibrary.sd_journal_wait(sdJournal, timeout_musec);
    }

    public int sd_journal_get_data(SystemdLibrary.SdJournal sdJournal, String field, Pointer data, Pointer length) {
        return systemdLibrary.sd_journal_get_data(sdJournal, field, data, length);
    }

    public int sd_journal_enumerate_data(SystemdLibrary.SdJournal sdJournal, Pointer data, Pointer length) {
        return systemdLibrary.sd_journal_enumerate_data(sdJournal, data, length);
    }

    public int sd_journal_restart_data(SystemdLibrary.SdJournal sdJournal) {
        return systemdLibrary.sd_journal_restart_data(sdJournal);
    }

    private static SystemdLibrary loadLibrary() {
        try {
            return Native.load("systemd", SystemdLibrary.class);
        } catch (UnsatisfiedLinkError e) {
            throw new RuntimeException("Failed to load systemd library." +
                    " Please note that JNA requires an executable temporary folder." +
                    " It can be explicitly defined with -Djna.tmpdir", e);
        }
    }
}
