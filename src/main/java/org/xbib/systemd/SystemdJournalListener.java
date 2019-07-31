package org.xbib.systemd;

import java.io.IOException;

public interface SystemdJournalListener {

    void handleMessage(String message) throws IOException;
}
