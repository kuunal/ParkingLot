package com.parkinglot;

public class Owner implements Observers{

    signs sign;

    @Override
    public void getState(signs sign) {
        this.sign=sign;
    }

    public signs getSign() {
        return this.sign;
    }
}
