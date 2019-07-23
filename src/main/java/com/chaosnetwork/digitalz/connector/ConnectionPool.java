package com.chaosnetwork.digitalz.connector;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConnectionPool{
   
    private HikariDataSource dataSource; 

    public ConnectionPool() {
        setup();
    }
    
    private void setup() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(
            "jdbc:mysql://" +
                Config.Host +
                ":" +
                "3306" +
                "/" +
                Config.Database
        );
        
        config.setDriverClassName("com.mysql.jdbc.Driver");
        config.setUsername(Config.Username);
        config.setPassword(Config.Password);
        
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(5);
        
        
        //config.addDataSourceProperty("loginTimeout", 10);
        //config.addDataSourceProperty("socketTimeout", 10);
        
        //config.addDataSourceProperty("autoReconnect", "true");
        //config.setAutoCommit(false);
        
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        dataSource = new HikariDataSource(config);
    }
    
    public Connection getConnection() throws SQLException{
        return dataSource.getConnection();
    }
    
    public void close(Connection conn, PreparedStatement ps, ResultSet res) {
        if (conn != null) try { conn.close(); } catch (SQLException ignored) {}
        if (ps != null) try { ps.close(); } catch (SQLException ignored) {}
        if (res != null) try { res.close(); } catch (SQLException ignored) {}
    }
    
    public void close() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}