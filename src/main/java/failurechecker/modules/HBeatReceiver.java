package failurechecker.modules;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import failurechecker.models.AckMessage;
import failurechecker.models.HBeatMessage;

public class HBeatReceiver extends Thread {

    private DatagramSocket socket;
    private boolean running = true;
    private byte[] buf = new byte[256];

    public HBeatReceiver(int port) throws SocketException {
        System.out.println("SERVER: listening port: " + port);
        socket = new DatagramSocket(port);
    }

    public void stopServer() {
        synchronized (this) {
            System.out.println("SERVER: stopping server");
            running = false;
            socket.close();
        }
    }

    private boolean isRunning() {
        boolean isRunning = true;
        synchronized (this) {
            isRunning = running;
        }
        return isRunning;
    }

    public void run() {
        while (isRunning()) {
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);

                HBeatMessage hBeatMessage = packetToHbeatMessage(packet);
                System.out.println("SERVER: HBeat received: " + hBeatMessage);
                AckMessage ackMessage = new AckMessage(hBeatMessage);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ObjectOutputStream out = new ObjectOutputStream(outputStream);
                out.writeObject(ackMessage);
                out.close();

                byte[] ackData = outputStream.toByteArray();
                outputStream.close();

                packet = new DatagramPacket(ackData, ackData.length, packet.getAddress(), packet.getPort());

                System.out.println("SERVER: Ack sent: " + ackMessage);
                socket.send(packet);
            } catch (Exception e) {
                if (e instanceof SocketException) {
                    break;
                }
                e.printStackTrace();
            }

        }
        socket.close();
        System.out.println("SERVER: DONE");
    }

    public HBeatMessage packetToHbeatMessage(DatagramPacket packet) throws IOException, ClassNotFoundException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(packet.getData());
        ObjectInputStream in = new ObjectInputStream(inputStream);
        HBeatMessage receivedHbeatMessage = (HBeatMessage) in.readObject();
        in.close();
        inputStream.close();
        return receivedHbeatMessage;
    }
}
