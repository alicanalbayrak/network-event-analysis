package com.gilmour.nea.core;

import com.gilmour.nea.db.ConnectionSummaryDAO;
import com.gilmour.nea.model.ConnectionSummary;
import com.google.common.collect.Multiset;
import io.dropwizard.hibernate.UnitOfWork;

/**
 * Created by gilmour on Jul, 2017.
 */
public class ConnectionSummaryDaoProxy {

    private ConnectionSummaryDAO connectionSummaryDAO;

    public ConnectionSummaryDaoProxy(ConnectionSummaryDAO connectionSummaryDAO) {
        this.connectionSummaryDAO = connectionSummaryDAO;
    }

    @UnitOfWork
    public void persistList(Multiset<ConnectionDTO> connectionDTOList, String sourceFile, String uploadCode) {

        connectionDTOList.elementSet().forEach(element -> {
            ConnectionSummary connectionSummary = new ConnectionSummary();

            CopyStrategy.connDtoToConnSummary(connectionSummary, element);
            connectionSummary.setNumberOfEvents(connectionDTOList.count(element));
            connectionSummary.setSourceFile(sourceFile);
            connectionSummary.setUploadCode(uploadCode);

            connectionSummaryDAO.create(connectionSummary);

        });
    }


}
