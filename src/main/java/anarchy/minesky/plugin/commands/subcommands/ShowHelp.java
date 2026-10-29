package anarchy.minesky.plugin.commands.subcommands;

import anarchy.minesky.plugin.AnarchyVanish;
import anarchy.minesky.plugin.commands.CommandAction;
import anarchy.minesky.plugin.commands.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ShowHelp extends SubCommand {

    public ShowHelp(AnarchyVanish plugin) {
        super(plugin);
    }

    @Override
    public void execute(Command cmd, CommandSender sender, String[] args, String label) {
        if (canDo(sender, CommandAction.SHOW_HELP, true)) {
            plugin.sendMessage(sender, "HelpHeader", sender);
            for (CommandAction action : CommandAction.values()) {
                if (canDo(sender, action, false)) {
                    plugin.sendMessage(sender, plugin.getMessage("HelpFormat")
                            .replace("%usage%", action.getUsage())
                            .replace("%description%", action.getDescription())
                            .replace("%permission%", action.getMainPermission()), sender);
                }
            }
        }
    }
}
