package com.parkinglot;

import com.parkinglot.exceptions.ParkingLotException;
import com.parkinglot.model.AirportSecurityClass;
import com.parkinglot.model.Owner;
import com.parkinglot.services.ParkingLot;
import com.parkinglot.services.ParkingManager;
import com.parkinglot.services.ParkingSigns;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ParkingLotTest {
    Owner owner;

    @Before
    public void init(){
        owner = new Owner();

    }

    @Test
    public void givenVehicle_WhenToPark_ReturnsTrue() {
        ParkingLot parkingLot = new ParkingLot();
        Object vehicle = new Object();
        parkingLot.setUser(owner);
        parkingLot.park(vehicle);
        Boolean isParked = parkingLot.isParked(vehicle);
        Assert.assertTrue(isParked);
    }

    @Test
    public void givenParkedVehicle_WhenToPark_ThrowsException() {
        try {
            Owner owner = new Owner();
            ParkingLot parkingLot = new ParkingLot();
            parkingLot.setUser(owner);
            Object car = new Object();
            Object car1 = new Object();
            parkingLot.park(car);
            parkingLot.park(car);
        } catch (ParkingLotException e) {
            Assert.assertEquals("Vehicle already parked!", e.getMessage());
        }
    }

    @Test
    public void givenDriver_WhenUnparksCar_ReturnsTrue() {
        ParkingLot parkingLot = new ParkingLot();
        Owner owner = new Owner();
        parkingLot.setUser(owner);
        Object vehicle = new Object();
        parkingLot.park(vehicle);
        parkingLot.unPark(vehicle);
        boolean isUnparked = parkingLot.isUnparked(vehicle);
        Assert.assertTrue(isUnparked);
    }

    @Test
    public void givenUnparkedCar_WhenToUnpark_ThrowsException() {
        try {
            ParkingLot parkingLot = new ParkingLot();
            Object car = new Object();
            Object car1 = new Object();
            parkingLot.setUser(owner);
            parkingLot.park(car);
            parkingLot.unPark(car1);
        } catch (ParkingLotException e) {
            Assert.assertEquals("No such vehicle parked!", e.getMessage());
        }
    }


    @Test
    public void givenCarToPark_WhenLotIsFull_InformsOwner() {
        Owner owner = new Owner();
        try {
            ParkingLot parkingLot = new ParkingLot();
            parkingLot.setUser(owner);
            parkingLot.setCapacity(1);
            Object car = new Object();
            Object car1 = new Object();
            parkingLot.park(car);
            parkingLot.park(car1);
        } catch (Exception e) {
            Assert.assertEquals(Owner.signs.PARKING_FULL, owner.getSign());
        }
    }

    @Test
    public void givenCarToPark_WhenLotIsFull_InformsAirportSecurity() {
        AirportSecurityClass airportSecurityClass = new AirportSecurityClass();
        try {
            ParkingLot parkingLot = new ParkingLot();
            parkingLot.setUser(airportSecurityClass);
            parkingLot.setCapacity(1);
            Object car = new Object();
            Object car1 = new Object();
            parkingLot.park(car);
            parkingLot.park(car1);
        } catch (Exception e) {
            Assert.assertEquals(AirportSecurityClass.signs.PARKING_FULL, airportSecurityClass.getSign());
        }
    }

    @Test
    public void givenParkingLot_WhenUnparked_AndHasSpaceAgain_InformsOwner() {
        Owner owner = new Owner();
        Object car = new Object();
        ParkingLot parkingLot = new ParkingLot();
        try {
            parkingLot.setUser(owner);
            parkingLot.setCapacity(1);
            parkingLot.park(car);
            parkingLot.unPark(car);
        } catch (ParkingLotException e) {
            Assert.assertEquals(Owner.signs.PARKING_FULL, owner.getSign());
            parkingLot.unPark(car);
            Assert.assertEquals(ParkingSigns.signs.PARKING_AVAILABLE, owner.getSign());
        }
    }

    @Test
    public void givenNumberOfLots_WhenLoaded_ReturnsTrue() {
        ParkingManager parkingManager = new ParkingManager();
        int givenNoOfLots = 3;
        parkingManager.setNumberOfLots(givenNoOfLots, owner,5);
        int noOfLots = parkingManager.getNumberOfLots();
        Assert.assertTrue(givenNoOfLots == noOfLots);
    }

    @Test
    public void givenVehicle_WhenToPark_ShouldParkInLot_WithMostEmptySlots() {
        ParkingManager parkingManager = new ParkingManager();
        ParkingLot[] parkingLot = {
                new ParkingLot(44 ,owner),
                new ParkingLot(8,owner),
                new ParkingLot(7,owner)
        };
        Object vehicle = new Object();
        Object vehicle1 = new Object();
        parkingManager.setNumberOfLots(parkingLot);
        parkingManager.park(vehicle);
        parkingManager.park(vehicle1);
        boolean isParked = parkingManager.isParked(vehicle1);
        boolean isParked1 = parkingManager.isParked(vehicle);
        Assert.assertTrue(isParked && isParked1);
    }

    @Test
    public void givenSameVehicle_WhenToPark_InDifferentLot_ThrowsException() {
        try {
            ParkingManager parkingManager = new ParkingManager();
            ParkingLot[] parkingLot = {
                    new ParkingLot(4,owner),
                    new ParkingLot(4,owner)
            };
            Object vehicle = new Object();
            parkingManager.setNumberOfLots(parkingLot);
            parkingManager.park(vehicle);
            parkingManager.park(vehicle);
            boolean isParked = parkingManager.isParked(vehicle);
        } catch (ParkingLotException e) {
            Assert.assertEquals("Vehicle already parked!", e.getMessage());
        }
    }

    @Test
    public void givenParkedVehicleToUnPark_WhenInParkingManager_ReturnsTrue() {
        ParkingManager parkingManager = new ParkingManager();
        parkingManager.setNumberOfLots(3,owner);
        Object vehicle = new Object();
        Object vehicle1 = new Object();
        parkingManager.park(vehicle);
        parkingManager.park(vehicle1);
        parkingManager.unPark(vehicle);
        boolean isUnparked = parkingManager.isUnparked(vehicle);
        Assert.assertTrue(isUnparked);
    }

    @Test
    public void givenUnParkedVehicleToUnPark_WhenInParkingManager_ThrowsException() {
        ParkingManager parkingManager = new ParkingManager();
        parkingManager.setNumberOfLots(3,owner);
        Object vehicle = new Object();
        Object vehicle1 = new Object();
        try {
            parkingManager.park(vehicle1);
            parkingManager.unPark(vehicle);
            boolean isUnparked = parkingManager.isUnparked(vehicle);
        }catch (ParkingLotException e){
            Assert.assertEquals("No such vehicle parked!",e.getMessage());
        }
    }

    @Test
    public void givenParkedVehicle_WhenToFind_ReturnsLotAndPosition(){
        ParkingManager parkingManager = new ParkingManager();
        ParkingLot[] parkingLot = {
              new ParkingLot(4,owner),
              new ParkingLot(24,owner)
        };
        parkingManager.setNumberOfLots(parkingLot);
        Object vehicle = new Object();
        Object vehicle1 = new Object();
        parkingManager.park(vehicle);
        parkingManager.park(vehicle1);
        String vehicleLocation = parkingManager.getVehicleLocation(vehicle1);
        Assert.assertEquals("Parking Lot: 1 Position: 1",vehicleLocation);
    }


    @Test
    public void givenUnParkedVehicle_WhenToFind_ThrowsException(){
        try{
            ParkingManager parkingManager = new ParkingManager();
            ParkingLot[] parkingLot = {
                    new ParkingLot(4,owner),
                    new ParkingLot(24,owner)
            };
            parkingManager.setNumberOfLots(parkingLot);
            Object vehicle = new Object();
            Object vehicle1 = new Object();
            Object vehicle3 = new Object();
            parkingManager.park(vehicle);
            parkingManager.park(vehicle1);
            String vehicleLocation = parkingManager.getVehicleLocation(vehicle3);
        }catch (ParkingLotException e){
            Assert.assertEquals("No such car parked",e.getMessage());
        }
    }

    @Test
    public void givenVehicle_WhenToPark_ShouldReturnTime() {
        ParkingManager parkingManager = new ParkingManager();
        ParkingLot[] parkingLot = {
                new ParkingLot(44 ,owner),
                new ParkingLot(8,owner),
                new ParkingLot(7,owner)
        };
        Object vehicle = new Object();
        parkingManager.setNumberOfLots(parkingLot);
        parkingManager.park(vehicle);
        String time = parkingManager.getTime(vehicle);
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:MM:ss");
        String expectedTime = localDateTime.format(dateTimeFormatter);
        Assert.assertEquals(expectedTime,time);
    }

    @Test
    public void givenVehicle_WhenToPark_ShouldReturnTimeToSecurity() {
        AirportSecurityClass airportSecurityClass = new AirportSecurityClass();
        ParkingManager parkingManager = new ParkingManager();
        ParkingLot[] parkingLot = {
                new ParkingLot(44 ,airportSecurityClass),
                new ParkingLot(8,owner),
                new ParkingLot(7,owner)
        };
        Object vehicle = new Object();
        parkingManager.setNumberOfLots(parkingLot);
        parkingManager.park(vehicle);
        String time = parkingManager.getTime(vehicle);
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:MM:ss");
        String expectedTime = localDateTime.format(dateTimeFormatter);
        Assert.assertEquals(expectedTime,time);
    }

}