package com.parkinglot;

import com.parkinglot.exceptions.ParkingLotException;
import com.parkinglot.model.AirportSecurityClass;
import com.parkinglot.model.Owner;
import com.parkinglot.model.Vehicle;
import com.parkinglot.services.ParkingLot;
import com.parkinglot.services.ParkingLotSystem;
import com.parkinglot.services.ParkingSigns;
import com.parkinglot.services.Reservation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;

public class ParkingLotTest {
    Owner owner;

    @Before
    public void init(){
        owner = new Owner();

    }

    @Test
    public void givenVehicle_WhenToPark_ReturnsTrue() {
        ParkingLot parkingLot = new ParkingLot();
        Vehicle vehicle = new Vehicle();
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
            Vehicle car = new Vehicle();
            Vehicle car1 = new Vehicle();
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
        Vehicle vehicle = new Vehicle();
        parkingLot.park(vehicle);
        parkingLot.unPark(vehicle);
        boolean isUnparked = parkingLot.isUnparked(vehicle);
        Assert.assertTrue(isUnparked);
    }

    @Test
    public void givenUnparkedCar_WhenToUnpark_ThrowsException() {
        try {
            ParkingLot parkingLot = new ParkingLot();
            Vehicle car = new Vehicle();
            Vehicle car1 = new Vehicle();
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
            Vehicle car = new Vehicle();
            Vehicle car1 = new Vehicle();
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
            Vehicle car = new Vehicle();
            Vehicle car1 = new Vehicle();
            parkingLot.park(car);
            parkingLot.park(car1);
        } catch (Exception e) {
            Assert.assertEquals(AirportSecurityClass.signs.PARKING_FULL, airportSecurityClass.getSign());
        }
    }

    @Test
    public void givenParkingLot_WhenUnparked_AndHasSpaceAgain_InformsOwner() {
        Owner owner = new Owner();
        Vehicle car = new Vehicle();
        Vehicle car2 = new Vehicle();
        ParkingLot parkingLot = new ParkingLot();
        try {
            parkingLot.setUser(owner);
            parkingLot.setCapacity(1);
            parkingLot.park(car);
            parkingLot.park(car2);
        } catch (ParkingLotException e) {
            Assert.assertEquals(Owner.signs.PARKING_FULL, owner.getSign());
            parkingLot.unPark(car2);
            Assert.assertEquals(ParkingSigns.signs.PARKING_AVAILABLE, owner.getSign());
        }
    }

    @Test
    public void givenNumberOfLots_WhenLoaded_ReturnsTrue() {
        ParkingLotSystem parkingLotSystem = new ParkingLotSystem();
        int givenNoOfLots = 3;
        parkingLotSystem.setNumberOfLots(givenNoOfLots, owner,5);
        int noOfLots = parkingLotSystem.getNumberOfLots();
        Assert.assertTrue(givenNoOfLots == noOfLots);
    }

    @Test
    public void givenVehicle_WhenToPark_ShouldParkInLot_WithMostEmptySlots() {
        ParkingLotSystem parkingLotSystem = new ParkingLotSystem();
        ParkingLot[] parkingLot = {
                new ParkingLot(44 ,owner),
                new ParkingLot(8,owner),
                new ParkingLot(7,owner)
        };
        Vehicle vehicle = new Vehicle();
        Vehicle vehicle1 = new Vehicle();
        parkingLotSystem.setNumberOfLots(parkingLot);
        parkingLotSystem.park(vehicle);
        parkingLotSystem.park(vehicle1);
        boolean isParked = parkingLotSystem.isParked(vehicle1);
        boolean isParked1 = parkingLotSystem.isParked(vehicle);
        Assert.assertTrue(isParked && isParked1);
    }

    @Test
    public void givenSameVehicle_WhenToPark_InDifferentLot_ThrowsException() {
        try {
            ParkingLotSystem parkingLotSystem = new ParkingLotSystem();
            ParkingLot parkingLot1, parkingLot2;
            ParkingLot[] parkingLot = {
                    parkingLot1 = new ParkingLot(4,owner),
                    parkingLot2 = new ParkingLot(4,owner)
            };
            Vehicle vehicle = new Vehicle();
            parkingLotSystem.setNumberOfLots(parkingLot);
            parkingLotSystem.park(vehicle);
            parkingLotSystem.park(vehicle);
        } catch (ParkingLotException e) {
            Assert.assertEquals("Vehicle already parked!", e.getMessage());
        }
    }

    @Test
    public void givenParkedVehicleToUnPark_WhenInParkingManager_ReturnsTrue() {
        ParkingLotSystem parkingLotSystem = new ParkingLotSystem();
        parkingLotSystem.setNumberOfLots(3,owner);
        Vehicle vehicle = new Vehicle();
        Vehicle vehicle1 = new Vehicle();
        parkingLotSystem.park(vehicle);
        parkingLotSystem.park(vehicle1);
        parkingLotSystem.unPark(vehicle);
        boolean isUnparked = parkingLotSystem.isUnparked(vehicle);
        Assert.assertTrue(isUnparked);
    }

    @Test
    public void givenVehicle_WhenUnparked_CreatesAVacantSpace_ForOtherVehicles() {
        ParkingLotSystem parkingLotSystem = new ParkingLotSystem();
        ParkingLot parkingLot0;
        ParkingLot[] parkingLot = {
                parkingLot0 = new ParkingLot(44 ,owner)
        };
        Vehicle vehicle = new Vehicle();
        Vehicle vehicle2 = new Vehicle();
        Vehicle vehicle3 = new Vehicle();
        parkingLotSystem.setNumberOfLots(parkingLot);
        parkingLotSystem.park(vehicle);
        parkingLotSystem.park(vehicle2);
        parkingLotSystem.unPark(vehicle);
        parkingLotSystem.park(vehicle3);
        String getLargeVehicleLocation = parkingLotSystem.getVehicleLocation(vehicle3);
        Assert.assertEquals( "Parking Lot: 0 Position: 0",getLargeVehicleLocation);
    }

    @Test
    public void givenParkedVehicle_WhenQueriedToFind_ReturnsLotAndPosition(){
        ParkingLotSystem parkingLotSystem = new ParkingLotSystem();
        ParkingLot[] parkingLot = {
              new ParkingLot(4,owner),
              new ParkingLot(24,owner)
        };
        parkingLotSystem.setNumberOfLots(parkingLot);
        Vehicle vehicle = new Vehicle();
        Vehicle vehicle1 = new Vehicle();
        parkingLotSystem.park(vehicle);
        parkingLotSystem.park(vehicle1);
        String vehicleLocation = parkingLotSystem.getVehicleLocation(vehicle1);
        Assert.assertEquals("Parking Lot: 1 Position: 1",vehicleLocation);
    }


    @Test
    public void givenUnParkedVehicle_WhenToFindLocation_ThrowsException(){
        try{
            ParkingLotSystem parkingLotSystem = new ParkingLotSystem();
            ParkingLot[] parkingLot = {
                    new ParkingLot(4,owner),
                    new ParkingLot(24,owner)
            };
            parkingLotSystem.setNumberOfLots(parkingLot);
            Vehicle vehicle = new Vehicle();
            Vehicle vehicle1 = new Vehicle();
            Vehicle vehicle3 = new Vehicle();
            parkingLotSystem.park(vehicle);
            parkingLotSystem.park(vehicle1);
            String vehicleLocation = parkingLotSystem.getVehicleLocation(vehicle3);
        }catch (ParkingLotException e){
            Assert.assertEquals("No such car parked",e.getMessage());
        }
    }

    @Test
    public void givenVehicle_WhenToPark_ShouldReturnTime() {
        ParkingLotSystem parkingLotSystem = new ParkingLotSystem();
        ParkingLot[] parkingLot = {
                new ParkingLot(44 ,owner),
                new ParkingLot(8,owner),
                new ParkingLot(7,owner)
        };
        Vehicle vehicle = new Vehicle();
        parkingLotSystem.setNumberOfLots(parkingLot);
        parkingLotSystem.park(vehicle);
        String time = parkingLotSystem.getTime(vehicle);
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String expectedTime = localDateTime.format(dateTimeFormatter);
        Assert.assertEquals(expectedTime,time);
    }


    @Test
    public void givenVehicles_WhenToPark_ShouldParkEvenly(){
        ParkingLotSystem parkingLotSystem = new ParkingLotSystem();
        ParkingLot parkingLot1,parkingLot2;
        ParkingLot[] parkingLot = {
                parkingLot1 = new ParkingLot(4,owner),
                parkingLot2 = new ParkingLot(4,owner)
        };
        Vehicle vehicle = new Vehicle();
        Vehicle vehicle1 = new Vehicle();
        Vehicle vehicle2 = new Vehicle();
        Vehicle vehicle3 = new Vehicle();
        parkingLotSystem.setNumberOfLots(parkingLot);
        parkingLotSystem.park(vehicle1);
        parkingLotSystem.park(vehicle2);
        parkingLotSystem.park(vehicle3);
        parkingLotSystem.park(vehicle);
        Assert.assertEquals("2   2",parkingLot1.getEmptySlots()+"   "+parkingLot2.getEmptySlots());
    }

    @Test
    public void givenVehicleOfHandicapped_WhenToParkInReservation_ReturnsTrue(){
        ParkingLotSystem parkingLotSystem = new ParkingLotSystem();
        ParkingLot parkingLot0,parkingLot1;
        ParkingLot[] parkingLot = {
                parkingLot0 = new ParkingLot(10,owner,4),
                parkingLot1 = new ParkingLot(10,owner,3)
        };
        Vehicle vehicle2 = new Vehicle();
        Vehicle vehicle1 = new Vehicle();
        parkingLotSystem.setNumberOfLots(parkingLot);
        parkingLotSystem.park(vehicle1,Reservation.HANDICAP);
        parkingLotSystem.park(vehicle2,Reservation.HANDICAP);
        String firstVehilceLocation = parkingLotSystem.getVehicleLocation(vehicle1);
        String secondVehilceLocation = parkingLotSystem.getVehicleLocation(vehicle2);
        Assert.assertEquals("Parking Lot: 0 Position: 0"+"  "+"Parking Lot: 0 Position: 1",
                firstVehilceLocation+"  "+secondVehilceLocation);

    }


    @Test
    public void givenHandicap_WhenToParkAtDifferentLot_ShouldNotBeKnownToOtherLots(){
        ParkingLotSystem parkingLotSystem = new ParkingLotSystem();
        ParkingLot parkingLot1,parkingLot2;
        ParkingLot[] parkingLot = {
                parkingLot1 = new ParkingLot(10,owner,4),
                parkingLot2 = new ParkingLot(10,owner,3)
        };
        Vehicle vehicle2 = new Vehicle();
        Vehicle vehicle1 = new Vehicle();
        parkingLotSystem.setNumberOfLots(parkingLot);
        parkingLotSystem.park(vehicle1,Reservation.HANDICAP);
        parkingLotSystem.park(vehicle2,Reservation.HANDICAP);
        Assert.assertFalse(parkingLot2.isParked(vehicle2)&&parkingLot2.isParked(vehicle1));
    }

    @Test
    public void givenVehicleOfHandicap_WhenOneLotIsFull_ParksInOtherLot(){
        ParkingLot parkingLot1,parkingLot2 = null;
        Vehicle vehicle2 = new Vehicle();
        Vehicle vehicle1 = new Vehicle();
        ParkingLotSystem parkingLotSystem = new ParkingLotSystem();
        ParkingLot[] parkingLot = {
                parkingLot1 = new ParkingLot(10,owner,0),
                parkingLot2 = new ParkingLot(10,owner,3)
        };
        parkingLotSystem.setNumberOfLots(parkingLot);
        parkingLotSystem.park(vehicle1,Reservation.HANDICAP);
        parkingLotSystem.park(vehicle2,Reservation.HANDICAP);
        Assert.assertTrue(!parkingLot1.isParked(vehicle1) && !parkingLot1.isParked(vehicle2) &&
                parkingLot2.isParked(vehicle1) && parkingLot2.isParked(vehicle2));

    }

    @Test
    public void givenMultipleHandicap_WhenOneLotIsFull_ShouldRedirectToAnother(){
        ParkingLot parkingLot1,parkingLot2 = null;
        Vehicle vehicle2 = new Vehicle();
        Vehicle vehicle1 = new Vehicle();
        ParkingLotSystem parkingLotSystem = new ParkingLotSystem();
        ParkingLot[] parkingLot = {
                parkingLot1 = new ParkingLot(10,owner,1),
                parkingLot2 = new ParkingLot(10,owner,3)
        };
        parkingLotSystem.setNumberOfLots(parkingLot);
        parkingLotSystem.park(vehicle1,Reservation.HANDICAP);
        parkingLotSystem.park(vehicle2,Reservation.HANDICAP);
        Assert.assertTrue(parkingLot1.isParked(vehicle1) && parkingLot2.isParked(vehicle2) &&
                !parkingLot2.isParked(vehicle1) && !parkingLot1.isParked(vehicle2) );

    }

    @Test
    public void givenHandicapped_WhenUnParked_ReturnsTrue(){
        ParkingLot parkingLot1,parkingLot2 = null;
        Vehicle vehicle2 = new Vehicle();
        Vehicle vehicle1 = new Vehicle();
        ParkingLotSystem parkingLotSystem = new ParkingLotSystem();

            ParkingLot[] parkingLot = {
                    parkingLot1 = new ParkingLot(10,owner,3),
                    parkingLot2 = new ParkingLot(10,owner,3)
            };
            parkingLotSystem.setNumberOfLots(parkingLot);
            parkingLotSystem.park(vehicle1,Reservation.HANDICAP);
            parkingLotSystem.park(vehicle2,Reservation.HANDICAP);
            parkingLotSystem.unPark(vehicle2);
            Assert.assertTrue(parkingLotSystem.isUnparked(vehicle2));
    }

    @Test
    public void givenLargeVehicle_WhenToPark_ShouldParkInLotWithMostFreeSpace() {
        ParkingLotSystem parkingLotSystem = new ParkingLotSystem();
        ParkingLot parkingLot0,parkingLot1,parkingLot2;
        ParkingLot[] parkingLot = {
                parkingLot0 = new ParkingLot(44 ,owner),
                parkingLot1 = new ParkingLot(79,owner),
                parkingLot2 = new ParkingLot(13,owner)
        };
        Vehicle vehicle = new Vehicle();
        parkingLotSystem.setNumberOfLots(parkingLot);
        parkingLotSystem.park(vehicle);
        String getLargeVehicleLocation = parkingLotSystem.getVehicleLocation(vehicle);
        Assert.assertEquals( "Parking Lot: 1 Position: 0",getLargeVehicleLocation);
    }

    @Test
    public void givenLargeVehicle_WhenToPark_ShouldParkInLotWithMostFreeSpace_WithConsideringReservation() {
        ParkingLotSystem parkingLotSystem = new ParkingLotSystem();
        ParkingLot parkingLot0,parkingLot1,parkingLot2;
        ParkingLot[] parkingLot = {
                parkingLot0 = new ParkingLot(44 ,owner,3),
                parkingLot1 = new ParkingLot(79,owner,55),
                parkingLot2 = new ParkingLot(13,owner)
        };
        Vehicle vehicle = new Vehicle();
        parkingLotSystem.setNumberOfLots(parkingLot);
        parkingLotSystem.park(vehicle);
        String getLargeVehicleLocation = parkingLotSystem.getVehicleLocation(vehicle);
        Assert.assertEquals( "Parking Lot: 0 Position: 3",getLargeVehicleLocation);
    }

    @Test
    public void givenParkingSystem_WhenModifiedHandicapSlot_ShouldWorkFine() {
        ParkingLotSystem parkingLotSystem = new ParkingLotSystem();
        ParkingLot parkingLot0,parkingLot1,parkingLot2;
        ParkingLot[] parkingLot = {
                parkingLot0 = new ParkingLot(44 ,owner,3),
                parkingLot1 = new ParkingLot(79,owner,4),
                parkingLot2 = new ParkingLot(13,owner)
        };
        Vehicle vehicle = new Vehicle();
        parkingLotSystem.setNumberOfLots(parkingLot);
        parkingLotSystem.updateHandicapReservation(1,8);
        parkingLotSystem.park(vehicle);
        String getVehicleLocation = parkingLotSystem.getVehicleLocation(vehicle);
        Assert.assertEquals( "Parking Lot: 1 Position: 8",getVehicleLocation);
    }

    @Test
    public void givenParkingSystem_WhenParkedAndModifiedHandicapSlot_ShouldWorkFine() {
        ParkingLotSystem parkingLotSystem = new ParkingLotSystem();
        ParkingLot parkingLot0,parkingLot1,parkingLot2;
        ParkingLot[] parkingLot = {
                parkingLot0 = new ParkingLot(44 ,owner,0),
                parkingLot1 = new ParkingLot(79,owner,0),
                parkingLot2 = new ParkingLot(13,owner)
        };
        Vehicle vehicle = new Vehicle();
        parkingLotSystem.setNumberOfLots(parkingLot);
        parkingLotSystem.park(vehicle);
        parkingLotSystem.updateHandicapReservation(1,8);
        parkingLotSystem.unPark(vehicle);
        parkingLotSystem.park(vehicle, Reservation.HANDICAP);
        String getHandicapVehicleLocation = parkingLotSystem.getVehicleLocation(vehicle);
        Assert.assertEquals( "Parking Lot: 1 Position: 0",getHandicapVehicleLocation);
    }

    @Test
    public void givenVehicleColor_WhenFound_ReturnsLocation(){
        ParkingLotSystem parkingLotSystem = new ParkingLotSystem();
        Vehicle vehicle = new Vehicle("Black","MH-1111","Toyota");
        Vehicle vehicle2 = new Vehicle("White","MH-2222","Mercedes");
        Vehicle vehicle3 = new Vehicle("White","MH-4444","Toyota");
        Vehicle vehicle4 = new Vehicle("White","MH-3333","BMW");
        Vehicle vehicle5 = new Vehicle("White","MH-9999","Audi");
        parkingLotSystem.setNumberOfLots(3,owner,5);
        parkingLotSystem.park(vehicle);
        parkingLotSystem.park(vehicle2);
        parkingLotSystem.park(vehicle3);
        parkingLotSystem.park(vehicle4);
        parkingLotSystem.park(vehicle5);
        List vehicleByColorList = parkingLotSystem.getLocationByColor("white");
        ArrayList expectedList = new ArrayList();
        expectedList.add("0 [1]");
        expectedList.add("1 [0, 1]");
        expectedList.add("2 [0]");
        Assert.assertEquals(expectedList,vehicleByColorList);
    }

    @Test
    public void givenVehicle_WhenForQuery_ShouldReturnDetailsAboutVehicle(){
        ParkingLotSystem parkingLotSystem = new ParkingLotSystem();
        Vehicle vehicle = new Vehicle("White","MH-1111","Toyota");
        Vehicle vehicle2 = new Vehicle("Black","MH-2222","Mercedes");
        Vehicle vehicle3 = new Vehicle("Blue","MH-4444","Toyota");
        Vehicle vehicle4 = new Vehicle("Black","MH-3333","BMW");
        parkingLotSystem.setNumberOfLots(3,owner,5);
        parkingLotSystem.park(vehicle);
        parkingLotSystem.park(vehicle2);
        parkingLotSystem.park(vehicle3);
        parkingLotSystem.park(vehicle4);
        HashMap<Integer,List<Vehicle>> expectedList = parkingLotSystem.getVehicleInformation("blue","toyota");
        Assert.assertEquals("Toyota",expectedList.get(2).get(0).getBrand());
    }

    @Test
    public void givenUnparkedVehicle_WhenForQuery_ThrowsException(){
        ParkingLotSystem parkingLotSystem = new ParkingLotSystem();
        try {
            Vehicle vehicle = new Vehicle("White","MH-1111","Toyota");
            Vehicle vehicle2 = new Vehicle("Black","MH-2222","Mercedes");
            Vehicle vehicle3 = new Vehicle("Blue","MH-4444","Toyota");
            Vehicle vehicle4 = new Vehicle("Black","MH-3333","BMW");
            parkingLotSystem.setNumberOfLots(3,owner,5);
            parkingLotSystem.park(vehicle);
            parkingLotSystem.park(vehicle2);
            parkingLotSystem.park(vehicle3);
            parkingLotSystem.park(vehicle4);
            HashMap<Integer,List<Vehicle>> expectedList = parkingLotSystem.getVehicleInformation("blue","Lambo");
        }catch (ParkingLotException e) {
            Assert.assertEquals("No such car parked!", e.getMessage());
        }
    }

    @Test
    public void givenVehicleBrand_WhenFound_ReturnsLocation(){
        ParkingLotSystem parkingLotSystem = new ParkingLotSystem();
        Vehicle vehicle = new Vehicle("White","MH-1111","Toyota");
        Vehicle vehicle2 = new Vehicle("Black","MH-2222","Mercedes");
        Vehicle vehicle3 = new Vehicle("Blue","MH-4444","Toyota");
        Vehicle vehicle4 = new Vehicle("Black","MH-3333","BMW");
        parkingLotSystem.setNumberOfLots(3,owner,5);
        parkingLotSystem.park(vehicle);
        parkingLotSystem.park(vehicle2);
        parkingLotSystem.park(vehicle3);
        parkingLotSystem.park(vehicle4);
        List vehicleByBrandNameList = parkingLotSystem.getLocationByBrand("BMW");
        Assert.assertEquals("0 [1]",vehicleByBrandNameList.get(0));
    }


    @Test
    public void givenVehicle_WhenParkedBetweenGivenTime_ReturnsVehicle() throws InterruptedException {
        ParkingLotSystem parkingLotSystem = new ParkingLotSystem();
        Vehicle vehicle = new Vehicle("White","MH-1111","Toyota");
        parkingLotSystem.setNumberOfLots(3,owner);
        parkingLotSystem.park(vehicle);
        ArrayList<Vehicle> expectedList =  new ArrayList<>();
        expectedList.add(vehicle);
        List vehicleByBrandNameList = parkingLotSystem.getVehicleByTime(30);
        Assert.assertEquals(vehicle,vehicleByBrandNameList.get(0));
    }

    @Test
    public void givenRow_WhenToFindVehicle_ShouldReturnVehicle(){
        ParkingLotSystem parkingLotSystem = new ParkingLotSystem();
        parkingLotSystem.setNumberOfLots(2,owner,5);
        parkingLotSystem.setRows('D');
        IntStream.range(0,10)
                .forEach(e->parkingLotSystem
                        .park(new Vehicle("black",String.valueOf(e),"BMW")) );
        ArrayList<Vehicle> arrayList = parkingLotSystem.getVehicleByRow('B');
        arrayList.addAll(parkingLotSystem.getVehicleByRow('D'));
        ArrayList<String > expectedList = new ArrayList<>();
        expectedList.add("2");
        expectedList.add("9");
        Assert.assertTrue(expectedList.get(0).equals(arrayList.get(0).getNumberPlate()) &&
                        expectedList.get(1).equals(arrayList.get(arrayList.size()-1).getNumberPlate()));
    }

    @Test
    public void givenRowToFindVehicle_WhenMoreThanCapacity_ThrowsException(){
        try {
            ParkingLotSystem parkingLotSystem = new ParkingLotSystem();
            parkingLotSystem.setNumberOfLots(2,owner,5);
            parkingLotSystem.setRows('Z');
        }catch (ParkingLotException e){
            Assert.assertEquals("Cant assign that much row for few slots!",e.getMessage());
        }
    }

    @Test
    public void givenParkedVehicle_WhenToFind_ReturnsAllInformation_AboutVehicles(){
        ParkingLotSystem parkingLotSystem = new ParkingLotSystem();
        parkingLotSystem.setNumberOfLots(2,owner,5);
        IntStream.range(0,10)
                .forEach(e->parkingLotSystem
                        .park(new Vehicle("black",String.valueOf(e),"BMW")) );
        HashMap<Integer,List<Vehicle>> map = parkingLotSystem.getAllVehicles();
        Assert.assertEquals(10,map.values()
                                                .stream()
                                                .mapToInt(List::size)
                                                .sum());
    }
}
