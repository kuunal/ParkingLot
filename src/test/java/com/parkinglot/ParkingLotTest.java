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



}
