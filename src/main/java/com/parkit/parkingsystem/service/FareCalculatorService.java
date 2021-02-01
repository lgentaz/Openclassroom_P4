package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    // add frequent user variable (::before create/update request for previous ticket in TicketDAO)
    private boolean frequentUser = false;

    /**
     * Calculates the parking fare when the vehicle leaves and saves it in DB
     *
     * @param ticket
     *          the information relative to the parked vehicle
     */
    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        long inTime = ticket.getInTime().getTime() / (1000);
        long outTime = ticket.getOutTime().getTime() / (1000);

        float duration;
        duration = (float) ((outTime - inTime)/3600.0);

        if(frequentUser) duration *= 0.95;

        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
                break;
            }
            case BIKE: {
                ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
    }
}