package com.parkit.parkingsystem;

import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ParkingSpotTest {

    private static ParkingSpot parkingSpot;

    @Mock
    private static ParkingSpotDAO parkingSpotDAO;

    //TODO test the setters for ParkingSpot and test the equality with hashcode

    @Test
    public void setValuesForParkingSpot() {
        //GIVEN
        //WHEN
        //THEN
    }

    @Test
    public void areTwoParkingSpotsDifferent() {
        //GIVEN
        //WHEN
        //THEN
    }

    @Test
    public void areTwoParkingSpotsIdentical() {
        //GIVEN
        //WHEN
        //THEN
    }
}
