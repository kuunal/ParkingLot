package com.parkinglot.services;

import com.parkinglot.exceptions.ParkingLotException;
import com.parkinglot.model.Owner;

import java.util.ArrayList;

public class ParkingLot{
    int capacity;
    int slot=0;
    ArrayList<Object> vehicleList = new ArrayList();
    Inform inform;

    public ParkingLot(){
        this.capacity=99;
    }

    public ParkingLot(Owner owner){
        this.capacity=100;
        inform = new Inform(owner);
    }

    public ParkingLot(int capacity,Owner owner){
        this.capacity =capacity;
        inform = new Inform(owner);
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

    public int getEmptySlots(){
        return this.capacity-this.vehicleList.size();
    }

}
