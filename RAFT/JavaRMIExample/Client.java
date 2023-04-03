package RAFT.JavaRMIExample;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {

    private Client() {}

    public static void main(String[] args) {

        String host = (args.length < 1) ? null : args[0];
        System.out.println("Host: " + host);
        try {
            Registry registry = LocateRegistry.getRegistry(host);
            Hello server1Stub = (Hello) registry.lookup("server1");
            String response = server1Stub.sayHello();
            System.out.println("response: " + response);

            Hello server2Stub = (Hello) registry.lookup("server2");
            response = server2Stub.sayHello();
            System.out.println("response: " + response);
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
