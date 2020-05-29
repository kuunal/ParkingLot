/*******************************************************************
 * @Purpose : To manage all the parking lots and retrieve information from them.
 * @Author : Kunal Deshmukh
 * @Date : 28-05-2020
 * *****************************************************************/

package com.parkinglot.services;

import java.util.ArrayList;

public class Inform {
    ArrayList<ParkingSigns> informList = new ArrayList();

    public Inform(ParkingSigns observers) {
        informList.add(observers);
    }

    /**+
     * @purpose: To update all owners
     * @param slots
     */
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
