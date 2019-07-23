package com.chaosnetwork.digitalz.connector;

import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class SystemConnector extends JavaPlugin {

    public static Logger log;
    private static ConnectionPool pool;
    
    @Override
    public void onEnable() {
        log = this.getLogger();
        Bukkit.getConsoleSender().sendMessage("§c[§eSystemConnector§c] §eSystemConnector ativado v§c" + getDescription().getVersion() + "§e.");
        new Config(this);
        pool = new ConnectionPool();
        new Mysql();
        
        
        try {
            ConnectionSql.connect();
        } catch (Exception e) {
            log.severe(String.format(" Desabilitando! Nao foi possivel conectar o sql!"));
            return;
        }
    }

    @Override
    public void onDisable() {
        getConnection().close();
    }

    public static SystemConnector getPlugin() {
        return (SystemConnector) Bukkit.getServer().getPluginManager().getPlugin("SystemConnector");
    }
    
    public static ConnectionPool getConnection(){
        return pool;
    }
}
