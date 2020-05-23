package com.parkinglot;

import java.util.ArrayList;

public class ParkingLot {
    int limit;
    ArrayList<Object> parkList = new ArrayList();
    Owner owner = new Owner();

    ParkingLot(int limit){
        this.limit=limit;
    }

    public boolean park(Object ...cars) {
        int noOfCarsParkedForCurrentArray=0;
        for (Object car : cars) {
            if(limit>0) {
                parkList.add(car);
                limit--;
                noOfCarsParkedForCurrentArray++;
            }else {
                owner.setSign(Owner.signs.PARKING_FULL);
                throw new Exceptions("Lot full! Cannot park "+(cars.length-noOfCarsParkedForCurrentArray)
                        + " cars");
            }
        }
        return true;

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
