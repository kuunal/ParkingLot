package com.parkinglot.services;

import com.parkinglot.exceptions.ParkingLotException;
import com.parkinglot.model.Vehicle;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ParkingLot{
    int capacity;
    ParkingSigns owner;
    ArrayList<Vehicle> vehicleList = new ArrayList();
    Inform inform;
    Vehicle vehicle = new Vehicle();
    int handicapReservationSlot;

    public ParkingLot(){
        this.capacity=99;
        initSlots(0,capacity);
    }

    public ParkingLot(ParkingSigns owner){
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

        if(capacity<vehicleList.size()){
            int isChangable = IntStream.range(capacity,vehicleList.size())
                                    .filter(e->vehicleList.get(e)!=null)
                                    .findFirst()
                                    .orElse(-1);
            if(isChangable==-1)
                IntStream.range(capacity-1,vehicleList.size()-1)
                        .filter(e->vehicleList.get(vehicleList.size()-1)==null)
                        .forEach(e->vehicleList.remove(vehicleList.size()-1));
            else
                throw new ParkingLotException("Capacity cannot be reduced, There are cars parked in!");
        }
        else if(capacity>vehicleList.size()){
            initSlots(vehicleList.size(),capacity);
        }
    }

    public void setUser(ParkingSigns owner){
        this.owner=owner;
        inform = new Inform(owner);
    }

    public void park(Vehicle vehicle,boolean ...isHandicapped) {
        if(isParked(vehicle))
            throw new ParkingLotException("Vehicle already parked!");
        if(vehicleList.size()==capacity && !vehicleList.contains(null)){
            inform.update(capacity-vehicleList.size());
            throw new ParkingLotException("Parking lot full!");
        }
        if(isHandicapped.length>0&&isHandicapped[0]==true){
            parkVehicle(vehicle,0,handicapReservationSlot);
        }else
            parkVehicle(vehicle,handicapReservationSlot,capacity);

    }

    public void parkVehicle(Vehicle vehicle, int start, int slots) {
        if(slots==0)
            return;
        IntStream.range(start,slots)
                .filter(e->!isParked(vehicle))
                .forEach(e-> addVehicleInList(vehicle,e));
    }

    public void addVehicleInList(Vehicle vehicle, int index) {
        if(vehicleList.get(index)==null && !isParked(vehicle))
            vehicleList.set(index, vehicle);
    }

    public boolean isParked(Vehicle vehicle) {
        if(vehicleList.contains(vehicle))
            return true;
        return false;
    }

    public void unPark(Vehicle vehicle) {
        IntStream.range(0,capacity)
                .filter(e->isParked(vehicle))
                .forEach(e-> removingFromList(vehicle,e));
            inform.update(capacity-vehicleList.size());
    }

    private void removingFromList(Vehicle vehicle, int index) {
        if(index==capacity){
            throw new ParkingLotException("No such vehicle parked!");
        }
        if(vehicleList.get(index)==vehicle) {
            vehicleList.set(index, null);
        }

    }

    public boolean isUnparked(Vehicle vehicle){
        if(isParked(vehicle))
            return false;
        return true;
    }

    public int getEmptySlots(){
        int total = (int) IntStream.range(0,vehicleList.size())
                .filter(e->vehicleList.get(e)==null).count();
        return total-handicapReservationSlot;
    }


    public String getTime(Vehicle vehicle){
        return vehicle.getTime();
    }

    public void initSlots(int start, int end){
        IntStream.range(start,end)
                .forEach(e->vehicleList.add(null));
    }

    public int getIndexOfVehicle(Vehicle vehicle){
        return vehicleList.indexOf(vehicle);
    }

    public void setHandicapReservationSlot(int reserveSlot){
        this.handicapReservationSlot=reserveSlot;
    }

    public List<Integer> getVehiclesByColor(String color){

        return vehicleList.stream()
                .filter(e->e!=null)
                .filter(e->e.getColor().toLowerCase().equals(color))
                .map(e->e.getSlot())
                .collect(Collectors.toList());
    }

}

