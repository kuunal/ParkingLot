package com.parkinglot.services;

import com.parkinglot.exceptions.ParkingLotException;
import com.parkinglot.model.Owner;
import com.parkinglot.model.Vehicle;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ParkingLotSystem {
    public ArrayList<ParkingLot> parkingLotArrayList = new ArrayList<>();
    private int numberOfLots;
    ParkingLot parkingLot;

    public ParkingLotSystem(){
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
        for(ParkingLot parkingLotVehicle : parkingLot){
            parkingLotArrayList.add(parkingLotVehicle);
        }
    }


    public int getNumberOfLots(){
        return parkingLotArrayList.size();
    }

    public void park(Vehicle vehicle, boolean... isHandicapped){
        if(isHandicapped.length>0&&isHandicapped[0]==true){
                parkingLotArrayList.stream()
                        .filter(e->!isParked(vehicle))
                        .forEach(e->e.park(vehicle,true));
        }else {
            parkingLot = parkingLotArrayList.stream()
                    .filter(e -> e.isParked(vehicle))
                    .findFirst()
                    .orElse(getEvenlyDistributed(vehicle));
            parkingLot.park(vehicle);
        }
    }

    public boolean isParked(Vehicle vehicle){
        parkingLot = parkingLotArrayList.stream()
                .filter(e->e.isParked(vehicle))
                .findFirst()
                .orElse(null);
        if(parkingLot==null)
            return false;
        return true;
    }

    public ParkingLot getEvenlyDistributed(Vehicle vehicle){
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

    public void unPark(Vehicle vehicle){
        parkingLot = parkingLotArrayList.stream()
                .filter(e -> e.isParked(vehicle))
                .findFirst()
                .orElse(null);
        if(parkingLot==null)
            throw new ParkingLotException("No such vehicle parked!");
        parkingLot.unPark(vehicle);
    }

    public boolean isUnparked(Vehicle vehicle){
        if(isParked(vehicle))
            return false;
        return true;
    }

    public String getVehicleLocation(Vehicle vehicle){
        int lotNumber = parkingLotArrayList.indexOf(parkingLot);
        int position = this.parkingLot.getIndexOfVehicle(vehicle);
        return "Parking Lot: "+lotNumber+" Position: "+position;
    }

    public String getTime(Vehicle vehicle){
        for(ParkingLot parkingLot : parkingLotArrayList){
            return parkingLot.getTime(vehicle);
        }
        throw new ParkingLotException("No such car parked!");
    }

    public void updateHandicapReservation(int parkingLotNumber, int noOfSlots){
        parkingLotArrayList.get(parkingLotNumber).setHandicapReservationSlot(noOfSlots);
    }

    public List<Integer> getLocationByColor(String color){
        List<Integer> list = new ArrayList<>();
        for(ParkingLot parkingLot : parkingLotArrayList){
            list.addAll(parkingLot.getVehiclesByColor(color));
        }
        return list;
    }

}
