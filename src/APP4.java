import controller.*;
import model.*;
import repository.*;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
/**
 * Main application class for the delivery management system.
 * This class provides a console-based user interface for interacting with different
 * aspects of the delivery system including users, sellers, customers, employees,
 * and delivery persons.
 */
public class APP4{
    private CustomerController customerController;
    private EmployeeController employeeController;
    private SellerController sellerController;
    private DeliveryPersonController deliveryPersonController;
    private UserController userController;

    private Scanner scanner;

    public APP4(CustomerController customerController, EmployeeController employeeController, SellerController sellerController, DeliveryPersonController deliveryPersonController, UserController userController){
        this.customerController = customerController;
        this.employeeController = employeeController;
        this.sellerController = sellerController;
        this.deliveryPersonController = deliveryPersonController;
        this.userController = userController;
    }
    /**
     * Creates and manages the main user interface loop.
     * Displays the main menu and handles user input for navigation between different menus.
     */
    private void createUI() {
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
            System.out.println("5. Back to Main Menu");
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
                    break;
                case 4:
                    userController.viewAllDeliveryPersons();

                    System.out.print("Enter Delivery Person ID: ");
                    int deliveryPersonId = scanner.nextInt();
                    scanner.nextLine();

                    userController.deleteDeliveryPerson(deliveryPersonId);

                    break;
                case 5:
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
            System.out.println("10.Create a Delivery");
            System.out.println("11. Back to Main Menu");
            System.out.print("Select an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    sellerController.viewAllStores();
                    break;
                case 2:
                    System.out.print("Enter Store Name: ");
                    String storeName = scanner.nextLine();
                    System.out.print("Enter Store Address: ");
                    String storeAddress = scanner.nextLine();
                    System.out.print("Enter Store Contact: ");
                    String storeContact = scanner.nextLine();
                    sellerController.createStore(storeName, storeAddress, storeContact);
                    break;
                case 3:
                    System.out.print("Enter Store ID: ");
                    int storeId = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    System.out.print("Enter Deposit Address: ");
                    String depositAddress = scanner.nextLine();
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
                    System.out.print("Enter Package Cost: ");
                    double cost = scanner.nextDouble();
                    scanner.nextLine(); // Consume newline
                    System.out.print("Enter Package Weight: ");
                    double weight = scanner.nextDouble();
                    scanner.nextLine(); // Consume newline
                    System.out.print("Enter Package Dimensions: ");
                    String dimensions = scanner.nextLine();
                    sellerController.createPackage(cost, weight, dimensions);
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
                    System.out.println("Enter a location for a Delivery to be created:");
                    String location = scanner.nextLine();
                    sellerController.createDelivery(location);
                case 11:
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
    /**
     * Manages the customer menu interface.
     * Provides functionality for customer management and order processing.
     */
    private void customerMenu() {
        while (true) {
            System.out.println("Customer Menu:");
            System.out.println("1. View All Customers");
            System.out.println("2. Create Customer");
            System.out.println("3. Make an Order");
            System.out.println("4. Remove an Order");
            System.out.println("5. View Personal Orders");
            System.out.println("6. Calculate Order Cost");
            System.out.println("7. Schedule Delivery Date");
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
                    System.out.print("Enter Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter Address: ");
                    String address = scanner.nextLine();
                    System.out.print("Enter Phone: ");
                    String phone = scanner.nextLine();
                    System.out.print("Enter Email: ");
                    String email = scanner.nextLine();
                    customerController.createLoggedInCustomer(name, address, phone, email);
                    break;
                case 3:
                    System.out.print("Enter customer ID: ");
                    Integer customerId = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("Enter order date (yyyy-mm-dd): ");
                    String orderDateString = scanner.nextLine();
                    Date orderDate = Date.valueOf(orderDateString);


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

                    LocalDateTime dateTime = LocalDateTime.parse(scanner.nextLine());

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

                    customerController.makeAnOrder(customerId, orderDate, dateTime, packageIds);
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
                    try {
                        System.out.print("Enter order ID: ");
                        Integer orderIdForSchedule = Integer.parseInt(scanner.nextLine());

                        System.out.print("Enter delivery date (yyyy-mm-dd): ");
                        String deliveryDateStringForSchedule = scanner.nextLine();
                        System.out.print("Enter delivery time (HH:mm): ");
                        String deliveryTimeStringForSchedule = scanner.nextLine();
                        LocalDateTime deliveryDateTimeForSchedule = LocalDateTime.parse(deliveryDateStringForSchedule + "T" + deliveryTimeStringForSchedule);

                        customerController.scheduleDeliveryDate(orderIdForSchedule, deliveryDateTimeForSchedule);
                    } catch (Exception e) {
                        System.out.println("Invalid input. Please try again.");
                    }
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
                    System.out.print("Enter Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter Phone: ");
                    String phone = scanner.nextLine();
                    System.out.print("Enter License: ");
                    String license = scanner.nextLine();
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
                    System.out.print("Enter Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter Phone: ");
                    String phone = scanner.nextLine();
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
                    deliveryPersonController.getDeliveriesForDeliveryPerson(deliveryPersonID);
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
        deliveryIRepository.create(new Delivery(1));//, Timestamp.valueOf(LocalDateTime.of(2024, 6, 6, 10, 0))));
        deliveryIRepository.create(new Delivery(2));//, Timestamp.valueOf(LocalDateTime.of(2024, 7, 8, 15, 30))));
        deliveryIRepository.create(new Delivery(3));//, Timestamp.valueOf(LocalDateTime.of(2024, 7, 9, 15, 30))));
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

    //LocalDateTime
    private static IRepository<Order> createInMemoryOrderRepository() {
        IRepository<Order> orderIRepository = new InMemoryRepo<>();
        orderIRepository.create(new Order(1, 1, Date.valueOf("2024-06-06"), LocalDateTime.of(2024, 6, 10, 12, 0)));//, 150.75, "Processing"));
        orderIRepository.create(new Order(2, 2, Date.valueOf("2024-06-07"), LocalDateTime.of(2024, 6, 12, 14, 30)));//, 200.50, "Shipped"));
        orderIRepository.create(new Order(3, 3, Date.valueOf("2024-06-08"), LocalDateTime.of(2024, 6, 15, 9, 0)));//, 100.25, "Delivered"));
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

    /**
     * Main entry point of the application.
     * Initializes all necessary repositories, creates service and controller instances,
     * and starts the application.
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        // Assuming the repositories are implemented and passed to the Service constructor
        IRepository<Customer> customerIRepository = createInMemoryCustomerRepository();
        IRepository<Employee> employeeIRepository = createInMemoryEmployeeRepository();
        IRepository<Delivery> deliveryIRepository = createInMemoryDeliveryRepository();
        IRepository<Delivery_Person> deliveryPersonIRepository = createInMemoryDeliveryPersonRepository();
        IRepository<Deposit> depositIRepository = createInMemoryDepositRepository();
        IRepository<Personal_Vehicle> personalVehicleIRepository = createInMemoryPersonalVehicleRepository();
        IRepository<Department> departmentIRepository = createInMemoryDepartmentRepository();
        IRepository<Order> orderIRepository = createInMemoryOrderRepository();
        IRepository<Store> storeIRepository = createInMemoryStoreRepository();
        IRepository<Packages> packagesIRepository = createInMemoryPackageRepository();
        IRepository<Customer> customerRepository = new InFileRepository<>(
                "src/data/customers.txt",
                Customer::toCsv,
                Customer::fromCsv
        );
        IRepository<Store> storeRepository = new InFileRepository<>(
                "src/data/stores.txt",
                Store::toCsv,
                Store::fromCsv
        );
        IRepository<Deposit>depositRepository = new InFileRepository<>(
                "src/data/deposits.txt",
                Deposit::toCsv,
                Deposit::fromCsv
        );
        IRepository<Order> orderRepository = new InFileRepository<>(
                "src/data/orders.txt",
                Order::toCsv,
                Order::fromCsv
        );
        IRepository<Packages> packagesRepository = new InFileRepository<>(
                "src/data/package.txt",
                Packages::toCsv,
                Packages::fromCsv
        );
        IRepository<Employee> employeeRepository = new InFileRepository<>(
                "src/data/employees.txt",
                Employee::toCsv,
                Employee::fromCsv
        );
        IRepository<Delivery> deliveryRepository = new InFileRepository<>(
                "src/data/delivery.txt",
                Delivery::toCsv,
                Delivery::fromCsv
        );
        IRepository<Personal_Vehicle> personalVehicleRepository = new InFileRepository<>(
                "src/data/personalVehicles.txt",
                Personal_Vehicle::toCsv,
                Personal_Vehicle::fromCsv
        );
        IRepository<Delivery_Person> deliveryPersonRepository = new InFileRepository<>(
                "src/data/deliveryPerson.txt",
                Delivery_Person::toCsv,
                Delivery_Person::fromCsv
        );
        IRepository<Department> departmentRepository = new InFileRepository<>(
                "src/data/departments.txt",
                Department::toCsv,
                Department::fromCsv
        );
        Service service = new Service(storeIRepository,packagesIRepository,orderIRepository,customerIRepository,departmentIRepository,employeeIRepository,deliveryIRepository,depositIRepository,deliveryPersonIRepository,personalVehicleIRepository);
        Service service1 = new Service(storeRepository,packagesRepository,orderRepository,customerRepository,departmentRepository,employeeRepository,deliveryRepository,depositRepository,deliveryPersonRepository,personalVehicleRepository);
        Controller controller = new Controller(service1);
        new APP3(controller);
    }
}