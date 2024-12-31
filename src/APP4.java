import controller.*;
import exceptions.ValidationException;
import helpers.Validation;
import model.*;
import repository.*;
import service.*;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Main application class for the delivery management system.
 * This class provides a console-based user interface for interacting with different
 * aspects of the delivery system including users, sellers, customers, employees,
 * and delivery persons.
 */
public class APP4 {
    private CustomerController customerController;
    private EmployeeController employeeController;
    private SellerController sellerController;
    private DeliveryPersonController deliveryPersonController;
    private UserController userController;

    private Scanner scanner;

    public APP4(CustomerController customerController, EmployeeController employeeController, SellerController sellerController, DeliveryPersonController deliveryPersonController, UserController userController) throws SQLException {
        this.customerController = customerController;
        this.employeeController = employeeController;
        this.sellerController = sellerController;
        this.deliveryPersonController = deliveryPersonController;
        this.userController = userController;
        this.scanner = new Scanner(System.in);
        createUI();
    }

    /**
     * Creates and manages the main user interface loop.
     * Displays the main menu and handles user input for navigation between different menus.
     */
    private void createUI() throws SQLException {
        while (true) {
            System.out.println("Main Menu:");
            System.out.println("1. User");
            System.out.println("2. Seller");
            System.out.println("3. Customer");
            System.out.println("4. Employee");
            System.out.println("5. Delivery Person");
            System.out.println("6. Exit");
            System.out.print("Select an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    userMenu();
                    break;
                case 2:
                    sellerMenu();
                    break;
                case 3:
                    customerMenu();
                    break;
                case 4:
                    employeeMenu();
                    break;
                case 5:
                    deliveryPersonMenu();
                    break;
                case 6:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    /**
     * Manages the user menu interface.
     * Provides functionality for viewing transportation types and managing user accounts.
     */
    private void userMenu() {
        while (true) {
            System.out.println("User Menu:");
            System.out.println("1. View All Transportation Types");
            System.out.println("2. Delete Customer");
            System.out.println("3. Delete Employee");
            System.out.println("4. Delete Delivery Person");
            System.out.println("5. Create Department");
            System.out.println("6. Back to Main Menu");
            System.out.print("Select an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    List<String> transportationTypes = userController.getAllTransportationTypes();
                    userController.viewAllTransportationTypes(transportationTypes);
                    break;
                case 2:
                    userController.viewAllCustomers();
                    System.out.print("Enter Customer ID: ");
                    int customerId = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    userController.deleteCustomer(customerId);
                    userController.viewAllCustomers();
                    break;
                case 3:
                    userController.viewAllEmployees();
                    System.out.print("Enter Employee ID: ");
                    int employeeId = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    userController.deleteEmployee(employeeId);
                    userController.viewAllEmployees();
                    break;
                case 4:
                    userController.viewAllDeliveryPersons();

                    System.out.print("Enter Delivery Person ID: ");
                    int deliveryPersonId = scanner.nextInt();
                    scanner.nextLine();

                    userController.deleteDeliveryPerson(deliveryPersonId);
                    userController.viewAllDeliveryPersons();

                    break;
                case 5:
                    String name;
                    while (true){
                        try {
                            System.out.print("Enter Department Name: ");
                            name = scanner.nextLine();
                            Validation.validateName(name);
                            break;
                        }catch (ValidationException e){
                            System.out.println(e.getMessage());
                        }
                    }

                    System.out.print("Enter Department Task: ");
                    String task = scanner.nextLine();
                    userController.createDepartment(name, task);
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    /**
     * Manages the seller menu interface.
     * Provides functionality for managing stores, deposits, and packages.
     */
    private void sellerMenu() {
        while (true) {
            System.out.println("Seller Menu:");
            System.out.println("1. View All Stores");
            System.out.println("2. Create Store");
            System.out.println("3. Register Deposit");
            System.out.println("4. View All Deposits");
            System.out.println("5. Delete Store");
            System.out.println("6. Delete Deposit");
            System.out.println("7. Create Package");
            System.out.println("8. Remove Package");
            System.out.println("9. View all Packages");
            System.out.println("10. Create a Delivery");
            System.out.println("11. Back to Main Menu");
            System.out.println("12. View Packages from Order");
            System.out.print("Select an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    sellerController.viewAllStores();
                    break;
                case 2:
                    String storeName;
                    while (true){
                        try {
                            System.out.print("Enter Store Name: ");
                            storeName = scanner.nextLine();
                            Validation.validateName(storeName);
                            break;
                        }catch (ValidationException e){
                            System.out.println(e.getMessage());
                        }
                    }

                    String storeAddress;
                    while (true){
                        try {
                            System.out.print("Enter Store Address: ");
                            storeAddress = scanner.nextLine();
                            Validation.validateAddress(storeAddress);
                            break;
                        }catch (ValidationException e){
                            System.out.println(e.getMessage());
                        }
                    }

                    String storeContact;
                    while (true){
                        try {
                            System.out.print("Enter Store Contact: ");
                            storeContact = scanner.nextLine();
                            Validation.validateName(storeContact);
                            break;
                        }catch (ValidationException e){
                            System.out.println(e.getMessage());
                        }
                    }

                    sellerController.createStore(storeName, storeAddress, storeContact);
                    break;
                case 3:
                    System.out.print("Enter Store ID: ");
                    int storeId = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    String depositAddress;
                    while (true) {
                        try {
                            System.out.print("Enter Deposit Address: ");
                            depositAddress = scanner.nextLine();
                            Validation.validateAddress(depositAddress);
                            break;
                        }catch (ValidationException e){
                            System.out.println(e.getMessage());
                        }
                    }

                    System.out.print("Enter Deposit Status: ");
                    String depositStatus = scanner.nextLine();
                    sellerController.registerDeposit(storeId, depositAddress, depositStatus);
                    break;
                case 4:
                    sellerController.viewAllDeposits();
                    break;
                case 5:
                    System.out.print("Enter Store ID: ");
                    int deleteStoreId = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    sellerController.deleteStore(deleteStoreId);
                    break;
                case 6:
                    System.out.print("Enter Store ID: ");
                    int storeIdForDeposit = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    System.out.print("Enter Deposit ID: ");
                    int depositId = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    sellerController.deleteDeposit(storeIdForDeposit, depositId);
                    break;
                case 7:
                    try {
                        System.out.print("Enter Package Cost: ");
                        double cost = scanner.nextDouble();
                        scanner.nextLine(); // Consume newline
                        System.out.print("Enter Package Weight: ");
                        double weight = scanner.nextDouble();
                        scanner.nextLine(); // Consume newline
                        System.out.print("Enter Package Dimensions: ");
                        String dimensions = scanner.nextLine();
                        sellerController.createPackage(cost, weight, dimensions);
                    }catch (ValidationException e){
                        System.out.println(e.getMessage());
                    }
                    break;
                case 8:
                    sellerController.viewAllPackages();
                    System.out.print("Enter Package ID: ");
                    Integer packageId = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    // Assuming there's a method to remove a package
                    //TODO crapa in InFileRepository, nu il gaseste sa l stearga
                    //
                    sellerController.removePackage(packageId);
                    break;
                case 9:
                    sellerController.viewAllPackages();
                    break;
                case 10:
                    try {
                        System.out.println("Enter a location for a Delivery to be created:");
                        String location = scanner.nextLine();
                        sellerController.createDelivery(location);
                    }catch (ValidationException e){
                        System.out.println(e.getMessage());
                    }
                case 11:
                    return;
                case 12:
                    System.out.print("Enter Order ID: ");
                    int orderId = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    sellerController.viewPackagesFromOrder(orderId);
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    /**
     * Manages the customer menu interface.
     * Provides functionality for customer management and order processing.
     */
    private void customerMenu() throws SQLException {
        while (true) {
            System.out.println("Customer Menu:");
            System.out.println("1. View All Customers");
            System.out.println("2. Create Customer");
            System.out.println("3. Make an Order");
            System.out.println("4. Remove an Order");
            System.out.println("5. View Personal Orders");
            System.out.println("6. Calculate Order Cost");
            System.out.println("7. Reschedule Delivery Date");
            System.out.println("8. View Orders sorted by price");
            System.out.println("9. Back to Main Menu");
            System.out.print("Select an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    customerController.viewAllCustomers();
                    break;
                case 2:
                    String name;
                    while (true){
                        try {
                            System.out.print("Enter Name: ");
                            name = scanner.nextLine();
                            Validation.validateName(name);
                            break;
                        }catch (ValidationException e){
                            System.out.println(e.getMessage());
                        }
                    }

                    String address;
                    while (true){
                        try {
                            System.out.print("Enter Address: ");
                            address = scanner.nextLine();
                            Validation.validateAddress(address);
                            break;
                        }catch (ValidationException e){
                            System.out.println(e.getMessage());
                        }
                    }

                    String phone;
                    while (true){
                        try {
                            System.out.print("Enter Phone: ");
                            phone = scanner.nextLine();
                            Validation.validatePhoneNumber(phone);
                            break;
                        }catch (ValidationException e){
                            System.out.println(e.getMessage());
                        }
                    }

                    String email;
                    while (true){
                        try {
                            System.out.print("Enter Email: ");
                            email = scanner.nextLine();
                            Validation.validateEmail(email);
                            break;
                        }catch (ValidationException e){
                            System.out.println(e.getMessage());
                        }
                    }

                    customerController.createLoggedInCustomer(name, address, phone, email);
                    break;
                case 3:
                    //TODO The delivery date has to be at a minimum of 1 day after the date of the order placement
                    //folosesc LocalDateTime.now() ca sa calculez diferenta

                    System.out.print("Enter customer ID: ");
                    Integer customerId = scanner.nextInt();
                    scanner.nextLine();

                    LocalDateTime dateTime;
                    while (true){
                        try {
                            System.out.println("Delivery Date and Time: ");
                            System.out.println("=======================================================");
                            System.out.println("Please enter the date and time in the following format: yyyy-MM-ddThh:mm,");
                            System.out.println("Where:");
                            System.out.println("y = year");
                            System.out.println("M = month");
                            System.out.println("d = day");
                            System.out.println("h = hour");
                            System.out.println("m = minutes");
                            System.out.println("=======================================================");
                            System.out.println("!!!IF YOU DONT WANT THE SPECIFIC MINUTES, ENTER: hh:00");
                            System.out.println("=======================================================");
                            System.out.println("DISCLAIMER: The order might not arrive in the exact specified minute");
                            System.out.println("=======================================================");

                            dateTime = LocalDateTime.parse(scanner.nextLine());
                            Validation.validateDeliveryDateTime(LocalDateTime.now(), dateTime);
                            break;
                        }catch (ValidationException | IllegalArgumentException e){
                            System.out.println(e.getMessage());
                        }
                    }

                    List<Integer> packageIds = new ArrayList<>();
                    System.out.println("How many Packages do you want to add to the order ?");
                    int numberPackages = scanner.nextInt();
                    scanner.nextLine();

                    for (int i = 0; i < numberPackages; i++) {
                        customerController.viewAllPackages();

                        System.out.println("The package you want to add:");
                        Integer packageId = scanner.nextInt();
                        scanner.nextLine();
                        packageIds.add(packageId);

                    }
                    customerController.makeAnOrder(customerId, dateTime, packageIds);
                    break;
                case 4:
                    System.out.print("Enter Customer ID: ");
                    int removeCustomerId = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    System.out.print("Enter Order ID: ");
                    int orderId = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    customerController.removeAnOrder(removeCustomerId, orderId);
                    break;
                case 5:
                    System.out.print("Enter Customer ID: ");
                    int personalCustomerId = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    customerController.viewPersonalOrders(personalCustomerId);
                    break;
                case 6:
                    System.out.print("Enter Customer ID: ");
                    int customerId1 = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    customerController.viewPersonalOrders(customerId1);
                    System.out.println("Pick an order");
                    int orderCostId = scanner.nextInt();
                    //List<Packages> packages = customerController.getPackagesFromOrder(orderCostId);
                    double totalCost = customerController.calculateOrderCost(orderCostId);
                    System.out.println("Total Order Cost: " + totalCost);

                    break;
                case 7:
                    System.out.print("Enter order ID: ");
                    Integer orderIdForSchedule = Integer.parseInt(scanner.nextLine());

                    Order order = customerController.getSpecificOrder(orderIdForSchedule);
                    LocalDateTime deliveryDateTimeForSchedule;
                    while (true) {
                        try {
                            System.out.println("Delivery Date and Time: ");
                            System.out.println("=======================================================");
                            System.out.println("Please enter the date and time in the following format: yyyy-MM-ddThh:mm,");
                            System.out.println("Where:");
                            System.out.println("y = year");
                            System.out.println("M = month");
                            System.out.println("d = day");
                            System.out.println("h = hour");
                            System.out.println("m = minutes");
                            System.out.println("=======================================================");
                            System.out.println("!!!IF YOU DONT WANT THE SPECIFIC MINUTES, ENTER: hh:00");
                            System.out.println("=======================================================");
                            System.out.println("DISCLAIMER: The order might not arrive in the exact specified minute");
                            System.out.println("=======================================================");

                            deliveryDateTimeForSchedule = LocalDateTime.parse(scanner.nextLine());
                            Validation.validateDeliveryDateTime(order.getOrderDate(), deliveryDateTimeForSchedule);
                            break;
                        }catch (ValidationException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                    customerController.scheduleDeliveryDate(orderIdForSchedule, deliveryDateTimeForSchedule);
                    break;
                case 8:
                    System.out.println("Enter Customer ID: ");
                    Integer customerSortId = scanner.nextInt();
                    customerController.getOrdersSortedByPriceDescending(customerSortId);
                    break;
                case 9:
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    /**
     * Manages the employee menu interface.
     * Provides functionality for employee management and delivery assignments.
     */
    private void employeeMenu() {
        while (true) {
            System.out.println("Employee Menu:");
            System.out.println("1. View All Employees");
            System.out.println("2. Create Employee");
            System.out.println("3. View My Deliveries");
            System.out.println("4. Drop Delivery");
            System.out.println("5. Pick Delivery");
            System.out.println("6. View All Deliveries Sorted by DeliveryDate");
            System.out.println("7. View All Departments");
            System.out.println("8. View All Deliveries");
            System.out.println("9. Back to Main Menu");
            System.out.print("Select an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    employeeController.viewAllEmployees();
                    break;
                case 2:
                    System.out.print("Enter Department ID: ");
                    int departmentId = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    String name;
                    while (true){
                        try {
                            System.out.print("Enter Name: ");
                            name = scanner.nextLine();
                            Validation.validateName(name);
                            break;
                        }catch (ValidationException e){
                            System.out.println(e.getMessage());
                        }
                    }

                    String phone;
                    while (true){
                        try {
                            System.out.print("Enter Phone: ");
                            phone = scanner.nextLine();
                            Validation.validatePhoneNumber(phone);
                            break;
                        }catch (ValidationException e){
                            System.out.println(e.getMessage());
                        }
                    }

                    System.out.print("Enter License: ");
                    String license = scanner.nextLine();

                    //TODO
                    /*
                    org.postgresql.util.PSQLException: ERROR: column "departmentid" does not exist
                    Hint: Perhaps you meant to reference the column "departments.departmentID".
                       Position: 33
                     */
                    employeeController.createEmployee(departmentId, name, phone, license);
                    break;
                case 3:
                    System.out.print("Enter Employee ID: ");
                    int employeeId = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    List<Delivery> deliveries = employeeController.getDeliveriesForEmployee(employeeId);
                    System.out.println("Deliveries for Employee ID " + employeeId + ":");
                    deliveries.forEach(delivery -> System.out.println(delivery.toString()));
                    break;
                case 4:
                    System.out.print("Enter Employee ID: ");
                    int empId = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    System.out.print("Enter Delivery ID: ");
                    int delId = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    if (employeeController.validateSelectedDelivery(delId)) {
                        employeeController.dropDelivery(empId, delId);
                    } else {
                        System.out.println("Invalid Delivery ID.");
                    }
                    break;
                case 5:
                    System.out.print("Enter Employee ID: ");
                    int empIdForDelivery = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    System.out.print("Enter Delivery ID: ");
                    int deliveryIdForEmployee = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    employeeController.assignEmployeeToUnassignedDelivery(empIdForDelivery, deliveryIdForEmployee);
                    break;
                case 6:
                    employeeController.getdeliveriesSortedByOrderDateTime();
                    break;
                case 7:
                    employeeController.viewAllDepartments();
                    break;
                case 8:
                    employeeController.viewAllDeliveries();
                    break;
                case 9:
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    /**
     * Manages the delivery person menu interface.
     * Provides functionality for delivery person management and delivery assignments.
     */
    private void deliveryPersonMenu() {
        while (true) {
            System.out.println("Delivery Person Menu:");
            System.out.println("1. View All Deliveries");
            System.out.println("2. Create Delivery Person");
            System.out.println("3. Pick Delivery by Person");
            System.out.println("4. Assign Personal Vehicle");
            System.out.println("5. View my deliveries");
            System.out.println("6. View All Delivery People ");
            System.out.println("7. Back to Main Menu");
            System.out.print("Select an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    deliveryPersonController.viewAllDeliveries();
                    break;
                case 2:
                    String name;
                    while (true){
                        try {
                            System.out.print("Enter Name: ");
                            name = scanner.nextLine();
                            Validation.validateName(name);
                            break;
                        }catch (ValidationException e){
                            System.out.println(e.getMessage());
                        }
                    }

                    String phone;
                    while (true){
                        try {
                            System.out.print("Enter Phone: ");
                            phone = scanner.nextLine();
                            Validation.validatePhoneNumber(phone);
                            break;
                        }catch (ValidationException e){
                            System.out.println(e.getMessage());
                        }
                    }

                    System.out.print("Enter License: ");
                    String license = scanner.nextLine();

                    deliveryPersonController.createDeliveryPerson(name, phone, license);
                    break;
                case 3:
                    System.out.print("Enter Delivery Person ID: ");
                    int deliveryPersonId = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    deliveryPersonController.viewDeliveriesForDeliveryPerson();
                    System.out.print("Enter Delivery ID: ");
                    int deliveryId = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    /*
                    System.out.println("Enter the location to deliver");
                    String location = scanner.nextLine();
                    deliveryPersonController.filterDeliveriesByLocation(location);
                     */
                    if (deliveryPersonController.validateSelectedDelivery(deliveryId)) {
                        deliveryPersonController.pickDeliveryByPerson(deliveryPersonId, deliveryId);
                    } else {
                        System.out.println("Invalid Delivery ID.");
                    }
                    break;
                case 4:
                    System.out.print("Enter Delivery Person ID: ");
                    int dpId = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    System.out.print("Enter Personal Vehicle ID: ");
                    int pvId = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    deliveryPersonController.assignPersonalVehicle(dpId, pvId);
                    break;
                case 5:
                    System.out.print("Enter Delivery Person ID: ");
                    Integer deliveryPersonID = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    //deliveryPersonController.getDeliveriesForDeliveryPerson(deliveryPersonID);

                    List<Delivery> deliveries = deliveryPersonController.getDeliveriesForDeliveryPerson(deliveryPersonID);

                    for (Delivery delivery: deliveries){
                        System.out.println(delivery);
                    }
                    break;
                case 6:
                    deliveryPersonController.viewAllDeliveryPersons();
                    break;
                case 7:
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    /**
     * Creates and initializes an in-memory repository for Customer entities.
     *
     * @return Initialized IRepository instance for Customer entities
     */

    /**
     * Main entry point of the application.
     * Initializes all necessary repositories, creates service and controller instances,
     * and starts the application.
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        try {
            // Testarea conexiunii la baza de date
            if (testDatabaseConnection()) {
                System.out.println("Conexiunea la baza de date a fost realizată cu succes.");
            } else {
                System.out.println("Conexiunea la baza de date a eșuat.");
                return;
            }

            // Selectarea serviciului
            Object[] selectedServices = selectService();

            // Crearea instanțelor de Controller folosind serviciul selectat
            CustomerController customerController = new CustomerController((CustomerService) selectedServices[0]);
            EmployeeController employeeController = new EmployeeController((EmployeeService) selectedServices[1]);
            SellerController sellerController = new SellerController((SellerService) selectedServices[2]);
            DeliveryPersonController deliveryPersonController = new DeliveryPersonController((DeliveryPersonService) selectedServices[3]);
            UserController userController = new UserController((UserService) selectedServices[4]);

            // Inițializarea aplicației
            new APP4(customerController, employeeController, sellerController, deliveryPersonController, userController).createUI();
        }catch (Exception e) {
                e.printStackTrace();
        }
    }

    public static boolean testDatabaseConnection() {
        //try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "1234")) {

        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Delivery_Service", "postgres", "parola")) {

            return connection != null;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Object[] selectService() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                System.out.println("Selectează tipul de serviciu:");
                System.out.println("1. InMemoryService");
                System.out.println("2. FileService");
                System.out.println("3. DbService");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        return createInMemoryServices();
                    case 2:
                        return createFileServices();
                    case 3:
                        return createDbServices();
                    default:
                        System.out.println("Selecție invalidă.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Input invalid. Te rog să introduci un număr.");
                scanner.next(); // Consumă input-ul invalid
            }
        }
    }

    public static Object[] createInMemoryServices() {
        // Creează și returnează instanțele de InMemoryService
        IRepository<Store> storeIRepository = createInMemoryStoreRepository();
        IRepository<Packages> packagesIRepository = createInMemoryPackageRepository();
        IRepository<Order> orderIRepository = createInMemoryOrderRepository();
        IRepository<Customer> customerIRepository = createInMemoryCustomerRepository();
        IRepository<Department> departmentIRepository = createInMemoryDepartmentRepository();
        IRepository<Employee> employeeIRepository = createInMemoryEmployeeRepository();
        IRepository<Delivery> deliveryIRepository = createInMemoryDeliveryRepository();
        IRepository<Deposit> depositIRepository = createInMemoryDepositRepository();
        IRepository<Delivery_Person> deliveryPersonIRepository = createInMemoryDeliveryPersonRepository();
        IRepository<Personal_Vehicle> personalVehicleIRepository = createInMemoryPersonalVehicleRepository();

        CustomerService customerService = new CustomerService(customerIRepository,orderIRepository,deliveryIRepository,packagesIRepository);
        EmployeeService employeeService = new EmployeeService(employeeIRepository,deliveryIRepository,departmentIRepository);
        SellerService sellerService = new SellerService(storeIRepository, depositIRepository, packagesIRepository,deliveryIRepository,customerIRepository,orderIRepository);
        DeliveryPersonService deliveryPersonService = new DeliveryPersonService(deliveryIRepository,deliveryPersonIRepository, personalVehicleIRepository);
        UserService userService = new UserService(customerIRepository, employeeIRepository, deliveryPersonIRepository,departmentIRepository);

        return new Object[]{customerService, employeeService, sellerService, deliveryPersonService, userService};
    }

    public static Object[] createFileServices() {
        // Creează și returnează instanțele de FileService
        IRepository<Store> storeRepository = new InFileRepository<>(
                "src/data/stores.txt",
                Store::toCsv,
                Store::fromCsv
        );
        IRepository<Packages> packagesRepository = new InFileRepository<>(
                "src/data/packages.txt",
                Packages::toCsv,
                Packages::fromCsv
        );
        IRepository<Order> orderRepository = new InFileRepository<>(
                "src/data/orders.txt",
                Order::toCsv,
                Order::fromCsv
        );
        IRepository<Customer> customerRepository = new InFileRepository<>(
                "src/data/customers.txt",
                Customer::toCsv,
                Customer::fromCsv
        );
        IRepository<Department> departmentRepository = new InFileRepository<>(
                "src/data/departments.txt",
                Department::toCsv,
                Department::fromCsv
        );
        IRepository<Employee> employeeRepository = new InFileRepository<>(
                "src/data/employees.txt",
                Employee::toCsv,
                Employee::fromCsv
        );
        IRepository<Delivery> deliveryRepository = new InFileRepository<>(
                "src/data/deliveries.txt",
                Delivery::toCsv,
                Delivery::fromCsv
        );
        IRepository<Deposit> depositRepository = new InFileRepository<>(
                "src/data/deposits.txt",
                Deposit::toCsv,
                Deposit::fromCsv
        );
        IRepository<Delivery_Person> deliveryPersonRepository = new InFileRepository<>(
                "src/data/delivery_persons.txt",
                Delivery_Person::toCsv,
                Delivery_Person::fromCsv
        );
        IRepository<Personal_Vehicle> personalVehicleRepository = new InFileRepository<>(
                "src/data/personal_vehicles.txt",
                Personal_Vehicle::toCsv,
                Personal_Vehicle::fromCsv
        );

        CustomerService customerService = new CustomerService(customerRepository,orderRepository,deliveryRepository,packagesRepository);
        EmployeeService employeeService = new EmployeeService(employeeRepository,deliveryRepository,departmentRepository);
        SellerService sellerService = new SellerService(storeRepository, depositRepository, packagesRepository,deliveryRepository,customerRepository,orderRepository);
        DeliveryPersonService deliveryPersonService = new DeliveryPersonService(deliveryRepository,deliveryPersonRepository, personalVehicleRepository);
        UserService userService = new UserService(customerRepository, employeeRepository, deliveryPersonRepository,departmentRepository);

        return new Object[]{customerService, employeeService, sellerService, deliveryPersonService, userService};
    }

    public static Object[] createDbServices() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Delivery_Service", "postgres", "parola");


            //Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "1234");
            DbUtil dbUtil = new DbUtil(connection);
            // Creează și returnează instanțele de DbService
            RowMapper<Department> departmentsRowMapper = rs -> new Department(
                    rs.getInt("departmentid"),
                    rs.getString("name"),
                    rs.getString("task")
            );
            DBRepository<Department> departmentDBRepository = new DBRepository<>(dbUtil.getConnection(), "departments", departmentsRowMapper, "departmentid");

            RowMapper<Employee> employeeRowMapper = rs -> new Employee(
                    rs.getInt("employeeID"),
                    rs.getInt("departmentID"),
                    rs.getString("name"),
                    rs.getString("phone"),
                    rs.getString("license")
            );
            DBRepository<Employee> employeeDBRepository = new DBRepository<>(dbUtil.getConnection(), "employees", employeeRowMapper, "employeeID");

            RowMapper<Customer> customerRowMapper = rs -> new Customer(
                    rs.getInt("customerID"),
                    rs.getString("name"),
                    rs.getString("address"),
                    rs.getString("phone"),
                    rs.getString("email")
            );
            DBRepository<Customer> customerDBRepository = new DBRepository<>(dbUtil.getConnection(), "customers", customerRowMapper, "customerID");

            RowMapper<Store> storeRowMapper = rs -> new Store(
                    rs.getInt("storeID"),
                    rs.getString("name"),
                    rs.getString("address"),
                    rs.getString("contact")
            );
            DBRepository<Store> storeDBRepository = new DBRepository<>(dbUtil.getConnection(), "stores", storeRowMapper, "storeID");

            RowMapper<Delivery_Person> deliveryPersonRowMapper = rs -> new Delivery_Person(
                    rs.getInt("deliveryPersonID"),
                    rs.getString("phone"),
                    rs.getString("name")
            );
            DBRepository<Delivery_Person> deliveryPersonDBRepository = new DBRepository<>(dbUtil.getConnection(), "delivery_persons", deliveryPersonRowMapper, "deliveryPersonID");

            RowMapper<Deposit> depositRowMapper = rs -> new Deposit(
                    rs.getInt("depositID"),
                    rs.getString("address"),
                    rs.getString("status"),
                    rs.getInt("storeID")

            );
            DBRepository<Deposit> depositDBRepository = new DBRepository<>(dbUtil.getConnection(), "deposits", depositRowMapper, "depositID");

            RowMapper<Order> orderRowMapper = rs -> new Order(
                    rs.getInt("orderID"),
                    rs.getInt("customerID"),
                    rs.getTimestamp("deliveryDateTime").toLocalDateTime()
                    //rs.getDouble("totalCost"),
                    //rs.getString("status"),
                    //rs.getInt("deliveryID"),
                    //rs.getString("location")
            );
            DBRepository<Order> orderDBRepository = new DBRepository<>(dbUtil.getConnection(), "orders", orderRowMapper, "orderID");

            RowMapper<Personal_Vehicle> personalVehicleRowMapper = rs -> new Personal_Vehicle(
                    rs.getInt("personalVehicleID"),
                    rs.getInt("extraFee"),
                    rs.getInt("deliveryPersonID"),
                    //rs.getInt("capacity"),
                    Transportation_Type.valueOf(rs.getString("transportation_type"))
            );
            DBRepository<Personal_Vehicle> personalVehicleDBRepository = new DBRepository<>(dbUtil.getConnection(), "personal_vehicles", personalVehicleRowMapper, "personalVehicleID");

            RowMapper<Packages> packagesRowMapper = rs -> new Packages(
                    rs.getInt("packageID"),
                    rs.getDouble("weight"),
                    rs.getString("dimensions"),
                    rs.getDouble("cost")
                    //rs.getInt("depositID")
            );
            DBRepository<Packages> packagesDBRepository = new DBRepository<>(dbUtil.getConnection(), "packages", packagesRowMapper, "packageID");

            RowMapper<Delivery> deliveryRowMapper = rs -> new Delivery(
                    rs.getInt("deliveryID")
                    //rs.getInt("deliveryPersonID"),
                    //rs.getInt("employeeID"),
                    //rs.getInt("transportationID"),
                    //rs.getString("transportation_type")
                    //rs.getString("location")
            );
            DBRepository<Delivery> deliveryDBRepository = new DBRepository<>(dbUtil.getConnection(), "deliveries", deliveryRowMapper, "deliveryID");

            CustomerService customerService = new CustomerService(customerDBRepository,orderDBRepository,deliveryDBRepository,packagesDBRepository);
            EmployeeService employeeService = new EmployeeService(employeeDBRepository,deliveryDBRepository,departmentDBRepository);
            SellerService sellerService = new SellerService(storeDBRepository,depositDBRepository, packagesDBRepository, deliveryDBRepository,customerDBRepository,orderDBRepository);
            DeliveryPersonService deliveryPersonService = new DeliveryPersonService(deliveryDBRepository, deliveryPersonDBRepository, personalVehicleDBRepository);
            UserService userService = new UserService(customerDBRepository, employeeDBRepository, deliveryPersonDBRepository,departmentDBRepository);

            return new Object[]{customerService, employeeService, sellerService, deliveryPersonService, userService};

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Metodele pentru crearea repository-urilor in-memory
    private static IRepository<Customer> createInMemoryCustomerRepository() {
        IRepository<Customer> customerIRepository = new InMemoryRepo<>();
        customerIRepository.create(new Customer(1, "Dorel", "Cluj-Napoca", "0774596204", "dorel@gmail.com"));
        customerIRepository.create(new Customer(2, "Balintescu", "Cluj-Napoca", "0734682134", "balintescu@gmail.com"));
        customerIRepository.create(new Customer(3, "Andrei", "Zalau", "0797794239", "andrei@gmail.com"));
        return customerIRepository;
    }

    private static IRepository<Employee> createInMemoryEmployeeRepository() {
        IRepository<Employee> employeeIRepository = new InMemoryRepo<>();
        employeeIRepository.create(new Employee(1, 1, "Eminovici", "0742092989", "part time"));
        employeeIRepository.create(new Employee(2, 1, "Stefan", "071998491", "full time"));
        employeeIRepository.create(new Employee(3, 2, "David", "077636274", "full time"));
        return employeeIRepository;
    }

    private static IRepository<Delivery> createInMemoryDeliveryRepository() {
        IRepository<Delivery> deliveryIRepository = new InMemoryRepo<>();
        deliveryIRepository.create(new Delivery(1));//, Timestamp.valueOf(LocalDateTime.of(2024, 6, 6, 10, 0)))); // Exemplu
        deliveryIRepository.create(new Delivery(2));//, Timestamp.valueOf(LocalDateTime.of(2024, 7, 8, 15, 30)))); // Exemplu
        deliveryIRepository.create(new Delivery(3));//, Timestamp.valueOf(LocalDateTime.of(2024, 7, 9, 15, 30)))); // Exemplu
        return deliveryIRepository;
    }

    private static IRepository<Delivery_Person> createInMemoryDeliveryPersonRepository() {
        IRepository<Delivery_Person> deliveryPersonIRepository = new InMemoryRepo<>();
        deliveryPersonIRepository.create(new Delivery_Person(1, "0742092989", "Eminovici"));
        deliveryPersonIRepository.create(new Delivery_Person(2, "071998491", "Stefan"));
        deliveryPersonIRepository.create(new Delivery_Person(3, "077636274", "David"));
        return deliveryPersonIRepository;
    }

    private static IRepository<Department> createInMemoryDepartmentRepository() {
        IRepository<Department> departmentIRepository = new InMemoryRepo<>();
        departmentIRepository.create(new Department(1, "Business Intelligence", "Develop Business"));
        return departmentIRepository;
    }

    private static IRepository<Deposit> createInMemoryDepositRepository() {
        IRepository<Deposit> depositIRepository = new InMemoryRepo<>();
        depositIRepository.create(new Deposit(1, "Str. Ploiesti", "Full", 1));
        depositIRepository.create(new Deposit(2, "Str. Constanta", "Empty", 2));
        depositIRepository.create(new Deposit(3, "Str. Fabricii", "Not full", 3));
        return depositIRepository;
    }

    private static IRepository<Order> createInMemoryOrderRepository() {
        IRepository<Order> orderIRepository = new InMemoryRepo<>();
        orderIRepository.create(new Order(1, 1, LocalDateTime.of(2024, 6, 10, 12, 0))); // Exemplu
        orderIRepository.create(new Order(2, 2, LocalDateTime.of(2024, 6, 12, 14, 30))); // Exemplu
        orderIRepository.create(new Order(3, 3, LocalDateTime.of(2024, 6, 15, 9, 0))); // Exemplu
        return orderIRepository;
    }

    private static IRepository<Packages> createInMemoryPackageRepository() {
        IRepository<Packages> packagesIRepository = new InMemoryRepo<>();
        packagesIRepository.create(new Packages(1, 100.5, "4x4x4", 100));
        packagesIRepository.create(new Packages(2, 20.8, "2x4x3", 50));
        packagesIRepository.create(new Packages(3, 10.2, "5x2x3", 120));
        return packagesIRepository;
    }

    private static IRepository<Personal_Vehicle> createInMemoryPersonalVehicleRepository() {
        IRepository<Personal_Vehicle> personalVehicleIRepository = new InMemoryRepo<>();
        personalVehicleIRepository.create(new Personal_Vehicle(1, 10, 20, Transportation_Type.Ground));
        personalVehicleIRepository.create(new Personal_Vehicle(2, 50, 1100, Transportation_Type.Aerial));
        personalVehicleIRepository.create(new Personal_Vehicle(3, 30, 9345, Transportation_Type.Naval));
        return personalVehicleIRepository;
    }

    private static IRepository<Store> createInMemoryStoreRepository() {
        IRepository<Store> storeIRepository = new InMemoryRepo<>();
        storeIRepository.create(new Store(1, "Auchan", "Str. Posada", "Alexandru"));
        storeIRepository.create(new Store(2, "Dedeman", "Str. Livezii", "Stefan"));
        storeIRepository.create(new Store(3, "Kaufland", "Calea Manastur", "Mihai"));
        return storeIRepository;
    }
}