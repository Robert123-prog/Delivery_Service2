package controller;

import exceptions.BusinessLogicException;
import exceptions.EntityNotFound;
import model.Customer;
import model.Delivery_Person;
import model.Employee;
import service.UserService;

import java.util.List;
import java.util.Objects;

public class UserController {
    private UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    /**
     * Retrieves all Transportation Types available in system.
     *
     * @return list containing Transportation Types as Strings.
     */

    public List<String> getAllTransportationTypes() {
        return userService.getTransportationTypes();
    }

    /**
     * Displays Transportation Types in formatted manner.
     *
     * @param transportationTypes list containing Transportation Types as Strings.
     */

    public void viewAllTransportationTypes(List<String> transportationTypes) {
        StringBuilder output = new StringBuilder("Transportation Types:\n");
        transportationTypes.forEach(transportationType -> output.append(transportationType).append("\n"));
        System.out.println(output);
    }

    /**
     * Deletes a customer by their ID.
     *
     * @param customerId the ID of the customer to delete
     */
    public void deleteCustomer(Integer customerId) {
        try {
            userService.deleteCustomer(customerId);
            System.out.println("Deleted customer with id " + customerId + " successfully");
        }catch (EntityNotFound e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Displays all available customers
     */
    public void viewAllCustomers() {
        StringBuilder output = new StringBuilder("Available Customers:\n");
        userService.getCustomers().forEach(customer -> output.append(customer.toString()).append("\n"));
        System.out.println(output);
    }

    /**
     * Displays all employees and their details in a formatted manner.
     */
    public void viewAllEmployees() {
        StringBuilder output = new StringBuilder("All Employees:\n");
        userService.getEmployees().forEach(employee -> output.append(employee.toString()).append("\n"));
        System.out.println(output);
    }

    /**
     * Deletes an employee by their ID.
     *
     * @param employeeId the ID of the employee to delete
     */
    public void deleteEmployee(Integer employeeId) {
        try {
            userService.unenrollEmployee(employeeId);
            System.out.println("Employee with id " + employeeId + " unenrolled successfully");
        }catch (EntityNotFound e){
            System.out.println(e.getMessage());
        }catch (BusinessLogicException e1){
            System.out.println(e1.getMessage());
        }
    }

    /**
     * Displays all registered Delivery Persons in a formatted manner.
     */
    public void viewAllDeliveryPersons() {
        StringBuilder output = new StringBuilder("All Delivery Persons:\n");
        userService.getDeliveryPerson().forEach(deliveryPerson -> output.append(deliveryPerson.toString()).append("\n"));
        System.out.println(output);
    }

    /**
     * Deletes a delivery person by their ID.
     *
     * @param deliveryPersonId the ID of the delivery person to delete
     */
    public void deleteDeliveryPerson(Integer deliveryPersonId) {
        try {
            userService.unenrollDeliveryPerson(deliveryPersonId);
            System.out.println("Delivery person with id " + deliveryPersonId + " unenrolled successfully");
        }catch (EntityNotFound e){
            System.out.println(e.getMessage());
        }
    }


}
