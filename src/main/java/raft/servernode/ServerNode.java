package raft.servernode;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ServerNode extends Thread implements RAFTServerNodeAPI  {
    private Boolean isStub;

    // Server Configuration
    private String nodeId;
    private String host;
    private List<ServerConfig> otherServerNodeConfigs;
    private List<RAFTServerNodeAPI> otherServerNodes;

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

    private Random random;

    public ServerNode(ServerConfig config) throws RemoteException, NotBoundException {
        this.isStub = false;
        this.nodeId = config.nodeId;
        this.host = config.host;
        this.otherServerNodes = new ArrayList<RAFTServerNodeAPI>();
        this.otherServerNodeConfigs = config.otherServerNodes;
        random = new Random();
    }

    @Override
    public synchronized void start() {
        try {
            RAFTServerNodeAPI serverStub = (RAFTServerNodeAPI) UnicastRemoteObject.exportObject(this, 0);

            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.getRegistry();
            try {
                registry.unbind(nodeId);
            } catch (Exception e) {}
            registry.bind(nodeId, serverStub);

            System.err.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
            return;
        }

        for (ServerConfig serverConfig: otherServerNodeConfigs) {
            int numOfAttempts = 1;
            while (true) {
                System.out.println("Connection to " + serverConfig.nodeId + " attempt " + numOfAttempts);
                try {
                    Registry registry = LocateRegistry.getRegistry(serverConfig.host);
                    RAFTServerNodeAPI serverNodeStub = (RAFTServerNodeAPI) registry.lookup(serverConfig.nodeId);
                    otherServerNodes.add(serverNodeStub);
                    System.out.println("Connection to " + serverConfig.nodeId + " successful");
                    break;
                } catch (Exception e) {
                    System.out.println("Unable to connect to " + serverConfig.nodeId + ": retrying in 1 second");
                    numOfAttempts++;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {}
                }
            }
        }
        System.out.println("Successfully connected to all other server nodes, system ready for use");
        
        super.start();
    }

    private int randomTimeout() {
        return random.nextInt(1000) + 1001;
    }

    @Override
    public void run() {
        while (true) {
            int randomTimeout = randomTimeout();
            System.out.println("Sleeping for " + randomTimeout + " ms");
            try {
                Thread.sleep(randomTimeout);
                System.out.println("Server now leader");
                serverState = ServerState.leader;
                sendHeartbeats();
            } catch (Exception e) {
                if (e instanceof InterruptedException) {
                    System.out.println("Interrupted, resetting timeout");
                }
            }
        }
        
    }

    private void sendHeartbeats() {
        for (RAFTServerNodeAPI remoteServer: otherServerNodes) {
            AppendEntriesResult appendEntriesResult;
            try {
                appendEntriesResult = remoteServer.appendEntries(currentTerm, host, currentTerm, null, commitIndex);
                System.out.println(appendEntriesResult);
            } catch (RemoteException e) {}
            RequestVoteResult requestVoteResult;
            try {
                requestVoteResult = remoteServer.requestVote(currentTerm, host, currentTerm, commitIndex);
                System.out.println(requestVoteResult);
            } catch (RemoteException e) {}
        }
    }

    private void requestVotes() {
        for (RAFTServerNodeAPI remoteServer: otherServerNodes) {
            RequestVoteResult requestVoteResult;
            try {
                requestVoteResult = remoteServer.requestVote(currentTerm, host, currentTerm, commitIndex);
                System.out.println(requestVoteResult);
            } catch (RemoteException e) {}
        }
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
        System.out.println("AppendEntries called, interrupting");
        interrupt();
        return new AppendEntriesResult(1, false);
    }

    @Override
    public RequestVoteResult requestVote(Integer term, String candidateId, Integer lastLogIndex, Integer lastLogTerm) {
        System.out.println("RequestVote called");
        return new RequestVoteResult(1, false);
    }
    
}
