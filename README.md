# Systemd journal for Java

[![Build Status](https://travis-ci.org/jprante/systemd-journal-appender.png?branch=master)](https://travis-ci.org/jprante/systemd-journal)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.xbib/log4j-systemd-journal/badge.svg)](http://maven-badges.herokuapp.com/maven-central/org.xbib/log4j-systemd-journal)
[![Apache License](https://img.shields.io/github/license/jprante/log4j-systemd-journal.svg)](https://opensource.org/licenses/Apache-2.0)
[![Donate](https://img.shields.io/badge/Donate-PayPal-green.svg)](https://www.paypal.me/JoergPrante)

## Reading systemd-journal from Java

Please see the junit test file to find out how to consume systemd journal from Java.

The implementation use bridj.

## Log4j2 systemd-journal appender

This [Log4j][log4j] appender logs event meta data such as timestamp, logger name, exception stacktrace, 
[ThreadContext (MDC)][thread-context] or the thread name to [fields][systemd-journal-fields] 
in [systemd journal][systemd-journal].

Learn more about systemd-journal at Lennart Poettering's site [systemd for Developers III][systemd-for-developers] 
or at the manual page [systemd journal][systemd-journal].

## Usage
Add the following Maven dependency to your project:

Gradle
```
dependency {
  runtime "org.xbib:log4j-systemd-journal:2.13.3.0"
}
```

### Runtime dependencies ###

- Java 11+
- Linux with systemd library installed (/usr/lib64/libsystemd.so)
- Log4j 2.12.0+

**Note:**

JNA requires execute permissions in `java.io.tmpdir` (which defaults to `/tmp`).
For example, if the folder is mounted with "`noexec`" for security reasons, you need to define a different temporary directory for JNA:

    -Djna.tmpdir=/tmp-folder/with/exec/permissions

## Configuration

The appender can be configured with the following properties

Property name         | Default           | Type    | Description
--------------------- | ----------------- | ------- | -----------
`logSource`           | false             | boolean | Determines whether the log locations are logged. Note that there is a performance overhead when switched on. The data is logged in standard systemd journal fields `CODE_FILE`, `CODE_LINE` and `CODE_FUNC`.
`logStacktrace`       | true              | boolean | Determines whether the full exception stack trace is logged. This data is logged in the user field `STACKTRACE`.
`logThreadName`       | true              | boolean | Determines whether the thread name is logged. This data is logged in the user field `THREAD_NAME`.
`logLoggerName`       | true              | boolean | Determines whether the logger name is logged. This data is logged in the user field `LOG4J_LOGGER`.
`logAppenderName`     | true              | boolean | Determines whether the appender name is logged. This data is logged in the user field `LOG4J_APPENDER`.
`logThreadContext`    | true              | boolean | Determines whether the [thread context][thread-context] is logged. Each key/value pair is logged as user field with the `threadContextPrefix` prefix.
`threadContextPrefix` | `THREAD_CONTEXT_` | String  | Determines how [thread context][thread-context] keys should be prefixed when `logThreadContext` is set to true. Note that keys need to match the regex pattern `[A-Z0-9_]+` and are normalized otherwise.
`syslogIdentifier`    | null              | String  | This data is logged in the user field `SYSLOG_IDENTIFIER`.  If this is not set, the underlying system will use the command name (usually `java`) instead.

## Example ##

### `log4j2.xml`
```xml
<?xml version="1.0" encoding="UTF-8"?>
<Configuration status="INFO" packages="org.xbib.log4j.systemd">
    <Appenders>
        <Console name="console" target="SYSTEM_OUT">
            <PatternLayout pattern="%d{HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n" />
        </Console>
        <SystemdJournalAppender name="journal" logStacktrace="true" logSource="false" />
    </Appenders>
    <Loggers>
        <Root level="INFO">
            <AppenderRef ref="console" />
            <AppenderRef ref="journal" />
        </Root>
    </Loggers>
</Configuration>
```

This will tell Log4j to log to [systemd journal][systemd-journal] as well as to stdout (console).
Note that a layout is optional for `SystemdJournal`.
This is because meta data of a log event such as the timestamp, the logger name or the Java thread name are mapped to [systemd-journal fields][systemd-journal-fields] and need not be rendered into a string that loses all the semantic information.

### `YourExample.java`
```java
import org.apache.logging.log4j.*;

class YourExample {

    private static Logger logger = LogManager.getLogger(YourExample.class);

    public static void main(String[] args) {
        ThreadContext.put("MY_KEY", "some value");
        logger.info("this is an example");
    }
}
```

Running this sample class will log a message to journald:

### Systemd Journal

```
# journalctl -n
Okt 13 21:26:00 myhost java[2370]: this is an example
```

Use `journalctl -o verbose` to show all fields:

```
# journalctl -o verbose -n
Di 2015-09-29 21:07:05.850017 CEST [s=45e0…;i=984;b=c257…;m=1833…;t=520e…;x=3e1e…]
    PRIORITY=6
    _TRANSPORT=journal
    _UID=1000
    _GID=1000
    _CAP_EFFECTIVE=0
    _SYSTEMD_OWNER_UID=1000
    _SYSTEMD_SLICE=user-1000.slice
    _MACHINE_ID=4abc6d…
    _HOSTNAME=myhost
    _SYSTEMD_CGROUP=/user.slice/user-1000.slice/session-2.scope
    _SYSTEMD_SESSION=2
    _SYSTEMD_UNIT=session-2.scope
    _BOOT_ID=c257f8…
    THREAD_NAME=main
    LOG4J_LOGGER=org.xbib.log4j.systemd.SystemdJournalAppenderIntegrationTest
    _COMM=java
    _EXE=/usr/bin/java
    MESSAGE=this is a test message with a MDC
    CODE_FILE=SystemdJournalAppenderIntegrationTest.java
    CODE_FUNC=testMessageWithMDC
    CODE_LINE=36
    THREAD_CONTEXT_MY_KEY=some value
    SYSLOG_IDENTIFIER=log4j2-test
    LOG4J_APPENDER=Journal
    _PID=8224
    _CMDLINE=/usr/bin/java …
    _SOURCE_REALTIME_TIMESTAMP=1443553625850017
```

Note that the [ThreadContext][thread-context] key-value pair `{"MY_KEY": "some value"}` is automatically added as field with prefix `THREAD_CONTEXT`.

You can use the power of [systemd journal][systemd-journal] to filter for interesting messages. Example:

`journalctl CODE_FUNC=testMessageWithMDC THREAD_NAME=main` will only show messages that are logged from the Java main thread in a method called `testMessageWithMDC`.

## Bridj or JNA

As you noted, I use both bridj and JNA. But only one is necessary. The only reason for this is that it works. 

bridj looks easier and more powerful, but is getting old. I am considering a fork of bridj and implement a log4j2 appender for bridj, or porting the API methods `sd_journal_open`, `sd_journal_add_match`, etc.  to JNA.

Feel free to submit patches.

