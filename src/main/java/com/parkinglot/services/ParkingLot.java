package com.parkinglot.services;

import com.parkinglot.exception.ParkingLotExceptions;

import java.util.ArrayList;

public class ParkingLot{
    int limit;
    ArrayList<Object> parkList = new ArrayList();
    Inform inform;
    ParkingLot(){}

    public ParkingLot(int limit){
        this.limit=limit;
    }

    public boolean park(Object car) {
        if(limit>0) {
            parkList.add(car);
            limit--;
        }else {
            inform.update(limit);
            throw new ParkingLotExceptions("Lot full! Cannot park cars");
        }
        return true;
    }

    public void setUser(ParkingSigns owner){
        inform = new Inform(owner);
        inform.update(limit);
    }


    public boolean unPark(Object car) {
        if(parkList.contains(car)){
            parkList.remove(car);
            limit++;
            inform.update(limit);
            return true;
        }
        throw new ParkingLotExceptions("No such car parked!");
    }
}
