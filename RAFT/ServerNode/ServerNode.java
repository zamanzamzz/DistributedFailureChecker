package RAFT.ServerNode;

import java.rmi.RemoteException;

public class ServerNode implements RAFTServerNodeAPI {
    private Integer currentTerm;
    private String votedFor;


    @Override
    public AppendEntriesResult appendEntries(Integer term, String leaderId, Integer prevLogTerm, Integer[] entries,
            Integer leaderCommit) throws RemoteException {
        return new AppendEntriesResult(1, false);
    }

    @Override
    public RequestVoteResult requestVote(Integer term, String candidateId, Integer lastLogIndex, Integer lastLogTerm)
            throws RemoteException {
        return new RequestVoteResult(1, false);
    }

    public static void main(String[] args) {
        
    }
    
}
