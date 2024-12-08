import model.*;
import repository.IRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
/**
 * org.example.Service class that manages the business logic for a delivery management system.
 * This class handles operations related to stores, packages, orders, customers,
 * departments, employees, deliveries, deposits, and delivery personnel.
 */
public class Service {
    /**
     * Maximum limit for deposits
     */
    private List<Person> persons = new ArrayList<>();
    protected static final int depozitLimit = 1000;

    /**
     * Constructs a new org.example.Service with all required repositories
     *
     * @param storeIRepository Repository for Store entities
     * @param packageIRepository Repository for Package entities
     * @param orderIRepository Repository for Order entities
     * @param customerIRepository Repository for Customer entities
     * @param departmentIRepository Repository for Department entities
     * @param employeeIRepository Repository for Employee entities
     * @param deliveryIRepository Repository for Delivery entities
     * @param depositIRepository Repository for Deposit entities
     * @param deliveryPersonIRepository Repository for DeliveryPerson entities
     * @param personalVehicleIRepository Repository for PersonalVehicle entities
     */
    private final IRepository<Store> storeIRepository;
    private final IRepository<Packages> packageIRepository;
    private final IRepository<Order> orderIRepository;
    private final IRepository<Customer> customerIRepository;
    private final IRepository<Department> departmentIRepository;
    private final IRepository<Employee> employeeIRepository;
    private final IRepository<Delivery> deliveryIRepository;
    private final IRepository<Deposit> depositIRepository;
    private final IRepository<Delivery_Person> deliveryPersonIRepository;
    private final IRepository<Personal_Vehicle> personalVehicleIRepository;


    public Service(IRepository<Store> storeIRepository, IRepository<Packages> packageIRepository, IRepository<Order> orderIRepository, IRepository<Customer> customerIRepository, IRepository<Department> departmentIRepository, IRepository<Employee> employeeIRepository, IRepository<Delivery> deliveryIRepository, IRepository<Deposit> depositIRepository, IRepository<Delivery_Person> deliveryPersonIRepository, IRepository<Personal_Vehicle> personalVehicleIRepository) {
        this.storeIRepository = storeIRepository;
        this.packageIRepository = packageIRepository;
        this.orderIRepository = orderIRepository;
        this.customerIRepository = customerIRepository;
        this.departmentIRepository = departmentIRepository;
        this.employeeIRepository = employeeIRepository;
        this.deliveryIRepository = deliveryIRepository;
        this.depositIRepository = depositIRepository;
        this.deliveryPersonIRepository = deliveryPersonIRepository;
        this.personalVehicleIRepository = personalVehicleIRepository;
    }

    /**
     * Retrieves all employees from the repository
     *
     * @return List of all employees
     */
    public List<Employee> getEmployees() {
        return employeeIRepository.readAll();
    }
    /**
     * Retrieves all packages associated with a specific order
     *
     * @param orderId The order to get packages for
     * @return List of packages in the order
     */
    public List<Packages> getPackagesFromOrder(Integer orderId) {
        Order order = orderIRepository.get(orderId);
        return order.getPackages();
    }
    public List<Order> getOrdersFromCustomers(Integer customerId) {
        Customer customer = customerIRepository.get(customerId);
        return customer.getOrders();
    }

    public List<Order> getOrders() {
        return orderIRepository.readAll();
    }

    public List<Customer> getCustomers() {
        return customerIRepository.readAll();
    }

    public List<Department> getDepartments() {
        return departmentIRepository.readAll();
    }

    public List<Delivery> getDelivery() {
        return deliveryIRepository.readAll();
    }

    public List<Delivery_Person> getDeliveryPerson() {
        return deliveryPersonIRepository.readAll();
    }

    public List<Store> getStores() {
        return storeIRepository.readAll();
    }

    public List<Personal_Vehicle> getPersonalVehicle() {
        return personalVehicleIRepository.readAll();
    }

    public List<Packages> getPackages() {
        return packageIRepository.readAll();
    }

    public List<Deposit> getDeposits(){
        return depositIRepository.readAll();
    }

    public List<String> getTransportationTypes(){
        List<String> enumNames = Stream.of(Transportation_Type.values())
                .map(Enum::name)
                .collect(Collectors.toList());

        return enumNames;
    }
    //TODO the complex method that combines three tables Customers, Package and Order
    /**
     * Places a new order in the system
     *
     * @param customerId Customer's unique identifier
     * @param orderID Order's unique identifier
     * @param orderDate Date when the order was placed
     * @param deliveryDateTime Scheduled delivery date and time
     * @param /cost Total cost of the order
     * @param /status Current status of the order
     */

    public void placeOrder(Integer customerId, Integer orderID, Date orderDate, LocalDateTime deliveryDateTime, List<Integer> packageIds) {
        Customer customer = customerIRepository.get(customerId);
        if (customer == null) {
            throw new IllegalArgumentException("Customer not found for ID: " + customerId);
        }

        String location = customer.getAddress();
        Order order = new Order(orderID, customerId, orderDate, deliveryDateTime);
        order.setLocation(location);

        for (Integer packageId : packageIds) {
            Packages packages = packageIRepository.get(packageId);
            if (packages != null) {
                order.addPackage(packages);
            }
        }

        customer.addDOrder(order);
        order.setCustomerID(customerId);

        orderIRepository.create(order);
        customerIRepository.update(customer);

        double totalCost = calculateAndUpdateOrderCost(orderID);
        order.setCost(totalCost);
    }



    public void removeOrder(Integer customerId, Integer orderID) {
        Customer customer = customerIRepository.get(customerId);
        if (customer != null && customer.getOrders() != null) {
            Order order = orderIRepository.get(orderID);

            if (order != null && customer.getOrders().remove(order)) {
                // Successfully removed order from customer's orders list
                orderIRepository.delete(orderID); // Delete the order from repository
                customerIRepository.update(customer);
            }
        }
    }
    /**
     * Schedules a delivery for a specific order
     *
     * @param orderId ID of the order to schedule delivery for
     * @param deliveryDateTime Date and time when delivery should occur
     * @throws IllegalArgumentException if order with specified ID is not found
     */
    public void scheduleDelivery(Integer orderId, LocalDateTime deliveryDateTime) {
        Order order = orderIRepository.get(orderId);
        if (order != null) {
            order.setDeliveryDateTime(deliveryDateTime);
            orderIRepository.update(order);
        } else {
            throw new IllegalArgumentException("Order with ID " + orderId + " not found.");
        }
    }

    public void deleteCustomer(Integer customerId) {
        Customer customer = customerIRepository.get(customerId);
        if (customer != null) {
            customerIRepository.delete(customerId);
        }
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
    // Create a new DeliveryPerson instance
    Delivery_Person deliveryPerson = new Delivery_Person(deliveryPersonId,name,phone);
    // Add the DeliveryPerson instance to both repositories if needed
    deliveryPerson.setVerified(true);
    deliveryPersonIRepository.create(deliveryPerson);
    //employeeIRepository.create(deliveryPerson);
    }
    /**
     * Registers a new store in the system
     *
     * @param storeId Unique identifier for the store
     * @param name Name of the store
     * @param address Physical address of the store
     * @param contact Contact information for the store
     * @throws IllegalArgumentException if any required field is null or empty
     */
    public void registerStore(Integer storeId, String name, String address, String contact) {
        if (storeId == null || name == null || name.isEmpty() || address == null || address.isEmpty() || contact == null || contact.isEmpty()) {
            throw new IllegalArgumentException("All fields are required for shop registration.");
        }
        Store newStore = new Store(storeId, name, address, contact);
        storeIRepository.create(newStore);
    }

    public void registerDeposit(Integer depositId, Integer storeId, String address, String status) {
        if (depositId == null || storeId == null ||
                address == null || address.isEmpty() ||
                status == null || status.isEmpty()) {
            throw new IllegalArgumentException("All fields are required for deposit registration.");
        }
        Deposit newDeposit = new Deposit(depositId, address, status, storeId);
        depositIRepository.create(newDeposit);
        Store store = storeIRepository.get(storeId);
        if (store != null) {
            store.addDeposit(newDeposit);
            storeIRepository.update(store);
        } else {
            throw new IllegalArgumentException("Deposit with ID " + storeId + " not found.");
        }
    }

    //TODO still not working
    public void removeStore(Integer storeId) {
        Store store = storeIRepository.get(storeId);

        if (store == null) {
            throw new IllegalArgumentException("Store with ID " + storeId + " does not exist.");
        }

        // get the deposits associated
        List<Deposit> deposits = store.getDeposits();


        for (Deposit deposit : deposits) {
            //null not supported by the fromCsv method
            deposit.setStoreID(0);
            depositIRepository.update(deposit);
        }

        // Delete the store from the repository
        storeIRepository.delete(storeId);

    }

    public void removeDeposit(Integer storeId, Integer depositId) {
        //Deposit deposit = depositIRepository.get(depositId);
        Store store = storeIRepository.get(storeId);
        store.getDeposits().removeIf(deposit -> Objects.equals(deposit.getDepositID(), depositId));
            depositIRepository.delete(depositId);
            storeIRepository.update(store);
    }

    public void createCustomer(Integer Id, String name, String address, String phone, String email) {
        Customer customer = new Customer(Id, name, address, phone, email);
        customerIRepository.create(customer);
    }

    public void createEmployee(Integer Id, Integer departmentId, String name, String phone, String license) {
        Employee employee = new Employee(Id, departmentId, name, phone, license);
        employeeIRepository.create(employee);
        Department department = departmentIRepository.get(departmentId);
        if (department != null) {
            department.addEmployee(employee);
        } else {
            throw new IllegalArgumentException("Department with ID " + departmentId + " not found.");
        }
    }
    public void pickDelivery(Integer employeeId ,Integer deliveryId) {
        Delivery delivery = deliveryIRepository.get(deliveryId);
        Employee employee = employeeIRepository.get(employeeId);
        if (delivery != null && employee != null) {
            employee.addDelivery(delivery);
            delivery.setEmployeeID(employeeId);
            employeeIRepository.update(employee);
            deliveryIRepository.update(delivery);
        }
        else
            throw new IllegalArgumentException("Delivery with ID " + deliveryId + " not found.");
    }

    public void pickDeliveryToPerson(Integer deliverPersonId, Integer deliveryId) {
        Delivery delivery = deliveryIRepository.get(deliveryId);
        Delivery_Person deliveryPerson = deliveryPersonIRepository.get(deliverPersonId);
        if (delivery != null && deliveryPerson != null) {
            deliveryPerson.addDelivery(delivery);
            deliveryPersonIRepository.update(deliveryPerson);
            delivery.setDeliveryPeronID(deliverPersonId);
            deliveryIRepository.update(delivery);
        }
    }

    /**
     *
     * @param deliveryId
     */
    public void removeDelivery(Integer deliveryId) {
        Delivery delivery = deliveryIRepository.get(deliveryId);
        Employee employee = employeeIRepository.get(delivery.getEmployeeID());
        employee.getDeliveries().removeIf(d -> d.getDeliveryID().equals(deliveryId));
        employeeIRepository.update(employee);
        deliveryIRepository.delete(deliveryId);
    }

    public void removeDeliveryToPerson(Integer deliveryId) {
        Delivery delivery = deliveryIRepository.get(deliveryId);
        Delivery_Person deliveryPerson = deliveryPersonIRepository.get(delivery.getDeliveryPeronID());
        deliveryPerson.getDeliveries().removeIf(d -> d.getDeliveryID().equals(deliveryId));
        deliveryPersonIRepository.update(deliveryPerson);
        deliveryIRepository.delete(deliveryId);
    }
    /**
     * Calculates and updates the total cost of an order based on its packages
     *
     * @param orderId ID of the order to calculate cost for
     */
    public double calculateAndUpdateOrderCost(Integer orderId) {
        Order order = orderIRepository.get(orderId);
        double totalCost = order.getPackages().stream()
                .mapToDouble(Packages::getCost)
                .sum();
        order.setCost(totalCost);
        orderIRepository.update(order);
        return totalCost;
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
            return false;
        }
    }
    //TODO first filtering method
    /** filtering Delivberies that have the order status as to be shipped, because a DeliveryPerson can only pick up one of these
     *
     * @return a list of these orders
     */

    /**
     * Retrieves a list of deliveries that contain at least one order with the status "to be shipped".
     *
     * @return a list of deliveries where at least one order is marked with the status "to be shipped".
     */
    public List<Delivery> getDeliveriesWithToBeShippedOrders() {
        List<Delivery> allDeliveries = deliveryIRepository.readAll();

        return allDeliveries.stream()
                .filter(delivery -> delivery.getOrders().stream()
                        .anyMatch(order -> "to be shipped".equalsIgnoreCase(order.getStatus())))
                .collect(Collectors.toList());
    }

    /**
     * Filters orders based on their delivery location.
     *
     * @param location the location to filter orders by (case-insensitive).
     *                 If {@code null}, no orders will be returned.
     * @return a list of orders that match the specified location.
     */

    //TODO complex
    public List<Order> filterDeliveriesByLocation(String location) {
        List<Order> allOrders = orderIRepository.readAll();
        List<Order> filteredOrders = new ArrayList<>();
        for (Order order : allOrders) {
            if (location != null && location.equalsIgnoreCase(order.getLocation())) {
                filteredOrders.add(order);
            }
        }
        return filteredOrders;
    }

    /**
     * Sorts a list of deliveries based on the earliest delivery date and time of their orders.
     *
     * @param deliveries the list of deliveries to sort.
     * @return a new list of deliveries sorted in ascending order by their earliest order's delivery date and time.
     *         Deliveries without any orders will appear last.
     */
    public List<Delivery> getSortedDeliveriesByOrderDateTime(List<Delivery> deliveries) {
        return deliveries.stream()
                .sorted(Comparator.comparing(delivery ->
                        delivery.getOrders().stream()
                                .map(Order::getDeliveryDateTime)
                                .min(Comparator.naturalOrder())
                                .orElse(LocalDateTime.MAX)))
                .collect(Collectors.toList());
    }

    /**
     * Sorts a list of orders in descending order by their cost.
     *
     * @param orders the list of orders to sort.
     * @return a new list of orders sorted in descending order by price.
     */
    public List<Order> getOrdersSortedByPriceDescending(List<Order> orders) {
        return orders.stream()
                .sorted(Comparator.comparingDouble(Order::getCost).reversed())
                .collect(Collectors.toList());
    }

    /** Creates a new delivery with the specified delivery ID, orders, and location,
            * and adds it to the repository.
            *
            * @param deliveryid the unique identifier for the new delivery. Must not be {@code null}.
            * @param orders     the list of orders to associate with the delivery.
            *                   If {@code null} or empty, no orders will be added.
            * @param location   the location for the delivery. Can be {@code null}.
            */
    public void createDelivery(Integer deliveryid, List<Order> orders, String location) {
        Delivery delivery = new Delivery(deliveryid);
        deliveryIRepository.create(delivery);
        delivery.setLocation(location);
        if (orders != null) {
            for (Order order : orders) {
                delivery.addOrder(order);
            }
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

    public void unenrollEmployee(Integer employeeId) {
        Employee employee = employeeIRepository.get(employeeId);
        Department department = departmentIRepository.get(employee.getDepartmentID());
        if (department != null) {
            department.removeEmployee(employee);
        }
        employeeIRepository.delete(employeeId);

    }
    public void unenrollDeliveryPerson(Integer deliveryPersonId) {
        //Delivery_Person deliveryPerson = deliveryPersonIRepository.get(deliveryPersonId);
        deliveryPersonIRepository.delete(deliveryPersonId);
    }
    /**
     * Generates a new unique customer ID
     *
     * @return Next available customer ID
     */
    public Integer getNewCustomerId() {
        int maxId = 0;
        for (Integer Id : customerIRepository.getKeys()) {
            if (Id.compareTo(maxId) > 0) {
                maxId = Id;
            }
        }
        return maxId + 1;
    }
    public Integer getNewDeliveryId() {
        int maxId = 0;
        for (Integer Id : deliveryIRepository.getKeys()) {
            if (Id.compareTo(maxId) > 0) {
                maxId = Id;
            }
        }
        return maxId + 1;
    }

    public Integer getLastLoggedInCustomerId() {
        int maxId = 0;
        for (Integer Id : customerIRepository.getKeys()) {
            if (Id.compareTo(maxId) > 0) {
                maxId = Id;
            }
        }
        return maxId;
    }


    public Integer getNewDepositId() {
        int maxId = 0;
        for (Integer Id : depositIRepository.getKeys()) {
            if (Id.compareTo(maxId) > 0) {
                maxId = Id;
            }
        }
        return maxId + 1;
    }

    public Integer getNewEmployeeId() {
        int maxId = 0;
        for (Integer Id : employeeIRepository.getKeys()) {
            if (Id.compareTo(maxId) > 0) {
                maxId = Id;
            }
        }
        return maxId + 1;
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

    public Integer getNewStoreId() {
        int maxId = 0;
        for (Integer Id : storeIRepository.getKeys()) {
            if (Id.compareTo(maxId) > 0) {
                maxId = Id;
            }
        }
        return maxId + 1;
    }

    public Integer getNewOrderId() {
        int maxId = 0;
        for (Integer Id : orderIRepository.getKeys()) {
            if (Id.compareTo(maxId) > 0) {
                maxId = Id;
            }
        }
        return maxId + 1;
    }

    public Integer getNewPackageId(){
        int maxId = 0;
        for (Integer Id : packageIRepository.getKeys()) {
            if (Id.compareTo(maxId) > 0) {
                maxId = Id;
            }
        }
        return maxId + 1;
    }

    public void createPackage(Integer packageId, double cost, double weight, String dimensions){
        Packages packages = new Packages(packageId, weight, dimensions, cost);
        packageIRepository.create(packages);
    }

    public void removePackage(Integer packageId){
        Packages packages = packageIRepository.get(packageId);
        if (packages != null) {
            customerIRepository.delete(packageId);
        }
    }

    public Packages getPackageById(Integer packageId){
        return packageIRepository.get(packageId);
    }

    public Integer getLastLoggedInEmployeeId() {
        int maxId = 0;
        for (Integer Id : employeeIRepository.getKeys()) {
            if (Id.compareTo(maxId) > 0) {
                maxId = Id;
            }
        }
        return maxId;
    }

    public Integer getLastLoggedInDeliveryPersonId() {
        int maxId = 0;
        for (Integer Id : deliveryPersonIRepository.getKeys()) {
            if (Id.compareTo(maxId) > 0) {
                maxId = Id;
            }
        }
        return maxId;
    }
    /**
     * Assigns a personal vehicle to a delivery person
     *
     * @param deliveryPersonId ID of the delivery person
     * @param personalVehicleId ID of the personal vehicle to assign
     */
    public void assignPersonalVehicle(Integer deliveryPersonId, Integer personalVehicleId){
        Delivery_Person deliveryPerson = deliveryPersonIRepository.get(deliveryPersonId);
        Personal_Vehicle personalVehicle = personalVehicleIRepository.get(personalVehicleId);

        //deliveryPerson.setPersonalVehicleId(personalVehicleId);
        personalVehicle.setDeliveryPersonID(deliveryPersonId);
        deliveryPerson.setPersonalVehicleId(personalVehicleId);
        //deliveryPersonIRepository.update(deliveryPerson);
        personalVehicleIRepository.update(personalVehicle);
    }

    public Delivery_Person getLastLoggedInDeliveryPerson() {
        Integer Id = getLastLoggedInDeliveryPersonId();
        Delivery_Person deliveryPerson = deliveryPersonIRepository.get(Id);

        return deliveryPerson;
    }

    public Personal_Vehicle getPersonalVehicle(Integer Id){
        return personalVehicleIRepository.get(Id);
    }
    /**
     * Retrieves all deliveries assigned to a specific employee
     *
     * @param employeeId ID of the employee
     * @return List of deliveries assigned to the employee
     */
    public List<Delivery> getDeliveriesForEmployee(Integer employeeId) {
        Employee employee = employeeIRepository.get(employeeId);
        return employee.getDeliveries();
    }

    public List<Delivery> getDeliveriesForDeliveryPerson(Integer deliveryPersonId) {
        Delivery_Person deliveryPerson = deliveryPersonIRepository.get(deliveryPersonId);
        return deliveryPerson.getDeliveries();
    }

    /**
     * Removes a delivery assignment from an employee
     *
     * @param employeeId ID of the employee
     * @param deliveryId ID of the delivery to remove
     * @throws IllegalArgumentException if employee or delivery not found
     */
    public void dropDelivery(Integer employeeId, Integer deliveryId) {
        Employee employee = employeeIRepository.get(employeeId);
        Delivery delivery = deliveryIRepository.get(deliveryId);
        if (employee != null && delivery != null) {
            employee.removeDeliv(deliveryId);
            delivery.setEmployeeID(0);
            employeeIRepository.update(employee);
            deliveryIRepository.update(delivery);
        } else {
            throw new IllegalArgumentException("Employee or Delivery not found.");
        }
    }
    public List<Employee> getAllEmployees() {
        return persons.stream()
                .filter(p -> p instanceof Employee && !(p instanceof Delivery_Person)) // Exclude DeliveryPerson
                .map(p -> (Employee) p)
                .collect(Collectors.toList());
    }

    public List<Delivery_Person> getAllDeliveryPersons() {
        return persons.stream()
                .filter(p -> p instanceof Delivery_Person)
                .map(p -> (Delivery_Person) p)
                .collect(Collectors.toList());
    }
}
