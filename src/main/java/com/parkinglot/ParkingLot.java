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
            inform.update(limit);
            throw new Exceptions("Lot full! Cannot park cars");
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
        return false;
    }
}
