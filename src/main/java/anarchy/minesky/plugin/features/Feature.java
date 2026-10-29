package anarchy.minesky.plugin.features;

import anarchy.minesky.plugin.AnarchyVanish;
import org.bukkit.event.Listener;

/**
 * Represents a toggleable feature of MineSkyVanish
 */
public abstract class Feature implements Listener {

    protected final AnarchyVanish plugin;

    public Feature(AnarchyVanish plugin) {
        this.plugin = plugin;
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public abstract boolean isActive();

    protected void delay(Runnable runnable) {
        plugin.getServer().getGlobalRegionScheduler().runDelayed(plugin, task -> runnable.run(), 1L);
    }
}
