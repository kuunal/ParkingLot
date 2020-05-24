package com.parkinglot;

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
