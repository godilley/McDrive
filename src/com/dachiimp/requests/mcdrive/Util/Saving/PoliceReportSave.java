package com.dachiimp.requests.mcdrive.Util.Saving;

import com.dachiimp.requests.mcdrive.McDrive;
import com.dachiimp.requests.mcdrive.Util.ClassSaving.PlayerID;
import com.dachiimp.requests.mcdrive.Util.ClassSaving.PoliceReport;
import com.dachiimp.requests.mcdrive.Util.Gender;
import com.dachiimp.requests.mcdrive.Util.ReportStatus;
import com.sun.media.jfxmedia.logging.Logger;
import org.bukkit.Color;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by DaChiimp on 09/09/2016. For Majors
 */
public class PoliceReportSave {

    private McDrive mcd;

    public PoliceReportSave(McDrive mcd) {
        this.mcd = mcd;
    }

    public boolean savePoliceReports() {
        mcd.logger.log("Received savePoliceReports() command");

        File file = new File(mcd.getDataFolder(),"PoliceReports.yml");

        if(!file.exists()) mcd.setupConfig();

        YamlConfiguration yc = YamlConfiguration.loadConfiguration(file);

        yc.set("PoliceReport",null);

        for(int id : mcd.policeReports.keySet()) {
            PoliceReport report = mcd.policeReports.get(id);
            yc.set("PoliceReport." + id + ".crime", report.getCrime());
            yc.set("PoliceReport." + id + ".reporter", report.getReporter());
            yc.set("PoliceReport." + id + ".reporterX", report.getReporterX());
            yc.set("PoliceReport." + id + ".reporterZ", report.getReporterZ());
            yc.set("PoliceReport." + id + ".date", report.getDate());
            Color shirtColor = report.getShirtColor();
            yc.set("PoliceReport." + id + ".shirtColor", shirtColor.getRed() + "," + shirtColor.getGreen() + "," + shirtColor.getBlue());
            Color pantsColor = report.getShirtColor();
            yc.set("PoliceReport." + id + ".pantsColor", pantsColor.getRed() + "," + pantsColor.getGreen() + "," + pantsColor.getBlue());
            Color bootsColor = report.getShirtColor();
            yc.set("PoliceReport." + id + ".bootsColor", bootsColor.getRed() + "," + bootsColor.getGreen() + "," + bootsColor.getBlue());
            yc.set("PoliceReport." + id + ".gender",report.getGender().string());
            yc.set("PoliceReport." + id + ".reportStatus",report.getReportStatus().string());
        }

        try {
            yc.save(file);
            mcd.logger.log("Saved PoliceReports.yml");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            mcd.logger.warn("Error saving PoliceReports.yml, retrying");
            try {
                yc.save(file);
                mcd.logger.log("Saved PoliceReports.yml");
                return true;
            } catch (IOException e2) {
                e.printStackTrace();
                mcd.logger.severe("(2nd Attempt) Error saving PoliceReports.yml. To save manually use /mcd reload");
            }
        }
        
        return false;
    }

    public void loadPoliceReports() {

        mcd.logger.log("Received loadPoliceReports() command");

        File file = new File(mcd.getDataFolder(), "PoliceReports.yml");
        if (!file.exists()) return;

        YamlConfiguration yc = YamlConfiguration.loadConfiguration(file);

        if (!yc.contains("PoliceReport") || yc.get("PoliceReport") == null) return;

        boolean cont = true;

        int i = -1;

        while (cont) {
            i++;
            if (yc.contains("PoliceReport." + i)) {
                String crime = yc.getString("PoliceReport." + i + ".crime");
                String reporter = yc.getString("PoliceReport." + i + ".reporter");
                String reporterX = yc.getString("PoliceReport." + i + ".reporterX");
                String reporterZ = yc.getString("PoliceReport." + i + ".reporterZ");
                String date = yc.getString("PoliceReport." + i + ".date");
                
                String cshirtColor = yc.getString("PoliceReport." + i + ".shirtColor");
                String cpantsColor = yc.getString("PoliceReport." + i + ".pantsColor");
                String cbootsColor = yc.getString("PoliceReport." + i + ".bootsColor");
                
                String cgender = yc.getString("PoliceReport." + i + ".gender");

                String creportStatus = yc.getString("PoliceReport." + i + ".reportStatus");

                ReportStatus reportStatus = ReportStatus.fromString(creportStatus);
                
                try{
                    String[] shirtSplit = cshirtColor.split(",");
                    int shirtR = Integer.parseInt(shirtSplit[0]);
                    int shirtG = Integer.parseInt(shirtSplit[1]);
                    int shirtB = Integer.parseInt(shirtSplit[2]);

                    String[] pantsSplit = cpantsColor.split(",");
                    int pantsR = Integer.parseInt(pantsSplit[0]);
                    int pantsG = Integer.parseInt(pantsSplit[1]);
                    int pantsB = Integer.parseInt(pantsSplit[2]);

                    String[] bootsSplit = cbootsColor.split(",");
                    int bootsR = Integer.parseInt(bootsSplit[0]);
                    int bootsG = Integer.parseInt(bootsSplit[1]);
                    int bootsB = Integer.parseInt(bootsSplit[2]);
                    
                    Color shirtColor = Color.fromRGB(shirtR,shirtG,shirtB);
                    Color pantsColor = Color.fromRGB(pantsR,pantsG,pantsB);
                    Color bootsColor = Color.fromRGB(bootsR,bootsG,bootsB);

                    Gender gender = Gender.fromString(cgender);

                    mcd.policeReports.put(i,new PoliceReport(crime,reporter,reporterX,reporterZ,gender,date,shirtColor,pantsColor,bootsColor,reportStatus));
                } catch (NumberFormatException e) {
                    mcd.logger.warn("Error parsing report for id " + i);
                }
            } else {
                cont = false;
            }
        }
    }

}
