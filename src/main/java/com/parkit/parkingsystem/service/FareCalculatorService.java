package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.config.GetProperties;
import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    private static final GetProperties fareConfig = new GetProperties();

    private static final int convertToSec = 1000;
    private static final double convertToHrs = 3600.0;
    private static final double halfHour = 0.5;
    private static final double discount =  Double.parseDouble(fareConfig.getProp("discount"));

    /**
     * Calculates the parking fare when the vehicle leaves and saves it in DB
     *
     * @param ticket
     *          the information relative to the parked vehicle
     * @param reduction
     *          boolean stating if vehicle is eligible for discount
     */
    public void calculateFare(Ticket ticket, boolean reduction){
        if(ticket.getOutTime().before(ticket.getInTime()))
            throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
        if (ticket.getOutTime() == null) throw new NullPointerException("No out time provided.");

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
        }
    }

}