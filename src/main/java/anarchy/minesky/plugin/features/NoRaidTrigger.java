package anarchy.minesky.plugin.features;

import anarchy.minesky.plugin.AnarchyVanish;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.raid.RaidTriggerEvent;

public class NoRaidTrigger extends Feature {

    public NoRaidTrigger(AnarchyVanish plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onInteract(RaidTriggerEvent e) {
        Player p = e.getPlayer();
        if (!plugin.getVanishStateMgr().isVanished(p.getUniqueId())) return;
        e.setCancelled(true);
    }

    @Override
    public boolean isActive() {
        return plugin.getSettings().getBoolean("InvisibilityFeatures.PreventRaidTriggering", true);
    }
}
