package raft.servernode;

import java.util.ArrayList;
import java.util.List;

public class ServerConfig {
    public final String nodeId;
    public final String host;
    public final List<ServerConfig> otherServerNodes;

    // Persistent State
    public final Integer currentTerm;
    public final String votedFor;
    public final List<LogEntry> log;

    public ServerConfig(String nodeId, String host, List<ServerConfig> otherServerNodes) {
        this.nodeId = nodeId;
        this.host = host;
        this.otherServerNodes = otherServerNodes;
        currentTerm = null;
        votedFor = null;
        log = new ArrayList<LogEntry>();
    }

    public ServerConfig(String nodeId, String host, List<ServerConfig> otherServerNodes, Integer currentTerm,
            String votedFor, List<LogEntry> log) {
        this.nodeId = nodeId;
        this.host = host;
        this.otherServerNodes = otherServerNodes;

        this.currentTerm = currentTerm;
        this.votedFor = votedFor;
        this.log = log;
    }

    // TODO fromJson constructor
}
