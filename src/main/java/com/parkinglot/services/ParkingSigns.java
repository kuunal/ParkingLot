package com.parkinglot.services;

public interface ParkingSigns {
    public enum signs{PARKING_FULL,PARKING_AVAILABLE};

    public void getState(signs sign);

    public void setTimeAndPayment(Object vehicle, int... charge);

    public String getTime(Object vehicle);

}
