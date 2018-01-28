package com.dachiimp.requests.mcdrive.Util;

/**
 * Created by DaChiimp on 9/8/2016.
 */
public enum Gender {
    MALE("Male", "M","Steve"),
    FEMALE("Female", "F","MHF_Alex");

    private String toString;
    private String charString;
    private String userName;

    Gender(String toString, String charString, String userName) {
        this.toString = toString;
        this.charString = charString;
        this.userName = userName;
    }

    public static Gender fromString(String gender) {
        for (Gender gend : Gender.values()) {
            if (gend.string().equalsIgnoreCase(gender))
                return gend;
            if (gend.charString.equalsIgnoreCase(gender))
                return gend;
        }
        return null;
    }

    public String getChar() {
        return charString;
    }

    public String string() {
        return toString;
    }

    public String getUserName() {
        return userName;
    }
}
