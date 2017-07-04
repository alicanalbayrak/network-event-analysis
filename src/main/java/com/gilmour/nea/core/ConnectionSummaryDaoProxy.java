package com.gilmour.nea.core;

import com.gilmour.nea.db.ConnectionSummaryDAO;
import com.gilmour.nea.model.ConnectionSummary;
import io.dropwizard.hibernate.UnitOfWork;

import java.util.List;
import java.util.Map;

/**
 * Created by gilmour on Jul, 2017.
 */
public class ConnectionSummaryDaoProxy {

    private ConnectionSummaryDAO connectionSummaryDAO;

    public ConnectionSummaryDaoProxy(ConnectionSummaryDAO connectionSummaryDAO) {
        this.connectionSummaryDAO = connectionSummaryDAO;
    }

    @UnitOfWork
    public void persistList(Map<ConnectionDTO, MutableAggregatorInt> freq, String sourceFile, String uploadCode) {

        freq.forEach((dto, aggregatorInt) -> {
            ConnectionSummary connectionSummary = new ConnectionSummary();

            CopyStrategy.convert(connectionSummary, dto);
            connectionSummary.setNumberOfEvents(aggregatorInt.get());
            connectionSummary.setSourceFile(sourceFile);
            connectionSummary.setUploadCode(uploadCode);

            connectionSummaryDAO.create(connectionSummary);
        });

    }


    @UnitOfWork
    public int deleteByUploadCode(String uploadCode) {
        return connectionSummaryDAO.deleteByUploadCode(uploadCode);
    }

    @UnitOfWork
    public List<ConnectionSummary> getByUploadCode(String uploadCode) {
        return connectionSummaryDAO.getConnectionByUploadCode(uploadCode);
    }
}
