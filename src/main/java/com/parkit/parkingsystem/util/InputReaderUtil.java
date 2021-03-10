package com.parkit.parkingsystem.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

/**
 * Reads the user's various inputs
 */
public class InputReaderUtil {

    private static final Scanner scan = new Scanner(System.in);
    private static final Logger logger = LogManager.getLogger("InputReaderUtil");

    /**
     * Reads the value of the user's input (an integer) and returns it
     *
     * @return the user's input or -1 if the input is not valid (integer)
     */
    public int readSelection() {
        try {
            return Integer.parseInt(scan.nextLine());
        }catch(Exception e){
            logger.error("Error while reading user input from Shell", e);
            System.out.println("Error reading input. Please enter valid number for proceeding further");
            return -1;
        }
    }

    /**
     * Reads the registration number of the user's vehicle (String of character) entered and returns it
     *
     * @return the vehicle registration number
     * @throws IllegalArgumentException if user's input is null value
     */
    public String readVehicleRegistrationNumber() {
        try {
            String vehicleRegNumber= scan.nextLine();
            if(vehicleRegNumber == null || vehicleRegNumber.trim().length()==0) {
                throw new IllegalArgumentException("Invalid input provided");
            }
            return vehicleRegNumber;
        }catch(IllegalArgumentException e){
            logger.error("Error while reading user input from Shell", e);
            System.out.println("Error reading input. Please enter a valid string for vehicle registration number");
            throw e;
        }
    }


}
