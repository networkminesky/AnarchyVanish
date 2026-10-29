package anarchy.minesky.plugin.config;

import anarchy.minesky.plugin.AnarchyVanish;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class StorageFile implements PluginFile<FileConfiguration> {

    private final AnarchyVanish plugin;
    private String name;
    private File file;
    private FileConfiguration config;

    public StorageFile(String name, AnarchyVanish plugin) {
        this.name = name;
        this.plugin = plugin;
        setup();
    }

    @Override
    public String getName() {
        return name;
    }

    private void setup() {
        file = new File(plugin.getDataFolder().getPath() + File.separator
                + name + ".yml");
        config = YamlConfiguration.loadConfiguration(file);
        save();
    }

    @Override
    public void reload() {
        setup();
    }

    @Override
    public FileConfiguration getConfig() {
        return config;
    }

    @Override
    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
