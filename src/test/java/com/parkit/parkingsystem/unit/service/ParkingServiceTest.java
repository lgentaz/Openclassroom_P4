package com.parkit.parkingsystem.unit.service;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.Assertions;
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
    private final static long millisecInHour = 3600000;

    @Mock
    private static InputReaderUtil inputReaderUtil;
    @Mock
    private static ParkingSpotDAO parkingSpotDAO;
    @Mock
    private static TicketDAO ticketDAO;

    @Test
    public void processExitingCarTest(){
        //GIVEN
        try {
            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
            Date inTime = new Date(System.currentTimeMillis() - millisecInHour);
            Ticket ticket = new Ticket(parkingSpot,"ABCDEF", inTime);
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
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
    public void processExitingCarNoTicketTest(){
        //GIVEN
        try {
            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
            Date inTime = new Date(System.currentTimeMillis() - millisecInHour);
            Ticket ticket = new Ticket(parkingSpot, "ABCDEF", inTime);
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
            when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
            when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(false);
            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        }catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException("Failed to set up test mock objects");
        }
        //WHEN
        parkingService.processExitingVehicle();
        //THEN
        verify(parkingSpotDAO, Mockito.times(0)).updateParking(any(ParkingSpot.class));
    }

    @Test
    public void processIncomingCarTest(){
        //GIVEN
        try {
            int spotId = 2;
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
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
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
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

    @Test
    public void noAvailableSlotForIncomingVehicleTest(){
        //GIVEN
        try {
            int spotId = -1;
            when(inputReaderUtil.readSelection()).thenReturn(2);
            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
            when(parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE)).thenReturn(spotId);
        }catch (Exception e){
            e.printStackTrace();
            throw  new RuntimeException("Failed to set up test mock objects");
        }

        parkingService.processIncomingVehicle();
        verify(parkingSpotDAO, Mockito.times(0)).updateParking(any(ParkingSpot.class));
    }

    @Test
    public void unknownVehicleTypeTest(){
        //GIVEN
        ParkingSpot parkingSpot;
        try {
            when(inputReaderUtil.readSelection()).thenReturn(3);
            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        }catch (Exception e){
            e.printStackTrace();
            throw  new RuntimeException("Failed to set up test mock objects");
        }

        parkingSpot = parkingService.getNextParkingNumberIfAvailable();

        Assertions.assertNull(parkingSpot);
    }

}
