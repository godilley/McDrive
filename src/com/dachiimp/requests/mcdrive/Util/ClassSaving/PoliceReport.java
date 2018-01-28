package com.dachiimp.requests.mcdrive.Util.ClassSaving;


import com.dachiimp.requests.mcdrive.Util.Gender;
import com.dachiimp.requests.mcdrive.Util.ReportStatus;
import org.bukkit.Color;

/**
 * Created by DaChiimp on 10/09/2016. For Majors
 */
public class PoliceReport {

    private String crime;
    private String reporter;
    private String reporterX;
    private String reporterZ;
    private String date;

    private Color shirtColor;
    private Color pantsColor;
    private Color bootsColor;
    private Gender gender;

    private ReportStatus reportStatus;

    public PoliceReport(String crime, String reporter, String reporterX, String reporterZ, Gender gender, String date, Color shirtColor, Color pantsColor, Color bootsColor, ReportStatus reportStatus) {
        this.crime = crime;
        this.reporter = reporter;
        this.reporterX = reporterX;
        this.reporterZ = reporterZ;
        this.date = date;
        this.shirtColor = shirtColor;
        this.pantsColor = pantsColor;
        this.bootsColor = bootsColor;
        this.gender = gender;
        this.reportStatus = reportStatus;
    }

    public Color getBootsColor() {
        return bootsColor;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Color getPantsColor() {
        return pantsColor;
    }

    public Color getShirtColor() {
        return shirtColor;
    }

    public Gender getGender() {
        return gender;
    }

    public String getCrime() {
        return crime;
    }

    public String getDate() {
        return date;
    }

    public String getReporter() {
        return reporter;
    }

    public String getReporterX() {
        return reporterX;
    }

    public String getReporterZ() {
        return reporterZ;
    }

    public void setBootsColor(Color bootsColor) {
        this.bootsColor = bootsColor;
    }

    public void setCrime(String crime) {
        this.crime = crime;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPantsColor(Color pantsColor) {
        this.pantsColor = pantsColor;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    public void setReporterX(String reporterX) {
        this.reporterX = reporterX;
    }

    public void setReporterZ(String reporterZ) {
        this.reporterZ = reporterZ;
    }

    public void setShirtColor(Color shirtColor) {
        this.shirtColor = shirtColor;
    }

    public ReportStatus getReportStatus() {
        return reportStatus;
    }

    public void setReportStatus(ReportStatus reportStatus) {
        this.reportStatus = reportStatus;
    }
}
