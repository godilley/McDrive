package com.dachiimp.requests.mcdrive.Commands;

import com.dachiimp.requests.mcdrive.McDrive;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by DaChiimp on 9/8/2016. For Major
 */
public class onCommandExecutorClass implements CommandExecutor {

    public String perm_police = "mcd.ispolice";
    public String perm_admin = "mcd.isadmin";
    public String perm_check = "mcd.check";
    private McDrive mcd;

    public onCommandExecutorClass(McDrive mcd) {
        this.mcd = mcd;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (mcd.setupCommands.getRawCommands().contains(cmd.getName().toLowerCase())) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (cmd.getName().equalsIgnoreCase("police")) {
                    mcd.reportCommands.execute(cmd, args, player);
                } else if (cmd.getName().equalsIgnoreCase("pc")) {
                    if (player.hasPermission(perm_police)) {
                        if (args.length > 0) {
                            String message = StringUtils.join(args, " ");
                            message = mcd.getMessage("policeChat").replaceAll("%message%", message).replaceAll("%playerName%", player.getName()).replaceAll("%playerDisplay%", player.getDisplayName());
                            for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                                if (p.hasPermission(perm_police)) {
                                    p.sendMessage(message);
                                }
                            }
                        } else {
                            wrongArguments(player);
                        }
                    } else {
                        noPerm(player);
                    }
                } else if (cmd.getName().equalsIgnoreCase("pr")) {
                    mcd.reportCommands.execute(cmd,args,player);
                } else if (cmd.getName().equalsIgnoreCase("id")) {
                    mcd.idCommand.execute(cmd, args, player);
                } else if (cmd.getName().equalsIgnoreCase("mcd")) {
                    if (args.length == 1) {
                        if (args[0].equalsIgnoreCase("reload")) {
                            if (player.hasPermission(perm_admin)) {
                                mcd.setupCrimes();
                                mcd.setupMessages.setupStrings();
                                player.sendMessage(ChatColor.GREEN + "Config.yml reloaded");
                                player.sendMessage(ChatColor.GREEN + "Messages.yml reloaded");
                                boolean result1 = mcd.playerIDSave.savePlayerIDs();
                                if(result1) {
                                    player.sendMessage(ChatColor.GREEN + "PlayerIDs.yml saved");
                                } else {
                                    player.sendMessage(ChatColor.GREEN + "Error saving PlayerIDs.yml");
                                }

                                boolean result2 = mcd.policeReportSave.savePoliceReports();
                                if(result2) {
                                    player.sendMessage(ChatColor.GREEN + "PoliceReports.yml saved");
                                } else {
                                    player.sendMessage(ChatColor.GREEN + "Error saving PoliceReports.yml");
                                }
                            } else {
                                noPerm(player);
                            }
                        } else if(args[0].equalsIgnoreCase("help")) {
                            String header = mcd.getMessage("helpHeader");
                            header = header.replaceAll("%prefix%", mcd.prefix);
                            header = header.replaceAll("(&([a-f0-9k-or]))", "\u00A7$2");
                            player.sendMessage(header);

                            String pmessage = mcd.getMessage("help");
                            pmessage = pmessage.replaceAll("%prefix%", mcd.prefix);
                            pmessage = pmessage.replaceAll("%command%", "INFO");
                            pmessage = pmessage.replaceAll("%desc%", "<> = Required | [] = Optional");
                            pmessage = pmessage.replaceAll("(&([a-f0-9k-or]))", "\u00A7$2");
                            player.sendMessage(pmessage);
                            
                            

                            for (String s : mcd.setupCommands.getCommands().keySet()) {

                                String desc = mcd.setupCommands.getCommands().get(s);

                                //

                                String message = mcd.getMessage("help");

                                message = message.replaceAll("%prefix%", mcd.prefix);

                                message = message.replaceAll("%command%", s);

                                message = message.replaceAll("%desc%", desc);

                                message = message.replaceAll("(&([a-f0-9k-or]))", "\u00A7$2");

                                player.sendMessage(message);

                            }

                            String footer = mcd.getMessage("helpFooter");
                            footer = footer.replaceAll("%prefix%", mcd.prefix);
                            footer = footer.replaceAll("(&([a-f0-9k-or]))", "\u00A7$2");
                            player.sendMessage(footer);
                        } else {
                            unknownCommand(player);
                        }
                    } else {
                        wrongArguments(player);
                    }
                }
            } else {
                sender.sendMessage("You must be a player to execute these commands!");
            }
            return true;
        }
        return false;
    }


    void unknownCommand(Player player) {
        mcd.message("unknownCommand", player);
    }

    void wrongArguments(Player player) {
        mcd.message("wrongArguments", player);
    }

    void noPerm(Player player) {
        mcd.message("noPerm", player);
    }

}
