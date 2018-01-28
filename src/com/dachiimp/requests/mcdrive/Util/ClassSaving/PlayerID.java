package com.dachiimp.requests.mcdrive.Util.ClassSaving;

import com.dachiimp.requests.mcdrive.Util.Gender;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by DaChiimp on 9/8/2016. Stores Players IDs
 */
public class PlayerID {

    private UUID uuid; // uuid of the player
    private String name; // Name of the player
    private int age; // Age of the player
    private Gender gender; // Gender of the player
    private List<Integer> crimes = new ArrayList<>();

    public PlayerID(UUID uuid, String name, int age, Gender gender) {
        this.uuid = uuid;
        this.name = name;
        this.age = age;
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public UUID getUUID() {
        return uuid;
    }

    public boolean addCrime(Integer crimeID) {
        if(crimes.contains(crimeID)) return false;

        crimes.add(crimeID);

        return true;
    }

    public boolean removeCrime(Integer crimeID) {
        if(!crimes.contains(crimeID)) return false;

        crimes.remove(crimeID);

        return true;
    }

    public void setCrimes(List<Integer> crimes) {
        this.crimes = crimes;
    }

    public List<Integer> getCrimes() {
        return crimes;
    }
}
