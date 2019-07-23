package com.chaosnetwork.digitalz.connector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Sql extends ConnectionSql{

    private static Object lock = new Object();
    
    public static boolean has(String table, String colun, String value) {
        try {
            PreparedStatement statement = prepare("SELECT * FROM "+table+" where LOWER("+colun+") = '"+value.toLowerCase()+"';");
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return true;
            }
            rs.close();
        } catch (SQLException e) {
            SystemConnector.log.severe(e.getMessage());
        }
        return false;
    }
    public static void setString(String table, String colun, String where, String playerName, String value) {
        synchronized (lock) {
            try {
                PreparedStatement statement = prepare("UPDATE "+table+" SET "+colun+" = ? WHERE LOWER("+where+") = ?");
                statement.setString(1, value);
                statement.setString(2, playerName.toLowerCase());
                statement.executeUpdate();
            } catch (SQLException e) {
                SystemConnector.log.severe(e.getMessage());
            }
        }
    }
    public static void setInt(String table, String colun, String where, String playerName, int value) {
        synchronized (lock) {
            try {
                PreparedStatement statement = prepare("UPDATE "+table+" SET "+colun+" = ? WHERE LOWER("+where+") = ?");
                statement.setInt(1, value);
                statement.setString(2, playerName.toLowerCase());
                statement.executeUpdate();
            } catch (SQLException e) {
                SystemConnector.log.severe(e.getMessage());
            }
        }
    }
    public static void setDouble(String table, String colun, String where, String playerName, double value) {
        synchronized (lock) {
            try {
                PreparedStatement statement = prepare("UPDATE "+table+" SET "+colun+" = ? WHERE LOWER("+where+") = ?");
                statement.setDouble(1, value);
                statement.setString(2, playerName.toLowerCase());
                statement.executeUpdate();
            } catch (SQLException e) {
                SystemConnector.log.severe(e.getMessage());
            }
        }
    }

    public static String getString(String table, String colun, String where, String playerName){
        try {
            PreparedStatement statement = prepare("SELECT * FROM " + table + " where LOWER("+where+") = '" + playerName.toLowerCase() + "';");
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getString(colun);
            }
        } catch (SQLException e) {
            SystemConnector.log.severe(e.getMessage());
        }
        return "";
    }
    public static int getInt(String table, String colun, String where, String playerName){
        try {
            PreparedStatement statement = prepare("SELECT * FROM " + table + " where LOWER("+where+") = '" + playerName.toLowerCase() + "';");
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt(colun);
            }
        } catch (SQLException e) {
            SystemConnector.log.severe(e.getMessage());
        }
        return 0;
    }
    public static double getDouble(String table, String colun, String where, String playerName){
        try {
            PreparedStatement statement = prepare("SELECT * FROM " + table + " where LOWER("+where+") = '" + playerName.toLowerCase() + "';");
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getDouble(colun);
            }
        } catch (SQLException e) {
            SystemConnector.log.severe(e.getMessage());
        }
        return 0;
    }
    
    
    public static void deleteValue(String table, String where, String value) {
        try {
            PreparedStatement statement = prepare("DELETE from " + table + " where LOWER(" + where + ") = ?");
            statement.executeUpdate();
        } catch (SQLException e) {
            SystemConnector.log.severe(e.getMessage());
        }
    }
    public static void deleteTable(String table) {
        try {
            PreparedStatement statement = prepare("DROP TABLE "+table+";");
            statement.executeUpdate();
        } catch (SQLException e) {
            SystemConnector.log.severe(e.getMessage());
        }
    }
}