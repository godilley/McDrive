package com.dachiimp.requests.mcdrive.Commands;

import com.dachiimp.requests.mcdrive.McDrive;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

/**
 * Created by DaChiimp on 9/2/2016. For RQ
 */
public class TemplateCommand {

    private McDrive mcd;
    private onCommandExecutorClass main;

    public TemplateCommand(McDrive mcd, onCommandExecutorClass main) {
        this.mcd = mcd;
        this.main = main;
    }

    void execute(Command cmd, String[] args, Player player) {

    }
}
