package com.parkinglot.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Vehicle {

    String color;
    String time;
    int slot;
    String numberPlate;
    String brand;

    public Vehicle(){
        this.time=setTime();
    }

    public Vehicle(String color){
        this.color=color;
        this.time=setTime();
    }

    public Vehicle(String color, String numberPlate,String brand){
        this.color=color;
        this.time=setTime();
        this.numberPlate=numberPlate;
        this.brand = brand;
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

    public String getBrand() {
        return brand;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

}
