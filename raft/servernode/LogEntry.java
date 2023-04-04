package raft.servernode;

import java.io.Serializable;

public class LogEntry implements Serializable {
    public final String command;
    public final Integer termReceived;

    LogEntry(String command, Integer termReceived) {
        this.command = command;
        this.termReceived = termReceived;
    }

    @Override
    public String toString() {
        return "LogEntry: command: " + command + " termReceived: " + termReceived;
    }
}
