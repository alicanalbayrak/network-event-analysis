package com.gilmour.nea.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by gilmour on Jul, 2017.
 */

@Entity
@Table(name = "connection_summary")
@NamedQueries(
        {
                @NamedQuery(
                        name = "com.gilmour.nea.model.ConnectionSummary.findAll",
                        query = "SELECT p FROM ConnectionSummary p"
                )
        }
)
public class ConnectionSummary {

    // connection_summary(hourRange(in miliseconds with hour resoulution),src_ip,dst_ip,protocol,numberOfEvents,sourceFile,uploadCode)

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Column(name = "hour_range")
    private long timestamp;

    @NotNull
    @Column(name = "src_ip")
    private long sourceIp;

    @NotNull
    @Column(name = "dest_ip")
    private long destinationIp;

    @NotNull
    @Column(name = "protocol")
    private int protocol;

    @NotNull
    @Column(name = "event_count")
    private int numberOfEvents;

    @NotNull
    @Column(name = "source_file")
    private String sourceFile;

    @NotNull
    @Column(name = "upload_code")
    private String uploadCode;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

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

    public int getProtocol() {
        return protocol;
    }

    public void setProtocol(int protocol) {
        this.protocol = protocol;
    }

    public int getNumberOfEvents() {
        return numberOfEvents;
    }

    public void setNumberOfEvents(int numberOfEvents) {
        this.numberOfEvents = numberOfEvents;
    }

    public String getSourceFile() {
        return sourceFile;
    }

    public void setSourceFile(String sourceFile) {
        this.sourceFile = sourceFile;
    }

    public String getUploadCode() {
        return uploadCode;
    }

    public void setUploadCode(String uploadCode) {
        this.uploadCode = uploadCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConnectionSummary)) return false;

        ConnectionSummary that = (ConnectionSummary) o;

        if (getId() != that.getId()) return false;
        if (getTimestamp() != that.getTimestamp()) return false;
        if (getSourceIp() != that.getSourceIp()) return false;
        if (getDestinationIp() != that.getDestinationIp()) return false;
        if (getProtocol() != that.getProtocol()) return false;
        if (getNumberOfEvents() != that.getNumberOfEvents()) return false;
        if (!getSourceFile().equals(that.getSourceFile())) return false;
        return getUploadCode().equals(that.getUploadCode());
    }

    @Override
    public int hashCode() {
        int result = (int) (getId() ^ (getId() >>> 32));
        result = 31 * result + (int) (getTimestamp() ^ (getTimestamp() >>> 32));
        result = 31 * result + (int) (getSourceIp() ^ (getSourceIp() >>> 32));
        result = 31 * result + (int) (getDestinationIp() ^ (getDestinationIp() >>> 32));
        result = 31 * result + getProtocol();
        result = 31 * result + getNumberOfEvents();
        result = 31 * result + getSourceFile().hashCode();
        result = 31 * result + getUploadCode().hashCode();
        return result;
    }
}
