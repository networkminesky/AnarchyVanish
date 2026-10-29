package anarchy.minesky.plugin.commands.subcommands;

import anarchy.minesky.plugin.AnarchyVanish;
import anarchy.minesky.plugin.commands.CommandAction;
import anarchy.minesky.plugin.commands.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class PrintStacktrace extends SubCommand {

    public PrintStacktrace(AnarchyVanish plugin) {
        super(plugin);
    }

    @Override
    public void execute(Command cmd, CommandSender sender, String[] args, String label) {
        if (canDo(sender, CommandAction.PRINT_STACKTRACE, true)) {
            plugin.sendMessage(sender, "PrintedStacktrace", sender);
            plugin.logException(null);
        }
    }
}
