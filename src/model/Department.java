package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a department within an organization.
 * This class implements the HasID interface and manages department information
 * including its employees, tasks, and basic details.
 */
public class Department implements HasID {
    private final Integer departmentID;
    private String name;
    private String task;
    private List<Employee> employees;

    /**
     * Constructs a new Department object.
     * Initializes an empty list of employees and sets the basic department information.
     *
     * @param departmentID The unique identifier for the department
     * @param name        The name of the department
     * @param task        The primary task or responsibility of the department
     */
    public Department(Integer departmentID, String name, String task) {
        this.departmentID = departmentID;
        this.name = name;
        this.task = task;
        this.employees = new ArrayList<>();
    }

    /**
     * Returns the unique identifier of the department.
     *
     * @return The department ID
     */
    public int getDepartmentID() {
        return departmentID;
    }

    /**
     * Returns the name of the department.
     *
     * @return The department name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets a new name for the department.
     *
     * @param name The new department name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the primary task of the department.
     *
     * @return The department's task
     */
    public String getTask() {
        return task;
    }

    /**
     * Sets a new task for the department.
     *
     * @param task The new department task
     */
    public void setTask(String task) {
        this.task = task;
    }

    /**
     * Returns the list of employees in this department.
     *
     * @return List of Employee objects assigned to this department
     */
    public List<Employee> getEmployees() {
        return employees;
    }

    /**
     * Adds a new employee to the department.
     *
     * @param employee The Employee object to be added to the department
     */
    public void addEmployee(Employee employee) {
        employees.add(employee);
    }

    /**
     * Removes an employee from the department.
     *
     * @param employee The Employee object to be removed from the department
     * @return true if the employee was successfully removed, false otherwise
     */
    public boolean removeEmployee(Employee employee) {
        boolean removed = employees.remove(employee);
        return removed;
    }

    /**
     * Returns a string representation of the Department object.
     *
     * @return A string containing the department's ID, name, task, and list of employees
     */
    @Override
    public String toString() {
        return "Department{" +
                "departmentID=" + departmentID +
                ", name='" + name + '\'' +
                ", task='" + task + '\'' +
                ", employees=" + employees +
                '}';
    }

    /**
     * Generates a string with the column names for SQL statements.
     *
     * @return A comma-separated string of column names.
     */
    public static String getColumns() {
        return "name, task";
    }

    /**
     * Generates a string with the values of the instance variables for SQL insertion.
     *
     * @return A formatted string of the instance variable values.
     */
    public String getValues() {
        return String.format(
                "'%s', '%s'",
                name,
                task
        );
    }

    /**
     * Generates a string with the column-value pairs for SQL update statements.
     *
     * @return A formatted string for SQL update statements.
     */
    public String getUpdateValues() {
        return String.format(
                "name = '%s', task = '%s'",
                name,
                task
        );
    }

    /**
     * Returns the ID of this department.
     * Implementation of the HasID interface.
     *
     * @return The department ID
     */
    public Integer getId() {
        return departmentID;
    }

    public String toCsv(){
        StringBuilder serilizedEmployees = new StringBuilder();

        for (Employee employee: employees){
            serilizedEmployees.append(employee.toCsv()).append(";");
        }

        return departmentID + ","+
                name + "," +
                task + serilizedEmployees.toString();
    }

    public static Department fromCsv(String csvLine) {
        // Split the input string into parts
        String[] parts = csvLine.split(",", 4);

        // Parse basic department information
        Integer departmentID = Integer.parseInt(parts[0].replace("Department", "").trim());
        String name = parts[1];
        String task = parts[2];
        String employeesString = parts[3]; // Serialized employees string

        // Create a new Department object
        Department department = new Department(departmentID, name, task);

        // Parse and add employees if they exist
        if (!employeesString.isEmpty()) {
            String[] employeeParts = employeesString.split(";");
            for (String employeeString : employeeParts) {
                if (!employeeString.trim().isEmpty()) {
                    Employee employee = Employee.fromCsv(employeeString); // Call Employee's deserialize method
                    department.addEmployee(employee);
                }
            }
        }

        return department;
    }
}