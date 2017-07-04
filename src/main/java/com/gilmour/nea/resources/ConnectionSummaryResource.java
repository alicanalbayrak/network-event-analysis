package com.gilmour.nea.resources;

import com.gilmour.nea.core.*;
import com.gilmour.nea.db.ConnectionSummaryDAO;
import com.gilmour.nea.model.ConnectionSummary;
import io.dropwizard.hibernate.UnitOfWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gilmour on Jul, 2017.
 */
@Path("/connection")
@Produces(MediaType.APPLICATION_JSON)
public class ConnectionSummaryResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionSummaryResource.class);

    private final ConnectionSummaryDAO dao;

    public ConnectionSummaryResource(ConnectionSummaryDAO dao) {
        this.dao = dao;
    }

    @GET
    @Path("/summary")
    @Consumes(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response getConnectionSummary(
            @DefaultValue("none") @QueryParam("srcIpBlock") String srcIpBlock,
            @DefaultValue("none") @QueryParam("destIpBlock") String destIpBlock,
            @DefaultValue("none") @QueryParam("protocol") String protocol,
            @DefaultValue("-1") @QueryParam("startTime") long startInMillis,
            @DefaultValue("-1") @QueryParam("endTime") long endInMillis,
            @DefaultValue("-1") @QueryParam("connectionThreshold") int minEvent,
            @DefaultValue("1") @QueryParam("hourResolution") int hourResolution
    ) {

        ConnSumQueryObject connSumQueryObject = new ConnSumQueryObject();
        connSumQueryObject.setSrcIpCidr(srcIpBlock);
        connSumQueryObject.setDestIpCidrd(destIpBlock);
        connSumQueryObject.setProtocolCode(protocol);
        connSumQueryObject.setStartInMillis(startInMillis);
        connSumQueryObject.setEndInMillis(endInMillis);
        connSumQueryObject.setNumberOfMinimumEvent(minEvent);
        connSumQueryObject.setHourResolution(hourResolution);
        LOGGER.info(connSumQueryObject.toString());

        List<ConnectionSummary> list = dao.findByCriteria(connSumQueryObject);
        LOGGER.info("Number of found connection summaries: " + list.size());

        Map<ConnectionSummaryDTO, MutableAggregatorInt> freq = new HashMap<>();

        list.forEach(connectionSummary -> {
            ConnectionSummaryDTO connectionSummaryDTO = new ConnectionSummaryDTO();
            connectionSummaryDTO.setSrc_ip(IPNumberConverter.longToIp(connectionSummary.getSourceIp()));
            connectionSummaryDTO.setDst_ip(IPNumberConverter.longToIp(connectionSummary.getDestinationIp()));
            connectionSummaryDTO.setProtocol(ProtocolNumberConverter.getInstance().decimalToKeyword(connectionSummary.getProtocol()));
            connectionSummaryDTO.setTimeBlock(TimeUtility.timeStampInGivenResoulution(connectionSummary.getTimestamp(), hourResolution));

            MutableAggregatorInt aggregate = freq.get(connectionSummaryDTO);
            if (aggregate == null) {
                freq.put(connectionSummaryDTO, new MutableAggregatorInt(connectionSummary.getNumberOfEvents()));
                connectionSummaryDTO.setEventCount(connectionSummary.getNumberOfEvents());
            } else {
                aggregate.increment(connectionSummary.getNumberOfEvents());
                connectionSummaryDTO.setEventCount(aggregate.get());
            }

        });

        // todo hour resolution check

        return Response.status(200).entity(freq.keySet()).build();
    }

    @DELETE
    @Path("/{uploadCode}")
    @Consumes(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response deleteConnectionByUploadCode(@PathParam("uploadCode") String uploadCode) {
        int result = dao.deleteByUploadCode(uploadCode);
        return Response.status(200).entity(result + " entry deleted").build();
    }

    @GET
    @Path("/{uploadCode}")
    @Consumes(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response getConnectionsByUploadCode(@PathParam("uploadCode") String uploadCode) {
        List<ConnectionSummary> sum = dao.getConnectionByUploadCode(uploadCode);
        return Response.status(200).entity(sum).build();
    }

    class MutableCustomInt {


    }


}
