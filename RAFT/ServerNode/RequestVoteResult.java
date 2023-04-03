package RAFT.ServerNode;

public class RequestVoteResult { 
    public final Integer term; 
    public final Boolean voteGranted; 
    public RequestVoteResult(Integer term, Boolean voteGranted) { 
      this.term = term; 
      this.voteGranted = voteGranted; 
    }
}
