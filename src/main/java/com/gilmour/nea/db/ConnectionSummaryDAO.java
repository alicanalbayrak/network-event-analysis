package com.gilmour.nea.db;

import com.gilmour.nea.core.ConnSumQueryObject;
import com.gilmour.nea.core.IPNumberConverter;
import com.gilmour.nea.core.ProtocolNumberConverter;
import com.gilmour.nea.model.ConnectionSummary;
import io.dropwizard.hibernate.AbstractDAO;
import org.apache.commons.lang3.tuple.Pair;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

/**
 * Created by gilmour on Jul, 2017.
 */
public class ConnectionSummaryDAO extends AbstractDAO<ConnectionSummary> {


    public ConnectionSummaryDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Optional<ConnectionSummary> findById(Long id) {
        return Optional.ofNullable(get(id));
    }

    public ConnectionSummary create(ConnectionSummary connectionSummary) {
        return persist(connectionSummary);
    }

    public List<ConnectionSummary> findAll() {
        return list(namedQuery("com.gilmour.nea.model.ConnectionSummary.findAll"));
    }

    public List<ConnectionSummary> findByCriteria(ConnSumQueryObject connSumQueryObject) {

        CriteriaBuilder builder = currentSession().getCriteriaBuilder();
        CriteriaQuery<ConnectionSummary> criteriaQuery = builder.createQuery(ConnectionSummary.class);
        Root<ConnectionSummary> connectionSummaryRoot = criteriaQuery.from(ConnectionSummary.class);

        criteriaQuery.select(connectionSummaryRoot);

        if (!connSumQueryObject.getSrcIpCidr().equalsIgnoreCase("none")) {
            Pair<String, String> pair = IPNumberConverter.getIpBlockFromCidrMask(connSumQueryObject.getSrcIpCidr());
            long lowestIp = IPNumberConverter.IpToLong(pair.getLeft());
            long highestIp = IPNumberConverter.IpToLong(pair.getRight());
            criteriaQuery.where(builder.between(connectionSummaryRoot.get("sourceIp"), lowestIp, highestIp));
        }

        if (!connSumQueryObject.getDestIpCidr().equalsIgnoreCase("none")) {
            Pair<String, String> pair = IPNumberConverter.getIpBlockFromCidrMask(connSumQueryObject.getDestIpCidr());
            long lowestIp = IPNumberConverter.IpToLong(pair.getLeft());
            long highestIp = IPNumberConverter.IpToLong(pair.getRight());
            criteriaQuery.where(builder.between(connectionSummaryRoot.get("destinationIp"), lowestIp, highestIp));
        }

        if (!connSumQueryObject.getProtocolCode().equalsIgnoreCase("none")) {
            int protocolCode = ProtocolNumberConverter.getInstance().keywordToDecimal(connSumQueryObject.getProtocolCode());
            criteriaQuery.where(builder.equal(connectionSummaryRoot.get("protocol"), protocolCode));
        }

        if (connSumQueryObject.getStartInMillis() != -1) {
            // TODO implement
        }

        if (connSumQueryObject.getEndInMillis() != -1) {
            // TODO implement
        }

        return list(criteriaQuery);

    }

    public int deleteByUploadCode(String uploadCode) {
        Query query = currentSession().createQuery("DELETE FROM ConnectionSummary p WHERE p.uploadCode = :uploadCode");
        query.setParameter("uploadCode", uploadCode);

        return query.executeUpdate();
    }

    public List<ConnectionSummary> getConnectionByUploadCode(String uploadCode) {
        return list(namedQuery("com.gilmour.nea.model.ConnectionSummary.getConnectionByUploadCode").setParameter("uploadCode", uploadCode));
    }
}
