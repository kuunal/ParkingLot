package com.parkinglot;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ParkingLotTest {

    @Test
    public void givenParkingLot_WhenDriver_ReturnsParkedCar(){
        ParkingLot parkingLot = new ParkingLot();
        Object car = new Object();
        Boolean isParked = parkingLot.park(car);
        Assert.assertTrue(isParked);
    }

    @Test
    public void givenDriver_WhenUnparksCar_ReturnsTrue(){
        ParkingLot parkingLot = new ParkingLot();
        Object car = new Object();
        parkingLot.park(car);
        boolean unparked = parkingLot.unPark(car);
        Assert.assertTrue(unparked);
    }

    @Test
    public void givenUnparkedCar_WhenToUnpark_ReturnsFalse(){
        ParkingLot parkingLot = new ParkingLot();
        Object car = new Object();
        Object car1 = new Object();
        parkingLot.park(car);
        boolean unparked = parkingLot.unPark(car1);
        Assert.assertFalse(unparked);
    }





}
