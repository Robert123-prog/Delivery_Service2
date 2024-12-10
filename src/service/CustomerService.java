package service;

import exceptions.BusinessLogicException;
import exceptions.DatabaseException;
import exceptions.EntityNotFound;
import model.*;
import repository.*;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class CustomerService {
    private final IRepository<Customer> customerIRepository;
    private final IRepository<Order> orderIRepository;
    private final IRepository<Delivery> deliveryIRepository;
    private final IRepository<Packages> packageIRepository;


    public CustomerService(IRepository<Customer> customerIRepository, IRepository<Order> orderIRepository, IRepository<Delivery> deliveryIRepository, IRepository<Packages> packageIRepository){
        this.customerIRepository = customerIRepository;
        this.orderIRepository = orderIRepository;
        this.deliveryIRepository = deliveryIRepository;
        this.packageIRepository = packageIRepository;
    }

    public List<Order> getOrdersFromCustomers(Integer customerId) {
        Customer customer = customerIRepository.get(customerId);
        if (customer == null){
            throw new EntityNotFound("No customer was found with ID " + customerId);
        }
        return customer.getOrders();
    }

    public List<Order> getOrders() {
        List<Order> orders = orderIRepository.readAll();

        if (orders.isEmpty()) throw new DatabaseException("No orders were found");

        return orders;
    }

    public List<Delivery> getDelivery() {
        List<Delivery> deliveries = deliveryIRepository.readAll();

        if (deliveries.isEmpty()) throw new DatabaseException("No deliveries were found");

        return deliveries;
    }

    public List<Customer> getCustomers() {
        List<Customer> customers = customerIRepository.readAll();

        if (customers.isEmpty()) throw new DatabaseException("No customers were found");

        return customers;
    }

    public List<Packages> getPackages() {
        List<Packages> packages = packageIRepository.readAll();

        if (packages.isEmpty()) throw new DatabaseException("No packages were found");

        return packages;
    }

    public void deleteCustomer(Integer customerId) {
        Customer customer = customerIRepository.get(customerId);

        if (customer == null) throw new DatabaseException("No customer was found");

        customerIRepository.delete(customerId);
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

    public void placeOrder(Integer customerId, Integer orderID, Date orderDate, LocalDateTime deliveryDateTime, List<Integer> packageIds) {
        Customer customer = customerIRepository.get(customerId);

        if (customer == null) throw new DatabaseException("No customer was found");

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

    /**
     * Calculates and updates the total cost of an order based on its packages
     *
     * @param orderId ID of the order to calculate cost for
     */
    public double calculateAndUpdateOrderCost(Integer orderId) {
        Order order = orderIRepository.get(orderId);

        if (order == null) throw new DatabaseException("No order was found");

        double totalCost = order.getPackages().stream()
                .mapToDouble(Packages::getCost)
                .sum();
        order.setCost(totalCost);
        orderIRepository.update(order);
        return totalCost;

    }

    public void removeOrder(Integer customerId, Integer orderID) {
        Customer customer = customerIRepository.get(customerId);

        if (customer == null) throw new DatabaseException("No customer was found");
        if (customer.getOrders() == null) throw new BusinessLogicException("The customer has no related orders");

        Order order = orderIRepository.get(orderID);

        if (order == null) throw new DatabaseException("No order was found");

        orderIRepository.delete(orderID); // Delete the order from repository
        customerIRepository.update(customer);
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
            throw new DatabaseException("No order found");
        }
    }

    /**
     * Sorts a list of orders in descending order by their cost.
     *
     * @param orders the list of orders to sort.
     * @return a new list of orders sorted in descending order by price.
     */
    public List<Order> getOrdersSortedByPriceDescending(List<Order> orders) {
        try {
            return orders.stream()
                    .sorted(Comparator.comparingDouble(Order::getCost).reversed())
                    .collect(Collectors.toList());
        }catch (EntityNotFound e){
            throw new DatabaseException("No order found");
        }
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

    public void createCustomer(Integer Id, String name, String address, String phone, String email) {
        Customer customer = new Customer(Id, name, address, phone, email);
        customerIRepository.create(customer);
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
}
