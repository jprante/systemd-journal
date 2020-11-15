package org.xbib.systemd.journal;

import java.io.IOException;

public interface SystemdJournalListener {

    void handleEntry(JournalEntry entry) throws IOException;
}
