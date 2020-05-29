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
import java.util.stream.IntStream;

public class ParkingLotSystem {
    public ArrayList<ParkingLot> parkingLotArrayList = new ArrayList<>();
    private int numberOfLots;
    private String time = null;
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

    public void updateCapacity(int newCapacity,int parkingLotNumber){
        parkingLotArrayList.get(parkingLotNumber).updateCapacity(newCapacity);
    }

    /**+
     * @purpose Acts as Load Balancing for parking lots.
     * @return parking lot with most free space.
     */
    public ParkingLot getEvenlyDistributed(){
        parkingLot = parkingLotArrayList.stream()
                .max(Comparator.comparing(e->e.getEmptySlots()))
                .orElse(parkingLotArrayList.get(0));
        return parkingLot;
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
        for(ParkingLot parkingLot : parkingLotArrayList) {
            if( parkingLot.getTime(vehicle)!=null)
            time = parkingLot.getTime(vehicle);
        }
        return time;
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
    public HashMap<Integer,List<Integer>> getLocationByColor(String color){
        HashMap<Integer,List<Integer>> infoList = new HashMap<>();
        parkingLotArrayList.stream()
                .forEach(e->infoList.put(parkingLotArrayList.indexOf(e),e.getVehiclesByColor(color)));

        return infoList;
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
    public HashMap<Integer,List<Integer>> getLocationByBrand(String brandName){
        HashMap<Integer,List<Integer>> infoList = new HashMap<>();
        parkingLotArrayList.stream()
                .forEach(e->infoList.put(parkingLotArrayList.indexOf(e),e.getVehiclesByBrand(brandName)));
        return infoList;
    }


    /**+
     * @purpose To retrieve all vehicles with input time.
     * @param minute
     * @return List which contains parking lot number and vehicles within given time.
     */
    public HashMap<Integer,List<Vehicle>> getVehicleByTime(int minute){
        HashMap<Integer,List<Vehicle>> infoList = new HashMap<>();
        parkingLotArrayList.stream()
                .forEach(e->infoList.put(parkingLotArrayList.indexOf(e),e.getVehiclesByTime(minute)));
        return infoList;
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

    public HashMap<Integer, List<Vehicle>> getReservationVehiclesInformation() {
        HashMap<Integer,List<Vehicle>> infoList = new HashMap<>();
        parkingLotArrayList.stream()
                .forEach(e->infoList.put(parkingLotArrayList.indexOf(e),e.getReservationVehiclesInformation()));
        return infoList;
    }
}
