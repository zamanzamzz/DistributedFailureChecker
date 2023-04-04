package failurechecker.models;

import java.util.Date;

public class HBeatMessage implements java.io.Serializable {
    public Date timeSent;
    public String epochNonce;
    public int sequenceNumber;

    public HBeatMessage(String epochNonce, int sequenceNumber) {
        this.timeSent = new Date();
        this.epochNonce = epochNonce;
        this.sequenceNumber = sequenceNumber;
    }

    @Override
    public String toString() {
        return "HBeatMessage: sequenceNumber: " + sequenceNumber + " epochNonce: " + epochNonce + " timeSent: " + timeSent; 
    }
}