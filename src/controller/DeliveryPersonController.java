package controller;

import model.Delivery;
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
        List<Delivery> deliveries = deliveryPersonService.getDeliveriesWithToBeShippedOrders();
//        List<Delivery> deliveries = deliveryPersonService.getDelivery();
        StringBuilder output = new StringBuilder("Deliveries suitable for Delivery Person to pick up:\n");
        if (deliveries.isEmpty()) {
            output.append("No deliveries available for 'to be shipped' orders.\n");
        } else {
            deliveries.forEach(delivery -> output.append(delivery.toString()).append("\n"));
        }

        System.out.println(output);
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
        deliveryPersonService.pickDeliveryToPerson(deliveryPersonId, deliveryId);
        System.out.println("Picked delivery with id " + deliveryId + " by Person with id " + deliveryPersonId + " successfully");
    }

    /**
     * Assigns given Personal Vehicle to specified Delivery Person.
     * <p>
     * *@ param deliverer_id unique identifier for Deliverer object.
     *
     * @ param vehicle_id unique identifier for Vehicle object.
     */

    public void assignPersonalVehicle(Integer deliverer_id, Integer vehicle_id) {
        deliveryPersonService.assignPersonalVehicle(deliverer_id, vehicle_id);
    }

    public List<Delivery> getDeliveriesForDeliveryPerson(Integer deliveryPersonId) {
        return deliveryPersonService.getDeliveriesForDeliveryPerson(deliveryPersonId);
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
