package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The Order class represents a customer's order in the system.
 * It contains details such as order ID, customer ID, order date,
 * delivery date and time, cost, status, and associated packages.
 */
public class Order implements HasID {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final Integer orderID;
    private Integer customerID;
    private LocalDateTime orderDate;
    protected LocalDateTime deliveryDateTime;
    private double totalCost;
    private String status;
    private List<Packages> packages;
    private Integer deliveryId;
    private String location;

    /*
    Order - Package: Aggregation => packages not initialized in the constructor
    Order - Customer: Association => customer will be added by the setter
     */

    /**
     * Constructs an Order with the specified details.
     *
     * @param orderID          the unique identifier for this order     the date when the order was placed
     * @param deliveryDateTime the date and time when the order is to be delivered
     * @param /totalCost       the total cost of the order
     * @param /status          the current status of the order
     */
    public Order(Integer orderID, Integer customerID, LocalDateTime deliveryDateTime) {
        this.orderID = orderID;
        this.orderDate = orderDate != null ? orderDate : LocalDateTime.now();
        this.deliveryDateTime = deliveryDateTime;
        //this.totalCost = totalCost;
        //this.status = status;
        this.packages = new ArrayList<>();
        this.customerID = customerID;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setCustomerID(Integer customerID) {
        this.customerID = customerID;
    }

    /**
     * Gets the unique identifier for this order.
     *
     * @return the order ID
     */
    public Integer getOrderID() {
        return orderID;
    }

    /**
     * Gets the customer ID associated with this order.
     *
     * @return the customer ID
     */
    public Integer getCustomerID() {
        return customerID;
    }

    /**
     * Sets the customer ID for this order.
     *
     * @param customerID the unique identifier for the customer placing this order
     */
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    /**
     * Gets the date when this order was placed.
     *
     * @return the order date
     */
    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    /**
     * Sets the date when this order was placed.
     *
     * @param orderDate the new date for this order
     */
    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public void setPackages(List<Packages> packages) {
        this.packages = packages;
    }

    public Integer getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(Integer deliveryId) {
        this.deliveryId = deliveryId;
    }

    /**
     * Gets the delivery date and time for this order.
     *
     * @return the delivery date and time
     */
    public LocalDateTime getDeliveryDateTime() {
        return deliveryDateTime;
    }

    /**
     * Sets the delivery date and time for this order.
     *
     * @param deliveryDateTime the new delivery date and time
     */
    public void setDeliveryDateTime(LocalDateTime deliveryDateTime) {
        this.deliveryDateTime = deliveryDateTime;
    }

    /**
     * Gets the total cost of this order.
     *
     * @return the cost of the order
     */
    public double getCost() {
        return totalCost;
    }

    /**
     * Sets a new total cost for this order.
     *
     * @param totalCost the new total cost of the order
     */
    public void setCost(double totalCost) {
        this.totalCost = totalCost;
    }

    /**
     * Gets the current status of this order.
     *
     * @return the status of the order
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets a new status for this order.
     *
     * @param status the new status of the order
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Gets a list of packages associated with this order.
     *
     * @return a list of packages in this order
     */
    public List<Packages> getPackages() {
        return packages;
    }

    /**
     * Adds a package to this order.
     *
     * @param packag the package to be added to this order
     */
    public void addPackage(Packages packag) {
        packages.add(packag);
    }

    /**
     * Returns a string representation of this Order object,
     * including all its attributes.
     *
     * @return a string representation of this Order
     */
    @Override
    public String toString() {
        return "Order{" +
                "orderID=" + orderID +
                ", customerID=" + customerID +
                ", orderDate=" + orderDate +
                ", deliveryDate=" + deliveryDateTime +
                ", cost=" + totalCost +
                ", status='" + status + '\'' +
                ", packages=" + packages +
                ", location='" + location + '\'' +
                '}';
    }

    /**
     * Generates a string with the column names for SQL statements.
     *
     * @return A comma-separated string of column names.
     */
    public static String getColumns() {
        return "orderID, customerID, orderDate, deliveryDateTime, totalCost, status, deliveryId, location";
    }

    /**
     * Generates a string with the values of the instance variables for SQL insertion.
     *
     * @return A formatted string of the instance variable values.
     */
    public String getValues() {
        return String.format(
                "%d, %d, '%s', '%s', %.2f, '%s', %d, '%s'",
                orderID,
                customerID,
                orderDate != null ? java.sql.Timestamp.valueOf(orderDate) : null,
                deliveryDateTime != null ? java.sql.Timestamp.valueOf(deliveryDateTime) : null,
                totalCost,
                status,
                deliveryId,
                location
        );
    }

    /**
     * Generates a string with the column-value pairs for SQL update statements.
     *
     * @return A formatted string for SQL update statements.
     */
    public String getUpdateValues() {
        return String.format(
                "customerID = %d, orderDate = '%s', deliveryDateTime = '%s', totalCost = %.2f, status = '%s', deliveryId = %d, location = '%s'",
                customerID,
                orderDate != null ? java.sql.Timestamp.valueOf(orderDate) : null,
                deliveryDateTime != null ? java.sql.Timestamp.valueOf(deliveryDateTime) : null,
                totalCost,
                status,
                deliveryId,
                location
        );
    }

    /**
     * Gets the unique identifier for this object, as required by HasID interface.
     *
     * @return unique identifier (order ID)
     */
    @Override
    public Integer getId() {
        return orderID;
    }

    public String toCsv() {
        StringBuilder serializedPackages = new StringBuilder();
        for (Packages pack : packages) {
            serializedPackages.append(pack.toCsv()).append(";");
        }

        return orderID + "," +
                customerID + "," +
                orderDate + "," +
                deliveryDateTime.format(DATE_TIME_FORMATTER) + "," +
                String.format("%.2f", totalCost) + "," +
                location;
                //serializedPackages.toString();
    }

    public static Order fromCsv(String csvLine) {
        // Split the CSV line into parts
        String[] parts = csvLine.split(",", 6); // Changed to 6 to include totalCost

        Integer orderID = Integer.parseInt(parts[0]);
        Integer customerID = Integer.parseInt(parts[1]);
        String orderDateStr = parts[2];
        Date orderDate = java.sql.Date.valueOf(orderDateStr);
        LocalDateTime deliveryDateTime = LocalDateTime.parse(parts[3], DATE_TIME_FORMATTER);

        // Create the order object
        Order order = new Order(orderID, customerID, deliveryDateTime);

        if (parts.length > 4) {
            order.setTotalCost(Double.parseDouble(parts[4]));
        }
        if (parts.length > 5){
            String location = parts[5];
            order.setLocation(location);
        }

        /*
        // Deserialize packages if any
        if (parts.length > 6) {
            String packagesString = parts[6];
            String[] packagesData = packagesString.split(";");
            for (String packageData : packagesData) {
                if (!packageData.isEmpty()) {
                    order.addPackage(Packages.fromCsv(packageData));
                }
            }
        }
         */
        return order;
    }
}

