package service;

import exceptions.BusinessLogicException;
import exceptions.DatabaseException;
import exceptions.EntityNotFound;
import model.*;
import repository.IRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Service class for managing users, including customers, employees, delivery persons, and departments.
 */
public class UserService {
    private final IRepository<Customer> customerIRepository;
    private final IRepository<Employee> employeeIRepository;
    private final IRepository<Delivery_Person> deliveryPersonIRepository;
    private final IRepository<Department> departmentIRepository;

    /**
     * Constructor to initialize repositories.
     *
     * @param customerIRepository Repository for Customer entities
     * @param employeeIRepository Repository for Employee entities
     * @param deliveryPersonIRepository Repository for Delivery_Person entities
     * @param departmentIRepository Repository for Department entities
     */
    public UserService(IRepository<Customer> customerIRepository, IRepository<Employee> employeeIRepository, IRepository<Delivery_Person> deliveryPersonIRepository, IRepository<Department> departmentIRepository) {
        this.customerIRepository = customerIRepository;
        this.employeeIRepository = employeeIRepository;
        this.deliveryPersonIRepository = deliveryPersonIRepository;
        this.departmentIRepository = departmentIRepository;
    }

    /**
     * Retrieves all available transportation types.
     *
     * @return List of transportation type names
     */
    public List<String> getTransportationTypes() {
        return Stream.of(Transportation_Type.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    /**
     * Creates a new department and adds it to the repository.
     *
     * @param name The name of the department
     * @param task The primary task of the department
     */
    public void createDepartment(String name, String task) {
        Department department = new Department(generateDepartmentId(), name, task);
        departmentIRepository.create(department);
    }

    /**
     * Generates a new unique ID for a department.
     *
     * @return A unique department ID
     */
    private Integer generateDepartmentId() {
        // Implement logic to generate a unique department ID
        return departmentIRepository.readAll().size() + 1;
    }
    /**
     * Retrieves all customers from the repository.
     *
     * @return List of all customers
     */
    public List<Customer> getCustomers() {
        return customerIRepository.readAll();
    }

    /**
     * Deletes a customer from the repository.
     *
     * @param customerId ID of the customer to delete
     */
    public void deleteCustomer(Integer customerId) {
        Customer customer = customerIRepository.get(customerId);
        if (customer == null) throw new EntityNotFound("No customer was found with ID " + customerId);

        customerIRepository.delete(customerId);
    }

    /**
     * Retrieves all employees from the repository.
     *
     * @return List of all employees
     */
    public List<Employee> getEmployees() {
        return employeeIRepository.readAll();
    }

    /**
     * Removes an employee from the repository and their associated department.
     *
     * @param employeeId ID of the employee to unenroll
     */
    public void unenrollEmployee(Integer employeeId) {
        Employee employee = employeeIRepository.get(employeeId);
        if (employee == null) throw new EntityNotFound("No employee was found with ID " + employeeId);

        Department department = departmentIRepository.get(employee.getDepartmentID());
        if (department == null) throw new BusinessLogicException("The employee is not assigned to any department");

        department.removeEmployee(employee);
        employeeIRepository.delete(employeeId);
    }

    /**
     * Retrieves all delivery persons from the repository.
     *
     * @return List of all delivery persons
     */
    public List<Delivery_Person> getDeliveryPerson() {
        return deliveryPersonIRepository.readAll();
    }

    /**
     * Deletes a delivery person from the repository.
     *
     * @param deliveryPersonId ID of the delivery person to delete
     */
    public void unenrollDeliveryPerson(Integer deliveryPersonId) {
        Delivery_Person deliveryPerson = deliveryPersonIRepository.get(deliveryPersonId);
        if (deliveryPerson == null) throw new EntityNotFound("No delivery person found with ID " + deliveryPersonId);

        deliveryPersonIRepository.delete(deliveryPersonId);
    }
}
