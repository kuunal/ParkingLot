package com.parkinglot.model;

import com.parkinglot.services.ParkingSigns;

import java.util.ArrayList;

public class Owner implements ParkingSigns {
    signs sign;

    @Override
    public void getState(signs sign) {
        this.sign=sign;
    }

    public signs getSign() {
        return this.sign;
    }



}
