package service;

import exceptions.BusinessLogicException;
import exceptions.EntityNotFound;
import model.*;
import repository.IRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class EmployeeService {
    private final IRepository<Employee> employeeIRepository;
    private final IRepository<Delivery> deliveryIRepository;
    private final IRepository<Department> departmentIRepository;

    public EmployeeService(IRepository<Employee> employeeIRepository, IRepository<Delivery> deliveryIRepository, IRepository<Department> departmentIRepository){
        this.employeeIRepository = employeeIRepository;
        this.deliveryIRepository = deliveryIRepository;
        this.departmentIRepository = departmentIRepository;
    }


    /**
     * Retrieves all employees from the repository
     *
     * @return List of all employees
     */
    public List<Employee> getEmployees() {
        return employeeIRepository.readAll();
    }

    public void createEmployee(Integer Id, Integer departmentId, String name, String phone, String license) {
        Employee employee = new Employee(Id, departmentId, name, phone, license);
        employeeIRepository.create(employee);

        List<Department> departments = departmentIRepository.readAll();
        boolean existsDepartment = false;

        for (Department department: departments){
            if (department.getDepartmentID() == departmentId){
                existsDepartment = true;
                break;
            }
        }

        if (!existsDepartment) throw new EntityNotFound("No department found with ID " + departmentId);

        Department department = departmentIRepository.get(departmentId);
        department.addEmployee(employee);
    }

    public List<Department> getDepartments() {
        return departmentIRepository.readAll();
    }

    /**
     * Retrieves all deliveries assigned to a specific employee
     *
     * @param employeeId ID of the employee
     * @return List of deliveries assigned to the employee
     */
    public List<Delivery> getDeliveriesForEmployee(Integer employeeId) {
        List<Employee> employees = employeeIRepository.readAll();
        boolean existsEmployee = false;

        for (Employee employee: employees){
            if (employee.getEmployeeID() == employeeId){
                existsEmployee = true;
                break;
            }
        }

        if (!existsEmployee) throw new EntityNotFound("No employee found with ID " + employeeId);

        Employee employee = employeeIRepository.get(employeeId);

        List<Delivery> deliveries = employee.getDeliveries();

        if (deliveries.isEmpty()) throw new BusinessLogicException("The selected employee has no related deliveries");

        return deliveries;
    }

    public Integer getNewEmployeeId() {
        int maxId = 0;
        for (Integer Id : employeeIRepository.getKeys()) {
            if (Id.compareTo(maxId) > 0) {
                maxId = Id;
            }
        }
        return maxId + 1;
    }


    public List<Delivery> getDelivery() {
        return deliveryIRepository.readAll();
    }

    /**
     * Removes a delivery assignment from an employee
     *
     * @param employeeId ID of the employee
     * @param deliveryId ID of the delivery to remove
     * @throws IllegalArgumentException if employee or delivery not found
     */
    public void dropDelivery(Integer employeeId, Integer deliveryId) {
        List<Employee> employees = employeeIRepository.readAll();
        boolean existsEmployee = false;

        for (Employee employee: employees){
            if (employee.getEmployeeID() == employeeId){
                existsEmployee = true;
                break;
            }
        }

        if (!existsEmployee) throw new EntityNotFound("No employee found with ID " + employeeId);

        Employee employee = employeeIRepository.get(employeeId);

        List<Delivery> deliveries = deliveryIRepository.readAll();
        boolean existsDelivery = false;

        for (Delivery delivery: deliveries){
            if (delivery.getEmployeeID() == deliveryId){
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

    public void pickDelivery(Integer employeeId ,Integer deliveryId) {
        List<Employee> employees = employeeIRepository.readAll();
        boolean existsEmployee = false;

        for (Employee employee : employees) {
            if (employee.getEmployeeID() == employeeId) {
                existsEmployee = true;
                break;
            }
        }

        if (!existsEmployee) throw new EntityNotFound("No employee found with ID " + employeeId);

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
        Employee employee = employeeIRepository.get(employeeId);

        employee.addDelivery(delivery);
        delivery.setEmployeeID(employeeId);
        employeeIRepository.update(employee);
        deliveryIRepository.update(delivery);
    }

    /**
     * Sorts a list of deliveries based on the earliest delivery date and time of their orders.
     *
     * @param deliveries the list of deliveries to sort.
     * @return a new list of deliveries sorted in ascending order by their earliest order's delivery date and time.
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
