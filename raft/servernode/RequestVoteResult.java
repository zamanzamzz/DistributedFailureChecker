package raft.servernode;

import java.io.Serializable;

public class RequestVoteResult implements Serializable { 
    public final Integer term; 
    public final Boolean voteGranted; 
    public RequestVoteResult(Integer term, Boolean voteGranted) { 
      this.term = term; 
      this.voteGranted = voteGranted; 
    }

    @Override
    public String toString() {
      return "RequestVoteResult: term: " + term + " voteGranted: " + voteGranted;
    }
}
