package raft.servernode;

import java.io.Serializable;

public class AppendEntriesResult implements Serializable { 
    public final Integer term; 
    public final Boolean result; 
    public AppendEntriesResult(Integer term, Boolean result) { 
      this.term = term; 
      this.result = result; 
    }

    @Override
    public String toString() {
        return "AppendEntriesResult: term: " + term + " result: " + result;
    }
}
