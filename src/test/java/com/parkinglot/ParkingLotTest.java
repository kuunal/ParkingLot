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
            ParkingLot parkingLot1, parkingLot2;
            ParkingLot[] parkingLot = {
                    parkingLot1 = new ParkingLot(4,owner),
                    parkingLot2 = new ParkingLot(4,owner)
            };
            Object vehicle = new Object();
            parkingManager.setNumberOfLots(parkingLot);
            parkingManager.park(vehicle);
            parkingManager.park(vehicle);
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
    public void givenVehicle_WhenUnparked_CreatesAVacantSpace_ForOtherVehicles() {
        ParkingManager parkingManager = new ParkingManager();
        ParkingLot parkingLot0;
        ParkingLot[] parkingLot = {
                parkingLot0 = new ParkingLot(44 ,owner)
        };
        Object vehicle = new Object();
        Object vehicle2 = new Object();
        Object vehicle3 = new Object();
        parkingManager.setNumberOfLots(parkingLot);
        parkingManager.park(vehicle);
        parkingManager.park(vehicle2);
        parkingManager.unPark(vehicle);
        parkingManager.park(vehicle3);
        String getLargeVehicleLocation = parkingManager.getVehicleLocation(vehicle3);
        Assert.assertEquals( "Parking Lot: 0 Position: 0",getLargeVehicleLocation);
    }

    @Test
    public void givenParkedVehicle_WhenQueriedToFind_ReturnsLotAndPosition(){
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

    @Test
    public void givenVehicles_WhenToPark_ShouldParkEvenly(){
        ParkingManager parkingManager = new ParkingManager();
        ParkingLot parkingLot1,parkingLot2;
        ParkingLot[] parkingLot = {
                parkingLot1 = new ParkingLot(4,owner),
                parkingLot2 = new ParkingLot(4,owner)
        };
        Object vehicle = new Object();
        Object vehicle1 = new Object();
        Object vehicle2 = new Object();
        Object vehicle3 = new Object();
        parkingManager.setNumberOfLots(parkingLot);
        parkingManager.park(vehicle1);
        parkingManager.park(vehicle2);
        parkingManager.park(vehicle3);
        parkingManager.park(vehicle);
        Assert.assertEquals("2   2",parkingLot1.getEmptySlots()+"   "+parkingLot2.getEmptySlots());
    }

    @Test
    public void givenVehicleOfHandicapped_WhenToParkInReservation_ReturnsTrue(){
        ParkingManager parkingManager = new ParkingManager();
        ParkingLot parkingLot0,parkingLot1;
        ParkingLot[] parkingLot = {
                parkingLot0 = new ParkingLot(10,owner,4),
                parkingLot1 = new ParkingLot(10,owner,3)
        };
        Object vehicle2 = new Object();
        Object vehicle1 = new Object();
        parkingManager.setNumberOfLots(parkingLot);
        parkingManager.park(vehicle1,true);
        parkingManager.park(vehicle2,true);
        String firstVehilceLocation = parkingManager.getVehicleLocation(vehicle1);
        String secondVehilceLocation = parkingManager.getVehicleLocation(vehicle2);
        Assert.assertEquals("Parking Lot: 0 Position: 0"+"  "+"Parking Lot: 0 Position: 1",
                firstVehilceLocation+"  "+secondVehilceLocation);

    }


    @Test
    public void givenHandicap_WhenToParkAtDifferentLot_ShouldNotBeKnownToOtherLots(){
        ParkingManager parkingManager = new ParkingManager();
        ParkingLot parkingLot1,parkingLot2;
        ParkingLot[] parkingLot = {
                parkingLot1 = new ParkingLot(10,owner,4),
                parkingLot2 = new ParkingLot(10,owner,3)
        };
        Object vehicle2 = new Object();
        Object vehicle1 = new Object();
        parkingManager.setNumberOfLots(parkingLot);
        parkingManager.park(vehicle1,true);
        parkingManager.park(vehicle2,true);
        Assert.assertFalse(parkingLot2.isParked(vehicle2)&&parkingLot2.isParked(vehicle1));
    }

    @Test
    public void givenVehicleOfHandicapped_WhenToParkInDifferentSlot_ReturnsTrue(){
        ParkingLot parkingLot1,parkingLot2 = null;
        Object vehicle2 = new Object();
        Object vehicle1 = new Object();
        ParkingManager parkingManager = new ParkingManager();
        ParkingLot[] parkingLot = {
                parkingLot1 = new ParkingLot(10,owner,0),
                parkingLot2 = new ParkingLot(10,owner,3)
        };
        parkingManager.setNumberOfLots(parkingLot);
        parkingManager.park(vehicle1,true);
        parkingManager.park(vehicle2,true);
        Assert.assertTrue(!parkingLot1.isParked(vehicle1) && !parkingLot1.isParked(vehicle2) &&
                parkingLot2.isParked(vehicle1) && parkingLot2.isParked(vehicle2));

    }

    @Test
    public void givenMultipleHandicap_WhenOneLotIsFull_ShouldRedirectToAnother(){
        ParkingLot parkingLot1,parkingLot2 = null;
        Object vehicle2 = new Object();
        Object vehicle1 = new Object();
        ParkingManager parkingManager = new ParkingManager();
        ParkingLot[] parkingLot = {
                parkingLot1 = new ParkingLot(10,owner,1),
                parkingLot2 = new ParkingLot(10,owner,3)
        };
        parkingManager.setNumberOfLots(parkingLot);
        parkingManager.park(vehicle1,true);
        parkingManager.park(vehicle2,true);
        Assert.assertTrue(parkingLot1.isParked(vehicle1) && parkingLot2.isParked(vehicle2) &&
                !parkingLot2.isParked(vehicle1) && !parkingLot1.isParked(vehicle2) );

    }

    @Test
    public void givenHandicapped_WhenUnParked_ReturnsTrue(){
        ParkingLot parkingLot1,parkingLot2 = null;
        Object vehicle2 = new Object();
        Object vehicle1 = new Object();
        ParkingManager parkingManager = new ParkingManager();

            ParkingLot[] parkingLot = {
                    parkingLot1 = new ParkingLot(10,owner,3),
                    parkingLot2 = new ParkingLot(10,owner,3)
            };
            parkingManager.setNumberOfLots(parkingLot);
            parkingManager.park(vehicle1,true);
            parkingManager.park(vehicle2,true);
            parkingManager.unPark(vehicle2);
            Assert.assertTrue(parkingManager.isUnparked(vehicle2));
    }

    @Test
    public void givenLargeVehicle_WhenToPark_ShouldParkInLotWithMostFreeSpace() {
        ParkingManager parkingManager = new ParkingManager();
        ParkingLot parkingLot0,parkingLot1,parkingLot2;
        ParkingLot[] parkingLot = {
                parkingLot0 = new ParkingLot(44 ,owner),
                parkingLot1 = new ParkingLot(79,owner),
                parkingLot2 = new ParkingLot(13,owner)
        };
        Object vehicle = new Object();
        parkingManager.setNumberOfLots(parkingLot);
        parkingManager.park(vehicle);
        String getLargeVehicleLocation = parkingManager.getVehicleLocation(vehicle);
        Assert.assertEquals( "Parking Lot: 1 Position: 0",getLargeVehicleLocation);
    }

    @Test
    public void givenLargeVehicle_WhenToPark_ShouldParkInLotWithMostFreeSpace_WithoutaffectingReservation() {
        ParkingManager parkingManager = new ParkingManager();
        ParkingLot parkingLot0,parkingLot1,parkingLot2;
        ParkingLot[] parkingLot = {
                parkingLot0 = new ParkingLot(44 ,owner,3),
                parkingLot1 = new ParkingLot(79,owner,55),
                parkingLot2 = new ParkingLot(13,owner)
        };
        Object vehicle = new Object();
        parkingManager.setNumberOfLots(parkingLot);
        parkingManager.park(vehicle);
        String getLargeVehicleLocation = parkingManager.getVehicleLocation(vehicle);
        Assert.assertEquals( "Parking Lot: 0 Position: 3",getLargeVehicleLocation);
    }

    @Test
    public void givenParkingSystem_WhenModifiedHandicapSlot_ShouldWorkFine() {
        ParkingManager parkingManager = new ParkingManager();
        ParkingLot parkingLot0,parkingLot1,parkingLot2;
        ParkingLot[] parkingLot = {
                parkingLot0 = new ParkingLot(44 ,owner,3),
                parkingLot1 = new ParkingLot(79,owner,4),
                parkingLot2 = new ParkingLot(13,owner)
        };
        Object vehicle = new Object();
        parkingManager.setNumberOfLots(parkingLot);
        parkingManager.updateHandicapReservation(1,8);
        parkingManager.park(vehicle);
        String getVehicleLocation = parkingManager.getVehicleLocation(vehicle);
        Assert.assertEquals( "Parking Lot: 1 Position: 8",getVehicleLocation);
    }

    @Test
    public void givenParkingSystem_WhenParkedAndModifiedHandicapSlot_ShouldWorkFine() {
        ParkingManager parkingManager = new ParkingManager();
        ParkingLot parkingLot0,parkingLot1,parkingLot2;
        ParkingLot[] parkingLot = {
                parkingLot0 = new ParkingLot(44 ,owner,0),
                parkingLot1 = new ParkingLot(79,owner,0),
                parkingLot2 = new ParkingLot(13,owner)
        };
        Object vehicle = new Object();
        parkingManager.setNumberOfLots(parkingLot);
        parkingManager.park(vehicle);
        parkingManager.updateHandicapReservation(1,8);
        parkingManager.unPark(vehicle);
        parkingManager.park(vehicle,true);
        String getHandicapVehicleLocation = parkingManager.getVehicleLocation(vehicle);
        Assert.assertEquals( "Parking Lot: 1 Position: 0",getHandicapVehicleLocation);
    }

}
