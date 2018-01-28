package com.dachiimp.requests.mcdrive.Commands;

import com.dachiimp.requests.mcdrive.McDrive;
import com.dachiimp.requests.mcdrive.Util.ClassSaving.PlayerID;
import com.dachiimp.requests.mcdrive.Util.ClassSaving.PoliceReport;
import com.dachiimp.requests.mcdrive.Util.ReportStatus;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created by DaChiimp on 9/2/2016. For RQ
 */
public class IDCommand {

    private McDrive mcd;
    private onCommandExecutorClass main;

    public IDCommand(McDrive mcd, onCommandExecutorClass main) {
        this.mcd = mcd;
        this.main = main;
    }

    void execute(Command cmd, String[] args, Player player) {
        if (args.length > 0) {
            String subCmd = args[0];
            if (subCmd.equalsIgnoreCase("set")) {
                if (args.length >= 3) {
                    String toSet = args[1];
                    String value = args[2];
                    if (toSet.equalsIgnoreCase("name")) {
                        if (args.length == 4) {
                            value = value + " " + args[3];
                            boolean result = mcd.playerIDMethods.modifyID(player.getUniqueId(), "name", value);
                            if (result) {
                                mcd.message("modifiedID", player);
                            } else {
                                mcd.message("errorModifyingID", player);
                            }
                        } else {
                            String message = mcd.getMessage("incorrectFormat").replaceAll("%format%", "/id name FirstName LastName");
                            player.sendMessage(message);
                        }
                    } else if (toSet.equalsIgnoreCase("age")) {
                        // handle age
                        try {
                            int age = Integer.parseInt(value);
                            if (age > 0) {
                                boolean result = mcd.playerIDMethods.modifyID(player.getUniqueId(), "age", value);
                                if (result) {
                                    mcd.message("modifiedID", player);
                                } else {
                                    mcd.message("errorModifyingID", player);
                                }
                            } else {
                                String message = mcd.getMessage("notAnInt").replaceAll("%int%", value);
                                player.sendMessage(message);
                            }
                        } catch (NumberFormatException e) {
                            String message = mcd.getMessage("notAnInt").replaceAll("%int%", value);
                            player.sendMessage(message);
                        }
                    } else if (toSet.equalsIgnoreCase("gender")) {
                        // handle gender
                        boolean result = mcd.playerIDMethods.modifyID(player.getUniqueId(), "gender", value);
                        if (result) {
                            mcd.message("modifiedID", player);
                        } else {
                            mcd.message("errorModifyingID", player);
                        }
                    } else {
                        main.unknownCommand(player);
                    }
                } else {
                    main.wrongArguments(player);
                }
            } else if (subCmd.equalsIgnoreCase("see")) {
                if (args.length == 2) {
                    if (player.hasPermission(main.perm_police)) {
                        if (mcd.playerIDMethods.hasSentIDRequest(player)) {
                            mcd.message("alreadyHaveRequest", player);
                            return;
                        }
                        Player target = Bukkit.getServer().getPlayer(args[1]);
                        if (target != null) {
                            if (target == player) {
                                mcd.message("playerCantBeSelf", player);
                                return;
                            }
                            UUID uuid = target.getUniqueId();
                            if (mcd.playerIDMethods.hasID(uuid)) {
                                boolean result = mcd.playerIDMethods.sendRequest(player, target);
                                if (result) {
                                    mcd.message("requestSent", player, "%player%", target.getName());
                                } else {
                                    mcd.message("requestFailed", player);
                                }
                            } else {
                                mcd.message("targetDoesntHaveID", player);
                            }
                        } else {
                            String message = mcd.getMessage("unknownPlayer").replaceAll("%player%", args[1]);
                            player.sendMessage(message);
                        }
                    } else {
                        main.noPerm(player);
                    }
                } else {
                    main.wrongArguments(player);
                }
            } else if (subCmd.equalsIgnoreCase("accept") || subCmd.equalsIgnoreCase("deny")) {
                if (mcd.playerIDMethods.hasReceivedIDRequest(player)) {
                    // handle
                    boolean accept = subCmd.equalsIgnoreCase("accept");
                    if (accept) {
                        mcd.playerIDMethods.acceptRequest(player);
                    } else {
                        mcd.playerIDMethods.denyRequest(player);
                    }
                } else {
                    mcd.message("notReceivedRequest", player);
                }
            } else if (subCmd.equalsIgnoreCase("forcesee")) {
                if (player.hasPermission(main.perm_admin)) {
                    if (args.length == 2) {
                        Player target = Bukkit.getServer().getPlayer(args[1]);
                        if (target != null) {
                            UUID uuid = target.getUniqueId();
                            if (mcd.playerIDMethods.hasID(uuid)) {
                                mcd.playerIDMethods.openIDGUI(player, target);
                            } else {
                                mcd.message("targetDoesntHaveID", player);
                            }
                        } else {
                            String message = mcd.getMessage("unknownPlayer").replaceAll("%player%", args[1]);
                            player.sendMessage(message);
                        }
                    } else {
                        main.wrongArguments(player);
                    }
                } else {
                    main.noPerm(player);
                }
            } else if (subCmd.equalsIgnoreCase("check")) {
                if (player.hasPermission(main.perm_check)) {
                    if(mcd.playerIDMethods.hasID(player.getUniqueId())) {
                        mcd.playerIDMethods.openIDGUI(player,player);
                    } else {
                        mcd.message("dontHaveID",player);
                    }
                } else {
                    main.noPerm(player);
                }
            } else if (subCmd.equalsIgnoreCase("setcrime")) {
               if(player.hasPermission(main.perm_police)) {
                   if(args.length == 3) {
                       String ign = args[1];
                       String crimeID = args[2];

                       Player target = Bukkit.getServer().getPlayer(ign);

                       if(target != null) {
                           if (target == player) {
                               mcd.message("playerCantBeSelf", player);
                               return;
                           }
                           try{
                               int id = Integer.parseInt(crimeID);
                               PoliceReport report = mcd.reportMethods.getReport(id);

                               if(report == null) {
                                   mcd.message("reportNotFound",player);
                               } else {
                                   if(report.getReportStatus() == ReportStatus.OPEN) {
                                       if (mcd.playerIDMethods.hasID(target.getUniqueId())) {
                                           PlayerID playerID = mcd.playerIDMethods.getPlayerID(target.getUniqueId());
                                           if (playerID.getCrimes().contains(id)) {
                                               mcd.message("crimeAlreadyAdded", player, "%id%", crimeID);
                                           } else {
                                               playerID.addCrime(id);
                                               mcd.message("crimeAdded", player, "%id%", crimeID);
                                           }
                                       } else {
                                           mcd.message("targetDoesntHaveID", player);
                                       }
                                   } else {
                                       mcd.message("reportHasBeenClosed", player);
                                   }
                               }
                           } catch (NumberFormatException e) {
                               mcd.message("notAnInt",player,"%int%",crimeID);
                           }
                       } else {
                           mcd.message("unknownPlayer",player);
                       }
                   } else {
                       main.wrongArguments(player);
                   }
               } else {
                   main.noPerm(player);
               }
            } else if (subCmd.equalsIgnoreCase("removecrime")) {
                if(player.hasPermission(main.perm_police)) {
                    if(args.length == 3) {
                        String ign = args[1];
                        String crimeID = args[2];

                        Player target = Bukkit.getServer().getPlayer(ign);

                        if(target != null) {
                            if (target == player) {
                                mcd.message("playerCantBeSelf", player);
                                return;
                            }
                            try{
                                int id = Integer.parseInt(crimeID);
                                PoliceReport report = mcd.reportMethods.getReport(id);

                                if(report == null) {
                                    mcd.message("reportNotFound",player);
                                } else {
                                    if (mcd.playerIDMethods.hasID(target.getUniqueId())) {
                                        PlayerID playerID = mcd.playerIDMethods.getPlayerID(target.getUniqueId());
                                        if (!playerID.getCrimes().contains(id)) {
                                            mcd.message("crimeNotAdded", player, "%id%", crimeID);
                                        } else {
                                            playerID.removeCrime(id);
                                            mcd.message("crimeRemoved", player, "%id%", crimeID);
                                        }
                                    } else {
                                        mcd.message("targetDoesntHaveID", player);
                                    }
                                }
                            } catch (NumberFormatException e) {
                                mcd.message("notAnInt",player,"%int%",crimeID);
                            }
                        } else {
                            mcd.message("unknownPlayer",player);
                        }
                    } else {
                        main.wrongArguments(player);
                    }
                } else {
                    main.noPerm(player);
                }
            } else {
                main.unknownCommand(player);
            }
        } else {
            main.unknownCommand(player);
        }
    }
}
