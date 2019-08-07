package org.xbib.systemd;

public interface JournalEntry {

    String getMessage();

    String getMessageId();

    int getPriority();

    String getCodeFile();

    String getCodeLine();

    String getCodeFunc();

    String getErrno();

    String getSyslogFacility();

    String getSyslogIdentifier();

    String getSyslogPid();

    String getSyslogTimestamp();

    String getSyslogRaw();

    String getPid();

    String getUid();

    String getGid();

    String getComm();

    String getExe();

    String getCmdLine();

    String getCapEffective();

    String getAuditSession();

    String getAuditLoginUid();

    String getSystemdCgroup();

    String getSystemdSlice();

    String getSystemdUnit();

    String getSystemdUserSlice();

    String getSystemdUserUnit();

    String getSystemdSession();

    String getSystemdOwnerUid();

    String getSelinuxContext();

    String getSourceRealtimeTimestamp();

    String getBootId();

    String getMachineId();

    String getSystemdInvocationId();

    String getHostname();

    String getTransport();

    String getStreamId();

    String getLineBreak();

    String getFieldValue(String fieldName);
}
