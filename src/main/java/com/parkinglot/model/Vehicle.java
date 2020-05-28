package com.parkinglot.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Vehicle {

    String color;
    String time;
    int slot;
    int lotIndex;
    String numberPlate;
    String model;

    public Vehicle(){
        this.time=setTime();
    }

    public Vehicle(String color){
        this.color=color;
        this.time=setTime();
    }

    public Vehicle(String color, String numberPlate,String model){
        this.color=color;
        this.time=setTime();
        this.numberPlate=numberPlate;
        this.model=model;
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

    public String getNumberPlate() {
        return numberPlate;
    }

    public String getModel() {
        return model;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public void setLotIndex(int lotIndex) {
        this.lotIndex = lotIndex;
    }

    public int getLotIndex() {
        return lotIndex;
    }
}
