package com.chaosnetwork.digitalz.connector;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Config {
    private final SystemConnector plugin;
    
    public static FileConfiguration c;
    public File file_config;
    
    public static String Username;
    public static String Password;
    public static String Database;
    public static String Host;

    public Config(SystemConnector instance) {
        this.plugin = instance;
        
        file_config = new File(plugin.getDataFolder(), "config.yml");
        if (!file_config.exists()) {
            if (!file_config.getParentFile().mkdirs()) this.plugin.getLogger().warning("");
                this.plugin.saveResource("config.yml", false);
        }
        
        this.reload();
    }

    public void reload(){
    	this.plugin.reloadConfig();
    	
        c = new YamlConfiguration();
        
        try {
            try {
                c.load(file_config);
            } catch (InvalidConfigurationException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        Username = c.getString("MySQL.Username");
        Password = c.getString("MySQL.Password");
        Database = c.getString("MySQL.Database");
        Host = c.getString("MySQL.Host");
    }

    public void save(){
        try {			
            c.save(file_config);
            this.reload();
        } catch (Exception e) {
            SystemConnector.getPlugin().getLogger().warning("Nao foi possivel salvar o arquivo de config e player_data.");
            e.printStackTrace();
        }
    }
}
