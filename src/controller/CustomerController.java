package controller;

import model.Order;
import service.CustomerService;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService){
        this.customerService = customerService;
    }

    public void viewAllCustomers() {
        StringBuilder output = new StringBuilder("Available Customers:\n");
        customerService.getCustomers().forEach(customer -> output.append(customer.toString()).append("\n"));
        System.out.println(output);
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
        Integer id = customerService.getNewCustomerId();
        customerService.createCustomer(id, name, address, phone, email);
        System.out.println("Registered customer with id " + id + " successfully");
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
        Integer orderId = customerService.getNewOrderId();
        customerService.placeOrder(customerId, orderId, orderDate, deliveryDateTime, packageIds);
        System.out.println("Order with id " + orderId + " by customer with id " + customerId + " successfully");
    }

    /**
     * Removes an existing order for a customer.
     *
     * @param customerId the ID of the customer whose order will be removed
     * @param orderId    the ID of the order to remove
     */
    public void removeAnOrder(Integer customerId, Integer orderId) {
        customerService.removeOrder(customerId, orderId);
        System.out.println("Order with id " + orderId + " by customer with id " + customerId + " removed successfully");
    }

    /**
     * Displays personal orders in a formatted manner.
     *
     * @param /orders list of orders to display
     */
    public void viewPersonalOrders(Integer customerId) {
        List<Order> orders = customerService.getOrdersFromCustomers(customerId);
        StringBuilder output = new StringBuilder("Available Orders:\n");

        orders.forEach(order -> output.append(order.toString()).append("\n"));

        System.out.println(output);
    }

    /**
     * Calculates total cost based on provided packages.
     * <p>
     * /@param packages list of packages to calculate cost for
     *
     * @return total cost calculated from packages
     */
    public double calculateOrderCost(Integer orderId) {
        return customerService.calculateAndUpdateOrderCost(orderId);
    }

    /**
     * Schedules Delivery date/time for given Order.
     *
     * @param orderID          unique identifier for Order object
     * @param deliveryDateTime date/time when Delivery should occur.
     */

    public void scheduleDeliveryDate(Integer orderID, LocalDateTime deliveryDateTime) {
        customerService.scheduleDelivery(orderID, deliveryDateTime);
        System.out.println("Scheduled Delivery date for Order with id " + orderID + " successfully");
    }

    public void viewAllPackages() {
        StringBuilder output = new StringBuilder("All Packages:\n");
        customerService.getPackages().forEach(packages -> output.append(packages.toString()).append("\n"));
        System.out.println(output);
    }

    /**
     * @param customerId
     * @return
     */
    public void getOrdersSortedByPriceDescending(Integer customerId) {
        List<Order>orders = customerService.getOrdersFromCustomers(customerId);
        List<Order>sortedOrders = customerService.getOrdersSortedByPriceDescending(orders);
        StringBuilder output = new StringBuilder("Available Orders:\n");
        sortedOrders.forEach(order -> output.append(order.toString()).append("\n"));
        System.out.println(output);
    }
}
