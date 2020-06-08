package org.xbib.log4j.systemd;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.spi.DefaultThreadContextMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@EnabledOnOs({OS.LINUX})
@ExtendWith(MockitoExtension.class)
class SystemdJournalAppenderTest {

    @Mock
    private Message message;

    @Mock
    private SystemdLibraryAPI api;

    @BeforeEach
    void prepare() {
        ThreadContext.clearAll();
    }

    @Test
    void testSimple() {
        SystemdLibraryAPI api = mock(SystemdLibraryAPI.class);
        Message message = mock(Message.class);
        SystemdJournalAppender journalAppender =
                new SystemdJournalAppender("Journal", null, null, false, api,
                false, false, false, false, false, false, null, null);
        when(message.getFormattedMessage()).thenReturn("some message");
        LogEvent event = new Log4jLogEvent.Builder().setMessage(message).setLevel(Level.INFO).build();
        journalAppender.append(event);
        List<Object> expectedArgs = new ArrayList<>();
        expectedArgs.add("some message");
        expectedArgs.add("PRIORITY=%d");
        expectedArgs.add(6);
        expectedArgs.add(null);
        verify(api).journal_send("MESSAGE=%s", expectedArgs);
    }

    @Test
    void testLogSource() {
        SystemdLibraryAPI api = mock(SystemdLibraryAPI.class);
        Message message = mock(Message.class);
        SystemdJournalAppender journalAppender =
                new SystemdJournalAppender("Journal", null, null, false, api,
                true, false, false, false, false, false, null, null);
        when(message.getFormattedMessage()).thenReturn("some message");
        LogEvent event = new Log4jLogEvent.Builder() //
                .setMessage(message)//
                .setLoggerFqcn(journalAppender.getClass().getName())//
                .setLevel(Level.INFO).build();
        event.setIncludeLocation(true);
        journalAppender.append(event);
        List<Object> expectedArgs = new ArrayList<>();
        expectedArgs.add("some message");
        expectedArgs.add("PRIORITY=%d");
        expectedArgs.add(6);
        expectedArgs.add("CODE_FILE=%s");
        expectedArgs.add("SystemdJournalAppenderTest.java");
        expectedArgs.add("CODE_FUNC=%s");
        expectedArgs.add("testLogSource");
        expectedArgs.add("CODE_LINE=%d");
        expectedArgs.add(69);
        expectedArgs.add(null);
        verify(api).journal_send("MESSAGE=%s", expectedArgs);
    }

    @Test
    void testDoNotLogException() {
        SystemdLibraryAPI api = mock(SystemdLibraryAPI.class);
        Message message = mock(Message.class);
        SystemdJournalAppender journalAppender =
                new SystemdJournalAppender("Journal", null, null, false, api,
                false, false, false, false, false, false, null, null);
        when(message.getFormattedMessage()).thenReturn("some message");
        LogEvent event = new Log4jLogEvent.Builder()
                .setMessage(message)
                .setLoggerFqcn(journalAppender.getClass().getName())
                .setThrown(new Throwable())
                .setLevel(Level.INFO).build();
        event.setIncludeLocation(true);
        journalAppender.append(event);
        List<Object> expectedArgs = new ArrayList<>();
        expectedArgs.add("some message");
        expectedArgs.add("PRIORITY=%d");
        expectedArgs.add(6);
        expectedArgs.add(null);
        verify(api).journal_send("MESSAGE=%s", expectedArgs);
    }

    @Test
    void testThreadAndContext() {
        SystemdLibraryAPI api = mock(SystemdLibraryAPI.class);
        Message message = mock(Message.class);
        SystemdJournalAppender journalAppender =
                new SystemdJournalAppender("Journal", null, null, false, api,
                false, false, true, true, true, true, null, "some-identifier");
        when(message.getFormattedMessage()).thenReturn("some message");
        DefaultThreadContextMap contextMap = new DefaultThreadContextMap();
        LogEvent event = mock(LogEvent.class);
        when(event.getMessage()).thenReturn(message);
        when(event.getLoggerName()).thenReturn("some logger");
        when(event.getLevel()).thenReturn(Level.INFO);
        when(event.getThreadName()).thenReturn("the thread");
        when(event.getContextData()).thenReturn(contextMap);
        contextMap.put("foo%s$1%d", "bar");
        journalAppender.append(event);
        List<Object> expectedArgs = new ArrayList<>();
        expectedArgs.add("some message");
        expectedArgs.add("PRIORITY=%d");
        expectedArgs.add(6);
        expectedArgs.add("THREAD_NAME=%s");
        expectedArgs.add("the thread");
        expectedArgs.add("LOG4J_LOGGER=%s");
        expectedArgs.add("some logger");
        expectedArgs.add("LOG4J_APPENDER=%s");
        expectedArgs.add("Journal");
        expectedArgs.add("THREAD_CONTEXT_FOO_S_1_D=%s");
        expectedArgs.add("bar");
        expectedArgs.add("SYSLOG_IDENTIFIER=%s");
        expectedArgs.add("some-identifier");
        expectedArgs.add(null);
        verify(api).journal_send("MESSAGE=%s", expectedArgs);
    }
}
