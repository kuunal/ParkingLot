package com.parkinglot;

import java.util.ArrayList;

public class ParkingLot{
    int capacity;
    ArrayList<Object> vehicleList = new ArrayList();
    Inform inform;

    ParkingLot(){
        this.capacity =100;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setUser(ParkingSigns owner){
        inform = new Inform(owner);
    }

    public void park(Object vehicle) {
        if(vehicleList.size()==capacity){
            inform.update(capacity-vehicleList.size());
            throw new ParkingLotException("Parking lot full!");
        }
        if(isParked(vehicle))
            throw new ParkingLotException("Vehicle already parked!");
        vehicleList.add(vehicle);
    }

    public boolean isParked(Object vehicle) {
        if(vehicleList.contains(vehicle))
            return true;
        return false;
    }

    public void unPark(Object vehicle) {
        if(isParked(vehicle)){
            vehicleList.remove(vehicle);
            inform.update(capacity-vehicleList.size());
        }else
            throw new ParkingLotException("No such vehicle parked!");
    }

    public boolean isUnparked(Object vehicle){
        if(isParked(vehicleList))
            return false;
        return true;
    }


}
