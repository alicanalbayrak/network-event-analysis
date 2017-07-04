package com.gilmour.nea.dto;

/**
 * Created by gilmour on Jul, 2017.
 */
public class ConnSumQueryObject {

    private String srcIpCidr;
    private String destIpCidrd;
    private String protocolCode;
    private long startInMillis;
    private long endInMillis;
    private int numberOfMinimumEvent;
    private int hourResolution;

    public String getSrcIpCidr() {
        return srcIpCidr;
    }

    public void setSrcIpCidr(String srcIpCidr) {
        this.srcIpCidr = srcIpCidr;
    }

    public String getDestIpCidr() {
        return destIpCidrd;
    }

    public void setDestIpCidrd(String destIpCidrd) {
        this.destIpCidrd = destIpCidrd;
    }

    public String getProtocolCode() {
        return protocolCode;
    }

    public void setProtocolCode(String protocolCode) {
        this.protocolCode = protocolCode;
    }

    public long getStartInMillis() {
        return startInMillis;
    }

    public void setStartInMillis(long startInMillis) {
        this.startInMillis = startInMillis;
    }

    public long getEndInMillis() {
        return endInMillis;
    }

    public void setEndInMillis(long endInMillis) {
        this.endInMillis = endInMillis;
    }

    public int getNumberOfMinimumEvent() {
        return numberOfMinimumEvent;
    }

    public void setNumberOfMinimumEvent(int numberOfMinimumEvent) {
        this.numberOfMinimumEvent = numberOfMinimumEvent;
    }

    public int getHourResolution() {
        return hourResolution;
    }

    public void setHourResolution(int hourResolution) {
        this.hourResolution = hourResolution;
    }


    @Override
    public String toString() {
        return "ConnSumQueryObject{" +
                "srcIpCidr=" + srcIpCidr +
                ", destIpCidrd=" + destIpCidrd +
                ", protocolCode=" + protocolCode +
                ", startInMillis=" + startInMillis +
                ", endInMillis=" + endInMillis +
                ", numberOfMinimumEvent=" + numberOfMinimumEvent +
                ", hourResolution=" + hourResolution +
                '}';
    }
}
