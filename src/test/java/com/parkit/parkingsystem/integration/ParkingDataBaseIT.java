package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Calendar;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;

    @Mock
    private static InputReaderUtil inputReaderUtil;

    @BeforeAll
    private static void setUp(){
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }

    @BeforeEach
    private void setUpPerTest() throws Exception {
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        dataBasePrepareService.clearDataBaseEntries();
    }

    @AfterAll
    private static void tearDown(){
        dataBasePrepareService.clearDataBaseEntries();
    }

    @Test
    public void testParkingACar(){
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processIncomingVehicle();
        Ticket ticket = ticketDAO.getTicket("ABCDEF");

        Assertions.assertNotNull(ticket);
        Assertions.assertFalse(ticket.getParkingSpot().isAvailable());
    }

    @Test
    public void testParkingLotExit(){
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        testParkingACar();
        parkingService.processExitingVehicle();
        Ticket ticket = ticketDAO.getTicket("ABCDEF");

        Assertions.assertNotNull(ticket.getOutTime());
        Assertions.assertEquals(0.0, ticket.getPrice());
    }

    @Test
    public void testFrequentUserExitsParkingLot(){
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        Ticket ticket = new Ticket();
        String regNumber = "ABCDEF";
        ticket.setParkingSpot(parkingService.getNextParkingNumberIfAvailable());
        ticket.setVehicleRegNumber(regNumber);
        ticket.setPrice(0);
        Calendar before = Calendar.getInstance();
        before.set(2021,01,13,12,
                32);
        ticket.setInTime(before.getTime());
        ticket.setOutTime(before.getTime());
        ticketDAO.saveTicket(ticket);

        testParkingACar();
        parkingService.processExitingVehicle();
        Ticket secondTicket = ticketDAO.getTicket(regNumber);

        Assertions.assertTrue(ticketDAO.frequentUser(secondTicket));

    }

    @Test
    public void carAlreadyInSpotParkNewCarInNextSpot() throws Exception{
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        testParkingACar();
        Ticket ticket1 = ticketDAO.getTicket("ABCDEF");

        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("AZERTY");
        parkingService.processIncomingVehicle();
        Ticket ticket2 = ticketDAO.getTicket("AZERTY");

        Assertions.assertNotEquals(ticket1.getParkingSpot(), ticket2.getParkingSpot());
    }
}
