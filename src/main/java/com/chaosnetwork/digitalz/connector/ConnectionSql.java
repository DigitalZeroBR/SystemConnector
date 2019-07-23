package com.chaosnetwork.digitalz.connector;

import java.io.File;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public abstract class ConnectionSql {

    protected static Connection connection = null;
    private static Map<String, PreparedStatement> statementCache = new HashMap();
    private static boolean useStatementCache = true;

    public static boolean connect() throws SQLException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        if (connection != null) {
            return true;
        }
        File file_sql = new File(SystemConnector.getPlugin().getDataFolder()+"/database");
        if (!file_sql.exists()) {
            if (!file_sql.mkdirs()) {
            }
        }
        
        Driver driver = (Driver) Class.forName("org.sqlite.JDBC").newInstance();
        Properties properties = new Properties();
        connection = driver.connect("jdbc:sqlite:plugins/SystemConnector/database/data.db", properties);
        if (connection == null) {
            throw new NullPointerException("Conexao com o banco de dados falhou!");
        }
        return true;
    }

    public static void dispose() {
        statementCache.clear();
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            SystemConnector.log.severe(e.getMessage());
        }
        connection = null;
    }

    public static Connection getConnection() {
        if (connection == null) {
            throw new NullPointerException("Sem conexao!");
        }
        return connection;
    }

    public static PreparedStatement prepare(String sql) throws SQLException {
        return prepare(sql, false);
    }

    public static PreparedStatement prepare(String sql, boolean returnGeneratedKeys) throws SQLException {
        if (connection == null) {
            throw new SQLException("Sem conexao");
        }
        if ((useStatementCache) && (statementCache.containsKey(sql))) {
            return (PreparedStatement) statementCache.get(sql);
        }
        PreparedStatement preparedStatement = returnGeneratedKeys ? connection.prepareStatement(sql, 1) : connection.prepareStatement(sql);
        statementCache.put(sql, preparedStatement);
        return preparedStatement;
    }

    public static boolean useStatementCache() {
        return useStatementCache;
    }

    public static void setUseStatementCache(boolean useStatementCache) {
        useStatementCache = useStatementCache;
    }
}
