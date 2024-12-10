package controller;

import exceptions.BusinessLogicException;
import exceptions.EntityNotFound;
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
        List<Delivery_Person> deliveryPeople = deliveryPersonService.getDeliveryPerson();
        boolean existsDeliveryPerson = false;

        for (Delivery_Person deliveryPerson: deliveryPeople){
            if (deliveryPerson.getId() == deliveryPersonId){
                existsDeliveryPerson = true;
            }
        }

        if (!existsDeliveryPerson) throw new EntityNotFound("No delivery person found with ID " + deliveryPersonId);

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
            List<Delivery> deliveries = deliveryPersonService.getDelivery();
            Delivery deliveryToBeAssigned = null;

            for (Delivery delivery: deliveries){
                if (delivery.getDeliveryID() == deliveryId){
                    deliveryToBeAssigned = delivery;
                }
            }

            if (deliveryToBeAssigned != null) throw new EntityNotFound("No delivery found with ID " + deliveryId);

            if (deliveryToBeAssigned.getEmployeeID() != null || deliveryToBeAssigned.getDeliveryPeronID() != null){
                throw new BusinessLogicException("The delivery is already assigned!");
            }

        deliveryPersonService.pickDeliveryToPerson(deliveryPersonId, deliveryId);
        System.out.println("Picked delivery with id " + deliveryId + " by Person with id " + deliveryPersonId + " successfully");

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
        List<Delivery_Person> deliveryPeople = deliveryPersonService.getDeliveryPerson();
        boolean existsDeliveryPerson = false;

        for (Delivery_Person deliveryPerson: deliveryPeople){
            if (Objects.equals(deliveryPerson.getId(), deliveryPersonId)){
                existsDeliveryPerson = true;
                break;
            }
        }

        if (!existsDeliveryPerson) throw new EntityNotFound("No delivery person found with ID " + deliveryPersonId);

        List<Personal_Vehicle> personalVehicles = deliveryPersonService.getPersonalVehicles();
        Personal_Vehicle personalVehicleToBeAssigned = null;
        boolean existsPersonalVehicle = false;

        for (Personal_Vehicle personalVehicle: personalVehicles){
            if (Objects.equals(personalVehicle.getId(), vehicleId)){
                existsPersonalVehicle = true;
                personalVehicleToBeAssigned = personalVehicle;
                break;
            }
        }

        if (!existsPersonalVehicle) throw new EntityNotFound("No personal vehicle found with ID " + deliveryPersonId);

        if (personalVehicleToBeAssigned.getDeliveryPersonID() != null){
            throw new BusinessLogicException("The desired personal vehicle is already taken!");
        }

        deliveryPersonService.assignPersonalVehicle(deliveryPersonId, vehicleId);
    }

    public List<Delivery> getDeliveriesForDeliveryPerson(Integer deliveryPersonId) {
        List<Delivery_Person> deliveryPeople = deliveryPersonService.getDeliveryPerson();
        boolean existsDeliveryPerson = false;

        for (Delivery_Person deliveryPerson: deliveryPeople){
            if (Objects.equals(deliveryPerson.getId(), deliveryPersonId)){
                existsDeliveryPerson = true;
                break;
            }
        }

        if (!existsDeliveryPerson) throw new EntityNotFound("No delivery person found with ID " + deliveryPersonId);

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
