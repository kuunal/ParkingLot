package com.parkinglot;

import java.util.ArrayList;

public class Inform {
    ArrayList<ParkingSigns> informList = new ArrayList();

    public Inform(ParkingSigns observers) {
        informList.add(observers);
    }

    public void update(int slots){
        for( ParkingSigns observer : informList )
            inform(observer,slots);

    }

    private void inform(ParkingSigns observer, int slots) {
        if(slots==0)
            observer.getState(ParkingSigns.signs.PARKING_FULL);
        else
            observer.getState(ParkingSigns.signs.PARKING_AVAILABLE);
    }

}
