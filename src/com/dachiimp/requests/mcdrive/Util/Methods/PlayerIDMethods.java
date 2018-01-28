package com.dachiimp.requests.mcdrive.Util.Methods;

import com.dachiimp.requests.mcdrive.McDrive;
import com.dachiimp.requests.mcdrive.Util.ClassSaving.PlayerID;
import com.dachiimp.requests.mcdrive.Util.Gender;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by DaChiimp on 9/8/2016. For Majors
 */
public class PlayerIDMethods {

    public String idTitle = ChatColor.GREEN + "ID Card > ";
    public final String idTitlePlayer = idTitle + "%player%";
    private McDrive mcd;
    private HashMap<UUID, UUID> idRequests = new HashMap<>();

    public PlayerIDMethods(McDrive mcd) {
        this.mcd = mcd;
    }

    public boolean hasID(UUID uuid) {
        return mcd.playerIDs.containsKey(uuid);
    }

    public boolean modifyID(UUID uuid, String toModify, String value) {

        if (!hasID(uuid)) {
            boolean result = mcd.playerIDMethods.createID(uuid, null, -1, null);
            if (!result)
                return false;
        }

        PlayerID id = getPlayerID(uuid);
        switch (toModify.toLowerCase()) {
            case "name": {
                id.setName(value);
                return true;
            }
            case "age": {
                try {
                    int age = Integer.parseInt(value);
                    id.setAge(age);
                    return true;
                } catch (NumberFormatException e) {
                    return false;
                }
            }
            case "gender": {
                if (value.equalsIgnoreCase("male")) {
                    id.setGender(Gender.MALE);
                    return true;
                } else if (value.equalsIgnoreCase("female")) {
                    id.setGender(Gender.FEMALE);
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    public boolean createID(UUID uuid, String name, int age, Gender gender) {
        if (hasID(uuid)) return false;

        mcd.playerIDs.put(uuid, new PlayerID(uuid, name, age, gender));

        return hasID(uuid);

    }

    public PlayerID getPlayerID(UUID uuid) {
        if (mcd.playerIDs.containsKey(uuid)) {
            return mcd.playerIDs.get(uuid);
        }
        return null;
    }

    public void scheduleIDReminder(final Player player) {
        if (mcd.disableUntilIDCreatedListeners.cancelEvent(player)) {
            mcd.message("dontHaveID", player);
            player.teleport(mcd.disableUntilIDCreatedListeners.loginLocation.get(player.getUniqueId()));
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (player.isOnline()) {
                        scheduleIDReminder(player);
                    }
                }
            }.runTaskLater(mcd._plugin, 60L);
        }
    }

    public boolean hasSentIDRequest(Player player) {
        return idRequests.containsKey(player.getUniqueId());
    }

    public boolean hasReceivedIDRequest(Player player) {
        for (UUID uuid : idRequests.keySet()) {
            if (idRequests.get(uuid) == player.getUniqueId()) return true;
        }

        return false;
    }

    public UUID getReceivedIDRequestUUID(Player player) {

        if (!hasReceivedIDRequest(player)) return null;

        for (UUID uuid : idRequests.keySet()) {
            if (idRequests.get(uuid) == player.getUniqueId()) return uuid;
        }

        return null;
    }

    public boolean sendRequest(final Player player, final Player target) {
        // TODO: Send request method

        // check that player doesn't have an active request
        if (!hasSentIDRequest(player)) {
            idRequests.put(player.getUniqueId(), target.getUniqueId());
            mcd.message("receivedRequest", target, "%player%", player.getName());

            // Expire after 20 seconds
            new BukkitRunnable() {
                int seconds = 0;

                @Override
                public void run() {
                    seconds++;
                    if (idRequests.containsKey(player.getUniqueId())) {
                        if (idRequests.get(player.getUniqueId()) == target.getUniqueId()) {
                            if (seconds >= 21) {
                                idRequests.remove(player.getUniqueId());
                                if (player.isOnline())
                                    mcd.message("checkRequestExpired", player);
                                if (target.isOnline())
                                    mcd.message("checkRequestExpired", target);

                            }
                        } else {
                            cancel();
                        }
                    } else {
                        cancel();
                    }
                }
            }.runTaskTimer(mcd._plugin, 0L, 20L);

            return true;
        }

        return false;
    }

    public void acceptRequest(Player player) {
        UUID toAccept = getReceivedIDRequestUUID(player);
        Player target = Bukkit.getServer().getPlayer(toAccept); // is the player to show inv of
        if (target != null) {
            idRequests.remove(toAccept);
            mcd.message("checkRequestAccepted", player);
            mcd.message("checkRequestAccepted", target);
            openIDGUI(target, player);
        } else {
            mcd.message("playerOffline", player);
            idRequests.remove(toAccept);
        }
    }

    public void openIDGUI(Player player, Player target) {
        player.closeInventory();
        Inventory inv = Bukkit.createInventory(null, 27, idTitlePlayer.replaceAll("%player%", target.getName()));
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
        SkullMeta skullMeta = (SkullMeta) head.getItemMeta();
        skullMeta.setOwner(target.getName());
        skullMeta.setDisplayName(ChatColor.GREEN + "Photo");
        head.setItemMeta(skullMeta);
        inv.setItem(10, head);

        PlayerID id = getPlayerID(target.getUniqueId());

        for (int i = 0; i < 2; i++) {
            ItemStack sign = new ItemStack(Material.SIGN);
            ItemMeta meta = sign.getItemMeta();
            if (i == 1) {
                meta.setDisplayName(ChatColor.GREEN + "Name: " + ChatColor.DARK_GRAY + id.getName());
                sign.setItemMeta(meta);
                inv.setItem(12, sign);
            } else {
                meta.setDisplayName(ChatColor.GREEN + "Age: " + ChatColor.DARK_GRAY + id.getAge());
                sign.setItemMeta(meta);
                inv.setItem(13, sign);
            }

        }

        ItemStack dye = new ItemStack(Material.INK_SACK);
        ItemMeta dyeMeta = dye.getItemMeta();

        if (id.getGender() == Gender.MALE) {
            dye.setDurability((short) 10);
            dyeMeta.setDisplayName(ChatColor.DARK_GREEN + "Male");
        } else {
            dye.setDurability((short) 9);
            dyeMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Female");
        }

        dye.setItemMeta(dyeMeta);
        inv.setItem(14, dye);

        ItemStack book = new ItemStack(Material.BOOK);
        ItemMeta meta = book.getItemMeta();

        meta.setDisplayName(ChatColor.GREEN + "Criminal History");

        book.setItemMeta(meta);

        inv.setItem(26, book);

        /*
            10 = head
            12 = name
            13 = age
            14 = gender
            26 = criminal history
         */

        player.openInventory(inv);
    }

    public void denyRequest(Player player) {
        UUID toAccept = getReceivedIDRequestUUID(player);
        Player target = Bukkit.getServer().getPlayer(toAccept);

        idRequests.remove(toAccept);
        mcd.message("checkRequestDenied", player);
        if (target != null)
            mcd.message("checkRequestDenied", target);
    }
}
