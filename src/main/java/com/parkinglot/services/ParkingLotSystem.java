/*******************************************************************
 * @Purpose : To manage all the parking lots and retrieve information from them.
 * @Author : Kunal Deshmukh
 * @Date : 28-05-2020
 * *****************************************************************/

package com.parkinglot.services;

import com.parkinglot.exceptions.ParkingLotException;
import com.parkinglot.model.Owner;
import com.parkinglot.model.Vehicle;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ParkingLotSystem {
    public ArrayList<ParkingLot> parkingLotArrayList = new ArrayList<>();
    private int numberOfLots;
    ParkingLot parkingLot;


    public ParkingLotSystem(){
        this.numberOfLots=1;
    }

    /**+
     * @purpose To set number of parking lots
     * @param numberOfLots number of lot user wants
     * @param owner sets owner to inform about status of parking lot
     * @param capacity sets total capacity which lots can hold
     */
    public void setNumberOfLots(int numberOfLots, Owner owner, int... capacity){
        this.numberOfLots = numberOfLots;
        IntStream.range(0,numberOfLots)
                .forEach(__->parkingLotArrayList.add(addLotToList(owner, capacity)));
    }

    /**
     * @Purpose Adding slots to list to maintain them properly
     * @param owner sets owner to inform about status of parking lot
     * @param capacity sets total capacity which lots can hold
     * @return parkingLot object with initialized values
     */
    public ParkingLot addLotToList(Owner owner, int[] capacity){
        if(capacity.length==0)
            return new ParkingLot(owner);
        else
            return new ParkingLot(capacity[0],owner);
    }

    /**+
     * @purpose sets number of lots
     * @param parkingLot object array instead of inputting one at time
     */
    public void setNumberOfLots(ParkingLot ...parkingLot){
        for(ParkingLot parkingLotVehicle : parkingLot){
            parkingLotArrayList.add(parkingLotVehicle);
        }
    }


    public int getNumberOfLots(){
        return parkingLotArrayList.size();
    }

    /**+
     * @purpose parks vehicle based on reservation type and throws exception if any.
     * @param vehicle to park based on reservation type.
     * @param reservationType decides where to give slots.
     */
    public void park(Vehicle vehicle, Reservation... reservationType){
        if(reservationType.length>0 && reservationType[0].equals(Reservation.HANDICAP)){
                parkingLotArrayList.stream()
                        .filter(e->!isParked(vehicle))
                        .forEach(e->e.park(vehicle,reservationType));
        }else {
            parkingLot = parkingLotArrayList.stream()
                    .filter(e -> e.isParked(vehicle))
                    .findFirst()
                    .orElse(getEvenlyDistributed());
            parkingLot.park(vehicle);
        }
    }

    /**+
     *
     * @purpose Checks if the vehicle is parked or not.
     * @param vehicle
     * @return boolean based on parking status of vehicle.
     */
    public boolean isParked(Vehicle vehicle){
        parkingLot = parkingLotArrayList.stream()
                .filter(e->e.isParked(vehicle))
                .findFirst()
                .orElse(null);
        if(parkingLot==null)
            return false;
        return true;
    }

    /**+
     * @purpose Acts as Load Balancing for parking lots.
     * @return parking lot with most free space.
     */
    public ParkingLot getEvenlyDistributed(){
        int maximumSlotsLot = parkingLotArrayList.get(0).getEmptySlots();
        parkingLot = parkingLotArrayList.stream()
                .max(Comparator.comparing(e->e.getEmptySlots()))
                .orElse(parkingLotArrayList.get(0));
        return parkingLot;
    }

    /**+
     *
     * @param that parking lot with highest free space.
     * @param emptySlots gets empty slot from parking lot.
     * @param maximumSlotsLot local variable used to get highest empty slots.
     */
    public void getLotToPark(ParkingLot that, int emptySlots, int maximumSlotsLot){
        this.parkingLot=that;
        maximumSlotsLot = emptySlots;
    }

    /**+
     * @purpose To unpark vehicle and throw exceptions if any.
     * @param vehicle
     */
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

    /**+
     * @Purpose to get location where vehicle is parked on parking lot and slots.
     * @param vehicle
     * @return String format location of vehicle.
     */
    public String getVehicleLocation(Vehicle vehicle){
        int lotNumber = parkingLotArrayList.indexOf(parkingLot);
        int position = this.parkingLot.getIndexOfVehicle(vehicle);
        return "Parking Lot: "+lotNumber+" Position: "+position;
    }

    /**+
     * @purpose To get time of parked vehicle.
     * @param vehicle
     * @return Time in string format.
     */
    public String getTime(Vehicle vehicle){
        for(ParkingLot parkingLot : parkingLotArrayList){
            return parkingLot.getTime(vehicle);
        }
        throw new ParkingLotException("No such car parked!");
    }

    /**+
     * @purpose To reserve slots for handicapped.
     * @param parkingLotNumber indicates which parking lot.
     * @param noOfSlots number of slots to reserve.
     */
    public void updateHandicapReservation(int parkingLotNumber, int noOfSlots){
        parkingLotArrayList.get(parkingLotNumber).setHandicapReservationSlot(noOfSlots);
    }

    /**+
     * @purpose To retrieve all vehicles with input color.
     * @param color To search for.
     * @return List which contains parking lot number and vehicles with input color.
     */
    public ArrayList<String> getLocationByColor(String color){
        ArrayList<String> list = new ArrayList<>();
        for(int parkingLotIndex=0;parkingLotIndex<parkingLotArrayList.size();parkingLotIndex++){
            list.add(parkingLotIndex+" "+parkingLotArrayList.get(parkingLotIndex)
                    .getVehiclesByColor(color));
        }
        return list;
    }

    /**
     * @purpose Retrieve information of vehicle based on color and model.
     * @param color of vehicle.
     * @param model of vehicle.
     * @return Hashmap with parking lot number as key and other information in object.
     */
    public HashMap<Integer, List<Vehicle>> getVehicleInformation(String color, String model) {
        HashMap<Integer,List<Vehicle>> map = new HashMap<>();
        parkingLotArrayList.stream()
                .filter(e->e.getVehicleInformation(color,model)!=null)
                .forEach(e->map.put(parkingLotArrayList.indexOf(e),e.getVehicleInformation(color,model)));
        if(map.values().stream()
                .filter(e->e.size()>0)
                .findFirst()
                .orElse(null)==null)
            throw new ParkingLotException("No such car parked!");
        return map;
    }

    /**+
     * @purpose To retrieve all vehicles with input brand.
     * @param brandName To search for.
     * @return List which contains parking lot number and vehicles with input brand.
     */
    public ArrayList<String> getLocationByBrand(String brandName){
        ArrayList<String> list = new ArrayList<>();
        for(int parkingLotIndex=0;parkingLotIndex<parkingLotArrayList.size();parkingLotIndex++){
            list.add(parkingLotIndex+" "+parkingLotArrayList.get(parkingLotIndex)
                    .getVehiclesByBrand(brandName));
        }

        return list;
    }


    /**+
     * @purpose To retrieve all vehicles with input time.
     * @param minute
     * @return List which contains parking lot number and vehicles within given time.
     */
    public ArrayList<Vehicle> getVehicleByTime(int minute){
        ArrayList<Vehicle> list = new ArrayList<>();
        for(int parkingLotIndex=0;parkingLotIndex<parkingLotArrayList.size();parkingLotIndex++){
            list.addAll(parkingLotArrayList.get(parkingLotIndex)
                    .getVehiclesByTime(minute));
        }

        return list;
    }

    public ArrayList<Vehicle> getVehicleByRow(char row){
        ArrayList<Vehicle> rowvehicleList = new ArrayList<>();
        parkingLotArrayList.stream()
                .map(e->e.getByRows(row))
                .forEach(rowvehicleList::addAll);
        return rowvehicleList;
    }

    /**+
     * @purpose To set rows from A to last alphabet.
     * @param lastRow last alphabet of row.
     */
    public void setRows(char lastRow){
        parkingLotArrayList.stream()
                .forEach(e->e.calculateRows(lastRow));
    }


    /**+
     * @purpose To retrieve all vehicles inside of parking system.
     * @return Hashmap with parking lot number as key and other information in object.
     */
    public HashMap<Integer, List<Vehicle>> getAllVehicles(){
        HashMap<Integer,List<Vehicle>> infoList = new HashMap<>();
                parkingLotArrayList.stream()
                        .forEach(e->infoList.put(parkingLotArrayList.indexOf(e),e.getVehicleList()));
        return infoList;
    }

}
