package raft;

import java.util.ArrayList;
import java.util.List;

import raft.servernode.ServerConfig;
import raft.servernode.ServerNode;

public class ExampleServerNode2 {
    public static void main(String[] args) throws Exception {
        List<ServerConfig> otherServerNodes = new ArrayList<>();
        otherServerNodes.add(new ServerConfig("server1", null, new ArrayList<>()));
        ServerConfig config = new ServerConfig("server2", null, otherServerNodes);

        ServerNode serverNode = new ServerNode(config);
        serverNode.initialize(); 
    }
}
