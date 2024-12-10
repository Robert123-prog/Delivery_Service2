package controller;

import exceptions.BusinessLogicException;
import exceptions.EntityNotFound;
import exceptions.ValidationException;
import model.Customer;
import model.Order;
import service.CustomerService;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.LongStream;

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
        if (name == null || name.isEmpty()) {
            throw new ValidationException("Name cannot be null or empty.");
        }
        if (address == null || address.isEmpty()) {
            throw new ValidationException("Address cannot be null or empty.");
        }
        if (phone == null || phone.isEmpty()) {
            throw new ValidationException("Phone cannot be null or empty.");
        }
        if (email == null || email.isEmpty()) {
            throw new ValidationException("Invalid email format.");
        }
        Integer id = customerService.getNewCustomerId();
        customerService.createCustomer(id, name, address, phone, email);
        System.out.println("Registered customer with id " + id + " successfully");

//        Integer id = customerService.getNewCustomerId();
//        try {
//            customerService.createCustomer(id, name, address, phone, email);
//            System.out.println("Registered customer with id " + id + " successfully");
//        } catch (ValidationException e) {
//            throw new ValidationException("A validation error occured");
//        }
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
        List<Customer> customers = customerService.getCustomers();
        boolean existsCustomer = false;

        for (Customer customer: customers){
            if (customer.getCustomerID() == customerId){
                existsCustomer = true;
                break;
            }
        }

        if(!existsCustomer) throw new EntityNotFound("No customer found for ID " + customerId);

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
        List<Customer> customers = customerService.getCustomers();
        boolean existsCustomer = false;

        for (Customer customer: customers){
            if (customer.getCustomerID() == customerId){
                existsCustomer = true;
                break;
            }
        }

        if(!existsCustomer) throw new EntityNotFound("No customer found for ID " + customerId);

        List<Order> orders = customerService.getOrders();
        Order assignedOrder = null;
        boolean existsOrder = false;

        for (Order order: orders){
            if (order.getOrderID() == orderId){
                existsOrder = true;
                assignedOrder = order;
                break;
            }
        }

        if(!existsOrder) throw new EntityNotFound("No order found for ID " + orderId);

        if (assignedOrder.getCustomerID() != customerId) throw new BusinessLogicException("The order is not assigned to the selected customer");

        customerService.removeOrder(customerId, orderId);
        System.out.println("Order with id " + orderId + " by customer with id " + customerId + " removed successfully");
    }

    /**
     * Displays personal orders in a formatted manner.
     *
     * @param /orders list of orders to display
     */
    public void viewPersonalOrders(Integer customerId) {
        List<Customer> customers = customerService.getCustomers();
        boolean existsCustomer = false;

        for (Customer customer: customers){
            if (customer.getCustomerID() == customerId){
                existsCustomer = true;
                break;
            }
        }

        if(!existsCustomer) throw new EntityNotFound("No customer found for ID " + customerId);


        List<Order> orders = customerService.getOrdersFromCustomers(customerId);
        if (orders.isEmpty()) {
            throw new EntityNotFound("No orders found for customer with ID " + customerId + ".");
        }
        StringBuilder output = new StringBuilder("Available Orders:\n");
        orders.forEach(order -> output.append(order.toString()).append("\n"));
        System.out.println(output);

        /*
        List<Order> orders = customerService.getOrdersFromCustomers(customerId);
        StringBuilder output = new StringBuilder("Available Orders:\n");

        orders.forEach(order -> output.append(order.toString()).append("\n"));

        System.out.println(output);
        */

    }

    /**
     * Calculates total cost based on provided packages.
     * <p>
     * /@param packages list of packages to calculate cost for
     *
     * @return total cost calculated from packages
     */
    public double calculateOrderCost(Integer orderId) {
        List<Order> orders = customerService.getOrders();
        boolean orderExists = false;

        for (Order order: orders){
            if (order.getId() == orderId){
                orderExists = true;
                 break;
            }
        }

        if (!orderExists) throw new EntityNotFound("No order found with ID " + orderId);

        return customerService.calculateAndUpdateOrderCost(orderId);
    }

    /**
     * Schedules Delivery date/time for given Order.
     *
     * @param orderID          unique identifier for Order object
     * @param deliveryDateTime date/time when Delivery should occur.
     */

    public void scheduleDeliveryDate(Integer orderID, LocalDateTime deliveryDateTime) {
        List<Order> orders = customerService.getOrders();
        boolean orderExists = false;

        for (Order order: orders){
            if (Objects.equals(order.getId(), orderID)){
                orderExists = true;
                break;
            }
        }

        if (!orderExists) throw new EntityNotFound("No order found with ID " + orderID);

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
        List<Customer> customers = customerService.getCustomers();
        boolean customerExists = false;

        for (Customer customer: customers){
            if (customer.getId() == customerId){
                customerExists = true;
            }
        }

        if (!customerExists) throw new EntityNotFound("No customer found with ID " + customerId);

        List<Order>orders = customerService.getOrdersFromCustomers(customerId);
        List<Order>sortedOrders = customerService.getOrdersSortedByPriceDescending(orders);
        StringBuilder output = new StringBuilder("Available Orders:\n");
        sortedOrders.forEach(order -> output.append(order.toString()).append("\n"));
        System.out.println(output);
    }
}
