package com.parkinglot;

public class AirportSecurityClass implements Observers{

    signs sign;

    @Override
    public void getState(signs sign) {
        this.sign=sign;
    }

    public signs getSign() {
        return sign;
    }

}

