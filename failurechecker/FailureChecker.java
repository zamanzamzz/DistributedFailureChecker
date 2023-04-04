package failurechecker;

import java.net.SocketException;
import java.net.UnknownHostException;

import failurechecker.modules.HBeatReceiver;
import failurechecker.modules.HBeatSender;

public class FailureChecker {
    private HBeatSender hBeatSender;
    private HBeatReceiver hBeatReceiver;

    public FailureChecker(String serverToMonitorAddress, Integer serverToMonitorPort, Integer hBeatReceiverPort)
            throws SocketException, UnknownHostException {
        if (hBeatReceiverPort != null) {
            hBeatReceiver = new HBeatReceiver(hBeatReceiverPort);
        }

        if (serverToMonitorAddress != null && serverToMonitorPort != null) {
            hBeatSender = new HBeatSender(hBeatReceiver, serverToMonitorAddress, serverToMonitorPort);
        }
    }

    public boolean isHBeatSenderRunning() {
        if (hBeatSender != null) {
            return hBeatSender.isAlive();
        }

        return false;
    }

    public boolean isHBeatReceiverRunning() {
        if (hBeatReceiver != null) {
            return hBeatReceiver.isAlive();
        }

        return false;
    }

    public void changeHBeatReceiverPort(Integer hBeatReceiverPort) throws SocketException {
        if (isHBeatReceiverRunning())  {
            System.out.println("HBeatReceiver is running, cannot change port");
            return;
        }

        hBeatReceiver = new HBeatReceiver(hBeatReceiverPort);
    }

    

    public void clearMonitoredServer() {
        if (isHBeatSenderRunning()) {
            System.out.println("HBeatSender is running, cannot clear server to monitor");
            return;
        }

        hBeatSender = null;
    }

    public void changeServerToMonitor(String serverToMonitorAddress, Integer serverToMonitorPort)
            throws SocketException, UnknownHostException {
        if (isHBeatSenderRunning()) {
            System.out.println("HBeatSender is running, cannot change server to monitor");
            return;
        }

        hBeatSender = new HBeatSender(hBeatReceiver, serverToMonitorAddress, serverToMonitorPort);
    }

    public void stop() throws InterruptedException {
        if (hBeatSender != null) {
            hBeatSender.stopClient();
            hBeatSender.join();
        }
        if (hBeatReceiver != null) {
            hBeatReceiver.join();
        }
    }

    // use future maybe
    public void start() {
        if (hBeatSender != null) {
            hBeatSender.start();
        }
        if (hBeatReceiver != null) {
            hBeatReceiver.start();
        }
    }

    // set a timeout, and cancel if timeout reached
    public Boolean startNonBlocking() throws InterruptedException {
        if (hBeatSender != null) {
            hBeatSender.start();
        }

        if (hBeatReceiver != null) {
            hBeatReceiver.start();
        }

        if (hBeatSender != null) {
            hBeatSender.join();
        }

        if (hBeatReceiver != null) {
            hBeatReceiver.join();

        }

        if (hBeatSender != null) {
            return hBeatSender.isFailureDetected();

        }

        return null;
    }

    public Boolean startNonBlocking(int millis) throws InterruptedException {
        if (hBeatSender != null) {
            hBeatSender.start();
        }

        if (hBeatReceiver != null) {
            hBeatReceiver.start();
        }

        if (hBeatSender != null) {
            hBeatSender.join(millis);

            if (hBeatSender.isAlive()) {
                hBeatSender.stopClient();
                hBeatSender.join();
            }
        }

        if (hBeatReceiver != null) {
            hBeatReceiver.join();

        }

        if (hBeatSender != null) {
            return hBeatSender.isFailureDetected();

        }

        return null;
    }
}
