package atomicregisterlocal;

import io.microraft.RaftEndpoint;

import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Objects.requireNonNull;

/**
 * A very simple {@link RaftEndpoint} implementation used in the tutorial.
 * <p>
 * Unique Raft endpoints can be created
 * via {@link LocalRaftEndpoint#newEndpoint()}.
 * 
 * YOU CAN SEE THIS CLASS AT:
 *
 * https://github.com/MicroRaft/MicroRaft/blob/master/microraft-tutorial/src/main/java/io/microraft/tutorial/LocalRaftEndpoint.java
 *
 * @author mdogan
 * @author metanet
 */
public final class LocalRaftEndpoint
        implements RaftEndpoint {

    private static final AtomicInteger ID_GENERATOR = new AtomicInteger();

    /**
     * Returns a new unique Raft endpoint.
     *
     * @return a new unique Raft endpoint
     */
    public static LocalRaftEndpoint newEndpoint() {
        return new LocalRaftEndpoint("node" + ID_GENERATOR.incrementAndGet());
    }


    private final String id;

    private LocalRaftEndpoint(String id) {
        this.id = requireNonNull(id);
    }

    @Override
    public Object getId() {
        return id;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        LocalRaftEndpoint that = (LocalRaftEndpoint) o;

        return id.equals(that.id);
    }

    @Override
    public String toString() {
        return "LocalRaftEndpoint{" + "id=" + id + '}';
    }

}
