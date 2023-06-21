package atomicregister;


import io.microraft.statemachine.StateMachine;

import java.util.List;
import java.util.function.Consumer;

/**
 * This is the base class for our atomic register implementation.
 * In this class, we only define a marker interface for the operations we will
 * commit on the atomic register state machine, and the "new term operation"
 * which will be committed after leader elections.
 * <p>
 * Subclasses are expected to implement operation execution and snapshotting
 * logic.
 *
 * YOU CAN SEE THIS CLASS AT:
 *
 * https://github.com/MicroRaft/MicroRaft/blob/master/microraft-tutorial/src/main/java/io/microraft/tutorial/atomicregister/AtomicRegister.java
 *
 */
public class AtomicRegister
        implements StateMachine {

    @Override
    public Object runOperation(long commitIndex, Object operation) {
        if (operation instanceof NewTermOperation) {
            return null;
        }

        throw new IllegalArgumentException("Invalid operation: " + operation + " at commit index: " + commitIndex);
    }

    @Override
    public Object getNewTermOperation() {
        return new NewTermOperation();
    }

    @Override
    public void takeSnapshot(long commitIndex, Consumer<Object> snapshotChunkConsumer) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void installSnapshot(long commitIndex, List<Object> snapshotChunks) {
        throw new UnsupportedOperationException();
    }


    private static class NewTermOperation {
    }

}
