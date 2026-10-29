package anarchy.minesky.plugin.commands.subcommands;

import anarchy.minesky.plugin.AnarchyVanish;
import anarchy.minesky.plugin.commands.CommandAction;
import anarchy.minesky.plugin.commands.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BroadcastLogout extends SubCommand {

    public BroadcastLogout(AnarchyVanish plugin) {
        super(plugin);
    }

    @Override
    public void execute(Command cmd, CommandSender p, String[] args, String label) {
        if (canDo(p, CommandAction.BROADCAST_LOGOUT, true)) {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers())
                plugin.sendMessage(onlinePlayer, "VanishMessage", p, onlinePlayer);
        }
    }
}
