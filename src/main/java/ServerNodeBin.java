import java.io.File;
import java.nio.file.Path;

import raft.servernode.ServerConfig;
import raft.servernode.ServerNode;

public class ServerNodeBin {
    public static void main(String[] args) throws Exception {
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        Path jsonPath = (new File(args[0])).toPath();
        ServerConfig config = new ServerConfig(jsonPath);
        ServerNode serverNode = new ServerNode(config);
        serverNode.initialize(); 
    }
}
