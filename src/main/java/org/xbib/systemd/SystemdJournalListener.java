package org.xbib.systemd;

import java.io.IOException;

public interface SystemdJournalListener {

    void handleEntry(JournalEntry entry) throws IOException;
}
