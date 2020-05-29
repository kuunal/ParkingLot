/*******************************************************************
 * @Purpose : To park, unpark and give information to parking system.
 * @Author : Kunal Deshmukh
 * @Date : 28-05-2020
 * *****************************************************************/

package com.parkinglot.services;

import com.parkinglot.exceptions.ParkingLotException;
import com.parkinglot.model.Vehicle;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ParkingLot{
    int capacity;
    ArrayList<Vehicle> vehicleList = new ArrayList();
    HashMap<Character,Integer> rowMap = new HashMap<>();
    Inform inform;
    int handicapReservationSlot;

    /**+
     * @purpose initialize parking lot without owner and with default capacity.
     */
    public ParkingLot(){
        this.capacity=99;
        initSlots(0,capacity);
    }


    /**+
     * @purpose initialize parking lot with specified owner and with default capacity.
     */
    public ParkingLot(ParkingSigns owner){
        this.capacity=100;
        inform = new Inform(owner);
        initSlots(0,capacity);
    }

    /**+
     * @purpose initialize parking lot with specified owner and with specified capacity.
     */
    public ParkingLot(int capacity,ParkingSigns owner){
        this.capacity =capacity;
        inform = new Inform(owner);
        initSlots(0,capacity);
    }

    /**+
     * @purpose initialize parking lot with specified owner and with default capacity
     * and speified number of reservation slot.
     */

    public ParkingLot(int capacity,ParkingSigns owner,int handicapReservationSlot){
        this.capacity =capacity;
        inform = new Inform(owner);
        setHandicapReservationSlot(handicapReservationSlot);
        initSlots(0,capacity);
    }

    /**+
     * @purpose sets or update capacity. Update will work only if
     * while decreasing size the vehicle should not have been parked.
     */
    public void updateCapacity(int capacity) {
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
        inform = new Inform(owner);
    }

    /**+
     * @purpose To park vehicle based on reservation.
     * @param vehicle to park.
     * @param reservationType to decide the slots.
     */
    public void park(Vehicle vehicle,Reservation ...reservationType) {
        if(isParked(vehicle))
            throw new ParkingLotException("Vehicle already parked!");
        if(vehicleList.size()==capacity && !vehicleList.contains(null)){
            inform.update(capacity-vehicleList.size());
            throw new ParkingLotException("Parking lot full!");
        }
        if(reservationType.length>0&&reservationType[0].equals(Reservation.HANDICAP)){
            parkVehicle(vehicle,0,handicapReservationSlot);
        }
        else
            parkVehicle(vehicle,handicapReservationSlot,capacity);

    }

    /**+
     * @purpose To park vehicle based on reservation.
     * @param vehicle to park.
     * @param start start position of slot.
     * @param slots end position of slot to park on.
     */
    public void parkVehicle(Vehicle vehicle, int start, int slots) {
        if(slots==0)
            return;
        IntStream.range(start,slots)
                .filter(e->!isParked(vehicle))
                .forEach(e-> addVehicleInList(vehicle,e));
    }

    /**+
     * @purpose  To add vehicle in list which will indicate vehicle as parked.
     * @param vehicle
     * @param index of list to store vehicle.
     */
    public void addVehicleInList(Vehicle vehicle, int index) {
        if(vehicleList.get(index)==null && !isParked(vehicle))
            vehicleList.set(index, vehicle);
        vehicle.setSlot(getIndexOfVehicle(vehicle));
    }

    public boolean isParked(Vehicle vehicle) {
        if(vehicleList.contains(vehicle))
            return true;
        return false;
    }

    /**+
     * @purpose To unpark vehicle and inform owner about empty slot.
     * @param vehicle
     */
    public void unPark(Vehicle vehicle) {
        IntStream.range(0,capacity)
                .filter(e->isParked(vehicle))
                .forEach(e-> removingFromList(vehicle,e));
            inform.update(1);
    }

    /**+
     * @purpose To remove vehicle from list indicating vehicle as unparked.
     * @param vehicle to be removed.
     * @param index where it is stored and to make it null.
     */
    private void removingFromList(Vehicle vehicle, int index) {
        if(vehicleList.get(index)==vehicle) {
            vehicleList.set(index, null);
        }
    }

    public boolean isUnparked(Vehicle vehicle){
        if(isParked(vehicle))
            return false;
        return true;
    }

    /**+
     * @purpose To return empty slots for parking system to decide further parking of vehicles.
     * @return Empty slots for normal type.
     */
    public int getEmptySlots(){
        int total = (int) IntStream.range(0,vehicleList.size())
                .filter(e->vehicleList.get(e)==null).count();
        return total-handicapReservationSlot;
    }

    /**+
     * @purpose To get time of vehicle parked on.
     * @param vehicle to get time of.
     * @return String format time.
     */
    public String getTime(Vehicle vehicle){
        if(vehicleList.contains(vehicle))
            return vehicle.getTime();
        return null;
    }

    /**+
     * @purpose Filling null at arraylist position, so that it can act as slots.
     * @param start
     * @param end
     */
    public void initSlots(int start, int end){
        IntStream.range(start,end)
                .forEach(e->vehicleList.add(null));
    }

    /**
     * purpose To get slot position of where vehicle is parked.
     * @param vehicle
     * @return position of vehicle in lot.
     */
    public int getIndexOfVehicle(Vehicle vehicle){
        return vehicleList.indexOf(vehicle);
    }

    /**
     * @purpose Setting or updating reservations.
     * @param reserveSlot no of slots.
     */
    public void setHandicapReservationSlot(int reserveSlot){
        if(handicapReservationSlot>capacity)
            throw new ParkingLotException("Reserve slots cannot exceed capacity!");
        this.handicapReservationSlot=reserveSlot;
    }

    /**+
     * @purpose To retrieve all vehicles with input color.
     * @param color To search for.
     * @return List which contains parking lot number and vehicles with input color.
     */

    public List<Integer> getVehiclesByColor(String color){
        return vehicleList.stream()
                .filter(e->e!=null)
                .filter(e->e.getColor().toLowerCase().equals(color))
                .map(e->e.getSlot())
                .collect(Collectors.toList());
    }

    public List<Vehicle> getVehicleInformation(String color, String model){
        return vehicleList.stream()
                .filter(e->e!=null)
                .filter(e->e.getColor().toLowerCase().equals(color))
                .filter(e->e.getBrand().toLowerCase().equals(model))
                .collect(Collectors.toList());

    }

    /**+
     * @purpose To retrieve all vehicles with input brand.
     * @param brandName To search for.
     * @return List which contains parking lot number and vehicles with input brand.
     */
    public List<Integer> getVehiclesByBrand(String brandName){
        return vehicleList.stream()
                .filter(e->e!=null)
                .filter(e->e.getBrand().toLowerCase().equals(brandName.toLowerCase()))
                .map(e->e.getSlot())
                .collect(Collectors.toList());
    }

    public List<Vehicle> getVehiclesByTime(int minutes){
        return vehicleList.stream()
                .filter(e->e!=null)
                .filter(e->calculateTimes(e.getTime(),minutes))
                .collect(Collectors.toList());
    }

    /**
     *
     * @param time of Vehicle parked.
     * @param minutes To find vehicle within that minutes.
     */
    private boolean calculateTimes(String time, long minutes) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        LocalDateTime parkedTime = LocalDateTime.parse(time,dateTimeFormatter);
        LocalDateTime givenTime = LocalDateTime.now().minus(minutes, ChronoUnit.MINUTES);
        if(parkedTime.compareTo(givenTime)>0){
            return true;
        }
        return false;
    }

    /**+
     * @purpose To set rows from A to last alphabet.
     * @param lastRow last alphabet of row.
     */
    public void calculateRows(char lastRow){
        char row;
        int startIndexOfRow=0;
        if((lastRow-'A')>capacity)
            throw new ParkingLotException("Cant assign that much row for few slots!");
        else {
            int noOfSlotsPerRow=capacity/(lastRow-'A');
            for (row = 'A'; row <= lastRow && startIndexOfRow < capacity; row++, startIndexOfRow+=noOfSlotsPerRow) {
                rowMap.put(row, startIndexOfRow);
            }
        }
    }

    /**+
     * @purpose To get vehicles information from rows.
     * @param row last alphabet of row.
     */
    public ArrayList<Vehicle> getByRows(char row){
        row=Character.toUpperCase(row);
        int startIndexOfRow = rowMap.get(row);
        int endIndexOfRow;
        char nextChar = (char)((int)row+1);
        if(rowMap.containsKey(nextChar))
             endIndexOfRow = rowMap.get(nextChar);
        else
            endIndexOfRow = capacity    ;
        ArrayList<Vehicle> rowArrayList = new ArrayList<>();
        IntStream.range(startIndexOfRow,endIndexOfRow)
                .filter(e->vehicleList.get(e)!=null)
                .forEach(e->rowArrayList.add(vehicleList.get(e)));
        return rowArrayList;
    }

    /**+
     * @purpose To retrieve all vehicles inside of parking system.
     * @return List with all information in object.
     */
    public List<Vehicle> getVehicleList(){
        return vehicleList.stream()
                .filter(e->e!=null)
                .collect(Collectors.toList());
    }

    /**+
     * @purpose To retrieve all vehicles parked in reserved slots.
     * @return List with all information in object.
     */
    public List<Vehicle> getReservationVehiclesInformation() {
        return IntStream.range(0,handicapReservationSlot)
                .filter(e->vehicleList.get(e)!=null)
                .mapToObj(e->vehicleList.get(e))
                .collect(Collectors.toList());
    }
}

