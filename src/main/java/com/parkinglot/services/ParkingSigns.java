/*******************************************************************
 * @Purpose : Provide status of parking lot so that owner can put on parking sign.
 * @Author : Kunal Deshmukh
 * @Date : 28-05-2020
 * *****************************************************************/

package com.parkinglot.services;

import com.parkinglot.model.Vehicle;

public interface ParkingSigns {
    public enum signs{PARKING_FULL,PARKING_AVAILABLE};

    /**
     * @purpose Informs owner the state of lot using enums.
     * @param sign enum indicating some state.
     */
    public void getState(signs sign);


}
