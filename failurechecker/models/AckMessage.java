package failurechecker.models;

import java.util.Date;

public class AckMessage implements java.io.Serializable {
    public Date timeSent;
    public String epochNonce;
    public int sequenceNumber;

    public AckMessage(HBeatMessage hBeatMessage) {
        this.timeSent = new Date();
        this.epochNonce = hBeatMessage.epochNonce;
        this.sequenceNumber = hBeatMessage.sequenceNumber;
    }

    @Override
    public String toString() {
        return "AckMessage: sequenceNumber: " + sequenceNumber + " epochNonce: " + epochNonce + " timeSent: " + timeSent; 
    }
}