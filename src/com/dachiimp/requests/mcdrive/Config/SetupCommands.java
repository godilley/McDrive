package com.dachiimp.requests.mcdrive.Config;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by DIL15151969 on 22/06/2016. For McDrive
 */
public class SetupCommands {

    public HashMap<String, String> commands = new HashMap<>();

    public ArrayList<String> rawCommands = new ArrayList<>();

    public void doIt() {

        commands.clear();

        commands.put("/police ","Opens the police report GUI.");
        commands.put("/pc (message)","Send a message on the police chat. (Permission Needed)");
        commands.put("/pr","Open a gui with all open police reports.  (Permission Needed)");
        commands.put("/id set name (name)","Sets your roleplay name. ");
        commands.put("/id set age (age)","Sets your roleplay age.");
        commands.put("/id set gender (gender)","Sets your roleplay gender");
        commands.put("/id see (ign)","Send a request to see the player's id (Permission Needed)");
        commands.put("/id allow (ign)","This will allow the player to see your id. ");
        commands.put("/id deny (ign)","This dosent allow the player to see your id. ");
        commands.put("/id forcesee (ign)","Opens a gui with the player's id, bypassing the request. (Permission Needed)");
        commands.put("/id check","Checks your own id.");
        commands.put("/id setcrime (ign) (report number)","Adds the crime to the player's criminal history");
        commands.put("/id removecrime(ign) (report number)","Removes the crime from the player's criminal history");
        commands.put("/mcd reload","Reloads the plugin.");

    }

    public void setRawCommands() {
        rawCommands.clear();

        rawCommands.add("police");
        rawCommands.add("pc");
        rawCommands.add("pr");
        rawCommands.add("id");
        rawCommands.add("mcd");

    }

    public ArrayList<String> getRawCommands() {
        return rawCommands;
    }

    public HashMap<String, String> getCommands() {
        return commands;
    }
}
