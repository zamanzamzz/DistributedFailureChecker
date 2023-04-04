package failurechecker.modules;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import failurechecker.models.HBeatMessage;

public class HBeatSender extends Thread {
    private HBeatReceiver hBeatReceiver;
    private HBeatAckReceiver ackReceiver;
    private DatagramSocket socket;
    private int port;
    private InetAddress address;

    private byte[] buf;

    private int currentSequenceNumber = 0;

    private boolean failureDetected = false;

    private boolean stop = false;

     public void stopClient() {
        synchronized (this) {
            stop = true;
        }
    }

    public HBeatSender(HBeatReceiver hBeatReceiver, String address, int port) throws SocketException, UnknownHostException {
        socket = new DatagramSocket();
        socket.setSoTimeout(3000);
        this.address = InetAddress.getByName(address);
        this.port = port;
        ackReceiver = new HBeatAckReceiver(this, socket);
        this.hBeatReceiver = hBeatReceiver;
        System.out.println("CLIENT: address: " + address + " port: " + port);
    }

    @Override
    public void run() {
        ackReceiver.start();
        while(!isFailureDetected() && !stop) {
            try {
                sendHeartbeat();
            } catch (Exception e) {
                
            }
        }

        if (isFailureDetected()) {
            System.out.println("CLIENT: failure detected!");
        }

        System.out.println("CLIENT: stopping server");
        if (hBeatReceiver != null) {
            hBeatReceiver.stopServer();
        }
        close();
        System.out.println("CLIENT: DONE");
    }

    public boolean isFailureDetected() {
        boolean isFailureDetected = false;
        synchronized (this) {
            isFailureDetected = failureDetected;
        }
        return isFailureDetected;
    }

     public void setFailureDetected(boolean failureDetected) {
        synchronized (this) {
            this.failureDetected = failureDetected;
        }
    }

    public void sendHeartbeat() throws IOException, ClassNotFoundException, SocketTimeoutException, InterruptedException {
        HBeatMessage heartbeatToSend = new HBeatMessage("client", currentSequenceNumber++);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(outputStream);
        out.writeObject(heartbeatToSend);
        out.close();
        buf = outputStream.toByteArray();
        outputStream.close();

        DatagramPacket packet 
          = new DatagramPacket(buf, buf.length, address, port);

        System.out.println("CLIENT: HBeat sent: " + heartbeatToSend);
        socket.send(packet);
        Thread.sleep(10);
    }

    public void close() {
        socket.close();
    }
}