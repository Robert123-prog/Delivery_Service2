import model.Delivery;
import model.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The org.example.Controller class handles various operations related to customers, stores, deposits,
 * employees, deliveries, orders, and packages within the application.
 */
public class Controller {
    private final Service service;

    /**
     * Constructs a org.example.Controller with the specified service.
     *
     * @param service the service used to perform operations
     */
    public Controller(Service service) {
        this.service = service;
    }

    /**
     * Creates a new customer with the provided details.
     *
     * @param name    the name of the customer
     * @param address the address of the customer
     * @param phone   the phone number of the customer
     * @param email   the email address of the customer
     */
    public void createLoggedInCustomer(String name, String address, String phone, String email) {
        Integer id = service.getNewCustomerId();
        service.createCustomer(id, name, address, phone, email);
        System.out.println("Registered customer with id " + id + " successfully");
    }

    /**
     * Creates a new store with the provided details.
     *
     * @param name    the name of the store
     * @param address the address of the store
     * @param contact the contact information for the store
     */
    public void createStore(String name, String address, String contact) {
        Integer id = service.getNewStoreId();
        service.registerStore(id, name, address, contact);
        System.out.println("Registered store: " + name);
    }

    /**
     * Deletes a store by its ID.
     *
     * @param storeId the ID of the store to delete
     */
    public void deleteStore(Integer storeId) {
        service.removeStore(storeId);
        System.out.println("Removed store " + storeId);
    }

    /**
     * Displays all available stores.
     */
    public void viewAllStores() {
        StringBuilder output = new StringBuilder("Available Stores:\n");
        service.getStores().forEach(store -> output.append(store.toString()).append("\n"));
        System.out.println(output);
    }

    /**
     * Checks if a store exists based on its ID.
     *
     * @param id the ID of the store to check
     * @return true if the store exists; false otherwise
     */
    public boolean storeSelection(Integer id) {
        return service.getStores().stream().anyMatch(store -> Objects.equals(store.getId(), id));
    }

    /**
     * Registers a deposit for a specific store.
     *
     * @param storeId        the ID of the store receiving the deposit
     * @param depositAddress the address associated with the deposit
     * @param depositStatus  the status of the deposit
     */
    public void registerDeposit(Integer storeId, String depositAddress, String depositStatus) {
        Integer depositId = service.getNewDepositId();
        service.registerDeposit(depositId, storeId, depositAddress, depositStatus);
        System.out.println("Registered deposit " + depositId + " to store " + storeId);
    }

    /**
     * Deletes a specific deposit from a store.
     *
     * @param storeId   the ID of the store from which to delete the deposit
     * @param depositId the ID of the deposit to delete
     */
    public void deleteDeposit(Integer storeId, Integer depositId) {
        service.removeDeposit(storeId, depositId);
        System.out.println("Removed deposit " + depositId + " from store " + storeId);
    }

    /**
     * Displays all available deposits.
     */
    public void viewAllDeposits() {
        StringBuilder output = new StringBuilder("Available Deposits:\n");
        service.getDeposits().forEach(deposit -> output.append(deposit.toString()).append("\n"));
        System.out.println(output);
    }

    /**
     * Deletes a customer by their ID.
     *
     * @param customerId the ID of the customer to delete
     */
    public void deleteCustomer(Integer customerId) {
        service.deleteCustomer(customerId);
        System.out.println("Deleted customer with id " + customerId + " successfully");
    }

    /**
     * Checks if a deposit exists based on its ID.
     *
     * @param id the ID of the deposit to check
     * @return true if the deposit exists; false otherwise
     */
    public boolean depositSelection(Integer id) {
        return service.getDeposits().stream().anyMatch(deposit -> Objects.equals(deposit.getId(), id));
    }

    /**
     * Displays all available customers
     */
    public void viewAllCustomers() {
        StringBuilder output = new StringBuilder("Available Customers:\n");
        service.getCustomers().forEach(customer -> output.append(customer.toString()).append("\n"));
        System.out.println(output);
    }

    /**
     * Displays all available departments.
     */
    public void viewAllDepartments() {
        StringBuilder output = new StringBuilder("Available Departments:\n");
        service.getDepartments().forEach(department -> output.append(department.toString()).append("\n"));
        System.out.println(output);
    }

    /**
     * Checks if a department exists based on its ID.
     *
     * @param id the ID of the department to check
     * @return true if the department exists; false otherwise
     */
    public boolean departmentSelection(Integer id) {
        return service.getDepartments().stream().anyMatch(department -> Objects.equals(department.getId(), id));
    }

    /**
     * Creates a new employee and assigns them to a specified department.
     *
     * @param departmentId the ID of the department to which the employee will be assigned
     * @param name         the name of the employee
     * @param phone        the phone number of the employee
     * @param license      the license number of the employee
     */
    public void createEmployee(Integer departmentId, String name, String phone, String license) {
        Integer employeeId = service.getNewEmployeeId();
        service.createEmployee(employeeId, departmentId, name, phone, license);
        System.out.println("Registered employee " + employeeId + " to department " + departmentId);
    }

    /**
     * Verifies if a delivery person's license is valid.
     *
     * @param deliveryPersonId the ID of the delivery person to verify
     * @param license          the license number to check
     * @return true if the license is valid; false otherwise
     */
    public boolean verifyDeliveryPerson(Integer deliveryPersonId, String license) {
        return service.verifyDeliveryPersonLicense(deliveryPersonId, license);
    }

    /**
     * Creates a new delivery person with specified details.
     *
     * @param name    the name of the delivery person
     * @param phone   the phone number of the delivery person
     * @param license the license number of the delivery person
     */
    public void createDeliveryPerson(String name, String phone, String license) {
        Integer deliveryPersonId = service.getNewDeliveryPersonId();
        boolean verified = verifyDeliveryPerson(deliveryPersonId, license);
        System.out.println(verified);
        service.enrollAsDriver(deliveryPersonId, name, phone, license);
        System.out.println("Registered delivery person " + deliveryPersonId);
    }

    /**
     * Deletes an employee by their ID.
     *
     * @param employeeId the ID of the employee to delete
     */
    public void deleteEmployee(Integer employeeId) {
        service.unenrollEmployee(employeeId);
        System.out.println("Employee with id " + employeeId + " unenrolled successfully");
    }

    /**
     * Deletes a delivery person by their ID.
     *
     * @param deliveryPersonId the ID of the delivery person to delete
     */
    public void deleteDeliveryPerson(Integer deliveryPersonId) {
        service.unenrollDeliveryPerson(deliveryPersonId);
        System.out.println("Delivery person with id " + deliveryPersonId + " unenrolled successfully");
    }

    /**
     * Assigns a delivery to a specific delivery person.
     *
     * @param deliveryPersonId the ID of the delivery person picking up the delivery
     * @param deliveryId       the ID of the delivery being picked up
     */
    public void pickDeliveryByPerson(Integer deliveryPersonId, Integer deliveryId) {
        service.pickDeliveryToPerson(deliveryPersonId, deliveryId);
        System.out.println("Picked delivery with id " + deliveryId + " by Person with id " + deliveryPersonId + " successfully");
    }

    /**
     * Assigns a delivery to a specific employee.
     *
     * @param employeeId the ID of the employee picking up the delivery
     * @param deliveryId the ID of the delivery being picked up
     */
    public void pickDeliveryByEmployee(Integer employeeId, Integer deliveryId) {
        service.pickDelivery(employeeId, deliveryId);
        System.out.println("Picked delivery with id " + deliveryId + " by Employee with id " + employeeId + " successfully");
    }

    /**
     * Cancels a specific delivery by an employee.
     *
     * @param employeeId the ID of the employee cancelling the delivery
     * @param deliveryId the ID of the delivery to cancel
     */
    public void cancelDeliveryByEmployee(Integer employeeId, Integer deliveryId) {
        service.removeDelivery(deliveryId);
        System.out.println("Delivery with id " + deliveryId + " cancelled by Employee with id " + employeeId + " successfully");
    }

    /**
     * Cancels a specific delivery by a delivery person.
     *
     * @param deliveryPersonId the ID of the delivery person cancelling the delivery
     * @param deliveryId       the ID of the delivery to cancel
     */
    public void cancelDeliveryByPerson(Integer deliveryPersonId, Integer deliveryId) {
        service.removeDeliveryToPerson(deliveryId);
        System.out.println("Delivery with id " + deliveryId + " cancelled by Delivery Person with id " + deliveryPersonId + " successfully");
    }

    /**
     * Places an order for a customer.
     *
     * @param customerId       the ID of the customer placing the order
     * @param orderDate        the date when the order was placed
     * @param deliveryDateTime when the order should be delivered
     * @param /cost            total cost of the order
     * @param /status          current status of the order
     */
    public void makeAnOrder(Integer customerId, Date orderDate, LocalDateTime deliveryDateTime, List<Integer> packageIds) {
        Integer orderId = service.getNewOrderId();
        service.placeOrder(customerId, orderId, orderDate, deliveryDateTime, packageIds);
        System.out.println("Order with id " + orderId + " by customer with id " + customerId + " successfully");
    }

    /**
     * Removes an existing order for a customer.
     *
     * @param customerId the ID of the customer whose order will be removed
     * @param orderId    the ID of the order to remove
     */
    public void removeAnOrder(Integer customerId, Integer orderId) {
        service.removeOrder(customerId, orderId);
        System.out.println("Order with id " + orderId + " by customer with id " + customerId + " removed successfully");
    }

    /**
     * Retrieves and returns last logged-in customer's ID.
     *
     * @return last logged-in customer's ID
     */
    public Integer getLastLoggedInCustomerId() {
        return service.getLastLoggedInCustomerId();
    }

    /**
     * Displays all available packages.
     */
    public void getAllPackages() {
        System.out.println(service.getPackages());
    }

    /**
     * Validates if all package IDs provided are valid.
     *
     * @param ids list of package IDs to validate
     * @return true if all IDs are valid; false otherwise
     */
    public boolean validatePackages(List<Integer> ids) {
        List<Packages> packages = service.getPackages();
        Set<Integer> packageIds = packages.stream()
                .map(Packages::getId)
                .collect(Collectors.toSet());

        return ids.stream().allMatch(packageIds::contains);
    }


    /**
     * Retrieves validated packages based on provided IDs.
     *
     * @param packageIds list of package IDs to retrieve
     * @return list of validated packages matching provided IDs
     */
    public List<Packages> getValidatedPackages(List<Integer> packageIds) {
        List<Packages> allPackages = service.getPackages();
        List<Packages> selectedPackages = new ArrayList<>();

        for (Integer id : packageIds) {
            for (Packages packages : allPackages) {
                if (packages.getId().equals(id)) {
                    selectedPackages.add(packages);
                    break;
                }
            }
        }

        return selectedPackages;
    }

    /**
     * Calculates total cost based on provided packages.
     * <p>
     * /@param packages list of packages to calculate cost for
     *
     * @return total cost calculated from packages
     */
    public double calculateOrderCost(Integer orderId) {
        return service.calculateAndUpdateOrderCost(orderId);
    }



    /**
     * Retrieves personal orders for a specific customer.
     *
     * @param customerId ID of customer whose orders are retrieved
     * @return list of personal orders for that customer
     */
    /*
    public List<Order> getPersonalOrders(Integer customerId) {
        List<Order> orders = service.getOrdersFromCustomers(customerId);
        return orders;
    }
*/

    /**
     * Displays personal orders in a formatted manner.
     *
     * @param /orders list of orders to display
     */
    public void viewPersonalOrders(Integer customerId) {
        List<Order> orders = service.getOrdersFromCustomers(customerId);
        StringBuilder output = new StringBuilder("Available Orders:\n");

        orders.forEach(order -> output.append(order.toString()).append("\n"));

        System.out.println(output);
    }

    /**
     * Validates if a specified order exists based on its ID.
     *
     * @param id ID of order to validate
     * @return true if order exists; false otherwise
     */
    public boolean validateSelectedOrder(Integer id) {
        return service.getOrders().stream().anyMatch(order -> Objects.equals(order.getId(), id));
    }

    /**
     * Retrieves a selected order based on its ID.
     *
     * @param orderId ID of order to retrieve
     * @return Order object if found; null otherwise
     */
    public Order getSelectedOrder(Integer orderId) {
        return service.getOrders().stream()
                .filter(order -> Objects.equals(order.getId(), orderId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Removes a selected order based on customer and order IDs.
     *
     * @param customerID ID of customer who owns this order
     * @param orderID    ID of order to remove
     */
    public void removeSelectedOrder(Integer customerID, Integer orderID) {
        service.removeOrder(customerID, orderID);
    }

    /**
     * Displays all employees and their details in a formatted manner.
     */
    public void viewAllEmployees() {
        StringBuilder output = new StringBuilder("All Employees:\n");
        service.getEmployees().forEach(employee -> output.append(employee.toString()).append("\n"));
        System.out.println(output);
    }

    /**
     * Displays all registered Delivery Persons in a formatted manner.
     */
    public void viewAllDeliveryPersons() {
        StringBuilder output = new StringBuilder("All Delivery Persons:\n");
        service.getDeliveryPerson().forEach(deliveryPerson -> output.append(deliveryPerson.toString()).append("\n"));
        System.out.println(output);
    }

    /**
     * Displays all deliveries in a formatted manner.
     */
    public void viewAllDeliveries() {
        StringBuilder output = new StringBuilder("All Deliveries:\n");
        service.getDelivery().forEach(delivery -> output.append(delivery.toString()).append("\n"));
        System.out.println(output);
    }

    /**
     * Creates a new package with specified attributes.
     *
     * @param cost       cost associated with this package
     * @param weight     weight of this package
     * @param dimensions dimensions description for this package
     */
    public void createPackage(double cost, double weight, String dimensions) {
        Integer packageID = service.getNewPackageId();
        service.createPackage(packageID, cost, weight, dimensions);
    }

    public void viewAllPackages() {
        StringBuilder output = new StringBuilder("All Packages:\n");
        service.getPackages().forEach(packages -> output.append(packages.toString()).append("\n"));
        System.out.println(output);
    }

    /**
     * Retrieves unassigned deliveries from all deliveries available in system.
     *
     * @return list containing unassigned deliveries
     */
    public List<Delivery> getUnassignedDeliveries() {
        List<Delivery> deliveries = service.getDelivery();
        List<Delivery> unassignedDeliveries = new ArrayList<>();

        for (Delivery delivery : deliveries) {
            if (delivery.getEmployeeID() == null) {
                unassignedDeliveries.add(delivery);
            }
        }

        return unassignedDeliveries;
    }

    public Packages getPackageById(Integer packageId){
        return service.getPackageById(packageId);
    }

    /**
     * Displays unassigned deliveries in a formatted manner.
     *
     * @param deliveries list containing unassigned deliveries
     */
    public void viewUnassignedDeliveries(List<Delivery> deliveries) {
        StringBuilder output = new StringBuilder("Unassigned Deliveries:\n");

        deliveries.forEach(delivery -> output.append(delivery.toString()).append("\n"));

        System.out.println(output);
    }

    /**
     * Validates whether a selected Delivery exists based on its unique identifier.
     *
     * @param deliveryID unique identifier for Delivery object
     * @return true if it exists; false otherwise
     */
    public boolean validateSelectedDelivery(Integer deliveryID) {
        return service.getDelivery().stream().anyMatch(delivery -> Objects.equals(delivery.getDeliveryID(), deliveryID));
    }

    /**
     * Retrieves all deliveries assigned to given Employee.
     *
     * @param employeeID unique identifier for Employee object
     * @return list containing all deliveries assigned to Employee
     */
    public List<Delivery> getDeliveriesForEmployee(Integer employeeID) {
        return service.getDeliveriesForEmployee(employeeID);
    }

    public List<Delivery> getDeliveriesForDeliveryPerson(Integer deliveryPersonId) {
        return service.getDeliveriesForDeliveryPerson(deliveryPersonId);
    }

    /**
     * Drops an assigned Delivery from Employee.
     *
     * @param employeeID unique identifier for Employee object
     * @param deliveryID unique identifier for Delivery object
     */
    public void dropDelivery(Integer employeeID, Integer deliveryID) {
        service.dropDelivery(employeeID, deliveryID);
        System.out.println("Dropped Delivery with id " + deliveryID + " by Employee with id " + employeeID + " successfully");
    }

    /**
     * Assigns an Employee to an unassigned Delivery.
     *
     * @param employeeID unique identifier for Employee object
     * @param deliveryID unique identifier for Delivery object
     */
    public void assignEmployeeToUnassignedDelivery(Integer employeeID, Integer deliveryID) {
        service.pickDelivery(employeeID, deliveryID);
        System.out.println("Picked Delivery with id " + deliveryID + " by Employee with id " + employeeID + " successfully");
    }

    /**
     * Assigns Delivery Person to an unassigned Delivery.
     *
     * @param //employeeID unique identifier for Employee object
     * @param deliveryID unique identifier for Delivery object
     */

    public void assignDeliveryPersonToUnassignedDelivery(Integer deliveryPersonId, Integer deliveryID) {
        service.pickDelivery(deliveryPersonId, deliveryID);
    }

    /**
     * Retrieves last logged-in Employee's unique identifier.
     *
     * @return last logged-in Employee's unique identifier.
     */

    public Integer getLastLoggedInEmployeeID() {
        return service.getLastLoggedInEmployeeId();
    }

    /**
     * Retrieves last logged-in Delivery Person's unique identifier.
     *
     * @return last logged-in Delivery Person's unique identifier.
     */

    public Integer getLastLoggedInDeliveryPersonID() {
        return service.getLastLoggedInDeliveryPersonId();
    }

    /**
     * Retrieves all available Personal Vehicles that can be assigned.
     *
     * @return list containing all available Personal Vehicles.
     */

    public List<Personal_Vehicle> getAllAvailablePersonalVehicles() {
        List<Personal_Vehicle> allVehicles = service.getPersonalVehicle();
        List<Personal_Vehicle> availableVehicles = new ArrayList<>();
        for (Personal_Vehicle personalVehicle : allVehicles) {
            if (personalVehicle.getDeliveryPersonID() == null) {
                availableVehicles.add(personalVehicle);
            }
        }
        return availableVehicles;
    }

    /**
     * Schedules Delivery date/time for given Order.
     *
     * @param orderID          unique identifier for Order object
     * @param deliveryDateTime date/time when Delivery should occur.
     */

    public void scheduleDeliveryDate(Integer orderID, LocalDateTime deliveryDateTime) {
        service.scheduleDelivery(orderID, deliveryDateTime);
        System.out.println("Scheduled Delivery date for Order with id " + orderID + " successfully");
    }

    /**
     * Displays available Personal Vehicles in formatted manner.
     *
     * @param personalVehicles list containing Personal Vehicles objects.
     */

    public void viewAvailablePersonalVehicles(List<Personal_Vehicle> personalVehicles) {
        StringBuilder output = new StringBuilder("Available Personal Vehicles:\n");
        personalVehicles.forEach(personalVehicle -> output.append(personalVehicle.toString()).append("\n"));
        System.out.println(output);
    }

    /**
     * Validates whether selected Personal Vehicle exists based on its unique identifier.
     *
     * @param id unique identifier for Personal Vehicle object.
     * @return true if it exists; false otherwise.
     */

    public boolean validateSelectedPersonalVehicle(Integer id) {
        return service.getPersonalVehicle().stream().anyMatch(personalVehicle -> Objects.equals(personalVehicle.getId(), id));
    }

    /**
     * Assigns given Personal Vehicle to specified Delivery Person.
     * <p>
     * *@ param deliverer_id unique identifier for Deliverer object.
     *
     * @ param vehicle_id unique identifier for Vehicle object.
     */

    public void assignPersonalVehicle(Integer deliverer_id, Integer vehicle_id) {
        service.assignPersonalVehicle(deliverer_id, vehicle_id);
    }

    /**
     * Retrieves all Transportation Types available in system.
     *
     * @return list containing Transportation Types as Strings.
     */

    public List<String> getAllTransportationTypes() {
        return service.getTransportationTypes();
    }

    /**
     * Displays Transportation Types in formatted manner.
     *
     * @param transportationTypes list containing Transportation Types as Strings.
     */

    public void viewAllTransportationTypes(List<String> transportationTypes) {
        StringBuilder output = new StringBuilder("Transportation Types:\n");
        transportationTypes.forEach(transportationType -> output.append(transportationType).append("\n"));
        System.out.println(output);
    }

    /**
     * Validates whether selected Transportation Type exists.
     *
     * @param transportationType Transportation Type string value.
     * @return true if it exists; false otherwise.
     */

    public boolean verifySelectedTransportationType(String transportationType) {
        return service.getTransportationTypes().contains(transportationType);
    }

    /**
     * Retrieves last logged-in Delivery Person's details.
     *
     * @return Last logged-in Delivery Person object.
     */

    public Delivery_Person getLastLoggedInDeliveryPerson() {
        return service.getLastLoggedInDeliveryPerson();
    }

    /**
     * Retrieves Personal Vehicle details based on given vehicle Id.
     *
     * @param id Unique Identifier for Vehicle.
     * @return Personal Vehicle Object corresponding provided Id.
     */

    public Personal_Vehicle getPersonalVehicle(Integer id) {
        return service.getPersonalVehicle(id);
    }

    /**
     * Displays Order details based on given Order Id.
     *
     * @param Order_ID Unique Identifier corresponding Order Object.
     */

    public void viewOrder(Integer Order_ID) {
        Order Order = getSelectedOrder(Order_ID);
        if (Order != null) {
            System.out.println(Order.toString());
        } else {
            System.out.println("Order with ID " + Order_ID + " not found.");
        }
    }

    /**
     * Retrieves Packages associated with given Order.
     *
     * @param orderId Object corresponding which Packages need retrieval.
     * @return List containing Packages associated provided Order Object.
     */

    public List<Packages> getPackagesFromOrder(Integer orderId) {
        return service.getPackagesFromOrder(orderId);
    }

    /**
     * @return a list of the Deliveries
     */

    public void viewDeliveriesForDeliveryPerson() {
        List<Delivery> deliveries = service.getDeliveriesWithToBeShippedOrders();
//        List<Delivery> deliveries = service.getDelivery();
        StringBuilder output = new StringBuilder("Deliveries suitable for Delivery Person to pick up:\n");
        if (deliveries.isEmpty()) {
            output.append("No deliveries available for 'to be shipped' orders.\n");
        } else {
            deliveries.forEach(delivery -> output.append(delivery.toString()).append("\n"));
        }

        System.out.println(output);
    }

    /**
     * @param location
     */

    public void createDelivery(String location) {
        List<Order> orders = service.filterDeliveriesByLocation(location);
        //String[] statuses = {"processing", "to be shipped", "in hub", "in transit"};
        //Random random = new Random();
        //String status = statuses[random.nextInt(statuses.length)];
        for (Order order : orders) {
            String[] statuses = {"processing", "to be shipped", "in hub", "in transit"};
            Random random = new Random();
            String status = statuses[random.nextInt(statuses.length)];
            order.setStatus(status);
        }
        Integer deliveryId = service.getNewDeliveryId();
        service.createDelivery(deliveryId, orders, location);

    }



    /**
     * @param
     * @return
     */

    public void getdeliveriesSortedByOrderDateTime() {
        List<Delivery> deliveries = service.getDelivery();
        List<Delivery> deliveryList = service.getSortedDeliveriesByOrderDateTime(deliveries);
        StringBuilder output = new StringBuilder("Deliveries suitable for Delivery Person to pick up:\n");
        if (deliveryList.isEmpty()) {
            output.append("No deliveries available for 'to be shipped' orders.\n");
        } else {
            deliveryList.forEach(delivery -> output.append(delivery.toString()).append("\n"));
        }

        System.out.println(output);
    }

    /**
     * @param customerId
     * @return
     */
    public void getOrdersSortedByPriceDescending(Integer customerId) {
        List<Order>orders = service.getOrdersFromCustomers(customerId);
        List<Order>sortedOrders = service.getOrdersSortedByPriceDescending(orders);
        StringBuilder output = new StringBuilder("Available Orders:\n");
        sortedOrders.forEach(order -> output.append(order.toString()).append("\n"));
        System.out.println(output);
    }

    public void removePackage(Integer packageId){
        service.removePackage(packageId);
    }
}
