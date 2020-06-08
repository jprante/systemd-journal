package org.xbib.log4j.systemd;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Core;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.util.Booleans;
import org.apache.logging.log4j.util.ReadOnlyStringMap;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

/**
 * Log4j appender for systemd journal.
 */
@Plugin(name = "SystemdJournalAppender", category = Core.CATEGORY_NAME, elementType = Appender.ELEMENT_TYPE, printObject = true)
public class SystemdJournalAppender extends AbstractAppender {

    private final SystemdLibraryAPI systemdLibraryAPI;

    private final boolean logSource;

    private final boolean logStacktrace;

    private final boolean logThreadName;

    private final boolean logLoggerName;

    private final boolean logAppenderName;

    private final boolean logThreadContext;

    private final String threadContextPrefix;

    private final String syslogIdentifier;

    public SystemdJournalAppender(String name, Filter filter, Layout<?> layout, boolean ignoreExceptions,
                                  SystemdLibraryAPI systemdLibraryAPI,
                                  boolean logSource, boolean logStacktrace, boolean logThreadName,
                                  boolean logLoggerName, boolean logAppenderName, boolean logThreadContext,
                                  String threadContextPrefix, String syslogIdentifier) {
        super(name, filter, layout, ignoreExceptions, Property.EMPTY_ARRAY);
        this.systemdLibraryAPI = systemdLibraryAPI != null ? systemdLibraryAPI : SystemdLibraryAPI.getInstance();
        this.logSource = logSource;
        this.logStacktrace = logStacktrace;
        this.logThreadName = logThreadName;
        this.logLoggerName = logLoggerName;
        this.logAppenderName = logAppenderName;
        this.logThreadContext = logThreadContext;
        if (threadContextPrefix == null) {
            this.threadContextPrefix = "THREAD_CONTEXT_";
        } else {
            this.threadContextPrefix = normalizeKey(threadContextPrefix);
        }
        this.syslogIdentifier = syslogIdentifier;
    }

    @PluginFactory
    public static SystemdJournalAppender createAppender(@PluginAttribute("name") final String name,
            @PluginAttribute("ignoreExceptions") final String ignoreExceptionsString,
            @PluginAttribute("logSource") final String logSourceString,
            @PluginAttribute("logStacktrace") final String logStacktraceString,
            @PluginAttribute("logLoggerName") final String logLoggerNameString,
            @PluginAttribute("logAppenderName") final String logAppenderNameString,
            @PluginAttribute("logThreadName") final String logThreadNameString,
            @PluginAttribute("logThreadContext") final String logThreadContextString,
            @PluginAttribute("threadContextPrefix") final String threadContextPrefix,
            @PluginAttribute("syslogIdentifier") final String syslogIdentifier,
            @PluginElement("Layout") final Layout<?> layout,
            @PluginElement("Filter") final Filter filter,
            @PluginConfiguration final Configuration config) {
        boolean ignoreExceptions = Booleans.parseBoolean(ignoreExceptionsString, true);
        boolean logSource = Booleans.parseBoolean(logSourceString, false);
        boolean logStacktrace = Booleans.parseBoolean(logStacktraceString, true);
        boolean logThreadName = Booleans.parseBoolean(logThreadNameString, true);
        boolean logLoggerName = Booleans.parseBoolean(logLoggerNameString, true);
        boolean logAppenderName = Booleans.parseBoolean(logAppenderNameString, true);
        boolean logThreadContext = Booleans.parseBoolean(logThreadContextString, true);
        if (name == null) {
            LOGGER.error("No name provided for SystemdJournalAppender");
            return null;
        }
        SystemdLibraryAPI systemdLibraryAPI = SystemdLibraryAPI.getInstance();
        return new SystemdJournalAppender(name, filter, layout, ignoreExceptions,
                systemdLibraryAPI,
                logSource, logStacktrace, logThreadName, logLoggerName, logAppenderName,
                logThreadContext, threadContextPrefix, syslogIdentifier);
    }

    private int log4jLevelToJournalPriority(Level level) {
        switch (level.getStandardLevel()) {
        case FATAL:
            return 2;
        case ERROR:
            return 3;
        case WARN:
            return 4;
        case INFO:
            return 6;
        case DEBUG:
        case TRACE:
            return 7;
        default:
            throw new IllegalArgumentException("unable to map log level: " + level);
        }
    }

    @Override
    public void append(LogEvent event) {
        List<Object> args = new ArrayList<>();
        args.add(buildFormattedMessage(event));
        args.add("PRIORITY=%d");
        args.add(log4jLevelToJournalPriority(event.getLevel()));
        if (logThreadName) {
            args.add("THREAD_NAME=%s");
            args.add(event.getThreadName());
        }
        if (logLoggerName) {
            args.add("LOG4J_LOGGER=%s");
            args.add(event.getLoggerName());
        }
        if (logAppenderName) {
            args.add("LOG4J_APPENDER=%s");
            args.add(getName());
        }
        if (logStacktrace && event.getThrown() != null) {
            args.add("STACKTRACE=%s");
            args.add(ExceptionFormatter.format(event.getThrown()));
        }
        if (logSource && event.getSource() != null) {
            String fileName = event.getSource().getFileName();
            args.add("CODE_FILE=%s");
            args.add(fileName);
            String methodName = event.getSource().getMethodName();
            args.add("CODE_FUNC=%s");
            args.add(methodName);
            int lineNumber = event.getSource().getLineNumber();
            args.add("CODE_LINE=%d");
            args.add(lineNumber);
        }
        if (logThreadContext) {
            ReadOnlyStringMap context = event.getContextData();
            if (context != null) {
                for (Entry<String, String> entry : context.toMap().entrySet()) {
                    String key = entry.getKey();
                    args.add(threadContextPrefix + normalizeKey(key) + "=%s");
                    args.add(entry.getValue());
                }
            }
        }
        if (syslogIdentifier != null && !syslogIdentifier.isEmpty()) {
            args.add("SYSLOG_IDENTIFIER=%s");
            args.add(syslogIdentifier);
        }
        args.add(null);
        int rc = systemdLibraryAPI.journal_send("MESSAGE=%s", args);
        if (rc != 0) {
            LOGGER.error("sd_journal_send failed: " + rc);
        }
    }

    private String buildFormattedMessage(LogEvent event) {
        if (getLayout() != null) {
            return new String(getLayout().toByteArray(event), StandardCharsets.UTF_8);
        }
        return event.getMessage().getFormattedMessage();
    }

    private static String normalizeKey(String key) {
        return key.toUpperCase().replaceAll("[^_A-Z0-9]", "_");
    }
}
