package failurechecker;

import java.net.SocketException;
import java.net.UnknownHostException;

public class FailureCheckerTest {
    public static void main(String[] args) throws SocketException, UnknownHostException, InterruptedException {
        String serverToMonitorAddress = args[0];
        int serverToMonitorPort = Integer.parseInt(args[1]);
        int hBeatReceiverPort = Integer.parseInt(args[2]);
        FailureChecker fCheck = new FailureChecker(serverToMonitorAddress, serverToMonitorPort, hBeatReceiverPort);
        boolean failureDetected = fCheck.startNonBlocking(20000);
        System.out.println("Failure Detected: " + failureDetected);
    }
}
