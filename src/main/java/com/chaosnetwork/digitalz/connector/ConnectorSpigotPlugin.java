package com.chaosnetwork.digitalz.connector;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ConnectorSpigotPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        reloadConfig();

        List<DataSource> dataSourceDetailsList = new ArrayList<>();
        ConfigurationSection section = getConfig().getConfigurationSection("Databases");
        Set<String> sourceNames = section.getKeys(false);

        for (String sourceName : sourceNames) {
            String username = section.getString(sourceName + ".Username");
            String password = section.getString(sourceName + ".Password");
            String url = section.getString(sourceName + ".URL");

            dataSourceDetailsList.add(new DataSource(sourceName, username, password, url));
        }

        // Create the data sources
        Connector instance = new Connector();
        instance.createDataSources(dataSourceDetailsList, getLogger());

        // Set the DbShare instance
        Connector.setInstance(instance);
    }

    @Override
    public void onDisable() {
        Connector instance = Connector.instance();
        if (instance != null) {
            // Unset the DbShare instance
            Connector.setInstance(null);

            // Close and remove the data sources
            instance.closeAndRemoveDataSources(getLogger());
        }
    }
}
