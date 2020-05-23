package com.parkinglot;

public class Owner extends RuntimeException{
    public enum signs{PARKING_FULL,PARKING_AVAILABLE}

    private static signs sign;

    public void setSign(signs sign) {
        this.sign = sign;
    }

    public signs getSign() {
        return this.sign;
    }
}
