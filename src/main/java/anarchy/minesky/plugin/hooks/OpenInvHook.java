package anarchy.minesky.plugin.hooks;

import anarchy.minesky.api.vanish.events.PlayerShowEvent;
import anarchy.minesky.api.vanish.events.PostPlayerHideEvent;
import anarchy.minesky.plugin.AnarchyVanish;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class OpenInvHook extends PluginHook {

    private boolean errorLogged = false;
    private final Set<UUID> alreadyHiddenBeforeVanishing = new HashSet<>();

    public OpenInvHook(AnarchyVanish anarchyVanish) {
        super(anarchyVanish);
    }

    private boolean getSilentContainerStatus(Player player) {
        if (plugin == null) return false;
        try {
            Method method = plugin.getClass().getMethod("getSilentContainerStatus", Player.class);
            return (boolean) method.invoke(plugin, player);
        } catch (Exception e) {
            logError(e);
            return false;
        }
    }

    private void setSilentContainerStatus(Player player, boolean status) {
        if (plugin == null) return;
        try {
            Method method = plugin.getClass().getMethod("setSilentContainerStatus", Player.class, boolean.class);
            method.invoke(plugin, player, status);
        } catch (Exception e) {
            logError(e);
        }
    }

    @EventHandler
    public void onVanish(PostPlayerHideEvent e) {
        try {
            Player p = e.getPlayer();
            if (getSilentContainerStatus(p)) {
                alreadyHiddenBeforeVanishing.add(p.getUniqueId());
            } else {
                if (!p.hasPermission("anarchyvanish.silentchest")) return;
                setSilentContainerStatus(p, true);
            }
        } catch (Exception er) {
            logError(er);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onReappear(PlayerShowEvent e) {
        try {
            Player p = e.getPlayer();
            if (!alreadyHiddenBeforeVanishing.remove(p.getUniqueId())) {
                setSilentContainerStatus(p, false);
            }
        } catch (Exception er) {
            logError(er);
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onJoin(PlayerJoinEvent e) {
        try {
            Player p = e.getPlayer();
            if (anarchyVanish.getVanishStateMgr().isVanished(p.getUniqueId())) {
                if (getSilentContainerStatus(p)) {
                    alreadyHiddenBeforeVanishing.add(p.getUniqueId());
                } else {
                    if (!p.hasPermission("anarchyvanish.silentchest")) return;
                    setSilentContainerStatus(p, true);
                }
            }
        } catch (Exception er) {
            logError(er);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent e) {
        try {
            Player p = e.getPlayer();
            if (anarchyVanish.getVanishStateMgr().isVanished(p.getUniqueId())) {
                if (!alreadyHiddenBeforeVanishing.remove(p.getUniqueId())) {
                    setSilentContainerStatus(p, false);
                }
            }
        } catch (Exception er) {
            logError(er);
        }
    }

    public boolean openPlayerInventory(Player vanished, Player target) {
        if (plugin == null) return false;
        try {
            Method getSpecialInv = plugin.getClass().getMethod("getSpecialInventory", Player.class, boolean.class);
            Object specialInv = getSpecialInv.invoke(plugin, target, true);
            for (Method m : plugin.getClass().getMethods()) {
                if (m.getName().equals("openInventory") && m.getParameterCount() == 2 && m.getParameterTypes()[0] == Player.class) {
                    m.invoke(plugin, vanished, specialInv);
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            logError(e);
            return false;
        }
    }

    private void logError(Throwable t) {
        if (!errorLogged) {
            anarchyVanish.logException(t);
            errorLogged = true;
        }
    }
}
