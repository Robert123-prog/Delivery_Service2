package helpers;

import exceptions.ValidationException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Date;

public class Validation {

    /**
     *
     * @param email
     * @return boolean
     *
     * Email must contain maximum one "@"
     */

    public static boolean validateEmail(String email){
        //assumed 7 is the shortest email possible
        if (email.length() < 7 || email == null || email.isEmpty() || !(email.chars().filter(ch -> ch == '@').count() == 1)) {
            throw new ValidationException("Email must be at least 7 letters long and contain one \"@\"");
        }
        return true;
    }

    /**
     *
     * @param phoneNumber
     * @return boolean
     *
     * Phone number must contain a + at the beginning, followed by a prefix, and must be max 11 digits long (12 with the plus)
     */

    public static boolean validatePhoneNumber(String phoneNumber){
        if (phoneNumber == null || phoneNumber.length() != 12 || !(phoneNumber.charAt(0) == '+')){
            throw new ValidationException("Phone number must contain a + at the beginning, followed by a prefix, and must be max 11 digits long (12 with the plus)");
        }
        return true;
    }

    /**
     *
     * @param name
     * @return boolean
     *
     * The name cant be empty and has to be made up of letters (no digits/numbers allowed)
     */

    public static boolean validateName(String name) throws ValidationException {
        if (name == null || name.isEmpty() || !name.matches("^[a-zA-Z\\s]+$")) {
            throw new ValidationException("The name cant be empty and has to be made up of letters (no digits/numbers allowed)");
        }
        return true;
    }

    /**
     *
     * @param address
     * @return boolean
     *
     * The address cannot be empty
     */

    public static boolean validateAddress(String address){
        if (address == null || address.isEmpty()){
            throw new ValidationException("The address cannot be empty");
        }
        return true;
    }


    /**
     *
     * @param orderDate
     * @param deliveryDateTime
     * @return
     *
     * The delivery date has to be at a minimum of 1 day after the date of the order placement
     */

    public static boolean validateDeliveryDateTime(LocalDateTime orderDate, LocalDateTime deliveryDateTime){
        if (orderDate == null || deliveryDateTime == null) {
            throw new IllegalArgumentException("Order date and delivery date must not be null");
        }

        // Check if the delivery date is at least 1 day after the order date
        if (ChronoUnit.DAYS.between(orderDate, deliveryDateTime) < 1) {
            throw new ValidationException("The delivery date must be at least one day after the order date.");
        }

        return true; // Delivery date is valid
    }
}
