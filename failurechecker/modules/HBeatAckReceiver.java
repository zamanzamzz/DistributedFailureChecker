package failurechecker.modules;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import failurechecker.models.AckMessage;

public class HBeatAckReceiver extends Thread {
    private HBeatSender client;
    private DatagramSocket socket;
    private byte[] buf = new byte[256];

    private int failCounter = 0;


    public HBeatAckReceiver(HBeatSender client, DatagramSocket socket) {
        this.client = client;
        this.socket = socket;
    }

    @Override
    public void run() {
        while(true) {
            try {
                receiveAck();
                failCounter = 0;
            } catch (Exception e) {
                if (e instanceof SocketTimeoutException) {
                    failCounter++;
                    System.out.println("CLIENT ACK RECEIVER: socket timeout: " + failCounter);
                    if (failCounter > 2) {
                        break;
                    }
                } else {
                    if (!(e instanceof SocketException)) {
                        e.printStackTrace();
                    }
                    System.out.println("CLIENT ACK RECEIVER: DONE");
                    return;
                }
            }
        }
        System.out.println("CLIENT ACK RECEIVER: failure detected!");
        client.setFailureDetected(true);
        System.out.println("CLIENT ACK RECEIVER: DONE");
    }

    private void receiveAck() throws IOException, ClassNotFoundException {
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(packet.getData());
        ObjectInputStream in = new ObjectInputStream(inputStream);
        AckMessage receivedAckMessage = (AckMessage) in.readObject();
        System.out.println("CLIENT ACK RECEIVER: Ack received: " + receivedAckMessage);
        in.close();
        inputStream.close();
    }
}
