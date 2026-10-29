package anarchy.minesky.plugin;

import anarchy.minesky.plugin.visibility.VanishStateMgr;

import java.util.logging.Level;
import java.util.logging.Logger;

public interface AnarchyVanishPlugin {

    void log(Level level, String msg);

    void log(Level level, String msg, Throwable ex);

    Logger getLogger();

    void logException(Throwable e);

    VanishStateMgr getVanishStateMgr();
}
