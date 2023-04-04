package RAFT.ServerNode;

public class AppendEntriesResult { 
    public final Integer term; 
    public final Boolean result; 
    public AppendEntriesResult(Integer term, Boolean result) { 
      this.term = term; 
      this.result = result; 
    }
}
