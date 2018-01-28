package com.dachiimp.requests.mcdrive.Util.Methods;

import com.dachiimp.requests.mcdrive.McDrive;
import com.dachiimp.requests.mcdrive.Util.ClassSaving.PlayerID;
import com.dachiimp.requests.mcdrive.Util.ClassSaving.PoliceReport;
import com.dachiimp.requests.mcdrive.Util.Gender;
import com.dachiimp.requests.mcdrive.Util.ReportStatus;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.util.StringUtil;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by DaChiimp on 9/8/2016. For Majors
 */
public class ReportMethods {

    public String startReportTitle = "Start Crime Report | ";
    public String suspectDescTitle = ChatColor.DARK_BLUE + "Suspect Description";
    public String reportsTitle = "Open Reports | ";
    public String reportTitle = "Crime Report #";

    public String criminalHistory = "Criminal History | ";

    private McDrive mcd;
    public ReportMethods(McDrive mcd) {
        this.mcd = mcd;
    }

    public void openStartReportGUI(Player player, int page) {
        player.closeInventory();
        Inventory inv = Bukkit.createInventory(null, 54, ChatColor.DARK_BLUE + startReportTitle + page);

        int max = 44 * page;
        int start = 0;

        if (page > 1)
            start = 46 * (page - 1);

        int i = -1;

        /*mcd.logger.log("mcd.reportNames = " + mcd.reportNames.toString());*/

        int position = 0;

        for (String report : mcd.reportNames) {
            i++;
            if (i >= start && i <= max) {
                ItemStack book = new ItemStack(Material.BOOK);
                ItemMeta meta = book.getItemMeta();
                meta.setDisplayName(ChatColor.GREEN + report);
                List<String> lores = Arrays.asList(ChatColor.GRAY + "" + ChatColor.ITALIC + "Click me to start a report", ChatColor.GRAY + "" + ChatColor.ITALIC + "for the crime of " + report);
                meta.setLore(lores);
                book.setItemMeta(meta);
                inv.setItem(position, book);
                position++;
            }
        }

        ItemStack next = new ItemStack(Material.ARROW);
        ItemMeta nextMeta = next.getItemMeta();
        nextMeta.setDisplayName(ChatColor.GREEN + "Next Page");
        next.setItemMeta(nextMeta);
        inv.setItem(53, next);

        if (page > 1) {
            ItemStack previous = new ItemStack(Material.LEASH);
            ItemMeta previousItemMeta = previous.getItemMeta();
            previousItemMeta.setDisplayName(ChatColor.GREEN + "Previous Page");
            previous.setItemMeta(previousItemMeta);
            inv.setItem(45, previous);
        }

        player.openInventory(inv);

    }

    public void openSuspectDescReport(Player player, String crime) {
        player.closeInventory();
        /*
            setup crime info
         */

        crime = ChatColor.stripColor(crime);
        Inventory inv = Bukkit.createInventory(null, 54, suspectDescTitle);
        ItemStack book = new ItemStack(Material.BOOK);
        ItemMeta bookMeta = book.getItemMeta();
        bookMeta.setDisplayName(ChatColor.GREEN + crime);
        book.setItemMeta(bookMeta);
        inv.setItem(4, book);


        /*
            Gender
         */

        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
        SkullMeta skullMeta = (SkullMeta) head.getItemMeta();
        skullMeta.setOwner("Steve");
        skullMeta.setDisplayName(ChatColor.GREEN + "Male");

        head.setItemMeta(skullMeta);

        inv.setItem(13, head);

        /*
            Armor
         */

        ItemStack item = new ItemStack(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setColor(Color.BLUE);
        meta.setDisplayName(ChatColor.GREEN + "Blue Shirt");
        item.setItemMeta(meta);
        inv.setItem(22, item);

        for (int i = 0; i < 2; i++) {
            switch (i) {
                case 0: {
                    item.setType(Material.LEATHER_LEGGINGS);
                    meta.setColor(Color.BLUE);
                    meta.setDisplayName(ChatColor.GREEN + "Blue Pants");
                    item.setItemMeta(meta);
                    inv.setItem(31, item);
                    break;
                }
                case 1: {
                    item.setType(Material.LEATHER_BOOTS);
                    meta.setColor(Color.BLUE);
                    meta.setDisplayName(ChatColor.GREEN + "Blue Shoes");
                    item.setItemMeta(meta);
                    inv.setItem(40, item);
                    break;
                }
            }
        }

        ItemStack back = new ItemStack(Material.ARROW);
        ItemMeta backMeta = back.getItemMeta();
        backMeta.setDisplayName(ChatColor.GREEN + "Back");
        back.setItemMeta(backMeta);
        inv.setItem(45,back);

        ItemStack confirm = new ItemStack(Material.SLIME_BALL);
        ItemMeta confirmMeta = confirm.getItemMeta();
        confirmMeta.setDisplayName(ChatColor.GREEN + "Confirm / Report Crime");
        confirm.setItemMeta(confirmMeta);
        inv.setItem(53,confirm);

        ItemStack info = new ItemStack(Material.PAPER);
        ItemMeta infoMeta = info.getItemMeta();
        infoMeta.setDisplayName(ChatColor.RED + "INFO:");
        List<String> lores = Arrays.asList(ChatColor.GRAY + "Left Click: Next color",ChatColor.GRAY + "Right Click: Previous Color");
        infoMeta.setLore(lores);
        info.setItemMeta(infoMeta);
        inv.setItem(49,info);

        player.openInventory(inv);

    }

    public Color getNextColor(Color currentColor) {
        if (currentColor.equals(Color.AQUA)) {
            return Color.BLACK;
        } else if (currentColor.equals(Color.BLACK)) {
            return Color.BLUE;
        } else if (currentColor.equals(Color.BLUE)) {
            return Color.FUCHSIA;
        } else if (currentColor.equals(Color.FUCHSIA)) {
            return Color.GRAY;
        } else if (currentColor.equals(Color.GRAY)) {
            return Color.GREEN;
        } else if (currentColor.equals(Color.GREEN)) {
            return Color.LIME;
        } else if (currentColor.equals(Color.LIME)) {
            return Color.MAROON;
        } else if (currentColor.equals(Color.MAROON)) {
            return Color.NAVY;
        } else if (currentColor.equals(Color.NAVY)) {
            return Color.OLIVE;
        } else if (currentColor.equals(Color.OLIVE)) {
            return Color.ORANGE;
        } else if (currentColor.equals(Color.ORANGE)) {
            return Color.PURPLE;
        } else if (currentColor.equals(Color.PURPLE)) {
            return Color.RED;
        } else if (currentColor.equals(Color.RED)) {
            return Color.SILVER;
        } else if (currentColor.equals(Color.SILVER)) {
            return Color.TEAL;
        } else if (currentColor.equals(Color.TEAL)) {
            return Color.WHITE;
        } else if (currentColor.equals(Color.WHITE)) {
            return Color.YELLOW;
        } else if (currentColor.equals(Color.YELLOW)) {
            return Color.AQUA;
        } else {
            return Color.BLUE;
        }
    }

    public Color getPreviousColor(Color currentColor) {
        if (currentColor.equals(Color.AQUA)) {
            return Color.YELLOW;
        } else if (currentColor.equals(Color.BLACK)) {
            return Color.AQUA;
        } else if (currentColor.equals(Color.BLUE)) {
            return Color.BLACK;
        } else if (currentColor.equals(Color.FUCHSIA)) {
            return Color.BLUE;
        } else if (currentColor.equals(Color.GRAY)) {
            return Color.FUCHSIA;
        } else if (currentColor.equals(Color.GREEN)) {
            return Color.GRAY;
        } else if (currentColor.equals(Color.LIME)) {
            return Color.GREEN;
        } else if (currentColor.equals(Color.MAROON)) {
            return Color.LIME;
        } else if (currentColor.equals(Color.NAVY)) {
            return Color.MAROON;
        } else if (currentColor.equals(Color.OLIVE)) {
            return Color.NAVY;
        } else if (currentColor.equals(Color.ORANGE)) {
            return Color.OLIVE;
        } else if (currentColor.equals(Color.PURPLE)) {
            return Color.ORANGE;
        } else if (currentColor.equals(Color.RED)) {
            return Color.PURPLE;
        } else if (currentColor.equals(Color.SILVER)) {
            return Color.RED;
        } else if (currentColor.equals(Color.TEAL)) {
            return Color.SILVER;
        } else if (currentColor.equals(Color.WHITE)) {
            return Color.TEAL;
        } else if (currentColor.equals(Color.YELLOW)) {
            return Color.WHITE;
        } else {
            return Color.BLUE;
        }
    }

    public int createReportID() {
        return mcd.policeReports.size();
    }

    public String getColorAsName(Color currentColor) {
        String color;
        if (currentColor.equals(Color.AQUA)) {
            color = "AQUA";
        } else if (currentColor.equals(Color.BLACK)) {
            color = "BLACK";
        } else if (currentColor.equals(Color.BLUE)) {
            color = "BLUE";
        } else if (currentColor.equals(Color.FUCHSIA)) {
            color = "FUCHSIA";
        } else if (currentColor.equals(Color.GRAY)) {
            color = "GRAY";
        } else if (currentColor.equals(Color.GREEN)) {
            color = "GREEN";
        } else if (currentColor.equals(Color.LIME)) {
            color = "LIME";
        } else if (currentColor.equals(Color.MAROON)) {
            color = "MAROON";
        } else if (currentColor.equals(Color.NAVY)) {
            color = "NAVY";
        } else if (currentColor.equals(Color.OLIVE)) {
            color = "OLIVE";
        } else if (currentColor.equals(Color.ORANGE)) {
            color = "ORANGE";
        } else if (currentColor.equals(Color.PURPLE)) {
            color = "PURPLE";
        } else if (currentColor.equals(Color.RED)) {
            color = "RED";
        } else if (currentColor.equals(Color.SILVER)) {
            color = "SILVER";
        } else if (currentColor.equals(Color.TEAL)) {
            color = "TEAL";
        } else if (currentColor.equals(Color.WHITE)) {
            color = "WHITE";
        } else if (currentColor.equals(Color.YELLOW)) {
            color = "YELLOW";
        } else {
            color = "UNKNOWN";
        }

        color = StringUtils.capitalize(color.toLowerCase());

        return color;
    }

    public void fileCrimeReport(Player player, String crime, Gender gender, Color cshirtColor, Color cpantsColor, Color cbootsColor) {
        player.closeInventory();
        crime = ChatColor.stripColor(crime);

        String shirtColor = getColorAsName(cshirtColor);
        String pantsColor = getColorAsName(cpantsColor);
        String bootsColor = getColorAsName(cbootsColor);



        String pX = "" + Math.round(player.getLocation().getX());
        String pZ = "" + Math.round(player.getLocation().getZ());


        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yy @ HH:mm");
        String date = format.format(now);

        String name = player.getName();

        mcd.policeReports.put(createReportID(),new PoliceReport(crime,name,pX,pZ,gender,date,cshirtColor,cpantsColor,cbootsColor, ReportStatus.OPEN));

        String message = mcd.getMessage("crimeReport");
        message = message.replaceAll("%crime%",crime);
        message = message.replaceAll("%x%",pX).replaceAll("%z%",pZ);
        message = message.replaceAll("%shirtColor%",shirtColor);
        message = message.replaceAll("%pantsColor%",pantsColor);
        message = message.replaceAll("%bootsColor%",bootsColor);

        for(Player p : Bukkit.getServer().getOnlinePlayers()) {
            if(p.hasPermission(mcd.cmdE.perm_police)) {
                // bc report
                p.sendMessage(message);
            }
        }

        mcd.message("reportFiled",player);
    }

    public void openReportsGUI(Player player, int page) {
        player.closeInventory();

        Inventory inv = Bukkit.createInventory(null, 54, ChatColor.DARK_BLUE + reportsTitle + page);

        HashMap<Integer,PoliceReport> openReports = new HashMap<>();

        for(int id : mcd.policeReports.keySet()) {
            PoliceReport report = mcd.policeReports.get(id);
            if(report.getReportStatus() == ReportStatus.OPEN) {
                openReports.put(id,report);
            }
        }

        int max = 44 * page;
        int start = 0;

        if (page > 1)
            start = 46 * (page - 1);

        int i = -1;

        /*mcd.logger.log("mcd.reportNames = " + mcd.reportNames.toString());*/

        int position = 0;

        for (Integer id : openReports.keySet()) {
            i++;
            if (i >= start && i <= max) {
                PoliceReport report = mcd.policeReports.get(id);
                ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
                SkullMeta meta = (SkullMeta) head.getItemMeta();
                meta.setOwner(report.getReporter());
                meta.setDisplayName(ChatColor.GREEN + "#" + id);
                List<String> lores = Arrays.asList(ChatColor.GREEN + "Crime: " + ChatColor.DARK_GRAY + report.getCrime(),ChatColor.GREEN + "Date: " + ChatColor.DARK_GRAY + report.getDate(),ChatColor.GREEN + "Reporter: " + ChatColor.DARK_GRAY + report.getReporter());
                meta.setLore(lores);
                head.setItemMeta(meta);
                inv.setItem(position, head);
                position++;
            }
        }

        ItemStack next = new ItemStack(Material.ARROW);
        ItemMeta nextMeta = next.getItemMeta();
        nextMeta.setDisplayName(ChatColor.GREEN + "Next Page");
        next.setItemMeta(nextMeta);
        inv.setItem(53, next);

        if (page > 1) {
            ItemStack previous = new ItemStack(Material.LEASH);
            ItemMeta previousItemMeta = previous.getItemMeta();
            previousItemMeta.setDisplayName(ChatColor.GREEN + "Previous Page");
            previous.setItemMeta(previousItemMeta);
            inv.setItem(45, previous);
        }

        player.openInventory(inv);
    }

    public void openReport(Player player, int id, boolean readOnly) {
        if(!mcd.policeReports.containsKey(id)) return;

        PoliceReport report = mcd.policeReports.get(id);

        player.closeInventory();

        Inventory inv = Bukkit.createInventory(null,54,ChatColor.DARK_BLUE + reportTitle + id);

        inv.setItem(1,createItemStack(Material.SIGN,ChatColor.GREEN + "Suspect"));
        
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
        SkullMeta skullMeta = (SkullMeta) head.getItemMeta();
        skullMeta.setOwner(report.getGender().getUserName());
        skullMeta.setDisplayName(ChatColor.GREEN + "Gender");
        head.setItemMeta(skullMeta);
        inv.setItem(10,head);
        
        ItemStack shirt = createItemStack(Material.LEATHER_CHESTPLATE,ChatColor.GREEN + getColorAsName(report.getShirtColor()) + " Shirt");
        LeatherArmorMeta shirtMeta = (LeatherArmorMeta) shirt.getItemMeta();
        shirtMeta.setColor(report.getShirtColor());
        shirt.setItemMeta(shirtMeta);
        inv.setItem(19,shirt);

        ItemStack pants = createItemStack(Material.LEATHER_LEGGINGS,ChatColor.GREEN + getColorAsName(report.getPantsColor()) + " Pants");
        LeatherArmorMeta pantsMeta = (LeatherArmorMeta) pants.getItemMeta();
        pantsMeta.setColor(report.getPantsColor());
        pants.setItemMeta(pantsMeta);
        inv.setItem(28,pants);

        ItemStack boots = createItemStack(Material.LEATHER_BOOTS,ChatColor.GREEN + getColorAsName(report.getPantsColor()) + " Shoes");
        LeatherArmorMeta bootsMeta = (LeatherArmorMeta) boots.getItemMeta();
        bootsMeta.setColor(report.getBootsColor());
        boots.setItemMeta(bootsMeta);
        inv.setItem(37,boots);

        inv.setItem(12,createItemStack(Material.SIGN,ChatColor.GREEN + "Crime"));
        inv.setItem(21,createItemStack(Material.BOOK,ChatColor.WHITE + report.getCrime()));
        inv.setItem(14,createItemStack(Material.SIGN,ChatColor.GREEN + "Reported On"));
        inv.setItem(23,createItemStack(Material.WATCH,ChatColor.WHITE + report.getDate()));
        inv.setItem(16,createItemStack(Material.SIGN,ChatColor.GREEN + "Location"));
        inv.setItem(25,createItemStack(Material.COMPASS,ChatColor.WHITE + "x: " + report.getReporterX() + " z: " + report.getReporterZ()));

        if(!readOnly)
            inv.setItem(53,createItemStack(Material.BARRIER,ChatColor.RED + "Close Report"));


        player.openInventory(inv);
    }

    private ItemStack createItemStack(Material material, String displayName) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(displayName);
        item.setItemMeta(meta);

        return item;
    }

    public PoliceReport getReport(int id) {
        if(mcd.policeReports.containsKey(id)) {
            return mcd.policeReports.get(id);
        }

        return null;
    }

    public void openCrimesFor(Player player, Player target, int page) {
        player.closeInventory();

        Inventory inv = Bukkit.createInventory(null, 54, ChatColor.DARK_BLUE + criminalHistory + page);

        PlayerID playerID = mcd.playerIDMethods.getPlayerID(target.getUniqueId());

        int max = 44 * page;
        int start = 0;

        if (page > 1)
            start = 46 * (page - 1);

        int i = -1;

        /*mcd.logger.log("mcd.reportNames = " + mcd.reportNames.toString());*/

        int position = 0;

        for(int id : playerID.getCrimes()) {
            i++;
            if (i >= start && i <= max) {
                PoliceReport report = mcd.policeReports.get(id);
                ItemStack head = new ItemStack(Material.BOOK);
                ItemMeta meta = head.getItemMeta();
                meta.setDisplayName(ChatColor.GREEN + "#" + id);
                List<String> lores = Arrays.asList(ChatColor.GREEN + "Crime: " + ChatColor.DARK_GRAY + report.getCrime(),ChatColor.GREEN + "Date: " + ChatColor.DARK_GRAY + report.getDate(),ChatColor.GREEN + "Reporter: " + ChatColor.DARK_GRAY + report.getReporter());
                meta.setLore(lores);
                head.setItemMeta(meta);
                inv.setItem(position, head);
                position++;
            }
        }

        ItemStack next = new ItemStack(Material.ARROW);
        ItemMeta nextMeta = next.getItemMeta();
        nextMeta.setDisplayName(ChatColor.GREEN + "Next Page");
        next.setItemMeta(nextMeta);
        inv.setItem(53, next);

        if (page > 1) {
            ItemStack previous = new ItemStack(Material.LEASH);
            ItemMeta previousItemMeta = previous.getItemMeta();
            previousItemMeta.setDisplayName(ChatColor.GREEN + "Previous Page");
            previous.setItemMeta(previousItemMeta);
            inv.setItem(45, previous);
        }


        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
        SkullMeta skullMeta = (SkullMeta) head.getItemMeta();
        skullMeta.setOwner(target.getName());
        skullMeta.setDisplayName(ChatColor.GREEN + "" + target.getUniqueId());
        List<String> lores = Arrays.asList(ChatColor.GREEN + "Name: " + ChatColor.DARK_GRAY + target.getName());
        skullMeta.setLore(lores);
        head.setItemMeta(skullMeta);
        inv.setItem(49,head);

        player.openInventory(inv);


    }
}
