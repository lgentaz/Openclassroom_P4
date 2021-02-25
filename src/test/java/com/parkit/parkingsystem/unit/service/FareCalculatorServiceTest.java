package com.parkit.parkingsystem.unit.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FareCalculatorServiceTest {

    private static FareCalculatorService fareCalculatorService;
    private Ticket ticket;
    private static long hrInMillis = 3600000;
    private static long dayInMillis = 86400000;

    @BeforeAll
    private static void setUp() {
        fareCalculatorService = new FareCalculatorService();
    }

    private Ticket ticketSetUp(long duration, ParkingSpot chosenSpot) {
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - duration);
        Date outTime = new Date();
        ticket = new Ticket(chosenSpot, "ABCDEF", inTime);
        ticket.setOutTime(outTime);
        return ticket;
    }

    @Test
    public void calculateFareCarFirstTimeUser() {
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket = ticketSetUp(hrInMillis, parkingSpot);
        fareCalculatorService.calculateFare(ticket, false);
        assertEquals(ticket.getPrice(), Fare.CAR_RATE_PER_HOUR);
    }

    @Test
    public void calculateFareBikeFirstTimeUser() {
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
        ticket = ticketSetUp(hrInMillis, parkingSpot);
        fareCalculatorService.calculateFare(ticket, false);
        assertEquals(ticket.getPrice(), Fare.BIKE_RATE_PER_HOUR);
    }

    @Test
    public void calculateFareUnknownType() {
        ParkingSpot parkingSpot = new ParkingSpot(1, null, false);
        ticket = ticketSetUp(hrInMillis, parkingSpot);

        assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket, false));
    }

    @Test
    public void calculateFareBikeWithFutureInTime() {
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
        ticket = ticketSetUp(-hrInMillis, parkingSpot);

        assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket, false));
    }

    @Test
    public void calculateFareBikeWithLessThanOneHourParkingTimeFirstTimeUser() {
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
        ticket = ticketSetUp(2700000, parkingSpot);

        fareCalculatorService.calculateFare(ticket, false);
        assertEquals((0.75 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
    }

    @Test
    public void calculateFareCarWithLessThanOneHourParkingTimeFirstTimeUser() {
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket = ticketSetUp(2700000, parkingSpot);

        fareCalculatorService.calculateFare(ticket, false);
        assertEquals((0.75 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
    }

    @Test
    public void calculateFareCarWithMoreThanADayParkingTimeFirstTimeUser() {
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket = ticketSetUp(dayInMillis, parkingSpot);

        fareCalculatorService.calculateFare(ticket, false);
        assertEquals((24 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
    }

    @Test
    public void calculateFareCarWithLessThanThirtyMinutesParkingTime() {
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket = ticketSetUp(900000, parkingSpot);

        fareCalculatorService.calculateFare(ticket, false);
        assertEquals(0.0, ticket.getPrice());
    }

    @Test
    public void calculateFareCarWithMoreThanADayParkingFrequentUser() {
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket = ticketSetUp(dayInMillis, parkingSpot);

        fareCalculatorService.calculateFare(ticket, true);
        assertEquals((24 * 0.95 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
    }
}
