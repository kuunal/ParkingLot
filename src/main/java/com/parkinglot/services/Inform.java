package com.parkinglot.services;

import java.util.ArrayList;

public class Inform {
    ArrayList<ParkingSigns> informList = new ArrayList();

    public Inform(ParkingSigns owner) {
        informList.add(owner);
    }

    public void update(int limit){
        for( ParkingSigns owner : informList )
            inform(owner,limit);

    }

    private void inform(ParkingSigns owner, int limit) {
        if(limit==0)
            owner.getState(ParkingSigns.signs.PARKING_FULL);
        else
            owner.getState(ParkingSigns.signs.PARKING_AVAILABLE);
    }

}
