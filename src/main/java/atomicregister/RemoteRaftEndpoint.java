package atomicregister;

import io.microraft.RaftEndpoint;

public final class RemoteRaftEndpoint
        implements RaftEndpoint {
    private final String id;
    private final String ipAddress;
    private final int port;

    private RemoteRaftEndpoint(String ipAddress, int port) {
        this.id = ipAddress + ":" + Integer.toString(port);
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public String getIpAddress() {
        return this.ipAddress;
    }

    public int getPort() {
        return this.port;
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

        RemoteRaftEndpoint that = (RemoteRaftEndpoint) o;

        return id.equals(that.id);
    }

    @Override
    public String toString() {
        return "RemoteRaftEndpoint{" + "id=" + id + '}';
    }

}
