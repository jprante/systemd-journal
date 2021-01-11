package org.xbib.log4j.systemd;

import com.sun.jna.Native;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.List;

/**
 * The systemd library API, loaded by Java Native Access (JNA).
 *
 * The native library is loaded only once, so this class is a singleton.
 */
public class SystemdLibraryAPI {

    private static final SystemdLibraryAPI instance = new SystemdLibraryAPI();

    private final SystemdLibrary systemdLibrary;

    private SystemdLibraryAPI() {
        this.systemdLibrary = loadLibrary();
    }

    public static SystemdLibraryAPI getInstance() {
        return instance;
    }

    public int journal_send(String format, Object... args) {
        return systemdLibrary.sd_journal_send(format, args);
    }

    public int journal_send(String format, List<Object> args) {
        return systemdLibrary.sd_journal_send(format, args.toArray());
    }

    private static SystemdLibrary loadLibrary() {
        try {
            return (SystemdLibrary) AccessController.doPrivileged((PrivilegedAction<Object>) () -> Native.load("systemd", SystemdLibrary.class));
        } catch (UnsatisfiedLinkError e) {
            throw new RuntimeException("Failed to load systemd library." +
                    " Please note that JNA requires an executable temporary folder." +
                    " It can be explicitly defined with -Djna.tmpdir", e);
        }
    }
}
