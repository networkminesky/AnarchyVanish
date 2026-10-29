package anarchy.minesky.plugin.hooks;

import anarchy.minesky.plugin.AnarchyVanish;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.UUID;

public class PlaceholderAPIHook extends PluginHook {

    private final String yes, no, prefix, suffix;

    public PlaceholderAPIHook(AnarchyVanish anarchyVanish) {
        super(anarchyVanish);
        yes = anarchyVanish.getMessage("PlaceholderIsVanishedYes");
        no = anarchyVanish.getMessage("PlaceholderIsVanishedNo");
        prefix = anarchyVanish.getMessage("PlaceholderVanishPrefix");
        suffix = anarchyVanish.getMessage("PlaceholderVanishSuffix");
        new MSVPlaceholderExpansion().register();
    }

    public static String translatePlaceholders(String msg, Player p) {
        return PlaceholderAPI.setPlaceholders((OfflinePlayer) p, msg);
    }

    public class MSVPlaceholderExpansion extends PlaceholderExpansion {

        @Override
        public boolean persist() {
            return true;
        }

        @Override
        public boolean canRegister() {
            return true;
        }

        @Override
        public String getAuthor() {
            return anarchyVanish.getDescription().getAuthors().toString();
        }

        @Override
        public String getIdentifier() {
            return "anarchyvanish";
        }

        @Override
        public String getVersion() {
            return anarchyVanish.getDescription().getVersion();
        }

        @Override
        public String onRequest(OfflinePlayer op, String id) {
            try {
                Player p;
                if (op instanceof Player)
                    p = (Player) op;
                else
                    p = null;
                if (id.equalsIgnoreCase("isvanished")
                        || id.equalsIgnoreCase("isinvisible")
                        || id.equalsIgnoreCase("vanished")
                        || id.equalsIgnoreCase("invisible"))
                    return p != null && anarchyVanish.getVanishStateMgr().isVanished(p.getUniqueId())
                            ? yes : no;
                if (id.equalsIgnoreCase("vanishprefix"))
                    return p != null && anarchyVanish.getVanishStateMgr().isVanished(p.getUniqueId())
                            ? prefix : "";
                if (id.equalsIgnoreCase("vanishsuffix"))
                    return p != null && anarchyVanish.getVanishStateMgr().isVanished(p.getUniqueId())
                            ? suffix : "";
                if (id.equalsIgnoreCase("onlinevanishedplayers")
                        || id.equalsIgnoreCase("onlinevanished")
                        || id.equalsIgnoreCase("invisibleplayers")
                        || id.equalsIgnoreCase("vanishedplayers")
                        || id.equalsIgnoreCase("hiddenplayers")) {
                    Collection<UUID> onlineVanishedPlayers = anarchyVanish.getVanishStateMgr()
                            .getOnlineVanishedPlayers();
                    String playerListMessage = "";
                    for (UUID uuid : onlineVanishedPlayers) {
                        Player onlineVanished = Bukkit.getPlayer(uuid);
                        if (onlineVanished == null) continue;
                        if (anarchyVanish.getSettings().getBoolean(
                                "IndicationFeatures.LayeredPermissions.HideInvisibleInCommands", false)
                                && !anarchyVanish.hasPermissionToSee(p, onlineVanished)) {
                            continue;
                        }
                        playerListMessage = playerListMessage + onlineVanished.getName() + ", ";
                    }
                    return playerListMessage.length() > 3
                            ? playerListMessage.substring(0, playerListMessage.length() - 2)
                            : playerListMessage;
                }
                if (id.equalsIgnoreCase("playercount")
                        || id.equalsIgnoreCase("onlineplayers")) {
                    int playercount = Bukkit.getOnlinePlayers().size();
                    for (UUID uuid : anarchyVanish.getVanishStateMgr()
                            .getOnlineVanishedPlayers()) {
                        Player onlineVanished = Bukkit.getPlayer(uuid);
                        if (onlineVanished == null) continue;
                        if (p == null || !anarchyVanish.canSee(p, onlineVanished)) playercount--;
                    }
                    return playercount + "";
                }
            } catch (Exception e) {
                anarchyVanish.logException(e);
            }
            return null;
        }
    }
}
