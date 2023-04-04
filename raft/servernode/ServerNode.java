package raft.servernode;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ServerNode implements RAFTServerNodeAPI {
    private Boolean isStub;

    // Server Configuration
    private String nodeId;
    private String host;
    private List<ServerNode> otherServerNodes;

    // Persistent State
    private Integer currentTerm = 0;
    private String votedFor;
    private List<LogEntry> log;

    // Volatile State
    private Integer commitIndex = 0;
    private Integer lastApplied = 0;
    private ServerState serverState = ServerState.follower;

    // Volatile Leader State
    private Map<String, Integer> nextIndex;
    private Map<String, Integer> matchIndex;

    public ServerNode(ServerConfig config) throws RemoteException, NotBoundException {
        this.isStub = false;
        this.nodeId = config.nodeId;
        this.host = config.host;
        this.otherServerNodes = new ArrayList<ServerNode>();
        for (ServerConfig serverConfig: config.otherServerNodes) {
            Registry registry = LocateRegistry.getRegistry(serverConfig.host);
            ServerNode serverNodeStub = (ServerNode) registry.lookup(serverConfig.nodeId);
            serverNodeStub.setAsStub(serverConfig);
            otherServerNodes.add(serverNodeStub);
        }
    }

    public void initialize() {
        
    }

    public String getNodeId() {
        return nodeId;
    }

    public String getHost() {
        return host;
    }

    public Boolean getIsStub() {
        return isStub;
    }

    public void setAsStub(ServerConfig serverConfig) {
        isStub = true;
        nodeId = serverConfig.nodeId;
        host = serverConfig.host;
    }


    @Override
    public AppendEntriesResult appendEntries(Integer term, String leaderId, Integer prevLogTerm, Integer[] entries,
            Integer leaderCommit) {
        return new AppendEntriesResult(1, false);
    }

    @Override
    public RequestVoteResult requestVote(Integer term, String candidateId, Integer lastLogIndex, Integer lastLogTerm) {
        return new RequestVoteResult(1, false);
    }

    public static void main(String[] args) {
        
    }
    
}
