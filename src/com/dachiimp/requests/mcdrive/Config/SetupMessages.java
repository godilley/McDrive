package com.dachiimp.requests.mcdrive.Config;

import com.dachiimp.requests.mcdrive.McDrive;
import com.dachiimp.requests.mcdrive.Util.Logger;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by DaChiimp on 9/8/2016. For Major
 */
public class SetupMessages {

    private McDrive mcd;
    private Logger logger;

    public SetupMessages(McDrive mcd) {
        this.mcd = mcd;
        logger = mcd.logger;
    }

    public void setupStrings() {

        File file = new File(mcd._plugin.getDataFolder(), File.separator + "messages.yml");
        YamlConfiguration yc = YamlConfiguration.loadConfiguration(file);

        ArrayList<String> messages = new ArrayList<>();

        ArrayList<String> messagesToAdd = new ArrayList<>();

        messages.add("noPerm");
        messages.add("unknownCommand");
        messages.add("unknownPlayer");
        messages.add("notAnInt");
        messages.add("helpHeader");
        messages.add("help");
        messages.add("helpFooter");
        messages.add("error");
        messages.add("creatingID");
        messages.add("createdID");
        messages.add("errorCreatingID");
        messages.add("alreadyHaveID");
        messages.add("dontHaveID");
        messages.add("modifiedID");
        messages.add("errorModifyingID");
        messages.add("incorrectFormat");
        messages.add("targetDoesntHaveID");
        messages.add("requestSent");
        messages.add("requestFailed");
        messages.add("playerCantBeSelf");
        messages.add("receivedRequest");
        messages.add("alreadyHaveRequest");
        messages.add("checkRequestExpired");
        messages.add("notReceivedRequest");
        messages.add("checkRequestDenied");
        messages.add("checkRequestAccepted");
        messages.add("playerOffline");
        messages.add("policeChat");
        messages.add("wrongArguments");
        messages.add("crimeReport");
        messages.add("reportFiled");
        messages.add("reportClosed");
        messages.add("reportNotFound");
        messages.add("crimeAdded");
        messages.add("crimeAlreadyAdded");
        messages.add("reportHasBeenClosed");
        messages.add("crimeNotAdded");
        messages.add("crimeRemoved");

        // stuff

        mcd.prefix = yc.getString("prefix").replaceAll("(&([a-f0-9k-or]))", "\u00A7$2");

        for (String s : messages) {
            if (yc.contains(s) && yc.getString(s) != null) {
                mcd.messages.put(s, yc.getString(s).replaceAll("(&([a-f0-9k-or]))", "\u00A7$2"));
            } else {
                messagesToAdd.add(s);
            }
        }

        if (messagesToAdd.size() > 0) {
            logger.warn("messages.yml doesn't contain " + messagesToAdd.size() + " value(s). Trying to manually update them to default, Otherwise they will be temporarily set to default values");
            ArrayList<String> added = new ArrayList<>();
            InputStream input = mcd.getResource("messages.yml");
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(input));
                String str;
                while ((str = br.readLine()) != null) {
                    for (String s : messagesToAdd) {
                        if (str.startsWith(s + ": ")) {
                            String value = str.replaceAll(s + ": ", "").replaceAll("\"", "");
                            yc.set(s, value);
                            added.add(s);
                            mcd.messages.put(s, value);
                        }
                    }
                }
                br.close();
            } catch (IOException e) {
                logger.log(ChatColor.RED + "Error trying to update messages.yml");
            } finally {
                if (added.size() > 0) {
                    logger.log("Added " + added.size() + "/" + messagesToAdd.size() + " values to messages.yml. Attempting to save");
                    try {
                        yc.save(file);
                        logger.log("Saved updated messages.yml");
                    } catch (IOException e) {
                        logger.warn("Error saving messages.yml, retrying");
                        try {
                            yc.save(file);
                            logger.log("Saved updated messages.yml");
                        } catch (IOException e2) {
                            logger.warn("Error saving messages.yml for a second time");
                        }
                    }
                } else {
                    logger.warn("Error adding any values to messages.yml");
                }
            }
        }

    }

}
