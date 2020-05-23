package com.parkinglot;

import org.junit.Assert;
import org.junit.Before;
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
        ParkingLot parkingLot = new ParkingLot(3);
        Object car = new Object();
        parkingLot.park(car);
        boolean unparked = parkingLot.unPark(car);
        Assert.assertTrue(unparked);
    }

    @Test
    public void givenUnparkedCar_WhenToUnpark_ReturnsFalse(){
        ParkingLot parkingLot = new ParkingLot(3);
        Object car = new Object();
        Object car1 = new Object();
        parkingLot.park(car);
        boolean unparked = parkingLot.unPark(car1);
        Assert.assertFalse(unparked);
    }


    @Test
    public void givenCarToPark_WhenLotIsFull_InformsOwner(){
        try {
            ParkingLot parkingLot = new ParkingLot(1);
            Object[] car = {
                    new Object(),
                    new Object(),
                    new Object(),
                    new Object()
            };
            parkingLot.park(car);
        }catch (Exception e){
            Assert.assertEquals(Owner.signs.PARKING_FULL,new Owner().getSign());
        }
    }

    @Test
    public void givenCarToPark_WhenLotIsFull_ReturnsException(){
        try {
            ParkingLot parkingLot = new ParkingLot(1);
            Object[] car = {
                    new Object(),
                    new Object(),
                    new Object(),
                    new Object()
            };
            parkingLot.park(car);
        }catch (Exception e){
            Assert.assertEquals("Lot full! Cannot park 3 cars",e.getMessage());
        }
    }



}
