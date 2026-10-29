package anarchy.minesky.plugin.config;

public interface PluginFile<CT> {

    String getName();

    void reload();

    CT getConfig();

    void save();
}
