package com.chaosnetwork.digitalz.connector;

import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ConnectorBungeePlugin extends Plugin {

    private Configuration config;

    @Override
    public void onEnable() {
        // Reload the config
        reloadConfig();

        List<DataSource> DataSourceList = new ArrayList<>();
        Configuration section = config.getSection("Databases");
        Collection<String> sourceNames = section.getKeys();

        // Read the new data source details
        for (String sourceName : sourceNames) {
            String username = section.getString(sourceName + ".Username");
            String password = section.getString(sourceName + ".Password");
            String url = section.getString(sourceName + ".URL");

            DataSourceList.add(new DataSource(sourceName, username, password, url));
        }

        // Create the data sources
        Connector instance = new Connector();
        instance.createDataSources(DataSourceList, getLogger());

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

    private void reloadConfig() {
        try {
            File file = saveResource(this, "config.yml", "config.yml");
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);

            if (config == null) {
                saveResource(this, "config.yml", "config.yml");
            }
        } catch (IOException e) {
            getLogger().severe("Failed to load configuration file.");
            e.printStackTrace();
        }
    }

    /**
     * Loads the resource from the JAR and saves it to the destination under the
     * plugin's data folder. By default, the destination file will not be
     * replaced if it exists.
     * <p>
     * Source for the majority of this method can be found at:
     * https://www.spigotmc.org/threads/bungeecords-configuration-api.11214/#post-119017
     * <p>
     * Originally authored by: vemacs, Feb 15, 2014
     *
     * @param plugin Plugin that contains the resource in it's JAR.
     * @param resourceName Filename of the resource.
     * @param destinationName Filename of the destination.
     *
     * @return Destination File.
     */
    private static File saveResource(Plugin plugin, String resourceName, String destinationName) {
        File folder = plugin.getDataFolder();
        if (!folder.exists() && !folder.mkdir()) {
            return null;
        }

        File destinationFile = new File(folder, destinationName);
        try {
            if (!destinationFile.exists()) {
                if (destinationFile.createNewFile()) {
                    try (InputStream in = plugin.getResourceAsStream(resourceName);
                            OutputStream out = new FileOutputStream(destinationFile)) {
                        ByteStreams.copy(in, out);
                    }
                } else {
                    return null;
                }
            }
            return destinationFile;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
