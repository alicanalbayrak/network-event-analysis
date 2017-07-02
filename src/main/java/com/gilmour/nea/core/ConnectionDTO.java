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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConnectionDTO)) return false;

        ConnectionDTO that = (ConnectionDTO) o;

        if (getSourceIp() != that.getSourceIp()) return false;
        if (getDestinationIp() != that.getDestinationIp()) return false;
        if (getTimestamp() != that.getTimestamp()) return false;
        return getProtocol() == that.getProtocol();
    }

    @Override
    public int hashCode() {
        int result = (int) (getSourceIp() ^ (getSourceIp() >>> 32));
        result = 31 * result + (int) (getDestinationIp() ^ (getDestinationIp() >>> 32));
        result = 31 * result + (int) (getTimestamp() ^ (getTimestamp() >>> 32));
        result = 31 * result + getProtocol();
        return result;
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
