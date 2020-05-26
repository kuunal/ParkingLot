package com.parkinglot.model;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

public class Payment {

    int parkingCharges;
    Object vehicle;
    String time;

    public Payment(){}

    public Payment(Object vehicle, int charges){
        this.vehicle = vehicle;
        LocalDateTime localDateTime=LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:MM:ss");
        this.time=localDateTime.format(dateTimeFormatter);
        parkingCharges = charges;
    }


    public String getTime() {
        return this.time;
    }

}
