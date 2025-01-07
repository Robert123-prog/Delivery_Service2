package service;

import exceptions.BusinessLogicException;
import exceptions.EntityNotFound;
import exceptions.ValidationException;
import model.*;
import repository.IRepository;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class DeliveryPersonService {
    private final IRepository<Delivery> deliveryIRepository;
    private final IRepository<Delivery_Person> deliveryPersonIRepository;
    private final IRepository<Personal_Vehicle> personalVehicleIRepository;


    public DeliveryPersonService(IRepository<Delivery> deliveryIRepository, IRepository<Delivery_Person> deliveryPersonIRepository, IRepository<Personal_Vehicle> personalVehicleIRepository){
        this.deliveryIRepository = deliveryIRepository;
        this.deliveryPersonIRepository = deliveryPersonIRepository;
        this.personalVehicleIRepository = personalVehicleIRepository;
    }

    public List<Delivery> getDelivery() {
        return deliveryIRepository.readAll();
    }

    public Integer getNewDeliveryPersonId() {
        int maxId = 0;
        for (Integer Id : deliveryPersonIRepository.getKeys()) {
            if (Id.compareTo(maxId) > 0) {
                maxId = Id;
            }
        }
        return maxId + 1;
    }

    /**
     * Registers a new delivery person in the system
     *
     * @param deliveryPersonId Unique identifier for the delivery person
     * @param name Full name of the delivery person
     * @param phone Contact phone number
     * @param license Driver's license number
     */
    public void enrollAsDriver(Integer deliveryPersonId, String name, String phone, String license) {
        Delivery_Person deliveryPerson = new Delivery_Person(deliveryPersonId, phone, name);

        deliveryPerson.setVerified(true);
        deliveryPersonIRepository.create(deliveryPerson);
    }

    /**
     * Retrieves a list of deliveries that contain at least one order with the status "to be shipped".
     *
     * @return a list of deliveries where at least one order is marked with the status "to be shipped".
     */
    public List<Delivery> getDeliveriesWithToBeShippedOrders() {
        List<Delivery> allDeliveries = deliveryIRepository.readAll();

        List<Delivery> deliveriesWithToBeShipped = allDeliveries.stream()
                .filter(delivery -> delivery.getOrders().stream()
                        .anyMatch(order -> "to be shipped".equalsIgnoreCase(order.getStatus())))
                .collect(Collectors.toList());

        if (deliveriesWithToBeShipped.isEmpty()) throw new BusinessLogicException("No deliveries with 'to be shipped' orders found");

        return deliveriesWithToBeShipped;
    }

    public void pickDeliveryToPerson(Integer deliveryPersonId, Integer deliveryId) {
        List<Delivery_Person> deliveryPeople = deliveryPersonIRepository.readAll();
        boolean existsDeliveryPerson = false;

        for (Delivery_Person deliveryPerson: deliveryPeople){
            if (Objects.equals(deliveryPerson.getId(), deliveryPersonId)){
                existsDeliveryPerson = true;
                break;
            }
        }

        if (!existsDeliveryPerson) throw new EntityNotFound("No delivery person found with ID " + deliveryPersonId);

        List<Delivery> deliveries = deliveryIRepository.readAll();
        boolean existsDelivery= false;

        for (Delivery delivery: deliveries){
            if (Objects.equals(delivery.getId(), deliveryId)){
                existsDelivery = true;
                break;
            }
        }

        if (!existsDelivery) throw new EntityNotFound("No delivery found with ID " + deliveryId);

        Delivery delivery = deliveryIRepository.get(deliveryId);
        Delivery_Person deliveryPerson = deliveryPersonIRepository.get(deliveryPersonId);

        if (delivery != null && deliveryPerson != null) {
            deliveryPerson.addDelivery(delivery);
            deliveryPersonIRepository.update(deliveryPerson);
            delivery.setDeliveryPeronID(deliveryPersonId);
            deliveryIRepository.update(delivery);
        }
    }

    /**
     * Assigns a personal vehicle to a delivery person
     *
     * @param deliveryPersonId ID of the delivery person
     * @param personalVehicleId ID of the personal vehicle to assign
     */
    public void assignPersonalVehicle(Integer deliveryPersonId, Integer personalVehicleId){
        Delivery_Person deliveryPerson = deliveryPersonIRepository.get(deliveryPersonId);
        if (deliveryPerson == null) throw new EntityNotFound("No delivery person found for ID " + deliveryPersonId);

        Personal_Vehicle personalVehicle = personalVehicleIRepository.get(personalVehicleId);
        if (personalVehicle == null) throw new EntityNotFound("No personal vehicle found for ID " + personalVehicleId);


        //deliveryPerson.setPersonalVehicleId(personalVehicleId);
        personalVehicle.setDeliveryPersonID(deliveryPersonId);
        deliveryPerson.setPersonalVehicleId(personalVehicleId);
        //deliveryPersonIRepository.update(deliveryPerson);
        personalVehicleIRepository.update(personalVehicle);
        deliveryPersonIRepository.update(deliveryPerson);
    }

    public List<Personal_Vehicle> getPersonalVehicles(){
        return personalVehicleIRepository.readAll();
    }

    public List<Delivery> getDeliveriesForDeliveryPerson(Integer deliveryPersonId) {
        Delivery_Person deliveryPerson = deliveryPersonIRepository.get(deliveryPersonId);
        if (deliveryPerson == null) throw new EntityNotFound("No delivery person found with ID " + deliveryPersonId);

        List<Delivery> deliveries = deliveryPerson.getDeliveries();
        if (deliveries == null) throw new BusinessLogicException("The delivery person has no related deliveries");

        return deliveries;
    }

    public List<Delivery_Person> getDeliveryPerson() {
        return deliveryPersonIRepository.readAll();
    }

    /**
     * Verifies if a delivery person's license is valid
     *
     * @param deliveryPersonId ID of the delivery person
     * @param license License to verify
     * @return true if license is valid, false otherwise
     */
    public boolean verifyDeliveryPersonLicense(Integer deliveryPersonId,String license) {
        if (isLicenseCategoryValid(license)) {
            System.out.println("License for delivery person " + deliveryPersonId + " is valid.");
            return true;
        } else {
            System.out.println("License for delivery person " + deliveryPersonId + " is not valid.");
            throw new ValidationException("Invalid license for Delivery Person");
        }
    }

    /**
     * Helper method to validate license categories
     *
     * @param licenseCategory Category of license to validate
     * @return true if license category is valid, false otherwise
     */
    private boolean isLicenseCategoryValid(String licenseCategory) {
        Set<String> validCategories = Set.of("B", "BE", "C", "CE");
        return validCategories.contains(licenseCategory);
    }

}
