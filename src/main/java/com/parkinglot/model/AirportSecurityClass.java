package com.parkinglot.model;

import com.parkinglot.services.ParkingSigns;

import java.util.ArrayList;

public class AirportSecurityClass implements ParkingSigns {
    ArrayList<Payment> paymentArrayList = new ArrayList<>();
    ParkingSigns.signs sign;

    @Override
    public void getState(ParkingSigns.signs sign) {
        this.sign=sign;
    }

    public ParkingSigns.signs getSign() {
        return this.sign;
    }

    public void setTimeAndPayment(Object vehicle, int... charge){
        paymentArrayList.add(new Payment(vehicle,charge));
    }

    public String getTime(Object vehicle){
        for(Payment payment : paymentArrayList){
            if(payment.vehicle.equals(vehicle))
                return payment.getTime();
        }
        return "";
    }


}
