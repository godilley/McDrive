package com.dachiimp.requests.mcdrive;

import com.dachiimp.requests.mcdrive.Commands.IDCommand;
import com.dachiimp.requests.mcdrive.Commands.ReportCommands;
import com.dachiimp.requests.mcdrive.Commands.onCommandExecutorClass;
import com.dachiimp.requests.mcdrive.Config.SetupCommands;
import com.dachiimp.requests.mcdrive.Config.SetupMessages;
import com.dachiimp.requests.mcdrive.Listeners.DisableUntilIDCreatedListeners;
import com.dachiimp.requests.mcdrive.Listeners.InventoryClickEventListener;
import com.dachiimp.requests.mcdrive.Listeners.PlayerJoinEventListener;
import com.dachiimp.requests.mcdrive.Util.ClassSaving.PlayerID;
import com.dachiimp.requests.mcdrive.Util.ClassSaving.PoliceReport;
import com.dachiimp.requests.mcdrive.Util.Logger;
import com.dachiimp.requests.mcdrive.Util.Methods.PlayerIDMethods;
import com.dachiimp.requests.mcdrive.Util.Methods.ReportMethods;
import com.dachiimp.requests.mcdrive.Util.Saving.PlayerIDSave;
import com.dachiimp.requests.mcdrive.Util.Saving.PoliceReportSave;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by DaChiimp on 9/8/2016. For Major
 */
public class McDrive extends JavaPlugin implements Listener {

    public Plugin _plugin = null;
    public SetupCommands setupCommands;
    public Logger logger;
    public PlayerIDMethods playerIDMethods;
    public ReportMethods reportMethods;
    public IDCommand idCommand;
    public ReportCommands reportCommands;
    public PoliceReportSave policeReportSave;
    public DisableUntilIDCreatedListeners disableUntilIDCreatedListeners;
    public String prefix;
    public HashMap<String, String> messages = new HashMap<>();
    public HashMap<UUID, PlayerID> playerIDs = new HashMap<>();

    public HashMap<Integer, PoliceReport> policeReports = new HashMap<>();
    public List<String> reportNames = new ArrayList<>();
    public onCommandExecutorClass cmdE;
    PlayerJoinEventListener joinEventListener;
    public SetupMessages setupMessages;
    public PlayerIDSave playerIDSave;
    InventoryClickEventListener inventoryClickEventListener;
    private String currentVersion = this.getDescription().getVersion();

    private int saveInteval = 600;

    public void onEnable() {
        _plugin = this;

        reload();

        setupCommands.doIt();
        setupCommands.setRawCommands();


        cmdE = new onCommandExecutorClass(this);
        idCommand = new IDCommand(this, cmdE);
        reportCommands = new ReportCommands(this, cmdE);
        for (String command : setupCommands.getRawCommands()) {
            if (getCommand(command) != null) {
                getCommand(command).setExecutor(cmdE);
            } else {
                logger.severe("Error getting command " + command);
            }
        }
        logger.log("Version " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + currentVersion + ChatColor.AQUA + " ENABLED");
        setupDataFolder();
        PluginManager pm = Bukkit.getServer().getPluginManager();
        pm.registerEvents(joinEventListener = new PlayerJoinEventListener(this), this);
        pm.registerEvents(disableUntilIDCreatedListeners = new DisableUntilIDCreatedListeners(this), this);
        pm.registerEvents(inventoryClickEventListener = new InventoryClickEventListener(this), this);

        setupConfig();
        setupMessages.setupStrings();

        playerIDSave.loadPlayerIDs();

        policeReportSave.loadPoliceReports();

        setupCrimes();

        saveFiles();
    }

    public void onDisable() {
        playerIDSave.savePlayerIDs();

        policeReportSave.savePoliceReports();

        unload();
        _plugin = null;
    }

    private void unload() {
        logger = null;
        setupCommands = null;
        cmdE = null;
        playerIDMethods = null;
        idCommand = null;
        reportCommands = null;
        joinEventListener = null;
        setupMessages = null;
        disableUntilIDCreatedListeners = null;
        playerIDSave = null;
        inventoryClickEventListener = null;
        reportMethods = null;
        policeReportSave = null;
    }

    private void reload() {
        logger = new Logger(this);
        setupCommands = new SetupCommands();
        playerIDMethods = new PlayerIDMethods(this);
        reportMethods = new ReportMethods(this);
        setupMessages = new SetupMessages(this);
        playerIDSave = new PlayerIDSave(this);
        policeReportSave = new PoliceReportSave(this);
    }

    public void setupConfig() {
        File file = new File(getDataFolder(), "config.yml");
        File file2 = new File(getDataFolder(), "messages.yml");
        File file3 = new File(getDataFolder(), "PlayerIDs.yml");
        File file4 = new File(getDataFolder(), "PoliceReports.yml");

        if (!file.exists()) {
            logger.log("Created config as one didn't exist");
            saveDefaultConfig();
        }

        if (!file2.exists()) {
            logger.log("Created messages.yml as one didn't exist");
            saveResource("messages.yml", false);
        }

        if (!file3.exists()) {
            logger.log("Created PlayerIDs.yml as one didn't exist");
            saveResource("PlayerIDs.yml", false);
        }

        if (!file4.exists()) {
            logger.log("Created PoliceReports.yml as one didn't exist");
            saveResource("PoliceReports.yml", false);
        }
    }

    private void setupDataFolder() {
        File dir = new File(getDataFolder(), "");
        if (!dir.exists()) {
            boolean d = dir.mkdirs();
            if (d) {
                logger.log("Created data folder as one didn't exist");
            } else {
                logger.log("Error creating data folder");
            }
        }
    }

    public String getMessage(String message) {
        if (messages.containsKey(message)) {
            return ChatColor.translateAlternateColorCodes('&', messages.get(message).replaceAll("%prefix%", prefix));
        } else {
            logger.warn("Tried to get message '" + message + "' | Available messages: " + StringUtils.join(messages.keySet(), ", "));
            return "Error getting message '" + message + "'";
        }
    }

    public void message(String message, Player player) {
        player.sendMessage(getMessage(message));
    }

    public void message(String messageToSend, Player player, String replacechar, String replaceto) {
        String message = getMessage(messageToSend).replaceAll(replacechar, replaceto);
        player.sendMessage(message);
    }

    public void setupCrimes() {
        File file = new File(getDataFolder(), "config.yml");
        if (!file.exists()) setupConfig();
        YamlConfiguration yc = YamlConfiguration.loadConfiguration(file);
        if (yc.contains("reportNames") && yc.getStringList("reportNames") != null) {
            reportNames.clear();
            reportNames = yc.getStringList("reportNames");
        }

        if(yc.contains("saveInterval") && yc.getString("saveInterval") != null) {
            String save = yc.getString("saveInterval");
            try{
                int saveInt = Integer.parseInt(save);
                saveInt = saveInt * 60;
                saveInteval = saveInt;
            } catch (NumberFormatException e) {
                logger.severe("Save interval is not a number, defaulting back to default of 10 minutes");
            }
        } else {
            logger.severe("Config doesn't contain 'saveInterval', defaulting back to default of 10 minutes");
        }
    }

    private void saveFiles() {
        new BukkitRunnable() {
            @Override
            public void run() {
                logger.log("Sending save files command...");
                playerIDSave.savePlayerIDs();
                policeReportSave.savePoliceReports();
                saveFiles();
            }
        }.runTaskLater(_plugin,saveInteval * 20);
    }
}
