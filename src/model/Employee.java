package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an employee in the organization.
 * This class implements the HasID interface and manages employee information
 * including their department affiliation and assigned deliveries.
 * It has a composition relationship with Department and an aggregation relationship with Delivery.
 */
public class Employee extends Person {
    private Integer employeeID;
    private int departmentID;
    private String license;
    private List<Delivery> deliveries;

    /**
     * Constructs a new Employee object with full details.
     * Note:
     * - Department-Employee: Composition relationship (department initialized in constructor)
     * - Employee-Delivery: Aggregation relationship (deliveries list initialized but empty)
     *
     * @param employeeID   The unique identifier for the employee
     * @param departmentID The ID of the department the employee belongs to
     * @param name         The name of the employee
     * @param phone        The phone number of the employee
     * @param license      The license information of the employee
     */
    public Employee(Integer employeeID, int departmentID, String name, String phone, String license) {
        this.employeeID = employeeID;
        this.departmentID = departmentID;
        this.name = name;
        this.phone = phone;
        this.license = license;
        this.deliveries = new ArrayList<>();
    }

    /**
     * Constructs a new Employee object with basic information.
     *
     * @param name  The name of the employee
     * @param phone The phone number of the employee
     */
    public Employee(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    /**
     * Constructs a new Employee object with only ID.
     *
     * @param employeeID The unique identifier for the employee
     */
    public Employee(int employeeID) {
        this.employeeID = employeeID;
    }

    /**
     * Returns the unique identifier of the employee.
     *
     * @return The employee ID
     */
    public int getEmployeeID() {
        return employeeID;
    }

    /**
     * Returns the ID of the department this employee belongs to.
     *
     * @return The department ID
     */
    public int getDepartmentID() {
        return departmentID;
    }

    /**
     * Sets the department ID for this employee.
     *
     * @param departmentID The new department ID
     */
    public void setDepartmentID(int departmentID) {
        this.departmentID = departmentID;
    }

    /**
     * Returns the license information of the employee.
     *
     * @return The employee's license information
     */
    public String getLicense() {
        return license;
    }

    /**
     * Sets new license information for the employee.
     *
     * @param license The new license information
     */
    public void setLicense(String license) {
        this.license = license;
    }

    /**
     * Returns the list of deliveries assigned to this employee.
     *
     * @return List of Delivery objects assigned to this employee
     */
    public List<Delivery> getDeliveries() {
        return deliveries;
    }

    /**
     * Adds a new delivery assignment to the employee.
     *
     * @param delivery The Delivery object to be added to the employee's assignments
     */
    public void addDelivery(Delivery delivery) {
        deliveries.add(delivery);
    }

    /**
     * Removes a delivery assignment from the employee by delivery ID.
     * Searches through the deliveries list and removes the matching delivery if found.
     *
     * @param deliveryId The ID of the delivery to be removed
     */
    public void removeDeliv(Integer deliveryId) {
        Delivery deliveryToRemove = null;
        for (Delivery delivery : deliveries) {
            if (delivery.getDeliveryID().equals(deliveryId)) {
                deliveryToRemove = delivery;
                break;
            }
        }
        if (deliveryToRemove != null) {
            deliveries.remove(deliveryToRemove);
        }
    }

    /**
     * Returns a string representation of the Employee object.
     *
     * @return A string containing the employee's ID, department ID, name, phone, license, and deliveries
     */
    @Override
    public String toString() {
        return "Employee{" +
                "employeeID=" + employeeID +
                ", departmentID=" + departmentID +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", license='" + license + '\'' +
                ", deliveries=" + deliveries +
                '}';
    }

    /**
     * Generates a string with the column names for SQL statements.
     *
     * @return A comma-separated string of column names.
     */
    public static String getColumns() {
        return "employeeID, departmentID, license, name, phone";
    }

    /**
     * Generates a string with the values of the instance variables for SQL insertion.
     *
     * @return A formatted string of the instance variable values.
     */
    public String getValues() {
        return String.format(
                "%d, %d, '%s', '%s', '%s'",
                employeeID,
                departmentID,
                license,
                name,
                phone
        );
    }

    /**
     * Generates a string with the column-value pairs for SQL update statements.
     *
     * @return A formatted string for SQL update statements.
     */
    public String getUpdateValues() {
        return String.format(
                "departmentID = %d, license = '%s', name = '%s', phone = '%s'",
                departmentID,
                license,
                name,
                phone
        );
    }

    /**
     * Returns the ID of this employee.
     * Implementation of the HasID interface.
     *
     * @return The employee ID
     */
    public Integer getId() {
        return employeeID;
    }

    public String toCsv(){
        StringBuilder serializedDeliveries = new StringBuilder();

        for (Delivery delivery: deliveries){
            serializedDeliveries.append(delivery.toCsv()).append(";");
        }

        return employeeID + "," +
                departmentID + "," +
                name + "," +
                phone + "," +
                license + "," +
                serializedDeliveries.toString();
    }

    public static Employee fromCsv(String csvLine){
        String[] parts = csvLine.split(",", 6);

        // Parse basic attributes
        Integer employeeID = Integer.parseInt(parts[0]);
        Integer departmentID = Integer.parseInt(parts[1]);
        String name = parts[2];
        String phone = parts[3];
        String license = parts[4];
        String deliveriesString = parts[5]; // Serialized deliveries

        // Create a new Employee object with parsed data
        Employee employee = new Employee(employeeID, departmentID, name, phone, license);

        // Parse deliveries if they exist
        if (!deliveriesString.isEmpty()) {
            String[] deliveryParts = deliveriesString.split(";");
            for (String deliveryString : deliveryParts) {
                Delivery delivery = Delivery.fromCsv(deliveryString); // Use Delivery's fromString method
                employee.addDelivery(delivery);
            }
        }

        return employee;
    }
}