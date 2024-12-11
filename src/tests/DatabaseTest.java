package tests;

import controller.SellerController;
import exceptions.BusinessLogicException;
import exceptions.EntityNotFound;
import exceptions.ValidationException;
import model.*;
import org.junit.jupiter.api.Test;
import repository.IRepository;
import repository.InMemoryRepo;
import service.CustomerService;
import service.DeliveryPersonService;
import service.SellerService;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class DatabaseTest {
    private IRepository<Store> storeIRepository = new InMemoryRepo<>();
    private IRepository<Packages> packageIRepository = new InMemoryRepo<>();
    private IRepository<Order> orderIRepository = new InMemoryRepo<>();
    private IRepository<Customer> customerIRepository = new InMemoryRepo<>();
    private IRepository<Department> departmentIRepository = new InMemoryRepo<>();
    private IRepository<Employee> employeeIRepository = new InMemoryRepo<>();
    private IRepository<Delivery> deliveryIRepository = new InMemoryRepo<>();
    private IRepository<Deposit> depositIRepository = new InMemoryRepo<>();
    private IRepository<Delivery_Person> deliveryPersonIRepository = new InMemoryRepo<>();
    private IRepository<Personal_Vehicle> personalVehicleIRepository = new InMemoryRepo<>();

    private CustomerService customerService = new CustomerService(customerIRepository, orderIRepository, deliveryIRepository, packageIRepository);
    private SellerService sellerService = new SellerService(storeIRepository, depositIRepository, packageIRepository, deliveryIRepository, customerIRepository, orderIRepository);
    private DeliveryPersonService deliveryPersonService = new DeliveryPersonService(deliveryIRepository, deliveryPersonIRepository, personalVehicleIRepository);

    private SellerController sellerController = new SellerController(sellerService);
    @Test
    public void testStores(){
        // Create and store an initial Store object
        Store store = new Store(1, "Kaufland", "Str. Posada", "Marcel");
        storeIRepository.create(store);

        // Test create + read(get)
        Store fetchedStore = storeIRepository.get(1);
        assertNotNull(fetchedStore);
        assertEquals("Str. Posada", fetchedStore.getAddress());

        // Test update
        fetchedStore.setAddress("Calea Manastur");
        storeIRepository.update(fetchedStore);

        Store refetchedStore = storeIRepository.get(1);
        assertEquals("Calea Manastur", refetchedStore.getAddress()); // Confirm update
        assertNotEquals("Str. Posada", refetchedStore.getAddress()); // Confirm it's different

        // Test delete
        storeIRepository.delete(1);
        Store deletedStore = storeIRepository.get(1);
        assertNull(deletedStore); // Confirm deletion

    }

    @Test
    public void testPackages(){
        //Test create + read
        Packages packages = new Packages(1, 12.5, "4x4x4", 100.5);
        packageIRepository.create(packages);

        Packages fetchedPackage = packageIRepository.get(1);
        assertNotNull(fetchedPackage);

        //Test update
        fetchedPackage.setCost(200.5);
        packageIRepository.update(fetchedPackage);

        Packages refetchedPackage = packageIRepository.get(1);
        assertEquals(200.5, refetchedPackage.getCost());
        assertNotEquals(100.5, refetchedPackage.getCost());

        //Test delete
        packageIRepository.delete(1);
        Packages deletedPackage = packageIRepository.get(1);
        assertNull(deletedPackage);
    }


    @Test
    public void testOrders(){
        //Test create + read
        Order order = new Order(1, 1, new Date(2024-12-12), LocalDateTime.of(2024, 12, 12, 12, 0));
        order.setLocation("Sibiu");
        orderIRepository.create(order);

        Order fetchedOrder = orderIRepository.get(1);
        assertNotNull(fetchedOrder);


        //Test update
        fetchedOrder.setLocation("Cluj");
        orderIRepository.update(fetchedOrder);

        Order refetchedOrder = orderIRepository.get(1);
        assertEquals("Cluj", refetchedOrder.getLocation());
        assertNotEquals("Sibiu", refetchedOrder.getLocation());

        //Test delete
        orderIRepository.delete(1);
        Order deletedOrder = orderIRepository.get(1);
        assertNull(deletedOrder);

    }

    @Test
    public void testCustomers(){
        //Test create + read
        Customer customer = new Customer(1, "Robert", "Sibiu", "123456789", "robert@mail.com");
        customerIRepository.create(customer);

        Customer fetchedCustomer = customerIRepository.get(1);
        assertNotNull(fetchedCustomer);


        //Test update
        fetchedCustomer.setAddress("Cluj");
        customerIRepository.update(fetchedCustomer);

        Customer refetchedCustomer = customerIRepository.get(1);
        assertEquals("Cluj", refetchedCustomer.getAddress());
        assertNotEquals("Sibiu", refetchedCustomer.getAddress());

        //Test delete
        customerIRepository.delete(1);
        Customer deletedCustomer = customerIRepository.get(1);
        assertNull(deletedCustomer);
    }

    @Test
    public void testDepartments(){
        //Test create + read
        Department department = new Department(1, "Web Development", "Develop Web Apps");
        departmentIRepository.create(department);

        Department fetchedDepartment = departmentIRepository.get(1);
        assertNotNull(fetchedDepartment);


        //Test update
        fetchedDepartment.setName("Full-Stuck Development");
        departmentIRepository.update(fetchedDepartment);

        Department refetchedDepartment = departmentIRepository.get(1);
        assertEquals("Full-Stuck Development", refetchedDepartment.getName());
        assertNotEquals("Web Development", refetchedDepartment.getName());

        //Test delete
        departmentIRepository.delete(1);
        Department deletedDepartment = departmentIRepository.get(1);
        assertNull(deletedDepartment);
    }

    @Test
    public void testEmployees(){
        //Test create + read
        Employee employee = new Employee(1, 1, "Alex", "987654321", "B");
        employeeIRepository.create(employee);

        Employee fetchedEmployee = employeeIRepository.get(1);
        assertNotNull(fetchedEmployee);

        //Test update
        fetchedEmployee.setLicense("C");
        employeeIRepository.update(fetchedEmployee);

        Employee refetchedEmployee = employeeIRepository.get(1);
        assertEquals("C", refetchedEmployee.getLicense());
        assertNotEquals("B", refetchedEmployee.getLicense());

        //Test delete
        employeeIRepository.delete(1);
        Employee deletedEmployee = employeeIRepository.get(1);
        assertNull(deletedEmployee);
    }

    @Test
    public void testDeliveries(){
        //Test create + read
        Delivery delivery = new Delivery(1);
        delivery.setLocation("Sibiu");
        deliveryIRepository.create(delivery);

        Delivery fetchedDelivery = deliveryIRepository.get(1);
        assertNotNull(fetchedDelivery);


        //Test update
        fetchedDelivery.setLocation("Cluj");
        deliveryIRepository.update(fetchedDelivery);

        Delivery refetchedDelivery = deliveryIRepository.get(1);
        assertEquals("Cluj", refetchedDelivery.getLocation());
        assertNotEquals("Sibiu", refetchedDelivery.getLocation());

        //Test delete
        deliveryIRepository.delete(1);
        Delivery deletedDelivery = deliveryIRepository.get(1);
        assertNull(deletedDelivery);
    }

    @Test
    public void testDeposits(){
        //Test create + read
        Deposit deposit = new Deposit(1, "Sibiu", "Full", 1);
        depositIRepository.create(deposit);

        Deposit fetchedDeposit = depositIRepository.get(1);
        assertNotNull(fetchedDeposit);


        //Test update
        fetchedDeposit.setStatus("Empty");
        depositIRepository.update(fetchedDeposit);

        Deposit refetchedDeposit = depositIRepository.get(1);
        assertEquals("Empty", refetchedDeposit.getStatus());
        assertNotEquals("Full", refetchedDeposit.getStatus());

        //Test delete
        depositIRepository.delete(1);
        Deposit deletedDeposit = depositIRepository.get(1);
        assertNull(deletedDeposit);
    }

    @Test
    public void testDeliveryPersons(){
        //Test create + read
        Delivery_Person deliveryPerson = new Delivery_Person(1, "123456789", "Robert");
        deliveryPersonIRepository.create(deliveryPerson);

        Delivery_Person fetchedDeliveryPerson = deliveryPersonIRepository.get(1);
        assertNotNull(fetchedDeliveryPerson);


        //Test update
        fetchedDeliveryPerson.setPhone("987654321");
        deliveryPersonIRepository.update(fetchedDeliveryPerson);

        Delivery_Person refetchedDeliveryPerson = deliveryPersonIRepository.get(1);
        assertEquals("987654321", refetchedDeliveryPerson.getPhone());
        assertNotEquals("123456789", refetchedDeliveryPerson.getPhone());

        //Test delete
        deliveryPersonIRepository.delete(1);
        Delivery_Person deletedDeliveryPerson = deliveryPersonIRepository.get(1);
        assertNull(deletedDeliveryPerson);
    }

    @Test
    public void testPersonalVehicles(){
        //Test create + read
        Personal_Vehicle personalVehicle = new Personal_Vehicle(1, 12, 100, Transportation_Type.Aerial);
        personalVehicleIRepository.create(personalVehicle);

        Personal_Vehicle fetchedPersonalVehicle = personalVehicleIRepository.get(1);
        assertNotNull(fetchedPersonalVehicle);


        //Test update
        fetchedPersonalVehicle.setExtraFee(10);
        personalVehicleIRepository.update(fetchedPersonalVehicle);

        Personal_Vehicle refetchedPersonalVehicle = personalVehicleIRepository.get(1);
        assertEquals(10, refetchedPersonalVehicle.getExtraFee());
        assertNotEquals(12, refetchedPersonalVehicle.getExtraFee());

        //Test delete
        personalVehicleIRepository.delete(1);
        Personal_Vehicle deletedPersonalVehicle = personalVehicleIRepository.get(1);
        assertNull(deletedPersonalVehicle);
    }

    @Test
    public void testPlaceOrder() throws SQLException {
        Integer customerId = 1;
        Integer orderId = 1;
        Date orderDate = new Date(2024 - 12 - 12);
        LocalDateTime deliveryDateTime = LocalDateTime.of(2024, 12, 12, 12, 0);
        List<Integer> packageIds = new ArrayList<>();

        Packages packages1 = new Packages(1, 11.1, "2x2x2", 100.2);
        Packages packages2 = new Packages(2, 21.1, "10x3x2", 150.2);

        packageIds.add(packages1.getId());
        packageIds.add(packages2.getId());
        packageIRepository.create(packages1);
        packageIRepository.create(packages2);

        Customer customer = new Customer(customerId, "Robert", "Manastur", "123456789", "robert@mail.com");
        customerIRepository.create(customer);

        customerService.placeOrder(customerId, orderId, orderDate, deliveryDateTime, packageIds);

        Order fetchedOrder = orderIRepository.get(orderId);
        assertNotNull(fetchedOrder);
    }

    @Test
    public void testFailedPlaceOrder(){
        //use an invalid customer id (negative, 0, or a customer id that does not exist)
        Integer customerId = 0;
        Integer orderId = 1;
        Date orderDate = new Date(2024 - 12 - 12);
        LocalDateTime deliveryDateTime = LocalDateTime.of(2024, 12, 12, 12, 0);
        List<Integer> packageIds = new ArrayList<>();

        Packages packages1 = new Packages(1, 11.1, "2x2x2", 100.2);
        Packages packages2 = new Packages(2, 21.1, "10x3x2", 150.2);

        packageIds.add(packages1.getId());
        packageIds.add(packages2.getId());
        packageIRepository.create(packages1);
        packageIRepository.create(packages2);

        Exception exception = assertThrows(
                EntityNotFound.class,
                () -> customerService.placeOrder(customerId, orderId, orderDate, deliveryDateTime, packageIds),
                "Expected placeOrder to throw ValidationException"
        );

        assertEquals("Customer not found for ID " + customerId, exception.getMessage());
    }

    @Test
    public void testRemoveStore(){
        Integer storeId = 1;

        Store store = new Store(storeId, "Kaufland", "Manastur", "Marcel");
        storeIRepository.create(store);
        Store fetchedStore = storeIRepository.get(storeId);
        assertNotNull(fetchedStore);

        Deposit deposit1 = new Deposit(1, "Manastur", "Full", 1);
        Deposit deposit2 = new Deposit(2, "Marasti", "Full", 1);
        depositIRepository.create(deposit1);
        depositIRepository.create(deposit2);

        sellerService.removeStore(storeId);
        Store refetchedStore = storeIRepository.get(storeId);
        assertNull(refetchedStore);
    }

    @Test
    public void testFailedRemoveStore(){
        //use an invalid store id (negative, 0, or a store id that does not exist)
        Integer storeId = 0;

        Exception exception = assertThrows(
                EntityNotFound.class,
                () -> sellerService.removeStore(storeId),
                "Expected removeStore to throw ValidationException"
        );

        assertEquals("No store found with ID " + storeId, exception.getMessage());
    }

    @Test
    public void testGetDeliveriesWithToBeShippedOrders(){
        Customer customer1 = new Customer(1, "Robert", "Manastur", "123456789", "robert@mail.com");
        Customer customer2 = new Customer(2, "Alex", "Gheorgheni", "987654321", "alex@mail.com");
        Customer customer3 = new Customer(3, "Stefan", "Gruia", "567891234", "stefan@mail.com");

        customerIRepository.create(customer1);
        customerIRepository.create(customer2);
        customerIRepository.create(customer3);

        Order order1 = new Order(1, 1, new Date(2024-12-12), LocalDateTime.of(2024, 12, 12, 12, 0));
        Order order2 = new Order(2, 1, new Date(2024-10-12), LocalDateTime.of(2024, 12, 12, 12, 0));
        Order order3 = new Order(3, 2, new Date(2024-10-12), LocalDateTime.of(2024, 12, 12, 12, 0));
        Order order4 = new Order(4, 3, new Date(2024-11-12), LocalDateTime.of(2024, 12, 12, 12, 0));
        Order order5 = new Order(5, 2, new Date(2024-12-12), LocalDateTime.of(2024, 12, 12, 12, 0));
        Order order6 = new Order(6, 3, new Date(2024-11-12), LocalDateTime.of(2024, 12, 12, 12, 0));

        order1.setStatus("to be shipped");
        order2.setStatus("processing");
        order3.setStatus("to be shipped");
        order4.setStatus("to be shipped");
        order5.setStatus("processing");
        order6.setStatus("processing");

        order1.setLocation("Cluj");
        order2.setLocation("Cluj");
        order3.setLocation("Sibiu");
        order4.setLocation("Cluj");
        order5.setLocation("Sibiu");
        order6.setLocation("Zalau");

        orderIRepository.create(order1);
        orderIRepository.create(order2);
        orderIRepository.create(order3);
        orderIRepository.create(order4);
        orderIRepository.create(order5);
        orderIRepository.create(order6);

        Delivery delivery1 = new Delivery(1); // includes order1, order2, and order3
        Delivery delivery2 = new Delivery(2); // includes order4 and order5
        Delivery delivery3 = new Delivery(3); // includes order6

        delivery1.addOrder(order1);
        delivery1.addOrder(order2);
        delivery1.addOrder(order3);

        delivery2.addOrder(order4);
        delivery2.addOrder(order5);

        delivery3.addOrder(order6);

        deliveryIRepository.create(delivery1);
        deliveryIRepository.create(delivery2);
        deliveryIRepository.create(delivery3);

        List<Delivery> deliveriesWithToBeShipped = deliveryPersonService.getDeliveriesWithToBeShippedOrders();

        assertNotNull(deliveriesWithToBeShipped);
        assertEquals(2, deliveriesWithToBeShipped.size()); // only delivery1 and delivery2 should be returned

        assertTrue(deliveriesWithToBeShipped.get(0).getOrders().stream()
                .anyMatch(order -> "to be shipped".equalsIgnoreCase(order.getStatus())));

        assertTrue(deliveriesWithToBeShipped.get(1).getOrders().stream()
                .anyMatch(order -> "to be shipped".equalsIgnoreCase(order.getStatus())));
    }

    @Test
    public void testFailedGetDeliveriesWithToBeShippedOrders(){
        Customer customer1 = new Customer(1, "Robert", "Manastur", "123456789", "robert@mail.com");
        Customer customer2 = new Customer(2, "Alex", "Gheorgheni", "987654321", "alex@mail.com");
        Customer customer3 = new Customer(3, "Stefan", "Gruia", "567891234", "stefan@mail.com");

        customerIRepository.create(customer1);
        customerIRepository.create(customer2);
        customerIRepository.create(customer3);

        Order order1 = new Order(1, 1, new Date(2024-12-12), LocalDateTime.of(2024, 12, 12, 12, 0));
        Order order2 = new Order(2, 1, new Date(2024-10-12), LocalDateTime.of(2024, 12, 12, 12, 0));
        Order order3 = new Order(3, 2, new Date(2024-10-12), LocalDateTime.of(2024, 12, 12, 12, 0));
        Order order4 = new Order(4, 3, new Date(2024-11-12), LocalDateTime.of(2024, 12, 12, 12, 0));
        Order order5 = new Order(5, 2, new Date(2024-12-12), LocalDateTime.of(2024, 12, 12, 12, 0));
        Order order6 = new Order(6, 3, new Date(2024-11-12), LocalDateTime.of(2024, 12, 12, 12, 0));

        order1.setStatus("processing");
        order2.setStatus("processing");
        order3.setStatus("processing");
        order4.setStatus("processing");
        order5.setStatus("processing");
        order6.setStatus("processing");

        order1.setLocation("Cluj");
        order2.setLocation("Cluj");
        order3.setLocation("Sibiu");
        order4.setLocation("Cluj");
        order5.setLocation("Sibiu");
        order6.setLocation("Zalau");

        orderIRepository.create(order1);
        orderIRepository.create(order2);
        orderIRepository.create(order3);
        orderIRepository.create(order4);
        orderIRepository.create(order5);
        orderIRepository.create(order6);

        Delivery delivery1 = new Delivery(1); //includes order1, order2, and order3
        Delivery delivery2 = new Delivery(2); //includes order4 and order5
        Delivery delivery3 = new Delivery(3); //includes order6

        delivery1.addOrder(order1);
        delivery1.addOrder(order2);
        delivery1.addOrder(order3);

        delivery2.addOrder(order4);
        delivery2.addOrder(order5);

        delivery3.addOrder(order6);

        deliveryIRepository.create(delivery1);
        deliveryIRepository.create(delivery2);
        deliveryIRepository.create(delivery3);

        Exception exception = assertThrows(
                BusinessLogicException.class,
                () -> deliveryPersonService.getDeliveriesWithToBeShippedOrders(),
                "Expected getDeliveriesWithToBeShippedOrders to throw BusinessLogicException"
        );

        assertEquals("No deliveries with 'to be shipped' orders found", exception.getMessage());
    }

    @Test
    public void testCalculateAndUpdateOrderCost(){
        Customer customer = new Customer(1, "Robert", "Manastur", "123456789", "robert@mail.com");
        customerIRepository.create(customer);

        Order order = new Order(1, 1, new Date(2024-12-12), LocalDateTime.of(2024, 12, 12, 12, 0));
        Packages packages1 = new Packages(1, 100.2, "4x4x4", 100.0);
        Packages packages2 = new Packages(2, 500.2, "2x8x4", 50.0);
        Packages packages3 = new Packages(3, 30.2, "2x3x5", 200.0);
        packageIRepository.create(packages1);
        packageIRepository.create(packages2);
        packageIRepository.create(packages3);
        orderIRepository.create(order);

        order.addPackage(packages1);
        order.addPackage(packages2);
        order.addPackage(packages3);

        double expectedTotalCost = packages1.getCost() + packages2.getCost() + packages3.getCost();
        double actualTotalCost = customerService.calculateAndUpdateOrderCost(order.getId());
        assertEquals(expectedTotalCost, actualTotalCost);
    }

    @Test
    public void testFailedCalculateAndUpdateOrderCost(){
        //use an invalid store id (negative, 0, or a store id that does not exist)
        Integer orderId = 0;

        Exception exception = assertThrows(
                EntityNotFound.class,
                () -> customerService.calculateAndUpdateOrderCost(orderId),
                "Expected calculateAndUpdateOrderCost to throw ValidationException"
        );
        assertEquals("No order found with ID " + orderId, exception.getMessage());
    }

    @Test
    public void testFilterDeliveriesByLocation(){
        Customer customer1 = new Customer(1 ,"Robert", "Manastur", "123456789", "robert@mail.com");
        Customer customer2 = new Customer(2 ,"Alex", "Gheorgheni", "987654321", "alex@mail.com");
        Customer customer3 = new Customer(3 ,"Stefan", "Gruia", "567891234", "stefan@mail.com");
        customerIRepository.create(customer1);
        customerIRepository.create(customer2);
        customerIRepository.create(customer3);

        Order order1 = new Order(1, 1, new Date(2024-12-12), LocalDateTime.of(2024, 12, 12, 12, 0));
        Order order2 = new Order(2, 1, new Date(2024-10-12), LocalDateTime.of(2024, 12, 12, 12, 0));
        Order order3 = new Order(3, 2, new Date(2024-10-12), LocalDateTime.of(2024, 12, 12, 12, 0));
        Order order4 = new Order(4, 3, new Date(2024-11-12), LocalDateTime.of(2024, 12, 12, 12, 0));
        Order order5 = new Order(5, 2, new Date(2024-12-12), LocalDateTime.of(2024, 12, 12, 12, 0));
        Order order6 = new Order(6, 3, new Date(2024-11-12), LocalDateTime.of(2024, 12, 12, 12, 0));

        customerIRepository.update(customer1);
        customerIRepository.update(customer2);
        customerIRepository.update(customer3);

        order1.setStatus("to be shipped");
        order2.setStatus("processing");
        order3.setStatus("to be shipped");
        order4.setStatus("to be shipped");
        order5.setStatus("processing");
        order6.setStatus("processing");

        order1.setLocation("Cluj");
        order2.setLocation("Cluj");
        order3.setLocation("Sibiu");
        order4.setLocation("Cluj");
        order5.setLocation("Sibiu");
        order6.setLocation("Zalau");
        orderIRepository.create(order1);
        orderIRepository.create(order2);
        orderIRepository.create(order3);
        orderIRepository.create(order4);
        orderIRepository.create(order5);
        orderIRepository.create(order6);

        String location = "Cluj";

        List<Order> expectedOrdersForCluj = new ArrayList<>();
        expectedOrdersForCluj.add(order1);
        expectedOrdersForCluj.add(order2);
        expectedOrdersForCluj.add(order4);

        List<Order> actualOrdersForCluj = sellerService.filterDeliveriesByLocation(location);
        assertEquals(expectedOrdersForCluj, actualOrdersForCluj);
    }

    @Test
    public void testFailedFilterDeliveriesByLocation(){
        Customer customer1 = new Customer(1 ,"Robert", "Manastur", "123456789", "robert@mail.com");
        Customer customer2 = new Customer(2 ,"Alex", "Gheorgheni", "987654321", "alex@mail.com");
        Customer customer3 = new Customer(3 ,"Stefan", "Gruia", "567891234", "stefan@mail.com");
        customerIRepository.create(customer1);
        customerIRepository.create(customer2);
        customerIRepository.create(customer3);

        Order order1 = new Order(1, 1, new Date(2024-12-12), LocalDateTime.of(2024, 12, 12, 12, 0));
        Order order2 = new Order(2, 1, new Date(2024-10-12), LocalDateTime.of(2024, 12, 12, 12, 0));
        Order order3 = new Order(3, 2, new Date(2024-10-12), LocalDateTime.of(2024, 12, 12, 12, 0));
        Order order4 = new Order(4, 3, new Date(2024-11-12), LocalDateTime.of(2024, 12, 12, 12, 0));
        Order order5 = new Order(5, 2, new Date(2024-12-12), LocalDateTime.of(2024, 12, 12, 12, 0));
        Order order6 = new Order(6, 3, new Date(2024-11-12), LocalDateTime.of(2024, 12, 12, 12, 0));

        customerIRepository.update(customer1);
        customerIRepository.update(customer2);
        customerIRepository.update(customer3);

        order1.setStatus("to be shipped");
        order2.setStatus("processing");
        order3.setStatus("to be shipped");
        order4.setStatus("to be shipped");
        order5.setStatus("processing");
        order6.setStatus("processing");

        //DO NOT SET ANY LOCATION ON THE ORDERS IN ORDER FOR THE EXCEPTION TO BE THROWN
        orderIRepository.create(order1);
        orderIRepository.create(order2);
        orderIRepository.create(order3);
        orderIRepository.create(order4);
        orderIRepository.create(order5);
        orderIRepository.create(order6);

        //location cluj set randomly -> could be any
        Exception exception = assertThrows(
                BusinessLogicException.class,
                () -> sellerService.filterDeliveriesByLocation("Cluj"),
                "Expected filterDeliveriesByLocation to throw ValidationException"
        );

        assertEquals("There are no locations set on the orders", exception.getMessage());
    }
}
