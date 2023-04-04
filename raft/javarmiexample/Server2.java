package raft.javarmiexample;

public class Server2 implements Hello {
    private int count = 100;
        
    public Server2() {}

    public String sayHello() {
        return "Server 2: Surprise!! " + count--;
    }
}