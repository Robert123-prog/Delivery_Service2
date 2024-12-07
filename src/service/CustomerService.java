package service;

import model.*;
import repository.*;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
        return customer.getOrders();
    }

    public List<Order> getOrders() {
        return orderIRepository.readAll();
    }

    public List<Delivery> getDelivery() {
        return deliveryIRepository.readAll();
    }

    public List<Customer> getCustomers() {
        return customerIRepository.readAll();
    }

    public List<Packages> getPackages() {
        return packageIRepository.readAll();
    }

    public void deleteCustomer(Integer customerId) {
        Customer customer = customerIRepository.get(customerId);
        if (customer != null) {
            customerIRepository.delete(customerId);
        }
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
