package com.dachiimp.requests.mcdrive.Util;

/**
 * Created by DaChiimp on 9/8/2016.
 */
public enum ReportStatus {
    OPEN("Open"),
    CLOSED("Close");

    private String toString;

    ReportStatus(String toString) {
        this.toString = toString;
    }

    public static ReportStatus fromString(String gender) {
        for (ReportStatus gend : ReportStatus.values()) {
            if (gend.string().equalsIgnoreCase(gender))
                return gend;
        }
        return null;
    }

    public String string() {
        return toString;
    }
}
