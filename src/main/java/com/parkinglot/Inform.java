package com.parkinglot;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Inform {
    ArrayList<Observers> informList = new ArrayList();

    public Inform(Observers owner) {
        informList.add(owner);
    }

    public void update(){
        for( Observers owner : informList )
            inform(owner);

    }

    private void inform(Observers owner) {
        owner.getState(Observers.signs.PARKING_FULL);
    }

}
