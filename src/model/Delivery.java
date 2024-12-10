/**
 * Represents a delivery in the delivery management system.
 * This class implements the HasID interface and manages delivery information,
 * including relationships with employees, delivery persons, orders, and transportation.
 *
 * Relationships:
 * - Employee - Delivery: Aggregation (employee not initialized in constructor)
 * - Delivery - Order: Composition (order initialized in constructor)
 * - Delivery - Transportation: Association (transportation assigned by setter)
 */
package model;

import java.util.ArrayList;
import java.util.List;

public class Delivery implements HasID {
    /** Unique identifier for the delivery */
    private final Integer deliveryID;

    /** ID of the delivery person assigned to this delivery */
    private Integer deliveryPersonID;

    /** ID of the employee handling this delivery */
    private int employeeID;

    /** ID of the order being delivered */
    private Integer orderID;

    /** ID of the transportation method used */
    private int transportationID;

    /** Timestamp of when the delivery is scheduled */
    //private Timestamp time;

    /** Type of transportation used for this delivery */
    private Transportation transportation_type;

    /** String of the location where the order will be shipped*/
    private String location;

    /** List of orders associated with this delivery */
    private List<Order> orders;

    /**
     * Constructs a new Delivery with the specified details.
     *
     * @param deliveryID Unique identifier for the delivery
     */
    public Delivery(Integer deliveryID) {
        this.deliveryID = deliveryID;
        this.orders = new ArrayList<Order>();
        //this.orderID = orderID;
        //this.time = time;
    }

    public String getLocation() {
        return location;
    }

    /**
     * Returns the delivery's unique identifier.
     *
     * @return The delivery's ID
     */
    public Integer getDeliveryID() {
        return deliveryID;
    }

    /**
     * Returns the ID of the employee assigned to this delivery.
     *
     * @return The employee's ID
     */
    public Integer getEmployeeID() {
        return employeeID;
    }

    /**
     * Returns the ID of the delivery person assigned to this delivery.
     *
     * @return The delivery person's ID
     */
    public Integer getDeliveryPeronID() {
        return deliveryPersonID;
    }

    /**
     * Sets the ID of the employee assigned to this delivery.
     *
     * @param employeeID ID of the employee to be assigned
     */
    public void setEmployeeID(Integer employeeID) {
        this.employeeID = employeeID;
    }

    /**
     * Returns the ID of the order being delivered.
     *
     * @return The order's ID
     */
    /*
    public Integer getOrderID() {
        return orderID;
    }
*/
    /**
     * Sets the ID of the order for this delivery.
     *
     * @param orderID ID of the order to be delivered
     */
    /*
    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }
*/
    /**
     * Returns the ID of the transportation method used.
     *
     * @return The transportation's ID
     */
    public int getTransportationID() {
        return transportationID;
    }

    /**
     * Sets the ID of the transportation method for this delivery.
     *
     * @param transportationID ID of the transportation method to be used
     */
    public void setTransportationID(int transportationID) {
        this.transportationID = transportationID;
    }

    /**
     * Sets the ID of the delivery person assigned to this delivery.
     *
     * @param deliveryPeronID ID of the delivery person to be assigned
     */
    public void setDeliveryPeronID(Integer deliveryPeronID) {
        this.deliveryPersonID = deliveryPeronID;
    }

    /**
     * Returns the type of transportation used for this delivery.
     *
     * @return The transportation type
     */
    public Transportation getTransportation_type() {
        return transportation_type;
    }

    /**
     * Sets the type of transportation for this delivery.
     *
     * @param transportation_type Type of transportation to be used
     */
    public void setTransportation_type(Transportation transportation_type) {
        this.transportation_type = transportation_type;
    }


    public List<Order> getOrders() {
        return orders;
    }
    /**
     * Adds an order to the list of orders for this delivery.
     *
     * @param order Order to be added to the delivery
     */
    public void addOrder(Order order) {
        orders.add(order);
    }

    /**
     * Returns a string representation of the Delivery object.
     * Includes delivery ID, employee ID, order ID, transportation ID, and transportation type.
     *
     * @return String representation of the delivery
     */
    @Override
    public String toString() {
        return "Delivery{" +
                "deliveryID=" + deliveryID +
                //", employeeID=" + employeeID +
                //", orderID=" + orderID +
                //", transportationID=" + transportationID +
                //", transportation_type=" + transportation_type +
                ", location='" + location +
                ", orders=" + orders +
                '}';
    }

    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Implementation of HasID interface.
     * Returns the delivery's unique identifier.
     *
     * @return The delivery's ID
     */
    @Override
    public Integer getId() {
        return deliveryID;
    }

    /**
     * Generates a string with the column names for SQL statements.
     *
     * @return A comma-separated string of column names.
     */
    public static String getColumns() {
        return "deliveryID, location";
    }

    /**
     * Generates a string with the values of the instance variables for SQL insertion.
     *
     * @return A formatted string of the instance variable values.
     */
    public String getValues() {
        return String.format(//%d, %d, %d, %d, '%s',
                "%d, '%s'",
                deliveryID,
                //deliveryPersonID,
                //employeeID,
                //orderID,
                //transportationID,
                //transportation_type != null ? transportation_type.toString() : null,
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
                "deliveryPersonID = %d, employeeID = %d, transportationID = %d, transportation_type = '%s', location = '%s'",
                deliveryPersonID,
                employeeID,
                transportationID,
                transportation_type != null ? transportation_type.toString() : null,
                location
        );
    }

    public String toCsv(){
        StringBuilder serializedOrders = new StringBuilder();

        for (Order order: orders){
            serializedOrders.append(order.toCsv()).append(";");
        }

        return  deliveryID + ",";
                //orderID + "," +
                //time + ",";

    }

    public static Delivery fromCsv(String csvLine){
        String[] parts = csvLine.split(",", 2);

        Integer deliveryId = Integer.parseInt(parts[0]);
//        Integer deliveryPersonId = Integer.parseInt(parts[1]);
//        Integer employeeId = Integer.parseInt(parts[2]);
//        Integer orderId = Integer.parseInt(parts[1]);
//        Integer transportationId = Integer.parseInt(parts[4]);

        // Parse the Timestamp
//        Timestamp time = Timestamp.valueOf(parts[2]);

        // Parse the Transportation_Type (assumes an enum with a valueOf method)
//        Transportation_Type transportationType = Transportation_Type.valueOf(parts[6]);

        // The location field
//        String location = parts[7];

        Delivery delivery = new Delivery(deliveryId);

        if (parts.length > 1){
            String ordersString = parts[1];
            String[] ordersData = ordersString.split(";");
            for (String orderData: ordersData){
                if (!orderData.isEmpty()){
                    delivery.addOrder(Order.fromCsv(orderData));
                }
            }
        }

        return new Delivery(deliveryId);//, time);
    }
}