package com.parkinglot.model;

import com.parkinglot.services.ParkingSigns;

import java.util.ArrayList;

public class AirportSecurityClass implements ParkingSigns {
    ParkingSigns.signs sign;

    @Override
    public void getState(ParkingSigns.signs sign) {
        this.sign=sign;
    }

    public ParkingSigns.signs getSign() {
        return this.sign;
    }




}
