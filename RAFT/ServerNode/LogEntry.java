package RAFT.ServerNode;

public class LogEntry {
    public final String command;
    public final Integer termReceived;

    LogEntry(String command, Integer termReceived) {
        this.command = command;
        this.termReceived = termReceived;
    }
}
