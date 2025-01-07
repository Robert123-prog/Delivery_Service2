package service;

import exceptions.BusinessLogicException;
import exceptions.EntityNotFound;
import model.*;
import repository.*;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class responsible for handling business logic related to customers, orders, deliveries, and packages.
 */
public class CustomerService {
    private final IRepository<Customer> customerIRepository;
    private final IRepository<Order> orderIRepository;
    private final IRepository<Delivery> deliveryIRepository;
    private final IRepository<Packages> packageIRepository;

    /**
     * Constructs a CustomerService with the specified repositories.
     *
     * @param customerIRepository Repository for managing customers.
     * @param orderIRepository    Repository for managing orders.
     * @param deliveryIRepository Repository for managing deliveries.
     * @param packageIRepository  Repository for managing packages.
     */
    public CustomerService(IRepository<Customer> customerIRepository, IRepository<Order> orderIRepository, IRepository<Delivery> deliveryIRepository, IRepository<Packages> packageIRepository) {
        this.customerIRepository = customerIRepository;
        this.orderIRepository = orderIRepository;
        this.deliveryIRepository = deliveryIRepository;
        this.packageIRepository = packageIRepository;
    }

    /**
     * Retrieves all orders from the repository.
     *
     * @return List of all orders.
     */
    public List<Order> getOrders() {
        return orderIRepository.readAll();
    }

    /**
     * Retrieves all deliveries from the repository.
     *
     * @return List of all deliveries.
     */
    public List<Delivery> getDelivery() {
        return deliveryIRepository.readAll();
    }

    /**
     * Retrieves all customers from the repository.
     *
     * @return List of all customers.
     */
    public List<Customer> getCustomers() {
        return customerIRepository.readAll();
    }

    /**
     * Retrieves all packages from the repository.
     *
     * @return List of all packages.
     */
    public List<Packages> getPackages() {
        return packageIRepository.readAll();
    }

    /**
     * Places a new order for a customer.
     *
     * @param customerId       ID of the customer placing the order.
     * @param orderID          Unique identifier for the order.
     * @param deliveryDateTime Date and time for the delivery.
     * @param packageIds       List of package IDs associated with the order.
     * @throws SQLException if there is an error accessing the database.
     */
    public void placeOrder(Integer customerId, Integer orderID, LocalDateTime deliveryDateTime, List<Integer> packageIds) throws SQLException {
        Customer customer = customerIRepository.get(customerId);
        if (customer == null) {
            throw new EntityNotFound("Customer not found for ID " + customerId);
        }

        String location = customer.getAddress();
        Order order = new Order(orderID, customerId, deliveryDateTime);
        orderIRepository.create(order);


        double totalCost = 0;
        for (Integer packageId : packageIds) {
            Packages packages = packageIRepository.get(packageId);
            if (packages != null) {
                packages.setOrderID(orderID);  // Set the order ID in package
                order.addPackage(packages);    // Add package to order

                if (orderIRepository instanceof DBRepository<Order>) {

                    String insertOrderPackageSQL = "INSERT INTO orderpackages VALUES(?, ?) ON CONFLICT (orderid, packageid) DO NOTHING";
                    DbUtil.executeUpdate(insertOrderPackageSQL, orderID, packageId);
                }
                totalCost += packages.getCost();
                packageIRepository.update(packages); // Update package with new order ID
            }
        }

        // Set order cost and save
        order.setTotalCost(totalCost);
        order.setLocation(location);
        orderIRepository.update(order);
        // Update customer
        customer.addDOrder(order);
        customerIRepository.update(customer);
    }

    /**
     * Calculates the total cost of an order based on its associated packages.
     *
     * @param orderId ID of the order to calculate the cost for.
     * @return Total cost of the order.
     * @throws EntityNotFound if no order is found with the specified ID.
     */
    public double calculateOrderCost(Integer orderId) {
        Order order = orderIRepository.get(orderId);
        return order.getTotalCost();
    }

    /**
     * Removes an order for a specific customer.
     *
     * @param customerId ID of the customer whose order is to be removed.
     * @param orderID    ID of the order to be removed.
     * @throws EntityNotFound if the customer or order is not found.
     * @throws BusinessLogicException if the customer has no related orders.
     */
    public void removeOrder(Integer customerId, Integer orderID) {
        Customer customer = customerIRepository.get(customerId);

        if (customer == null) throw new EntityNotFound("No customer found with ID " + customerId);
        if (customer.getOrders() == null) throw new BusinessLogicException("The customer has no related orders");

        Order order = orderIRepository.get(orderID);
        if (order == null) throw new EntityNotFound("No order found with ID " + orderID);

        // Successfully removed order from customer's orders list
        orderIRepository.delete(orderID); // Delete the order from repository
        customerIRepository.update(customer);
    }

    /**
     * Deletes a customer and all associated orders.
     *
     * @param customerId ID of the customer to be deleted.
     * @throws EntityNotFound if the customer is not found.
     */
    public void deleteCustomer(Integer customerId) {
        Customer customer = customerIRepository.get(customerId);

        if (customer == null) throw new EntityNotFound("No customer found with ID " + customerId);

        // Delete associated orders
        List<Order> orders = getOrdersFromCustomers(customerId);
        for (Order order : orders) {
            orderIRepository.delete(order.getId());
        }
        // Delete the customer
        customerIRepository.delete(customerId);
    }

    /**
     * Schedules a delivery for a specific order.
     *
     * @param orderId         ID of the order to schedule delivery for.
     * @param deliveryDateTime Date and time when delivery should occur.
     * @throws EntityNotFound if no order is found with the specified ID.
     */
    public void scheduleDelivery(Integer orderId, LocalDateTime deliveryDateTime) {
        Order order = orderIRepository.get(orderId);

        if (order == null) throw new EntityNotFound("No order found with ID " + orderId);

        order.setDeliveryDateTime(deliveryDateTime);
        orderIRepository.update(order);
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

    /**
     * Generates a new unique customer ID.
     *
     * @return Next available customer ID.
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

    /**
     * Creates a new customer with the specified details.
     *
     * @param Id      Unique identifier for the customer.
     * @param name    Name of the customer.
     * @param address Address of the customer.
     * @param phone   Phone number of the customer.
     * @param email   Email address of the customer.
     */
    public void createCustomer(Integer Id, String name, String address, String phone, String email) {
        Customer customer = new Customer(Id, name, address, phone, email);
        customerIRepository.create(customer);
    }

    /**
     * Generates a new unique order ID.
     *
     * @return Next available order ID.
     */
    public Integer getNewOrderId() {
        int maxId = 0;
        for (Integer Id : orderIRepository.getKeys()) {
            if (Id.compareTo(maxId) > 0) {
                maxId = Id;
            }
        }
        return maxId + 1;
    }

    /**
     * Retrieves all orders associated with a specific customer.
     *
     * @param customerId ID of the customer whose orders are to be retrieved.
     * @return List of orders associated with the specified customer.
     * @throws EntityNotFound if no customer is found with the specified ID.
     */
    public List<Order> getOrdersFromCustomers(Integer customerId) {
        Customer customer = customerIRepository.get(customerId);

        if (customer == null) throw new EntityNotFound("No customer found with ID " + customerId);

        // Using getAll() or a custom method based on implementation
        List<Order> orders = orderIRepository.readAll();
        // Filter orders based on customerId
        return orders.stream()
                .filter(order -> order.getCustomerID().equals(customerId))
                .collect(Collectors.toList());
    }
}