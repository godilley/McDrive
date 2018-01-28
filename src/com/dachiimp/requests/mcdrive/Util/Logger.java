package com.dachiimp.requests.mcdrive.Util;

import com.dachiimp.requests.mcdrive.McDrive;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;

/**
 * Created by DaChiimp on 6/19/2016. For McDrive
 */
public class Logger {

    private McDrive mcd;
    private java.util.logging.Logger logger = java.util.logging.Logger.getLogger("Minecraft");


    public Logger(McDrive mcd) {
        this.mcd = mcd;
    }

    void print(String s) {
        ConsoleCommandSender ccs = mcd._plugin.getServer().getConsoleSender();
        ccs.sendMessage(ChatColor.DARK_BLUE + "[" + mcd._plugin.getDescription().getName() + "] " + ChatColor.AQUA + s);
    }

    public void log(String s) {
        if (s.length() == 0) return;

        s = ChatColor.stripColor(s);

        logger.info("[McDrive] " + s);
    }

    public void warn(String s) {
        if (s.length() == 0) return;

        s = ChatColor.stripColor(s);

        logger.warning("[McDrive] " + s);
    }

    public void severe(String s) {
        if (s.length() == 0) return;

        s = ChatColor.stripColor("[McDrive] " + s);

        logger.severe(s);
    }

}
