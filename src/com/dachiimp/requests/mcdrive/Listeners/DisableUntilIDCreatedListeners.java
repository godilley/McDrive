package com.dachiimp.requests.mcdrive.Listeners;

import com.dachiimp.requests.mcdrive.McDrive;
import com.dachiimp.requests.mcdrive.Util.ClassSaving.PlayerID;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by DaChiimp on 09/09/2016. For Majors
 */
public class DisableUntilIDCreatedListeners implements Listener {

    public HashMap<UUID, Location> loginLocation = new HashMap<>();
    private McDrive mcd;

    public DisableUntilIDCreatedListeners(McDrive mcd) {
        this.mcd = mcd;
    }

    @EventHandler
    void onChat(AsyncPlayerChatEvent e) {
        if (e.isCancelled()) return;

        Player player = e.getPlayer();
        if (cancelEvent(player)) {
            e.setCancelled(true);
            player.sendMessage(mcd.getMessage("dontHaveID"));
        }
    }

    @EventHandler
    void onCommand(PlayerCommandPreprocessEvent e) {
        if (e.isCancelled()) return;

        if (!e.getMessage().split(" ")[0].toLowerCase().equalsIgnoreCase("id")) return;

        Player player = e.getPlayer();
        if (cancelEvent(player)) {
            e.setCancelled(true);
            player.sendMessage(mcd.getMessage("dontHaveID"));
        }
    }


    public boolean cancelEvent(Player player) {
        UUID uuid = player.getUniqueId();

        if (mcd.playerIDMethods.hasID(uuid)) {
            PlayerID id = mcd.playerIDMethods.getPlayerID(uuid);
            boolean cancel = id.getAge() != -1 && id.getName() != null && id.getGender() != null;
            return !cancel;
        } else {
            return true;
        }
    }
}
