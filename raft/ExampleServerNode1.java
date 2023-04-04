package raft;

import java.util.ArrayList;
import java.util.List;

import raft.servernode.ServerConfig;
import raft.servernode.ServerNode;

public class ExampleServerNode1 {
    public static void main(String[] args) throws Exception {
        List<ServerConfig> otherServerNodes = new ArrayList<>();
        otherServerNodes.add(new ServerConfig("server2", null, new ArrayList<>()));
        ServerConfig config = new ServerConfig("server1", null, otherServerNodes);

        ServerNode serverNode = new ServerNode(config);
        serverNode.initialize(); 
    }
}
