package com.parkinglot.services;

import com.parkinglot.exceptions.ParkingLotException;
import com.parkinglot.model.Owner;
import com.parkinglot.model.Payment;

import java.util.ArrayList;
import java.util.stream.IntStream;

public class ParkingLot{
    int capacity;
    ParkingSigns owner;
    ArrayList<Object> vehicleList = new ArrayList();
    Inform inform;
    Payment payment = new Payment();
    int handicapReservationSlot;

    public ParkingLot(){
        this.capacity=99;
        initSlots(0,capacity);
    }

    public ParkingLot(Owner owner){
        this.capacity=100;
        this.owner=owner;
        inform = new Inform(owner);
        initSlots(0,capacity);
    }

    public ParkingLot(int capacity,ParkingSigns owner){
        this.capacity =capacity;
        this.owner=owner;
        inform = new Inform(owner);
        initSlots(0,capacity);
    }


    public ParkingLot(int capacity,ParkingSigns owner,int handicapReservationSlot){
        this.capacity =capacity;
        this.owner=owner;
        inform = new Inform(owner);
        if(handicapReservationSlot>capacity)
            throw new ParkingLotException("Reserve slots cannot exceed capacity!");
        this.handicapReservationSlot=handicapReservationSlot;
        initSlots(0,capacity);
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setUser(ParkingSigns owner){
        this.owner=owner;
        inform = new Inform(owner);
    }

    public void park(Object vehicle,boolean ...isHandicapped) {
        if(isParked(vehicle))
            throw new ParkingLotException("Vehicle already parked!");
        if(isHandicapped.length>0&&isHandicapped[0]==true){
            parkVehicle(vehicle,0,handicapReservationSlot);
        }else
            parkVehicle(vehicle,handicapReservationSlot,capacity);
        owner.setTimeAndPayment(vehicle);
        if(vehicleList.size()==capacity && !vehicleList.contains(null)){
            inform.update(capacity-vehicleList.size());
            throw new ParkingLotException("Parking lot full!");
        }
    }

    public void parkVehicle(Object vehicle, int start, int slots) {
        if(slots==0)
            return;
        IntStream.range(start,slots)
                .filter(e->!isParked(vehicle))
                .forEach(e-> addVehicleInList(vehicle,e));
    }

    public void addVehicleInList(Object vehicle, int index) {
        if(vehicleList.get(index)==null && !isParked(vehicle))
            vehicleList.set(index, vehicle);
    }

    public boolean isParked(Object vehicle) {
        if(vehicleList.contains(vehicle))
            return true;
        return false;
    }

    public void unPark(Object vehicle) {
        IntStream.range(0,capacity)
                .filter(e->isParked(vehicle))
                .forEach(e-> handicappedUnParking(vehicle,e));
            inform.update(capacity-vehicleList.size());
    }

    private void handicappedUnParking(Object vehicle,int index) {
        if(index==capacity){
            throw new ParkingLotException("No such vehicle parked!");
        }
        if(vehicleList.get(index)==vehicle) {
            vehicleList.set(index, null);
        }

    }

    public boolean isUnparked(Object vehicle){
        if(isParked(vehicleList))
            return false;
        return true;
    }

    public int getEmptySlots(){
        int total = (int) IntStream.range(0,vehicleList.size())
                .filter(e->vehicleList.get(e)==null).count();
        return total-handicapReservationSlot;
    }


    public String getTime(Object vehicle){
        return owner.getTime(vehicle);
    }

    public void initSlots(int start, int end){
        IntStream.range(start,end)
                .forEach(e->vehicleList.add(null));
    }

    public int getIndexOfVehicle(Object vehicle){
        return vehicleList.indexOf(vehicle);
    }

    public void setHandicapReservationSlot(int reserveSlot){
        this.handicapReservationSlot=reserveSlot;
    }

}

