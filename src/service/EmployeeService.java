package service;

import exceptions.BusinessLogicException;
import exceptions.EntityNotFound;
import model.*;
import repository.IRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing employees, departments, and deliveries.
 */
public class EmployeeService {
    private final IRepository<Employee> employeeIRepository;
    private final IRepository<Delivery> deliveryIRepository;
    private final IRepository<Department> departmentIRepository;

    /**
     * Constructor for EmployeeService.
     *
     * @param employeeIRepository   Repository for employee entities
     * @param deliveryIRepository   Repository for delivery entities
     * @param departmentIRepository Repository for department entities
     */
    public EmployeeService(IRepository<Employee> employeeIRepository, IRepository<Delivery> deliveryIRepository, IRepository<Department> departmentIRepository) {
        this.employeeIRepository = employeeIRepository;
        this.deliveryIRepository = deliveryIRepository;
        this.departmentIRepository = departmentIRepository;
    }

    /**
     * Retrieves all employees from the repository.
     *
     * @return List of all employees.
     */
    public List<Employee> getEmployees() {
        return employeeIRepository.readAll();
    }

    /**
     * Creates a new employee and assigns them to a department.
     *
     * @param Id           The ID of the new employee
     * @param departmentId The ID of the department the employee belongs to
     * @param name         The name of the employee
     * @param phone        The phone number of the employee
     * @param license      The license of the employee (if applicable)
     */
    public void createEmployee(Integer Id, Integer departmentId, String name, String phone, String license) {
        Employee employee = new Employee(Id, departmentId, name, phone, license);
        employeeIRepository.create(employee);

        Department department = departmentIRepository.get(departmentId);

        if (department == null) throw new EntityNotFound("No department found for ID " + departmentId);

        department.addEmployee(employee);
    }

    /**
     * Retrieves all departments from the repository.
     *
     * @return List of all departments.
     */
    public List<Department> getDepartments() {
        return departmentIRepository.readAll();
    }

    /**
     * Retrieves all deliveries assigned to a specific employee.
     *
     * @param employeeId ID of the employee.
     * @return List of deliveries assigned to the employee.
     * @throws EntityNotFound if the employee does not exist.
     * @throws BusinessLogicException if the employee has no related deliveries.
     */
    public List<Delivery> getDeliveriesForEmployee(Integer employeeId) {
        Employee employee = employeeIRepository.get(employeeId);

        if (employee == null) throw new EntityNotFound("No employee found with ID " + employeeId);

        List<Delivery> deliveries = employee.getDeliveries();

        if (deliveries == null) throw new BusinessLogicException("The selected employee has no related deliveries");

        return deliveries;
    }

    /**
     * Generates a new unique ID for an employee.
     *
     * @return A new unique employee ID.
     */
    public Integer getNewEmployeeId() {
        int maxId = 0;
        for (Integer Id : employeeIRepository.getKeys()) {
            if (Id.compareTo(maxId) > 0) {
                maxId = Id;
            }
        }
        return maxId + 1;
    }

    /**
     * Retrieves all deliveries from the repository.
     *
     * @return List of all deliveries.
     */
    public List<Delivery> getDelivery() {
        return deliveryIRepository.readAll();
    }

    /**
     * Removes a delivery assignment from an employee.
     *
     * @param employeeId ID of the employee.
     * @param deliveryId ID of the delivery to remove.
     * @throws EntityNotFound if the employee or delivery does not exist.
     */
    public void dropDelivery(Integer employeeId, Integer deliveryId) {
        List<Employee> employees = employeeIRepository.readAll();
        boolean existsEmployee = false;

        for (Employee employee : employees) {
            if (employee.getEmployeeID() == employeeId) {
                existsEmployee = true;
                break;
            }
        }

        if (!existsEmployee) throw new EntityNotFound("No employee found with ID " + employeeId);

        Employee employee = employeeIRepository.get(employeeId);

        List<Delivery> deliveries = deliveryIRepository.readAll();
        boolean existsDelivery = false;

        for (Delivery delivery : deliveries) {
            if (delivery.getEmployeeID() == deliveryId) {
                existsDelivery = true;
                break;
            }
        }

        if (!existsDelivery) throw new EntityNotFound("No delivery found with ID " + deliveryId);

        Delivery delivery = deliveryIRepository.get(deliveryId);
        employee.removeDeliv(deliveryId);
        delivery.setEmployeeID(0);
        employeeIRepository.update(employee);
        deliveryIRepository.update(delivery);
    }

    /**
     * Assigns a delivery to an employee.
     *
     * @param employeeId ID of the employee.
     * @param deliveryId ID of the delivery to assign.
     * @throws EntityNotFound if the employee or delivery does not exist.
     */
    public void pickDelivery(Integer employeeId, Integer deliveryId) {
        Delivery delivery = deliveryIRepository.get(deliveryId);
        if (delivery == null) throw new EntityNotFound("No delivery with ID " + deliveryId);

        Employee employee = employeeIRepository.get(employeeId);
        if (employee == null) throw new EntityNotFound("No employee with ID " + employeeId);

        employee.addDelivery(delivery);
        delivery.setEmployeeID(employeeId);
        employeeIRepository.update(employee);
        deliveryIRepository.update(delivery);
    }

    /**
     * Sorts a list of deliveries based on the earliest delivery date and time of their orders.
     *
     * @param deliveries the list of deliveries to sort.
     * @return A new list of deliveries sorted in ascending order by their earliest order's delivery date and time.
     *         Deliveries without any orders will appear last.
     */
    public List<Delivery> getSortedDeliveriesByOrderDateTime(List<Delivery> deliveries) {
        return deliveries.stream()
                .sorted(Comparator.comparing(delivery ->
                        delivery.getOrders().stream()
                                .map(Order::getDeliveryDateTime)
                                .min(Comparator.naturalOrder())
                                .orElse(LocalDateTime.MAX)))
                .collect(Collectors.toList());
    }
}
