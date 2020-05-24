package com.parkinglot;

public interface Observers {
    public enum signs{PARKING_FULL};

    public void getState(signs sign);
}
