package controller;

import exceptions.BusinessLogicException;
import exceptions.EntityNotFound;
import exceptions.ValidationException;
import model.Delivery;
import model.Delivery_Person;
import model.Personal_Vehicle;
import service.DeliveryPersonService;

import java.util.List;
import java.util.Objects;

public class DeliveryPersonController {
    private final DeliveryPersonService deliveryPersonService;

    public DeliveryPersonController(DeliveryPersonService deliveryPersonService){
        this.deliveryPersonService = deliveryPersonService;
    }

    /**
     * Displays all deliveries in a formatted manner.
     */
    public void viewAllDeliveries() {
        StringBuilder output = new StringBuilder("All Deliveries:\n");
        deliveryPersonService.getDelivery().forEach(delivery -> output.append(delivery.toString()).append("\n"));
        System.out.println(output);
    }

    /**
     * Creates a new delivery person with specified details.
     *
     * @param name    the name of the delivery person
     * @param phone   the phone number of the delivery person
     * @param license the license number of the delivery person
     */
    public void createDeliveryPerson(String name, String phone, String license) {
        if (name.isEmpty() || phone.isEmpty() || license.isEmpty()){
            throw new ValidationException("One or more of the required fields is empty");
        }

        Integer deliveryPersonId = deliveryPersonService.getNewDeliveryPersonId();
        boolean verified = verifyDeliveryPerson(deliveryPersonId, license);
        System.out.println(verified);
        deliveryPersonService.enrollAsDriver(deliveryPersonId, name, phone, license);
        System.out.println("Registered delivery person " + deliveryPersonId);
    }

    /**
     * Verifies if a delivery person's license is valid.
     *
     * @param deliveryPersonId the ID of the delivery person to verify
     * @param license          the license number to check
     * @return true if the license is valid; false otherwise
     */
    public boolean verifyDeliveryPerson(Integer deliveryPersonId, String license) {
        return deliveryPersonService.verifyDeliveryPersonLicense(deliveryPersonId, license);
    }

    /**
     * @return a list of the Deliveries
     */

    public void viewDeliveriesForDeliveryPerson() {
        try {
            List<Delivery> deliveries = deliveryPersonService.getDeliveriesWithToBeShippedOrders();
//        List<Delivery> deliveries = deliveryPersonService.getDelivery();
            StringBuilder output = new StringBuilder("Deliveries suitable for Delivery Person to pick up:\n");
            if (deliveries.isEmpty()) {
                output.append("No deliveries available for 'to be shipped' orders.\n");
            } else {
                deliveries.forEach(delivery -> output.append(delivery.toString()).append("\n"));
            }
            System.out.println(output);
        }catch (BusinessLogicException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Validates whether a selected Delivery exists based on its unique identifier.
     *
     * @param deliveryID unique identifier for Delivery object
     * @return true if it exists; false otherwise
     */
    public boolean validateSelectedDelivery(Integer deliveryID) {
        return deliveryPersonService.getDelivery().stream().anyMatch(delivery -> Objects.equals(delivery.getDeliveryID(), deliveryID));
    }

    /**
     * Assigns a delivery to a specific delivery person.
     *
     * @param deliveryPersonId the ID of the delivery person picking up the delivery
     * @param deliveryId       the ID of the delivery being picked up
     */
    public void pickDeliveryByPerson(Integer deliveryPersonId, Integer deliveryId) {
//            List<Delivery> deliveries = deliveryPersonService.getDelivery();
//            Delivery deliveryToBeAssigned = null;
//
//            for (Delivery delivery: deliveries){
//                if (delivery.getDeliveryID() == deliveryId){
//                    deliveryToBeAssigned = delivery;
//                }
//            }
//
//            if (deliveryToBeAssigned != null) throw new EntityNotFound("No delivery found with ID " + deliveryId);
//
//            if (deliveryToBeAssigned.getEmployeeID() != null || deliveryToBeAssigned.getDeliveryPeronID() != null){
//                throw new BusinessLogicException("The delivery is already assigned!");
//            }

            try {

                deliveryPersonService.pickDeliveryToPerson(deliveryPersonId, deliveryId);
                System.out.println("Picked delivery with id " + deliveryId + " by Person with id " + deliveryPersonId + " successfully");
            }catch(BusinessLogicException e){
                e.setMessage("No delivery found with ID " + deliveryId);
                System.out.println(e.getMessage());
            }
        /*
        deliveryPersonService.pickDeliveryToPerson(deliveryPersonId, deliveryId);
        System.out.println("Picked delivery with id " + deliveryId + " by Person with id " + deliveryPersonId + " successfully");
        */
    }

    /**
     * Assigns given Personal Vehicle to specified Delivery Person.
     * <p>
     * *@ param deliverer_id unique identifier for Deliverer object.
     *
     * @ param vehicle_id unique identifier for Vehicle object.
     */

    public void assignPersonalVehicle(Integer deliveryPersonId, Integer vehicleId) {
        try {
            deliveryPersonService.assignPersonalVehicle(deliveryPersonId, vehicleId);
        }catch (EntityNotFound e){
            System.out.println(e.getMessage());
        }
    }

    public List<Delivery> getDeliveriesForDeliveryPerson(Integer deliveryPersonId) {
        try{
            return deliveryPersonService.getDeliveriesForDeliveryPerson(deliveryPersonId);
        }catch (EntityNotFound e){
            System.out.println(e.getMessage());
            return null;
        }catch (BusinessLogicException e1){
            System.out.println(e1.getMessage());
            return null;
        }
    }

    /**
     * Displays all registered Delivery Persons in a formatted manner.
     */
    public void viewAllDeliveryPersons() {
        StringBuilder output = new StringBuilder("All Delivery Persons:\n");
        deliveryPersonService.getDeliveryPerson().forEach(deliveryPerson -> output.append(deliveryPerson.toString()).append("\n"));
        System.out.println(output);
    }


}
