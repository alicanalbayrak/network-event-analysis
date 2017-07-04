package com.gilmour.nea.dto;

/**
 * Created by gilmour on Jul, 2017.
 */
public class ConnectionSummaryDTO {

    private String src_ip;
    private String dst_ip;
    private String protocol;
    private long timeBlock;
    private int eventCount;


    public String getSrc_ip() {
        return src_ip;
    }

    public void setSrc_ip(String src_ip) {
        this.src_ip = src_ip;
    }

    public String getDst_ip() {
        return dst_ip;
    }

    public void setDst_ip(String dst_ip) {
        this.dst_ip = dst_ip;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public long getTimeBlock() {
        return timeBlock;
    }

    public void setTimeBlock(long timeBlock) {
        this.timeBlock = timeBlock;
    }

    public int getEventCount() {
        return eventCount;
    }

    public void setEventCount(int eventCount) {
        this.eventCount = eventCount;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConnectionSummaryDTO)) return false;

        ConnectionSummaryDTO that = (ConnectionSummaryDTO) o;

        if (getTimeBlock() != that.getTimeBlock()) return false;
        if (getEventCount() != that.getEventCount()) return false;
        if (getSrc_ip() != null ? !getSrc_ip().equals(that.getSrc_ip()) : that.getSrc_ip() != null) return false;
        if (getDst_ip() != null ? !getDst_ip().equals(that.getDst_ip()) : that.getDst_ip() != null) return false;
        return getProtocol() != null ? getProtocol().equals(that.getProtocol()) : that.getProtocol() == null;
    }

    @Override
    public int hashCode() {
        int result = getSrc_ip() != null ? getSrc_ip().hashCode() : 0;
        result = 31 * result + (getDst_ip() != null ? getDst_ip().hashCode() : 0);
        result = 31 * result + (getProtocol() != null ? getProtocol().hashCode() : 0);
        result = 31 * result + (int) (getTimeBlock() ^ (getTimeBlock() >>> 32));
        return result;
    }
}
