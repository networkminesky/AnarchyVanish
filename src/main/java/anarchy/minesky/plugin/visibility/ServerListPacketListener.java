package anarchy.minesky.plugin.visibility;

import anarchy.minesky.plugin.AnarchyVanish;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.logging.Level;

public class ServerListPacketListener {

    public static void register(AnarchyVanish plugin) {
        try {
            Class.forName("com.destroystokyo.paper.event.server.PaperServerListPingEvent");
            plugin.getLogger().log(Level.INFO, "Hooked into PaperSpigot for server list vanish support");
            plugin.getServer().getPluginManager().registerEvents(new PaperServerPingListener(plugin), plugin);
        } catch (ClassNotFoundException e) {
            plugin.getLogger().log(Level.INFO, "PaperServerListPingEvent not found for server list vanish support");
        }
    }

    public static boolean isEnabled(AnarchyVanish plugin) {
        final FileConfiguration config = plugin.getSettings();
        return config.getBoolean("ExternalInvisibility.ServerList.AdjustAmountOfOnlinePlayers")
                || config.getBoolean("ExternalInvisibility.ServerList.AdjustListOfLoggedInPlayers");
    }
}
