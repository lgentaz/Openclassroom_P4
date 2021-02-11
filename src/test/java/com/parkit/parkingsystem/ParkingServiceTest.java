package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {

    private static ParkingService parkingService;

    @Mock
    private static InputReaderUtil inputReaderUtil;
    @Mock
    private static ParkingSpotDAO parkingSpotDAO;
    @Mock
    private static TicketDAO ticketDAO;
    @Mock
    private static Logger logger;

    @BeforeEach
    private void setUpPerTest() {
        try {
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        }catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException("Failed to set up test mock objects");
        }
    }

    @Test
    public void processExitingCarTest(){
        //GIVEN
        try {
            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
            Ticket ticket = new Ticket();
            ticket.setInTime(new Date(System.currentTimeMillis() - (60*60*1000)));
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
            when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
            when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);
            when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);
            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        }catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException("Failed to set up test mock objects");
        }
        //WHEN
        parkingService.processExitingVehicle();
        //THEN
        verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
    }

    @Test
    public void processIncomingCarTest(){
        //GIVEN
        try {
            int spotId = 2;
            when(inputReaderUtil.readSelection()).thenReturn(1);
            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
            when(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).thenReturn(spotId);
            when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);
            when(ticketDAO.saveTicket(any(Ticket.class))).thenReturn(true);
        }catch (Exception e){
            e.printStackTrace();
            throw  new RuntimeException("Failed to set up test mock objects");
        }
        //WHEN
        parkingService.processIncomingVehicle();
        //THEN
        verify(ticketDAO, Mockito.times(1)).saveTicket(any(Ticket.class));
    }

    @Test
    public void processIncomingBikeTest(){
        //GIVEN
        try {
            int spotId = 5;
            when(inputReaderUtil.readSelection()).thenReturn(2);
            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
            when(parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE)).thenReturn(spotId);
            when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);
            when(ticketDAO.saveTicket(any(Ticket.class))).thenReturn(true);
        }catch (Exception e){
            e.printStackTrace();
            throw  new RuntimeException("Failed to set up test mock objects");
        }
        //WHEN
        parkingService.processIncomingVehicle();
        //THEN
        verify(ticketDAO, Mockito.times(1)).saveTicket(any(Ticket.class));
    }

    /*
    @Test
    public void noAvailableSlotForIncomingVehicleTest(){
        //GIVEN
        ParkingSpot parkingSpot;
        try {
            int spotId = -1;
            when(inputReaderUtil.readSelection()).thenReturn(2);
            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
            when(parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE)).thenReturn(spotId);
            parkingSpot = parkingService.getNextParkingNumberIfAvailable();
        }catch (Exception e){
            e.printStackTrace();
            throw  new RuntimeException("Failed to set up test mock objects");
        }

        Assertions.assertNull(parkingSpot);
    }

    @Test
    public void unknownVehicleTypeTest(){
        //GIVEN
        try {
            when(inputReaderUtil.readSelection()).thenReturn(3);
            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        }catch (Exception e){
            e.printStackTrace();
            throw  new RuntimeException("Failed to set up test mock objects");
        }
    }

 */

}
