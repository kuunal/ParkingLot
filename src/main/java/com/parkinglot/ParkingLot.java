package com.parkinglot;

import java.util.ArrayList;

public class ParkingLot {
    ArrayList<Object> parkList = new ArrayList();

    public boolean park(Object car) {
        parkList.add(car);
        return true;
    }


    public boolean unPark(Object car) {
        if(parkList.contains(car)){
            parkList.remove(car);
            return true;
        }
        return false;
    }
}
