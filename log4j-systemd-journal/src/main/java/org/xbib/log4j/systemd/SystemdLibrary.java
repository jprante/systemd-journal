package org.xbib.log4j.systemd;

import com.sun.jna.Library;

public interface SystemdLibrary extends Library {

    int sd_journal_send(String format, Object... args);
}
