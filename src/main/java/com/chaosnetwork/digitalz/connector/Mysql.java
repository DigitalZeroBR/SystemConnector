package com.chaosnetwork.digitalz.connector;

import com.sun.rowset.CachedRowSetImpl;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.rowset.CachedRowSet;

public class Mysql {
    private static ConnectionPool pool;
    
    public Mysql() {
        pool = SystemConnector.getConnection();
    }
    
//    public static synchronized CachedRowSet executeQuery(String query){
//        Connection conn = null;
//        PreparedStatement ps = null;
//        try {
//            conn = pool.getConnection();
//            ps = conn.prepareStatement(query);
//            ResultSet rs = ps.executeQuery();
//
//            CachedRowSet crset = new CachedRowSetImpl();
//            crset.populate(rs);
//            return crset;
//
//        } catch (SQLException ex) {
//
//            ex.printStackTrace();
//
//        }
//        return null;
//    }
    
    public static ResultSet executeQuery(String query){
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = pool.getConnection();
            ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs;
            }
        } catch (SQLException e) {
            SystemConnector.log.severe(e.getMessage());
        } finally {
            pool.close(conn, ps, null);
        }
        return null;
    }
        
    public static boolean has(String table, String colun, String value) {
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = pool.getConnection();
            ps = conn.prepareStatement("SELECT * FROM `"+table+"` where LOWER("+colun+") = '"+value.toLowerCase()+"';");
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            SystemConnector.log.severe(e.getMessage());
        } finally {
            pool.close(conn, ps, null);
        }
        return false;
    }
    public static void setString(String table, String colun, String where, String playerName, String value) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = pool.getConnection();
            ps = conn.prepareStatement("UPDATE `"+table+"` SET `"+colun+"`= ? WHERE LOWER(`"+where+"`) = ?");
            
            ps.setString(1, value);
            ps.setString(2, playerName.toLowerCase());
            ps.executeUpdate();
        } catch (SQLException e) {
            SystemConnector.log.severe(e.getMessage());
        } finally {
            pool.close(conn, ps, null);
        }
    }
    public static void setInt(String table, String colun, String where, String playerName, int value) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = pool.getConnection();
            ps = conn.prepareStatement("UPDATE `"+table+"` SET `"+colun+"` = ? WHERE LOWER(`"+where+"`) = ?");
            ps.setInt(1, value);
            ps.setString(2, playerName.toLowerCase());
            ps.executeUpdate();
        } catch (SQLException e) {
            SystemConnector.log.severe(e.getMessage());
        } finally {
            pool.close(conn, ps, null);
        }
    }
    public static void setDouble(String table, String colun, String where, String playerName, double value) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = pool.getConnection();
            ps = conn.prepareStatement("UPDATE `"+table+"` SET `"+colun+"` = ? WHERE LOWER(`"+where+"`) = ?");
            ps.setDouble(1, value);
            ps.setString(2, playerName.toLowerCase());
            ps.executeUpdate();
        } catch (SQLException e) {
            SystemConnector.log.severe(e.getMessage());
        } finally {
            pool.close(conn, ps, null);
        }
    }

    public static String getString(String table, String colun, String where, String playerName){
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = pool.getConnection();
            ps = conn.prepareStatement("SELECT * FROM `"+table+"` where LOWER(`"+where+"`) = '" + playerName.toLowerCase() + "';");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString(colun);
            }
        } catch (SQLException e) {
            SystemConnector.log.severe(e.getMessage());
        } finally {
            pool.close(conn, ps, null);
        }
        return "";
    }
    public static int getInt(String table, String colun, String where, String playerName){
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = pool.getConnection();
            ps = conn.prepareStatement("SELECT * FROM `"+table+"` where LOWER(`"+where+"`) = '" + playerName.toLowerCase() + "';");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(colun);
            }
        } catch (SQLException e) {
            SystemConnector.log.severe(e.getMessage());
        } finally {
            pool.close(conn, ps, null);
        }
        return 0;
    }
    public static double getDouble(String table, String colun, String where, String playerName){
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = pool.getConnection();
            ps = conn.prepareStatement("SELECT * FROM `"+table+"` where LOWER(`"+where+"`) = '" + playerName.toLowerCase() + "';");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble(colun);
            }
        } catch (SQLException e) {
            SystemConnector.log.severe(e.getMessage());
        } finally {
            pool.close(conn, ps, null);
        }
        return 0;
    }

    public static void deleteValue(String table, String where, String value) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = pool.getConnection();
            ps = conn.prepareStatement("DELETE from " + table + " where LOWER(" + where + ") = ?");
            ps.setString(1, value.toLowerCase());
            ps.executeUpdate();
        } catch (SQLException e) {
            SystemConnector.log.severe(e.getMessage());
        } finally {
            pool.close(conn, ps, null);
        }
    }
    public static void deleteTable(String table) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = pool.getConnection();
            ps = conn.prepareStatement("DROP TABLE " + table + ";");
            ps.executeUpdate();
        } catch (SQLException e) {
            SystemConnector.log.severe(e.getMessage());
        } finally {
            pool.close(conn, ps, null);
        }
    }
}