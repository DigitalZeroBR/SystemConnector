package com.chaosnetwork.digitalz.connector;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class Connector {
    
    private static Connector instance;

    public static Connector instance() {
        return instance;
    }

    static void setInstance(Connector newInstance) {
        instance = newInstance;
    }

    private final Map<String, HikariDataSource> sources;

    Connector() {
        sources = new HashMap<>();
    }

    public synchronized HikariDataSource getDataSource(String name) {
        return sources.get(name.toLowerCase());
    }

    synchronized void createDataSources(List<DataSource> DataSourceList, Logger logger) {
        for (DataSource details : DataSourceList) {
            try {
                // Create the data source
                HikariDataSource dataSource = createDataSource(details);

                try (Connection connection = dataSource.getConnection()) {
                    try (Statement statement = connection.createStatement()) {
                        // Test the data source with a simple statement
                        statement.execute("SELECT 1;");
                    }
                }

                sources.put(details.getSourceName(), dataSource);
                logger.info("Created DataSource " + details + ".");
            } catch (Exception ex) {
                ex.printStackTrace();
                logger.severe("Failed to create DataSource '" + details + "'.");
            }
        }
    }

    synchronized void closeAndRemoveDataSources(Logger logger) {
        Iterator<Map.Entry<String, HikariDataSource>> iter = sources.entrySet().iterator();

        while (iter.hasNext()) {
            Map.Entry<String, HikariDataSource> entry = iter.next();
            String sourceName = entry.getKey();
            HikariDataSource dataSource = entry.getValue();

            // Remove in all cases
            iter.remove();

            if (dataSource != null) {
                try {
                    // Close the data source
                    dataSource.close();
                    logger.info("Closed DataSource '" + sourceName + "'.");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    logger.severe("Failed to close DataSource '" + sourceName + "'.");
                }
            }
        }
    }

    private HikariDataSource createDataSource(DataSource DataSource) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + DataSource.getUrl());
        config.setUsername(DataSource.getUsername());
        config.setPassword(DataSource.getPassword());
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("useServerPrepStmts", "true");
        return new HikariDataSource(config);
    }
}
