package com.parkinglot.model;

import com.parkinglot.services.ParkingSigns;

import java.util.ArrayList;

public class Owner implements ParkingSigns {
    ArrayList<Payment> paymentArrayList = new ArrayList<>();
    signs sign;

    @Override
    public void getState(signs sign) {
        this.sign=sign;
    }

    public signs getSign() {
        return this.sign;
    }

    public void setTimeAndPayment(Object vehicle, int... charge){
        if(charge.length>0)
            paymentArrayList.add(new Payment(vehicle,charge[0]));
        else
            paymentArrayList.add(new Payment(vehicle));
    }

    public String getTime(Object vehicle){
        for(Payment payment : paymentArrayList){
            if(payment.vehicle.equals(vehicle))
                return payment.getTime();
        }
        return "";
    }


}
