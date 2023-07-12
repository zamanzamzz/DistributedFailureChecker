# DistributedFailureChecker

## Implementation Roadmap
1. Atomic Register implementation with clean understandable code.
2. Atomic Register implementation that works with RaftNodes on different JVM processes.
3. Implement a Java UI (using Swing?) that can deploy an arbitrary number of raft nodes, and shows the internal state of each node. Can also add delays and longer timeouts to more easily visualize everything.
4. Extend Atomic Register implementation to be a counter.
5. Update java UI to handle counter specific behaviour, and to choose between atomic register and counter.
6. Implement a test framework which can validate the behaviour of the raft nodes. Look over notes of vector timestamps and possibly use that.
7. Implement own version of RAFT, and replace MicroRAFT in implementation for the counter and atomic register.
8. Reread DFD report and implement it using own version of RAFT.
9. Update Java UI to also have a DFD option.
10. Update Java UI to also visualize the FD heartbeats.