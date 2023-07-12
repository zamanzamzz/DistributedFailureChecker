package atomicregisterlocal;

import io.microraft.Ordered;
import io.microraft.RaftEndpoint;
import io.microraft.RaftNode;
import io.microraft.RaftNodeStatus;
import io.microraft.report.RaftNodeReport;
import io.microraft.report.RaftNodeReportListener;
import io.microraft.report.RaftTerm;
import io.microraft.statemachine.StateMachine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.assertj.core.api.Assertions.assertThat;
/*
   TO RUN THIS TEST ON YOUR MACHINE:
   $ gh repo clone MicroRaft/MicroRaft
   $ cd MicroRaft && ./mvnw clean test -Dtest=io.microraft.tutorial.LeaderElectionTest -DfailIfNoTests=false -Ptutorial
   YOU CAN SEE THIS CLASS AT:
   https://github.com/MicroRaft/MicroRaft/blob/master/microraft-tutorial/src/test/java/io/microraft/tutorial/LeaderElectionTest.java
 */
public class OperationCommitTest {

    private List<RaftEndpoint> initialMembers = Arrays
            .asList(LocalRaftEndpoint.newEndpoint(), LocalRaftEndpoint.newEndpoint(), LocalRaftEndpoint.newEndpoint(),
                    LocalRaftEndpoint.newEndpoint());
    private List<LocalTransport> transports = new ArrayList<>();
    private List<RaftNode> raftNodes = new ArrayList<>();

    private class Listener implements RaftNodeReportListener {
        @Override
        public void accept(RaftNodeReport t) {
            // System.out.println(t);
        }
    }

    public void startRaftGroup() {
        Listener listener = new Listener();
        for (RaftEndpoint endpoint : initialMembers) {
            RaftNode raftNode = createRaftNode(endpoint, listener);
            raftNode.start();
        }
    }

    public void terminateRaftGroup() {
        raftNodes.forEach(RaftNode::terminate);
    }

    public void testLeaderElection() {
        RaftNode leader = waitUntilLeaderElected();

        // assertThat(leader).isNotNull();

        System.out.println(leader.getLocalEndpoint().getId() + " is the leader!");

        String value1 = "value1";
        Ordered<String> result1 = leader.<String>replicate(OperableAtomicRegister.newSetOperation(value1)).join();

        assertThat(result1.getCommitIndex()).isGreaterThan(0);
        assertThat(result1.getResult()).isNull();

        System.out.println("1st operation commit index: " + result1.getCommitIndex() + ", result: " + result1.getResult());

        String value2 = "value2";
        Ordered<String> result2 = leader.<String>replicate(OperableAtomicRegister.newSetOperation(value2)).join();

        assertThat(result2.getCommitIndex()).isGreaterThan(result1.getCommitIndex());
        assertThat(result2.getResult()).isEqualTo(value1);

        System.out.println("2nd operation commit index: " + result2.getCommitIndex() + ", result: " + result2.getResult());

        String value3 = "value3";
        Ordered<Boolean> result3 = leader.<Boolean>replicate(OperableAtomicRegister.newCasOperation(value2, value3)).join();

        assertThat(result3.getCommitIndex()).isGreaterThan(result2.getCommitIndex());
        assertThat(result3.getResult()).isTrue();

        System.out.println("3rd operation commit index: " + result2.getCommitIndex() + ", result: " + result3.getResult());

        String value4 = "value4";
        Ordered<Boolean> result4 = leader.<Boolean>replicate(OperableAtomicRegister.newCasOperation(value2, value4)).join();

        assertThat(result4.getCommitIndex()).isGreaterThan(result3.getCommitIndex());
        assertThat(result4.getResult()).isFalse();

        System.out.println("4th operation commit index: " + result4.getCommitIndex() + ", result: " + result4.getResult());

        Ordered<String> result5 = leader.<String>replicate(OperableAtomicRegister.newGetOperation()).join();

        assertThat(result5.getCommitIndex()).isGreaterThan(result4.getCommitIndex());
        assertThat(result5.getResult()).isEqualTo(value3);

        System.out.println("5th operation commit index: " + result5.getCommitIndex() + ", result: " + result5.getResult());
    }

    private RaftNode createRaftNode(RaftEndpoint endpoint, Listener listener) {
        LocalTransport transport = new LocalTransport(endpoint);
        StateMachine stateMachine = new OperableAtomicRegister();
        RaftNode raftNode = RaftNode.newBuilder().setGroupId("default").setLocalEndpoint(endpoint)
                .setInitialGroupMembers(initialMembers).setTransport(transport)
                .setStateMachine(stateMachine).setRaftNodeReportListener(listener).build();

        raftNodes.add(raftNode);
        transports.add(transport);
        enableDiscovery(raftNode, transport);

        return raftNode;
    }

    private void enableDiscovery(RaftNode raftNode, LocalTransport transport) {
        for (int i = 0; i < raftNodes.size(); i++) {
            RaftNode otherNode = raftNodes.get(i);
            if (otherNode != raftNode) {
                transports.get(i).discoverNode(raftNode);
                transport.discoverNode(otherNode);
            }
        }
    }

    private RaftNode waitUntilLeaderElected() {
        long deadline = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(60);
        while (System.currentTimeMillis() < deadline) {
            RaftEndpoint leaderEndpoint = getLeaderEndpoint();
            if (leaderEndpoint != null) {
                return raftNodes.stream().filter(node -> node.getLocalEndpoint().equals(leaderEndpoint)).findFirst()
                        .orElseThrow(IllegalStateException::new);
            }

            try {
                MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        throw new AssertionError("Could not elect a leader on time!");
    }

    private RaftEndpoint getLeaderEndpoint() {
        RaftEndpoint leaderEndpoint = null;
        int leaderTerm = 0;
        for (RaftNode raftNode : raftNodes) {
            if (raftNode.getStatus() == RaftNodeStatus.TERMINATED) {
                continue;
            }

            RaftTerm term = raftNode.getTerm();
            if (term.getLeaderEndpoint() != null) {
                if (leaderEndpoint == null) {
                    leaderEndpoint = term.getLeaderEndpoint();
                    leaderTerm = term.getTerm();
                } else if (!(leaderEndpoint.equals(term.getLeaderEndpoint()) && leaderTerm == term.getTerm())) {
                    leaderEndpoint = null;
                    break;
                }
            } else {
                leaderEndpoint = null;
                break;
            }
        }

        return leaderEndpoint;
    }

    public static void main(String[] args) {
        OperationCommitTest leaderElectionTest = new OperationCommitTest();

        leaderElectionTest.startRaftGroup();

        leaderElectionTest.testLeaderElection();

        leaderElectionTest.terminateRaftGroup();
    }

}
