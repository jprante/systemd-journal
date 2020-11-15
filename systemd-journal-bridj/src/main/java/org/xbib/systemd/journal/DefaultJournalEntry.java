package org.xbib.systemd.journal;

public class DefaultJournalEntry implements JournalEntry {

    private String message;

    private String messageId;

    private int priority;

    private String codeFile;

    private String codeLine;

    private String codeFunc;

    private String errno;

    private String syslogFacility;

    private String syslogIdentifier;

    private String syslogPid;

    private String syslogTimestamp;

    private String syslogRaw;

    private String pid;

    private String uid;

    private String gid;

    private String comm;

    private String exe;

    private String cmdLine;

    private String capEffective;

    private String auditSession;

    private String auditLoginUid;

    private String systemdCgroup;

    private String systemdSlice;

    private String systemdUnit;

    private String systemdUserSlice;

    private String systemdUserUnit;

    private String systemdSession;

    private String systemdOwnerUid;

    private String selinuxContext;

    private String sourceRealtimeTimestamp;

    private String bootId;

    private String machineId;

    private String systemdInvocationId;

    private String hostname;

    private String transport;

    private String streamId;

    private String lineBreak;

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    @Override
    public String getMessageId() {
        return messageId;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    public void setCodeFile(String codeFile) {
        this.codeFile = codeFile;
    }

    @Override
    public String getCodeFile() {
        return codeFile;
    }

    public void setCodeLine(String codeLine) {
        this.codeLine = codeLine;
    }

    @Override
    public String getCodeLine() {
        return codeLine;
    }

    public void setCodeFunc(String codeFunc) {
        this.codeFunc = codeFunc;
    }

    @Override
    public String getCodeFunc() {
        return codeFunc;
    }

    public void setErrno(String errno) {
        this.errno = errno;
    }

    @Override
    public String getErrno() {
        return errno;
    }

    public void setSyslogFacility(String syslogFacility) {
        this.syslogFacility = syslogFacility;
    }

    @Override
    public String getSyslogFacility() {
        return syslogFacility;
    }

    public void setSyslogIdentifier(String syslogIdentifier) {
        this.syslogIdentifier = syslogIdentifier;
    }

    @Override
    public String getSyslogIdentifier() {
        return syslogIdentifier;
    }

    public void setSyslogPid(String syslogPid) {
        this.syslogPid = syslogPid;
    }

    @Override
    public String getSyslogPid() {
        return syslogPid;
    }

    public void setSyslogTimestamp(String syslogTimestamp) {
        this.syslogTimestamp = syslogTimestamp;
    }

    @Override
    public String getSyslogTimestamp() {
        return syslogTimestamp;
    }

    public void setSyslogRaw(String syslogRaw) {
        this.syslogRaw = syslogRaw;
    }

    @Override
    public String getSyslogRaw() {
        return syslogRaw;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    @Override
    public String getPid() {
        return pid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String getUid() {
        return uid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    @Override
    public String getGid() {
        return gid;
    }

    public void setComm(String comm) {
        this.comm = comm;
    }

    @Override
    public String getComm() {
        return comm;
    }

    public void setExe(String exe) {
        this.exe = exe;
    }

    @Override
    public String getExe() {
        return exe;
    }

    public void setCmdLine(String cmdline) {
        this.cmdLine = cmdline;
    }

    @Override
    public String getCmdLine() {
        return cmdLine;
    }

    public void setCapEffective(String capEffective) {
        this.capEffective = capEffective;
    }

    @Override
    public String getCapEffective() {
        return capEffective;
    }

    public void setAuditSession(String auditSession) {
        this.auditSession = auditSession;
    }

    @Override
    public String getAuditSession() {
        return auditSession;
    }

    public void setAuditLoginUid(String auditLoginUid) {
        this.auditLoginUid = auditLoginUid;
    }

    @Override
    public String getAuditLoginUid() {
        return auditLoginUid;
    }

    public void setSystemdCgroup(String systemdCgroup) {
        this.systemdCgroup = systemdCgroup;
    }

    @Override
    public String getSystemdCgroup() {
        return systemdCgroup;
    }

    public void setSystemdSlice(String systemdSlice) {
        this.systemdSlice = systemdSlice;
    }

    @Override
    public String getSystemdSlice() {
        return systemdSlice;
    }

    public void setSystemdUnit(String systemdUnit) {
        this.systemdUnit = systemdUnit;
    }

    @Override
    public String getSystemdUnit() {
        return systemdUnit;
    }

    public void setSystemdUserSlice(String systemdUserSlice) {
        this.systemdUserSlice = systemdUserSlice;
    }

    @Override
    public String getSystemdUserSlice() {
        return systemdUserSlice;
    }

    public void setSystemdUserUnit(String systemdUserUnit) {
        this.systemdUserUnit = systemdUserUnit;
    }

    @Override
    public String getSystemdUserUnit() {
        return systemdUserUnit;
    }

    public void setSystemdSession(String systemdSession) {
        this.systemdSession = systemdSession;
    }

    @Override
    public String getSystemdSession() {
        return systemdSession;
    }

    public void setSystemdOwnerUid(String systemdOwnerUid) {
        this.systemdOwnerUid = systemdOwnerUid;
    }

    @Override
    public String getSystemdOwnerUid() {
        return systemdOwnerUid;
    }

    public void setSelinuxContext(String selinuxContext) {
        this.selinuxContext = selinuxContext;
    }

    @Override
    public String getSelinuxContext() {
        return selinuxContext;
    }

    public void setSourceRealtimeTimestamp(String sourceRealtimeTimestamp) {
        this.sourceRealtimeTimestamp = sourceRealtimeTimestamp;
    }

    @Override
    public String getSourceRealtimeTimestamp() {
        return sourceRealtimeTimestamp;
    }

    public void setBootId(String bootId) {
        this.bootId = bootId;
    }

    @Override
    public String getBootId() {
        return bootId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    @Override
    public String getMachineId() {
        return machineId;
    }

    public void setSystemdInvocationId(String systemdInvocationId) {
        this.systemdInvocationId = systemdInvocationId;
    }

    @Override
    public String getSystemdInvocationId() {
        return systemdInvocationId;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    @Override
    public String getHostname() {
        return hostname;
    }

    public void setTransport(String transport) {
        this.transport = transport;
    }

    @Override
    public String getTransport() {
        return transport;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }

    @Override
    public String getStreamId() {
        return streamId;
    }

    public void setLineBreak(String lineBreak) {
        this.lineBreak = lineBreak;
    }

    @Override
    public String getLineBreak() {
        return lineBreak;
    }

    @Override
    public String getFieldValue(String fieldName) {
        return null;
    }

    @Override
    public String toString() {
        return "priority=" + this.priority + ",message=" + this.message;
    }
}
