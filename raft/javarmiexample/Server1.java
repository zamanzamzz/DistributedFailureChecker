package raft.javarmiexample;
        
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
        
public class Server1 implements Hello {
    private int count = 0;
        
    public Server1() {}

    public String sayHello() {

        return "Server 1: Surprise!! " + count++;
    }
        
    public static void main(String args[]) {
        
        try {
            Server1 server1 = new Server1();
            Hello server1Stub = (Hello) UnicastRemoteObject.exportObject(server1,0);

            Server2 server2 = new Server2();
            Hello server2Stub = (Hello) UnicastRemoteObject.exportObject(server2, 0);

            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.getRegistry();
            try {
                registry.unbind("server1");
            } catch (Exception e) {}
            try {
                registry.unbind("server2");
            } catch (Exception e) {}
            registry.bind("server1", server1Stub);
            registry.bind("server2", server2Stub);

            System.err.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}