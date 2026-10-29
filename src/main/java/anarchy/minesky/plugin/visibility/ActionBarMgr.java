package anarchy.minesky.plugin.visibility;

import anarchy.minesky.plugin.AnarchyVanish;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

public class ActionBarMgr {

    private final AnarchyVanish plugin;
    private final List<Player> actionBars = new CopyOnWriteArrayList<>();
    private ScheduledTask task;

    public ActionBarMgr(AnarchyVanish plugin) {
        this.plugin = plugin;
        startTask();
    }

    private void startTask() {
        this.task = Bukkit.getAsyncScheduler().runAtFixedRate(plugin, scheduledTask -> {
            for (Player p : actionBars) {
                if (p == null || !p.isOnline()) {
                    actionBars.remove(p);
                    continue;
                }

                try {
                    sendActionBar(p, plugin.replacePlaceholders(plugin.getMessage("ActionBarMessage"), p));
                } catch (Exception e) {
                    plugin.logException(e);
                }
            }
        }, 50, 2000, TimeUnit.MILLISECONDS);
    }

    private void sendActionBar(Player p, String bar) {
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(bar));
    }

    public void addActionBar(Player p) {
        if (!actionBars.contains(p)) {
            actionBars.add(p);
        }
    }

    public void removeActionBar(Player p) {
        actionBars.remove(p);
    }

    public void stopTask() {
        if (this.task != null) {
            this.task.cancel();
        }
    }
}
