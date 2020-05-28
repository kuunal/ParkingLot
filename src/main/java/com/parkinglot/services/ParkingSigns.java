package com.parkinglot.services;

import com.parkinglot.model.Vehicle;

public interface ParkingSigns {
    public enum signs{PARKING_FULL,PARKING_AVAILABLE};

    public void getState(signs sign);


}
