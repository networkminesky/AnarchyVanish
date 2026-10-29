package anarchy.minesky.plugin.visibility;

import anarchy.minesky.plugin.AnarchyVanishPlugin;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

public abstract class VanishStateMgr {

    protected final AnarchyVanishPlugin plugin;

    public VanishStateMgr(AnarchyVanishPlugin plugin) {
        this.plugin = plugin;
    }

    public abstract boolean isVanished(final UUID uuid);

    public abstract void setVanishedState(final UUID uuid, String name, boolean hide, String causeName);

    public final void setVanishedState(final UUID uuid, String name, boolean hide) {
        setVanishedState(uuid, name, hide, null);
    }

    public abstract Set<UUID> getVanishedPlayers();

    public abstract Collection<UUID> getOnlineVanishedPlayers();
}
