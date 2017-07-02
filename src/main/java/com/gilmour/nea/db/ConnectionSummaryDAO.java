package com.gilmour.nea.db;

import com.gilmour.nea.model.ConnectionSummary;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

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
        return list(namedQuery("findAll"));
    }
}
