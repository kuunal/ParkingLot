package com.parkinglot.services;

import com.parkinglot.exceptions.ParkingLotException;
import com.parkinglot.model.Owner;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ParkingManager {
    ArrayList<ParkingLot> parkingLotArrayList = new ArrayList<>();
    private int numberOfLots;
    ParkingLot parkingLot;

    public ParkingManager(){
        this.numberOfLots=1;
    }

    public void setNumberOfLots(int numberOfLots, Owner owner, int... capacity){
        this.numberOfLots = numberOfLots;
        IntStream.range(0,numberOfLots)
                .forEach(__->parkingLotArrayList.add(addLotToList(owner, capacity)));
    }

    public void setNumberOfLots(ParkingLot ...parkingLot){
        for(ParkingLot parkingLotObject : parkingLot){
            parkingLotArrayList.add(parkingLotObject);
        }
    }

    public ParkingLot addLotToList(Owner owner, int[] capacity){
        if(capacity.length==0)
            return new ParkingLot(owner);
        else
            return new ParkingLot(capacity[0],owner);
    }

    public int getNumberOfLots(){
        return parkingLotArrayList.size();
    }

    public void park(Object vehicle){
        findVehicleInAllLots(vehicle);
        System.out.println(parkingLot);
        parkingLot.park(vehicle);
    }

    public boolean isParked(Object vehicle){
        return this.parkingLot.isParked(vehicle);
    }

    public void getLotToPark(ParkingLot that, int slots, int maximumSlotsLot){
        this.parkingLot=that;
        maximumSlotsLot = slots;
    }

    public void findVehicleInAllLots(Object vehicle){
        int maximumSlotsLot = parkingLotArrayList.get(0).getEmptySlots();
        parkingLot=parkingLotArrayList.get(0);
        parkingLotArrayList.stream()
                .takeWhile(e->e.isParked(vehicle))
                .filter(e->e.getEmptySlots()>maximumSlotsLot)
                .forEach(e->getLotToPark(e,e.getEmptySlots(),maximumSlotsLot));

    }

    public void unPark(Object vehicle){
        findVehicleInAllLots(vehicle);
        parkingLot.unPark(vehicle);
    }

    public boolean isUnparked(Object vehicle){
        return parkingLot.isUnparked(vehicle);
    }


}
