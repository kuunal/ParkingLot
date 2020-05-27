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
    }

    public ParkingLot(Owner owner){
        this.capacity=100;
        this.owner=owner;
        inform = new Inform(owner);
    }

    public ParkingLot(int capacity,ParkingSigns owner){
        this.capacity =capacity;
        this.owner=owner;
        inform = new Inform(owner);
    }


    public ParkingLot(int capacity,ParkingSigns owner,int handicapReservationSlot){
        this.capacity =capacity;
        this.owner=owner;
        inform = new Inform(owner);
        if(handicapReservationSlot>capacity)
            throw new ParkingLotException("Reserve slots cannot exceed capacity!");
        this.handicapReservationSlot=handicapReservationSlot;
        reserveForHandicap(0,handicapReservationSlot);
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setUser(ParkingSigns owner){
        this.owner=owner;
        inform = new Inform(owner);
    }

    public void park(Object vehicle,boolean ...isHandicapped) {
        if(isHandicapped.length>0&&isHandicapped[0]==true){
            handicappedParking(vehicle);
        }
        owner.setTimeAndPayment(vehicle);
        if(vehicleList.size()==capacity){
            inform.update(capacity-vehicleList.size());
            throw new ParkingLotException("Parking lot full!");
        }
        if(isParked(vehicle))
            throw new ParkingLotException("Vehicle already parked!");
        vehicleList.add(vehicle);
    }

    public void handicappedParking(Object vehicle) {
        if(handicapReservationSlot==0)
            return;
        IntStream.range(0,handicapReservationSlot)
                .filter(e->!isParked(vehicle))
                .forEach(e->addVehicleInHandicappedReservation(vehicle,e));
    }

    public void addVehicleInHandicappedReservation(Object vehicle, int index) {
        if(vehicleList.get(index)==null && !isParked(vehicle))
                vehicleList.set(index,vehicle);
        if (index == handicapReservationSlot)
            throw new ParkingLotException("Reserved slots is full!");
    }

    public boolean isParked(Object vehicle) {
        if(vehicleList.contains(vehicle))
            return true;
        return false;
    }

    public void unPark(Object vehicle) {
        if(handicapReservationSlot>0)
        IntStream.range(0,handicapReservationSlot)
                .forEach(e-> handicappedUnParking(vehicle,e));
        else if(isParked(vehicle)){
            vehicleList.remove(vehicle);
            inform.update(capacity-vehicleList.size());
        }else
            throw new ParkingLotException("No such vehicle parked!");
    }

    private void handicappedUnParking(Object vehicle,int index) {
        if(vehicleList.get(index)==vehicle) {
            vehicleList.set(index, null);
        }
        if(index==handicapReservationSlot){
            throw new ParkingLotException("No such vehicle parked!");
        }
    }

    public boolean isUnparked(Object vehicle){
        if(isParked(vehicleList))
            return false;
        return true;
    }

    public int getEmptySlots(){
        return this.capacity-this.vehicleList.size()-this.handicapReservationSlot;
    }

    public String getTime(Object vehicle){
        return owner.getTime(vehicle);
    }

    public void reserveForHandicap(int start,int end){
        IntStream.range(start,handicapReservationSlot)
                .forEach(e->vehicleList.add(null));
    }

    public int getIndexOfVehicle(Object vehicle){
        return vehicleList.indexOf(vehicle);
    }

    public void setHandicapReservationSlot(int reserveSlot){
        this.handicapReservationSlot=reserveSlot;
        if(handicapReservationSlot-vehicleList.size()>0)
            reserveForHandicap(vehicleList.size(),handicapReservationSlot-vehicleList.size());
    }

}

