package com.dachiimp.requests.mcdrive.Commands;

import com.dachiimp.requests.mcdrive.McDrive;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

/**
 * Created by DaChiimp on 9/2/2016. For RQ
 */
public class ReportCommands {

    private McDrive mcd;
    private onCommandExecutorClass main;

    public ReportCommands(McDrive mcd, onCommandExecutorClass main) {
        this.mcd = mcd;
        this.main = main;
    }

    void execute(Command cmd, String[] args, Player player) {
        if (cmd.getName().equalsIgnoreCase("police")) {
            mcd.reportMethods.openStartReportGUI(player, 1);
        } else if (cmd.getName().equalsIgnoreCase("pr")) {
            if(player.hasPermission(main.perm_police)) {
                mcd.reportMethods.openReportsGUI(player,1);
            } else {
                main.noPerm(player);
            }
        } else {
            player.sendMessage("Error handling command in 'ReportCommands'");
        }
    }
}
