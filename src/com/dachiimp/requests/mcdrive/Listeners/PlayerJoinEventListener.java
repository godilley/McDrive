package com.dachiimp.requests.mcdrive.Listeners;

import com.dachiimp.requests.mcdrive.McDrive;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by DaChiimp on 9/8/2016. For Major
 */
public class PlayerJoinEventListener implements Listener {

    private McDrive mcd;

    public PlayerJoinEventListener(McDrive mcd) {
        this.mcd = mcd;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        final Player player = e.getPlayer();
        if (!mcd.playerIDMethods.hasID(player.getUniqueId())) {

            if (mcd.disableUntilIDCreatedListeners.loginLocation.containsKey(player.getUniqueId()))
                mcd.disableUntilIDCreatedListeners.loginLocation.remove(player.getUniqueId());

            mcd.disableUntilIDCreatedListeners.loginLocation.put(player.getUniqueId(), player.getLocation());

            mcd.playerIDMethods.scheduleIDReminder(player);

            new BukkitRunnable() {
                @Override
                public void run() {
                    player.sendMessage("");
                    mcd.message("creatingID", player);
                    player.sendMessage("");
                }
            }.runTaskLater(mcd._plugin, 1L);

            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!player.isOnline()) return;

                    boolean result = mcd.playerIDMethods.createID(player.getUniqueId(), null, -1, null);
                    if (result) {
                        mcd.message("createdID", player);
                    } else {
                        player.kickPlayer(mcd.getMessage("errorCreatingID"));
                    }
                }
            }.runTaskLater(mcd._plugin, 40L);
        }
    }
}
