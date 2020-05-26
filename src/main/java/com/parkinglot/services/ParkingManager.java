package com.parkinglot.services;

import com.parkinglot.exceptions.ParkingLotException;
import com.parkinglot.model.Owner;

import java.util.ArrayList;
import java.util.stream.IntStream;

public class ParkingManager {
    public ArrayList<ParkingLot> parkingLotArrayList = new ArrayList<>();
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

    public ParkingLot addLotToList(Owner owner, int[] capacity){
        if(capacity.length==0)
            return new ParkingLot(owner);
        else
            return new ParkingLot(capacity[0],owner);
    }

    public void setNumberOfLots(ParkingLot ...parkingLot){
        for(ParkingLot parkingLotObject : parkingLot){
            parkingLotArrayList.add(parkingLotObject);
        }
    }


    public int getNumberOfLots(){
        return parkingLotArrayList.size();
    }

    public void park(Object vehicle,boolean... isHandicapped){
        if(isHandicapped.length>0&&isHandicapped[0]==true){
                parkingLotArrayList.stream()
                        .filter(e->!isParked(vehicle))
                        .forEach(e->e.handicappedParking(vehicle));
        }else {
            parkingLot = parkingLotArrayList.stream()
                    .filter(e -> e.isParked(vehicle))
                    .findFirst()
                    .orElse(getEvenlyDistributed(vehicle));
            parkingLot.park(vehicle);
        }
    }

    public boolean isParked(Object vehicle){
        parkingLot = parkingLotArrayList.stream()
                .filter(e->e.isParked(vehicle))
                .findFirst()
                .orElse(null);
        if(parkingLot==null)
            return false;
        return true;
    }

    public ParkingLot getEvenlyDistributed(Object vehicle){
        int maximumSlotsLot = parkingLotArrayList.get(0).getEmptySlots();
        parkingLot = parkingLotArrayList.get(0);
        parkingLotArrayList.stream()
                .filter(e->e.getEmptySlots()>maximumSlotsLot)
                .forEach(e->getLotToPark(e,e.getEmptySlots(),maximumSlotsLot));
        return parkingLot;
    }

    public void getLotToPark(ParkingLot that, int emptySlots, int maximumSlotsLot){
        this.parkingLot=that;
        maximumSlotsLot = emptySlots;
    }

    public void unPark(Object vehicle){
        parkingLot = parkingLotArrayList.stream()
                .filter(e -> e.isParked(vehicle))
                .findFirst()
                .orElse(getEvenlyDistributed(vehicle));
        parkingLot.unPark(vehicle);
    }

    public boolean isUnparked(Object vehicle){
        if(isParked(vehicle))
            return false;
        return true;
    }

    public String getVehicleLocation(Object vehicle){
        int lotNumber = parkingLotArrayList.indexOf(parkingLot);
        int position = this.parkingLot.getIndexOfVehicle(vehicle);
        return "Parking Lot: "+lotNumber+" Position: "+position;
    }

    public String getTime(Object vehicle){
        for(ParkingLot parkingLot : parkingLotArrayList){
            return parkingLot.getTime(vehicle);
        }
        throw new ParkingLotException("No such car parked!");
    }


}
