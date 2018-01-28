package com.dachiimp.requests.mcdrive.Listeners;

import com.dachiimp.requests.mcdrive.McDrive;
import com.dachiimp.requests.mcdrive.Util.ClassSaving.PoliceReport;
import com.dachiimp.requests.mcdrive.Util.Gender;
import com.dachiimp.requests.mcdrive.Util.ReportStatus;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by DaChiimp on 09/09/2016. For Majors
 */
public class InventoryClickEventListener implements Listener {

    private McDrive mcd;

    public InventoryClickEventListener(McDrive mcd) {
        this.mcd = mcd;
    }

    @EventHandler(ignoreCancelled = true)
    void onInvClick(InventoryClickEvent e) {

        if (!(e.getWhoClicked() instanceof Player)) return;

        ItemStack curItem = e.getCurrentItem();

        if (curItem == null || curItem.getType() == Material.AIR) return;

        Player player = (Player) e.getWhoClicked();

        if (e.getClickedInventory().getTitle().startsWith(mcd.playerIDMethods.idTitle)) {
            e.setCancelled(true);
            if (curItem.getType() == Material.BOOK) {
                // open previous crimes
                ItemStack head = e.getClickedInventory().getItem(10);
                SkullMeta skullMeta = (SkullMeta) head.getItemMeta();
                Player target = Bukkit.getServer().getPlayer(skullMeta.getOwner());
                if(target == null) {
                    mcd.message("unknownPlayer",player);
                } else {
                    mcd.reportMethods.openCrimesFor(player, target, 1);
                }
            }
        } else if (ChatColor.stripColor(e.getClickedInventory().getTitle()).startsWith(mcd.reportMethods.startReportTitle)) {
            e.setCancelled(true);
            String title = ChatColor.stripColor(e.getClickedInventory().getTitle());
            title = title.replaceAll(mcd.reportMethods.startReportTitle, "").replaceAll(" ", "").replaceAll("\\|", "");
            if (curItem.getType() == Material.ARROW || curItem.getType() == Material.LEASH) {
                int page = -1;
                try {
                    page = Integer.parseInt(title);
                } catch (NumberFormatException e1) {
                    mcd.logger.warn("Error getting current page. Title = " + title);
                    player.sendMessage("Erorr getting current page");
                    return;
                }
                if (curItem.getType() == Material.ARROW) {
                    // next page
                    page++;
                } else if (curItem.getType() == Material.LEASH) {
                    // prev
                    page--;
                }

                if (page < 1) {
                    player.sendMessage("Erorr getting current page");
                } else {
                    mcd.reportMethods.openStartReportGUI(player, page);
                }
            } else if (curItem.getType() == Material.BOOK) {
                // start report
                mcd.reportMethods.openSuspectDescReport(player, curItem.getItemMeta().getDisplayName());
            }
        } else if (e.getClickedInventory().getTitle().equalsIgnoreCase(mcd.reportMethods.suspectDescTitle)) {
            e.setCancelled(true);
            if (curItem.getType() == Material.LEATHER_CHESTPLATE || curItem.getType() == Material.LEATHER_LEGGINGS || curItem.getType() == Material.LEATHER_BOOTS) {
                if (e.getClick() == ClickType.LEFT || e.getClick() == ClickType.RIGHT) {
                    LeatherArmorMeta meta = (LeatherArmorMeta) curItem.getItemMeta();
                    Color color = mcd.reportMethods.getNextColor(meta.getColor());
                    if (e.getClick() == ClickType.RIGHT) {
                        color = mcd.reportMethods.getPreviousColor(meta.getColor());
                    }
                    meta.setColor(color);
                    String name = ChatColor.GREEN + mcd.reportMethods.getColorAsName(color);
                    if(curItem.getType() == Material.LEATHER_LEGGINGS) {
                        name = name + " Pants";
                    } else if(curItem.getType() == Material.LEATHER_CHESTPLATE) {
                        name = name + " Shirt";
                    } else {
                        name = name + " Shoes";
                    }
                    meta.setDisplayName(name);
                    curItem.setItemMeta(meta);

                    player.updateInventory();
                }
            } else if (curItem.getType() == Material.SKULL_ITEM) {
                SkullMeta meta = (SkullMeta) curItem.getItemMeta();
                if (meta.getOwner().equalsIgnoreCase("Steve")) {
                    meta.setOwner("MHF_Alex");
                    meta.setDisplayName(org.bukkit.ChatColor.LIGHT_PURPLE + "Female");
                    curItem.setItemMeta(meta);
                } else {
                    meta.setOwner("Steve");
                    meta.setDisplayName(org.bukkit.ChatColor.GREEN + "Male");
                    curItem.setItemMeta(meta);
                }

                player.updateInventory();
            } else if(curItem.getType() == Material.ARROW) {
                mcd.reportMethods.openStartReportGUI(player,1);
            } else if(curItem.getType() == Material.SLIME_BALL) {
                Inventory inv = e.getClickedInventory();
                String crime = inv.getItem(4).getItemMeta().getDisplayName();
                Color shirtColor = null;
                Color pantsColor = null;
                Color bootsColor = null;
                int[] places = {22,31,40};
                for(int i : places) {
                    ItemStack shirt = inv.getItem(i);
                    switch (i) {
                        case 22: {
                            shirtColor = ((LeatherArmorMeta) shirt.getItemMeta()).getColor();
                            break;
                        }
                        case 31: {
                            pantsColor = ((LeatherArmorMeta) shirt.getItemMeta()).getColor();
                            break;
                        }
                        case 40: {
                            bootsColor = ((LeatherArmorMeta) shirt.getItemMeta()).getColor();
                            break;
                        }
                    }
                }

                Gender gender = Gender.MALE;

                ItemStack head = inv.getItem(13);
                SkullMeta skull = (SkullMeta) head.getItemMeta();

                if(skull.getOwner().equalsIgnoreCase("MHF_Alex")) {
                    gender = Gender.FEMALE;
                }

                if(shirtColor == null || pantsColor == null || bootsColor == null) {
                    mcd.message("error",player,"%error%","Filing a report");
                } else {
                    mcd.reportMethods.fileCrimeReport(player, crime, gender, shirtColor, pantsColor, bootsColor);
                }
            }

        } else if(ChatColor.stripColor(e.getClickedInventory().getTitle()).startsWith(mcd.reportMethods.reportsTitle)) {
            e.setCancelled(true);
            String title = ChatColor.stripColor(e.getClickedInventory().getTitle());
            title = title.replaceAll(mcd.reportMethods.reportsTitle, "").replaceAll(" ", "").replaceAll("\\|", "");
            if (curItem.getType() == Material.ARROW || curItem.getType() == Material.LEASH) {
                int page = -1;
                try {
                    page = Integer.parseInt(title);
                } catch (NumberFormatException e1) {
                    mcd.logger.warn("Error getting current page. Title = " + title);
                    player.sendMessage("Erorr getting current page");
                    return;
                }
                if (curItem.getType() == Material.ARROW) {
                    // next page
                    page++;
                } else if (curItem.getType() == Material.LEASH) {
                    // prev
                    page--;
                }

                if (page < 1) {
                    player.sendMessage("Error getting current page");
                } else {
                    mcd.reportMethods.openReportsGUI(player,page);
                }
            } else if(curItem.getType() == Material.SKULL_ITEM) {
                String t = ChatColor.stripColor(curItem.getItemMeta().getDisplayName()).replaceAll("\\#","");
                try{
                    int id = Integer.parseInt(t);
                    mcd.reportMethods.openReport(player,id,false);
                } catch(NumberFormatException e1) {
                    player.sendMessage("Error getting report ID");
                }
            }
        } else if(ChatColor.stripColor(e.getClickedInventory().getTitle()).startsWith(mcd.reportMethods.reportTitle)) {
            e.setCancelled(true);
            if(curItem.getType() == Material.BARRIER) {
                // close report
                String sid = ChatColor.stripColor(e.getClickedInventory().getTitle());
                sid = sid.replaceAll(mcd.reportMethods.reportTitle,"");
                try{
                    int id = Integer.parseInt(sid);
                    PoliceReport report = mcd.reportMethods.getReport(id);
                    if(report != null) {
                        report.setReportStatus(ReportStatus.CLOSED);
                        mcd.message("reportClosed",player);
                    } else {
                        player.sendMessage("Error getting report ID");
                    }
                } catch(NumberFormatException e1) {
                    player.sendMessage("Error getting report ID - id = " + sid);
                }

                player.closeInventory();
            }
        } else if(ChatColor.stripColor(e.getClickedInventory().getTitle()).startsWith(mcd.reportMethods.criminalHistory)) {
            e.setCancelled(true);
            if(curItem.getType() == Material.BOOK) {
                String t = ChatColor.stripColor(curItem.getItemMeta().getDisplayName()).replaceAll("\\#","");
                try{
                    int id = Integer.parseInt(t);
                    mcd.reportMethods.openReport(player,id,true);
                } catch(NumberFormatException e1) {
                    player.sendMessage("Error getting report ID");
                }
            } else if (curItem.getType() == Material.ARROW || curItem.getType() == Material.LEASH) {
                ItemStack head = e.getClickedInventory().getItem(49);
                SkullMeta skullMeta = (SkullMeta) head.getItemMeta();
                Player target = Bukkit.getServer().getPlayer(skullMeta.getOwner());
                if(target == null) {
                    mcd.message("unknownPlayer",player);
                    return;
                }
                String title = ChatColor.stripColor(e.getClickedInventory().getTitle());
                title = title.replaceAll(mcd.reportMethods.criminalHistory, "").replaceAll(" ", "").replaceAll("\\|", "");
                int page = -1;
                try {
                    page = Integer.parseInt(title);
                } catch (NumberFormatException e1) {
                    mcd.logger.warn("Error getting current page. Title = " + title);
                    player.sendMessage("Erorr getting current page");
                    return;
                }
                if (curItem.getType() == Material.ARROW) {
                    // next page
                    page++;
                } else if (curItem.getType() == Material.LEASH) {
                    // prev
                    page--;
                }

                if (page < 1) {
                    player.sendMessage("Error getting current page");
                } else {
                    mcd.reportMethods.openCrimesFor(player, target, page);
                }
            }
        }
    }
}
