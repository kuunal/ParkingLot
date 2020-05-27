package com.parkinglot.model;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

public class Vehicle {

    String color;
    int parkingCharges;
    Object vehicle;
    String time;
    int slot;

    public Vehicle(){
        this.time=setTime();
    }

    public Vehicle(String color){
        this.color=color;
        this.time=setTime();
    }

    public Vehicle(Object vehicle, int... charges){
        this.vehicle = vehicle;
        this.time=setTime();
        if(charges.length>0)
            parkingCharges = charges[0];
    }


    public String setTime(){
        LocalDateTime localDateTime=LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:MM:ss");
        return localDateTime.format(dateTimeFormatter);
    }

    public String getTime() {
        return this.time;
    }

    public int getSlot() {
        return this.slot;
    }

    public String getColor() {
        return this.color;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

}
