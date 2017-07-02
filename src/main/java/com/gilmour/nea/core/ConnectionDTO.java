package com.gilmour.nea.core;

/**
 * Created by gilmour on Jul, 2017.
 */
public class ConnectionDTO {

    private long sourceIp;

    private long destinationIp;

    private long timestamp;

    private int protocol;

    public long getSourceIp() {
        return sourceIp;
    }

    public void setSourceIp(long sourceIp) {
        this.sourceIp = sourceIp;
    }

    public long getDestinationIp() {
        return destinationIp;
    }

    public void setDestinationIp(long destinationIp) {
        this.destinationIp = destinationIp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timeInMillis) {
        this.timestamp = timeInMillis;
    }

    public int getProtocol() {
        return protocol;
    }

    public void setProtocol(int protocol) {
        this.protocol = protocol;
    }

    @Override
    public String toString() {
        return "ConnectionDTO{" +
                "sourceIp=" + sourceIp +
                ", destinationIp=" + destinationIp +
                ", timestamp=" + timestamp +
                ", protocol=" + protocol +
                '}';
    }
}
