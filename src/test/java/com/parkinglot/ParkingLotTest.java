package com.parkinglot;

import com.parkinglot.exception.ParkingLotExceptions;
import com.parkinglot.models.AirportSecurityClass;
import com.parkinglot.models.OwnerClass;
import com.parkinglot.services.ParkingLot;
import org.junit.Assert;
import org.junit.Test;

public class ParkingLotTest {

    @Test
    public void givenParkingLot_WhenDriver_ReturnsParkedCar(){
        ParkingLot parkingLot = new ParkingLot(3);
        Object car = new Object();
        Boolean isParked = parkingLot.park(car);
        Assert.assertTrue(isParked);
    }


    @Test
    public void givenDriver_WhenUnparksCar_ReturnsTrue(){
        OwnerClass ownerClass = new OwnerClass();
        ParkingLot parkingLot = new ParkingLot(3);
        parkingLot.setUser(ownerClass);
        Object car = new Object();
        parkingLot.park(car);
        boolean unparked = parkingLot.unPark(car);
        Assert.assertTrue(unparked);
    }

    @Test
    public void givenUnparkedCar_WhenToUnpark_ThrowsException(){
        try {
            OwnerClass ownerClass = new OwnerClass();
            ParkingLot parkingLot = new ParkingLot(3);
            parkingLot.setUser(ownerClass);
            Object car = new Object();
            Object car1 = new Object();
            parkingLot.park(car);
            boolean unparked = parkingLot.unPark(car1);

        }catch (ParkingLotExceptions e){
            Assert.assertEquals("No such car parked!",e.getMessage());
        }
    }


    @Test
    public void givenCarToPark_WhenLotIsFull_InformsOwner(){
        OwnerClass ownerClass = new OwnerClass();
        try {
            ParkingLot parkingLot = new ParkingLot(1);
            parkingLot.setUser(ownerClass);
            Object car = new Object();
            Object car1 = new Object();
            parkingLot.park(car);
            parkingLot.park(car1);
        }catch (ParkingLotExceptions e){
            Assert.assertEquals(OwnerClass.signs.PARKING_FULL, ownerClass.getSign());
        }
    }

    @Test
    public void givenCarToPark_WhenLotIsFull_InformsAirportSecurity(){
        AirportSecurityClass airportSecurityClass = new AirportSecurityClass();
        try {
            ParkingLot parkingLot = new ParkingLot(1);
            parkingLot.setUser(airportSecurityClass);
            Object car = new Object();
            Object car1 = new Object();
            parkingLot.park(car);
            parkingLot.park(car1);
        }catch (ParkingLotExceptions e){
            Assert.assertEquals(AirportSecurityClass.signs.PARKING_FULL,airportSecurityClass.getSign());
        }
    }

    @Test
    public void givenParkingLot_WhenVacant_InformsOwner(){
        OwnerClass ownerClass = new OwnerClass();
        ParkingLot parkingLot = new ParkingLot(1);
        parkingLot.setUser(ownerClass);
        Object car = new Object();
        parkingLot.park(car);
        parkingLot.unPark(car);
        Assert.assertEquals(OwnerClass.signs.PARKING_AVAILABLE, ownerClass.getSign());
    }

    @Test
    public void givenParkingLot_WhenUnparked_AndHasSpaceAgain_InformsOwner(){
        OwnerClass ownerClass = new OwnerClass();
        ParkingLot parkingLot = new ParkingLot(1);
        parkingLot.setUser(ownerClass);
        Object car = new Object();
        parkingLot.park(car);
        parkingLot.unPark(car);
        Assert.assertEquals(OwnerClass.signs.PARKING_AVAILABLE, ownerClass.getSign());
    }

}
