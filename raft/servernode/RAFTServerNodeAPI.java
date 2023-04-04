package raft.servernode;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RAFTServerNodeAPI extends Remote {
    AppendEntriesResult appendEntries(Integer term, String leaderId, Integer prevLogTerm, Integer[] entries,
            Integer leaderCommit) throws RemoteException;

    RequestVoteResult requestVote(Integer term, String candidateId, Integer lastLogIndex, Integer lastLogTerm)
            throws RemoteException;
}