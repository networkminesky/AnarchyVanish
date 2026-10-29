package anarchy.minesky.plugin;

import anarchy.minesky.plugin.utils.Validation;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlaceholderConverter {

    private final AnarchyVanish plugin;

    public PlaceholderConverter(AnarchyVanish plugin) {
        this.plugin = plugin;
    }

    public String replacePlaceholders(String msg, Object... additionalPlayerInfo) {
        Validation.checkIsTrue("Failed to replace variables (Illegal arguments)",
                msg != null, additionalPlayerInfo != null);
        //noinspection ConstantConditions
        Validation.checkIsTrue(additionalPlayerInfo.length > 0);

        final List<Object> additionalPlayerInfoList = Arrays.asList(additionalPlayerInfo);
        Object unspecifiedPlayer = additionalPlayerInfoList.get(0);
        String unspecifiedOtherPlayersName = null;

        if (additionalPlayerInfoList.size() > 1
                && (additionalPlayerInfoList.get(1) instanceof String
                || additionalPlayerInfoList.get(1) instanceof Player)) {
            unspecifiedOtherPlayersName = (String) (additionalPlayerInfoList.get(1) instanceof Player
                    ? ((Player) additionalPlayerInfoList.get(1)).getName()
                    : additionalPlayerInfoList.get(1));
        }

        msg = msg.replace("\\n", "\n");

        // Substituição das variáveis por tipo de remetente
        replaceVariables:
        {
            if (unspecifiedPlayer instanceof OfflinePlayer
                    && !(unspecifiedPlayer instanceof Player)) {
                // Jogador Offline
                OfflinePlayer specifiedPlayer = (OfflinePlayer) unspecifiedPlayer;

                // PlaceholderAPI (suporte a OfflinePlayer)
                if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")
                        && plugin.getSettings().getBoolean("HookOptions.EnablePlaceholderAPIHook", true)) {
                    String replaced = PlaceholderAPI.setPlaceholders(specifiedPlayer, msg);
                    msg = replaced == null ? msg : replaced;
                }

                String name = specifiedPlayer.getName() != null ? specifiedPlayer.getName() : "";

                // Variáveis gerais
                msg = msg.replace("%d%", name)
                        .replace("%p%", name)
                        .replace("%tab%", name)
                        .replace("%nick%", name);

                msg = msg.replace("%other%", unspecifiedOtherPlayersName != null
                        ? unspecifiedOtherPlayersName : "UNKNOWN");
                break replaceVariables;
            }

            if (unspecifiedPlayer instanceof Player) {
                // Jogador Online
                Player specifiedPlayer = (Player) unspecifiedPlayer;

                // PlaceholderAPI
                if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")
                        && plugin.getSettings().getBoolean("HookOptions.EnablePlaceholderAPIHook", true)) {
                    String replaced = PlaceholderAPI.setPlaceholders(specifiedPlayer, msg);
                    msg = replaced == null ? msg : replaced;
                }

                // Variáveis gerais do Bukkit nativo
                msg = msg.replace("%d%", specifiedPlayer.getDisplayName() != null ? specifiedPlayer.getDisplayName() : specifiedPlayer.getName())
                        .replace("%p%", specifiedPlayer.getName())
                        .replace("%tab%", specifiedPlayer.getPlayerListName() != null ? specifiedPlayer.getPlayerListName() : specifiedPlayer.getName())
                        .replace("%nick%", specifiedPlayer.getDisplayName() != null ? specifiedPlayer.getDisplayName() : specifiedPlayer.getName());

                msg = msg.replace("%other%", unspecifiedOtherPlayersName != null
                        ? unspecifiedOtherPlayersName : "UNKNOWN");

                break replaceVariables;
            }

            if (unspecifiedPlayer instanceof CommandSender) {
                // Console / CommandSender
                msg = msg.replace("%d%", "Console")
                        .replace("%p%", "Console")
                        .replace("%tab%", "Console")
                        .replace("%nick%", "Console");

                msg = msg.replace("%other%", unspecifiedOtherPlayersName != null
                        ? unspecifiedOtherPlayersName : "UNKNOWN");
            }
        }

        // Conversão de cores HEX (1.16+)
        if (plugin.getVersionUtil().isOneDotXOrHigher(16)) {
            Pattern pattern = Pattern.compile("\\{?&?#[a-fA-F0-9]{6}\\}?");
            Matcher matcher = pattern.matcher(msg);

            while (matcher.find()) {
                String color = msg.substring(matcher.start(), matcher.end());
                msg = msg.replace(color, net.md_5.bungee.api.ChatColor.of(color.replace("&", "").replace("{", "").replace("}", "")) + "");
                matcher = pattern.matcher(msg);
            }
        }

        msg = ChatColor.translateAlternateColorCodes('&', msg);
        return msg;
    }
}