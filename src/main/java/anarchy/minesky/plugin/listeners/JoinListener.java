package anarchy.minesky.plugin.listeners;

import anarchy.minesky.plugin.AnarchyVanish;
import anarchy.minesky.plugin.features.Broadcast;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.EventExecutor;

public class JoinListener implements EventExecutor, Listener {

    private final AnarchyVanish plugin;

    public JoinListener(AnarchyVanish plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Listener l, Event event) {
        try {
            if (event instanceof PlayerJoinEvent) {
                PlayerJoinEvent e = (PlayerJoinEvent) event;
                final Player p = e.getPlayer();
                // hide others
                for (Player onlinePlayer : Bukkit.getOnlinePlayers())
                    if (plugin.getVanishStateMgr().isVanished(onlinePlayer.getUniqueId())
                            && !plugin.hasPermissionToSee(p, onlinePlayer))
                        plugin.getVisibilityChanger().getHider().setHidden(onlinePlayer, p, true);
                // vanished:
                if (plugin.getVanishStateMgr().isVanished(p.getUniqueId())) {
                    // hide self
                    for (Player onlinePlayer : Bukkit.getOnlinePlayers())
                        if (!plugin.hasPermissionToSee(onlinePlayer, p))
                            plugin.getVisibilityChanger().getHider().setHidden(p, onlinePlayer, true);
                    // Join message
                    if (plugin.getSettings().getBoolean("MessageOptions.HideRealJoinQuitMessages")) {
                        e.setJoinMessage(null);
                        Broadcast.announceSilentJoin(p, plugin);
                    }
                    // reminding message
                    if (plugin.getSettings().getBoolean("MessageOptions.RemindVanishedOnJoin")) {
                        plugin.sendMessage(p, "RemindingMessage", p);
                    }
                    // re-add action bar
                    if (plugin.getActionBarMgr() != null && plugin.getSettings().getBoolean(
                            "MessageOptions.DisplayActionBar")) {
                        plugin.getActionBarMgr().addActionBar(p);
                    }
                    // sleep state
                    p.setSleepingIgnored(true);
                    // adjust fly
                    if (plugin.getSettings().getBoolean("InvisibilityFeatures.Fly.Enable")) {
                        p.setAllowFlight(true);
                    }
                    // metadata
                    p.setMetadata("vanished", new FixedMetadataValue(plugin, true));
                } else {
                    // not vanished:
                    // metadata
                    p.removeMetadata("vanished", plugin);
                }
                // not necessarily vanished:
                // recreate files msg
                if ((p.hasPermission("anarchyvanish.recreatecfg") || p.hasPermission("anarchyvanish.recreatefiles"))
                        && (plugin.getConfigMgr().isSettingsUpdateRequired()
                        || plugin.getConfigMgr().isMessagesUpdateRequired())) {
                    String currentVersion = plugin.getDescription().getVersion();
                    boolean isDismissed =
                            plugin.getPlayerData().getBoolean("PlayerData." + p.getUniqueId() + ".dismissed."
                                    + currentVersion.replace(".", "_"), false);
                    if (!isDismissed)
                        Bukkit.getGlobalRegionScheduler().runDelayed(plugin, (task) -> {
                            plugin.sendMessage(p, "RecreationRequiredMsg", p);
                        }, 1L);
                }
            }
        } catch (Exception er) {
            plugin.logException(er);
        }
    }
}