package com.gilmour.nea.core;

import com.gilmour.nea.model.ConnectionSummary;

/**
 * Created by gilmour on Jul, 2017.
 */
public class CopyStrategy {

    public static ConnectionSummary connDtoToConnSummary(ConnectionSummary to, ConnectionDTO from){

        to.setSourceIp(from.getSourceIp());
        to.setDestinationIp(from.getDestinationIp());
        to.setProtocol(from.getProtocol());
        to.setTimestamp(from.getTimestamp());

        return to;
    }

}
