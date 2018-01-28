package com.dachiimp.requests.mcdrive.Util.Saving;

import com.dachiimp.requests.mcdrive.McDrive;
import com.dachiimp.requests.mcdrive.Util.ClassSaving.PlayerID;
import com.dachiimp.requests.mcdrive.Util.Gender;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by DaChiimp on 09/09/2016. For Majors
 */
public class PlayerIDSave {

    private McDrive mcd;

    public PlayerIDSave(McDrive mcd) {
        this.mcd = mcd;
    }

    public boolean savePlayerIDs() {

        mcd.logger.log("Received savePlayerIDs() command");

        File file = new File(mcd.getDataFolder(), "PlayerIDs.yml");
        if (!file.exists()) mcd.setupConfig();

        YamlConfiguration yc = YamlConfiguration.loadConfiguration(file);

        yc.set("IDs", null);

        int i = 0;

        for (UUID uuid : mcd.playerIDs.keySet()) {
            i++;
            PlayerID id = mcd.playerIDs.get(uuid);
            yc.set("IDs." + i + ".uuid", uuid.toString());
            yc.set("IDs." + i + ".name", id.getName());
            yc.set("IDs." + i + ".age", id.getAge());
            yc.set("IDs." + i + ".gender", id.getGender().string());
            yc.set("IDs." + i + ".crimes", id.getCrimes());
        }

        try {
            yc.save(file);
            mcd.logger.log("Saved PlayerIDs.yml");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            mcd.logger.warn("Error saving PlayerIDs.yml, retrying");
            try {
                yc.save(file);
                mcd.logger.log("Saved PlayerIDs.yml");
                return true;
            } catch (IOException e2) {
                e.printStackTrace();
                mcd.logger.severe("(2nd Attempt) Error saving PlayerIDs.yml. To save manually use /mcd reload");
            }
        }

        return false;
    }


    public void loadPlayerIDs() {

        mcd.logger.log("Received loadPlayerIDs() command");

        File file = new File(mcd.getDataFolder(), "PlayerIDs.yml");
        if (!file.exists()) return;

        YamlConfiguration yc = YamlConfiguration.loadConfiguration(file);

        if (!yc.contains("IDs") || yc.get("IDs") == null) return;

        boolean cont = true;

        int i = 0;

        while (cont) {
            i++;
            if (yc.contains("IDs." + i)) {
                String suuid = yc.getString("IDs." + i + ".uuid");
                String name = yc.getString("IDs." + i + ".name");
                String sage = yc.getString("IDs." + i + ".age");
                String gender = yc.getString("IDs." + i + ".gender");
                List<String> ccrimes = yc.getStringList("IDs." + i + ".crimes");
                List<Integer> crimes = new ArrayList<>();
                for(String s : ccrimes) {
                    try{
                        int id = Integer.parseInt(s);
                        crimes.add(id);
                    } catch (NumberFormatException e) {
                        mcd.logger.log("Error parsing crime id of " + s + " for PlayerID of " + i);
                    }
                }
                UUID uuid = UUID.fromString(suuid);
                try {
                    int age = Integer.parseInt(sage);
                    boolean result = mcd.playerIDMethods.createID(uuid, name, age, Gender.fromString(gender));
                    if (!result) {
                        mcd.logger.warn("Error creating ID for UUID " + suuid + "with values: " + name + " > " + age + " > " + gender);
                    } else {
                        mcd.playerIDMethods.getPlayerID(uuid).setCrimes(crimes);
                    }
                } catch (NumberFormatException e) {
                    mcd.logger.warn("Error parsing age for UUID " + suuid);
                }
            } else {
                cont = false;
            }
        }
    }

}
