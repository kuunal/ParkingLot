package com.parkinglot.models;

import com.parkinglot.services.ParkingSigns;

public class OwnerClass implements ParkingSigns {

    signs sign;

    @Override
    public void getState(signs sign) {
        this.sign=sign;
    }

    public signs getSign() {
        return this.sign;
    }
}
