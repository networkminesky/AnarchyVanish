package anarchy.minesky.plugin.visibility.hiders;

import anarchy.minesky.plugin.AnarchyVanish;
import anarchy.minesky.plugin.utils.BukkitPlayerHidingUtil;
import org.bukkit.entity.Player;

public class PreventionHider extends PlayerHider implements Runnable {

    private Object taskId = null;

    public PreventionHider(AnarchyVanish plugin) {
        super(plugin);
        if (!BukkitPlayerHidingUtil.isNewPlayerHidingAPISupported(plugin)) {
            taskId = plugin.getServer().getGlobalRegionScheduler()
                    .runAtFixedRate(plugin, task -> this.run(), 2L, 2L);
        }
    }

    @Override
    public boolean setHidden(Player player, Player viewer, boolean hidden) {
        if (super.setHidden(player, viewer, hidden) || BukkitPlayerHidingUtil.isNewPlayerHidingAPISupported(plugin)) {
            if (hidden) BukkitPlayerHidingUtil.hidePlayer(player, viewer, plugin);
            else BukkitPlayerHidingUtil.showPlayer(player, viewer, plugin);
            return true;
        }
        return false;
    }

    @Override
    public String getName() {
        return "Prevention";
    }

    @Override
    public void run() {
        for (Player hidden : playerHiddenFromPlayersMap.keySet()) {
            if (BukkitPlayerHidingUtil.isNewPlayerHidingAPISupported(plugin)) {
                ((io.papermc.paper.threadedregions.scheduler.ScheduledTask) this.taskId).cancel();
                taskId = null;
                return;
            }
            for (Player viewer : playerHiddenFromPlayersMap.get(hidden)) {
                BukkitPlayerHidingUtil.hidePlayer(hidden, viewer, plugin);
            }
        }
    }
}
