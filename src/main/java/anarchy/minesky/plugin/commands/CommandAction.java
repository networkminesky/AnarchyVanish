package anarchy.minesky.plugin.commands;

import anarchy.minesky.plugin.AnarchyVanish;
import anarchy.minesky.plugin.utils.Validation;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public enum CommandAction {
    VANISH_SELF(
            "anarchyvanish.use",
            false,
            "/av [on|off]",
            "Toggles your visibility") {
        @Override
        public boolean checkPermission(CommandSender sender, AnarchyVanish plugin) {
            Validation.checkNotNull(plugin, "plugin cannot be null");
            return plugin.hasPermissionToVanish(sender);
        }
    },
    VANISH_OTHER(
            "anarchyvanish.others",
            true,
            "/av [on|off] <player>",
            "Shows or hides a player"),
    VANISHED_LIST(
            "anarchyvanish.list",
            true,
            "/av list",
            "Shows a list of vanished players"),
    BROADCAST_LOGIN(
            "anarchyvanish.login",
            false,
            "/av login",
            "Broadcasts a fake login message"),
    BROADCAST_LOGOUT(
            "anarchyvanish.logout",
            false,
            "/av logout",
            "Broadcasts a fake logout message"),
    TOGGLE_ITEM_PICKUPS(
            "anarchyvanish.toggleitems",
            false,
            "/av tipu",
            "Toggles picking up items") {
        @Override
        public boolean checkPermission(CommandSender sender, AnarchyVanish plugin) {
            return sender.hasPermission(getMainPermission()) || sender.hasPermission("anarchyvanish.toggleitempickups");
        }
    },
    RECREATE_FILES(
            "anarchyvanish.recreatefiles",
            true,
            "/av recreatefiles",
            "Recreates the config") {
        @Override
        public boolean checkPermission(CommandSender sender, AnarchyVanish plugin) {
            return sender.hasPermission(getMainPermission()) || sender.hasPermission("anarchyvanish.updatecfg");
        }
    },
    RELOAD(
            "anarchyvanish.reload",
            true,
            "/av reload",
            "Reloads all settings and messages"),
    PRINT_STACKTRACE(
            "anarchyvanish.stacktrace",
            true,
            "/av stacktrace",
            "Logs info for a bug report"),
    SHOW_HELP(
            "anarchyvanish.help",
            true,
            "/av help",
            "Shows this help page");

    /**
     * Use {@link #checkPermission(CommandSender, AnarchyVanish)} to check whether a {@link CommandSender} has
     * permission to perform an action or not
     */
    private final String mainPermission;
    private final boolean console;
    private final String usage, description;

    CommandAction(String mainPermission, boolean console, String usage, String description) {
        this.mainPermission = mainPermission;
        this.console = console;
        this.usage = usage;
        this.description = description;
    }

    public String getMainPermission() {
        return mainPermission;
    }

    public String getUsage() {
        return usage;
    }

    public String getDescription() {
        return description;
    }

    static List<String> getAvailableFirstArguments(CommandSender sender, AnarchyVanish plugin) {
        Validation.checkNotNull(plugin, "plugin cannot be null");
        List<String> list = new ArrayList<>();
        if (SHOW_HELP.checkPermission(sender, plugin))
            list.add("help");
        if (RECREATE_FILES.checkPermission(sender, plugin))
            list.add("recreatefiles");
        if (RELOAD.checkPermission(sender, plugin))
            list.add("reload");
        if (VANISHED_LIST.checkPermission(sender, plugin))
            list.add("list");
        if (VANISH_SELF.checkPermission(sender, plugin))
            list.add("on");
        if (VANISH_SELF.checkPermission(sender, plugin))
            list.add("off");
        if (TOGGLE_ITEM_PICKUPS.checkPermission(sender, plugin))
            list.add("tipu");
        if (PRINT_STACKTRACE.checkPermission(sender, plugin))
            list.add("stacktrace");
        return list;
    }

    public static boolean hasAnyCmdPermission(CommandSender sender, AnarchyVanish plugin) {
        for (CommandAction action : CommandAction.values())
            if (action.checkPermission(sender, plugin))
                return true;
        return false;
    }

    public boolean checkPermission(CommandSender sender, AnarchyVanish plugin) {
        return sender.hasPermission(getMainPermission());
    }

    public boolean usableByConsole() {
        return console;
    }
}