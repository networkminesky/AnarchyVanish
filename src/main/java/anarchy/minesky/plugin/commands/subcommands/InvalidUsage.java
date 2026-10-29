package anarchy.minesky.plugin.commands.subcommands;

import anarchy.minesky.plugin.AnarchyVanish;
import anarchy.minesky.plugin.commands.CommandAction;
import anarchy.minesky.plugin.commands.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class InvalidUsage extends SubCommand {

    public InvalidUsage(AnarchyVanish plugin) {
        super(plugin);
    }

    @Override
    public void execute(Command cmd, CommandSender sender, String[] args, String label) {
        if (!CommandAction.hasAnyCmdPermission(sender, plugin)) {
            plugin.sendMessage(sender, "NoPermission", sender);
            return;
        }
        plugin.sendMessage(sender, "InvalidUsage", sender);
    }
}
