package com.parkit.parkingsystem.unit.model;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import junit.framework.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ParkingSpotTest {

    private static ParkingSpot parkingSpot;

    @BeforeEach
    public void setUpPerTest() {
        parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
    }

    @Test
    public void changeParkingSpotId() {
        //WHEN
        parkingSpot.setId(2);
        //THEN
        Assert.assertEquals(2, parkingSpot.getId());
    }

    @Test
    public void changeParkingSpotType() {
        //WHEN
        parkingSpot.setParkingType(ParkingType.BIKE);
        //THEN
        Assert.assertEquals(ParkingType.BIKE, parkingSpot.getParkingType());
    }

    @Test
    public void areTwoParkingSpotsDifferent() {
        //GIVEN
        ParkingSpot otherSpot = new ParkingSpot(2, ParkingType.CAR, true);
        //WHEN
        Boolean equality = parkingSpot.equals(otherSpot);
        //THEN
        Assert.assertFalse(equality);
    }

    @Test
    public void areTwoParkingSpotsIdentical() {
        //GIVEN
        ParkingSpot otherSpot = new ParkingSpot(1, ParkingType.CAR, true);
        //WHEN
        Boolean equality = parkingSpot.equals(otherSpot);
        //THEN
        Assert.assertTrue(equality);
        Assert.assertEquals(parkingSpot.hashCode(), otherSpot.hashCode());
    }

    @Test
    public void isParkingAvailable() {
        Boolean availability = parkingSpot.isAvailable();
        //THEN
        Assert.assertTrue(availability);
    }
}
