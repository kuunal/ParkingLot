package com.parkinglot;

public interface ParkingSigns {
    public enum signs{PARKING_FULL,PARKING_AVAILABLE};

    public void getState(signs sign);
}
