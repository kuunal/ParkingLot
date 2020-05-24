package com.parkinglot;

import java.util.ArrayList;

public class ParkingLot{
    int limit;
    ArrayList<Object> parkList = new ArrayList();
    Inform inform;
    ParkingLot(){}

    ParkingLot(int limit){
        this.limit=limit;
    }

    public boolean park(Object car) {
        if(limit>0) {
            parkList.add(car);
            limit--;
        }else {
            inform.update();
            throw new Exceptions("Lot full! Cannot park cars");
        }
        return true;
    }

    public void setUser(Observers owner){
        inform = new Inform(owner);
    }


    public boolean unPark(Object car) {
        if(parkList.contains(car)){
            parkList.remove(car);
            limit++;
            return true;
        }
        return false;
    }
}
