package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    private final int convertToSec = 1000;
    private final double convertToHrs = 3600.0;
    private final double halfHour = 0.5;
    private final double discount = 0.95;

    /**
     * Calculates the parking fare when the vehicle leaves and saves it in DB
     *
     * @param ticket
     *          the information relative to the parked vehicle
     */
    public void calculateFare(Ticket ticket, boolean reduction){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        long inTime = ticket.getInTime().getTime()/convertToSec;
        long outTime = ticket.getOutTime().getTime()/convertToSec;

        double duration = (outTime - inTime)/convertToHrs;
        if (duration < halfHour) duration = 0.0;
        if (reduction) duration *= discount;

        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
                break;
            }
            case BIKE: {
                ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
                break;
            }
            default: throw new IllegalArgumentException("Unknown Parking Type");
        }
    }

}