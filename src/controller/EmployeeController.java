package controller;

import exceptions.BusinessLogicException;
import exceptions.EntityNotFound;
import model.Delivery;
import model.Department;
import model.Employee;
import service.EmployeeService;

import java.util.List;
import java.util.Objects;

public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService){
        this.employeeService = employeeService;
    }

    /**
     * Displays all employees and their details in a formatted manner.
     */
    public void viewAllEmployees() {
        StringBuilder output = new StringBuilder("All Employees:\n");
        employeeService.getEmployees().forEach(employee -> output.append(employee.toString()).append("\n"));
        System.out.println(output);
    }

    /**
     * Creates a new employee and assigns them to a specified department.
     *
     * @param departmentId the ID of the department to which the employee will be assigned
     * @param name         the name of the employee
     * @param phone        the phone number of the employee
     * @param license      the license number of the employee
     */
    public void createEmployee(Integer departmentId, String name, String phone, String license) {
        List<Department> departments = employeeService.getDepartments();
        boolean existsDepartment = false;

        for (Department department: departments){
            if (department.getDepartmentID() == departmentId){
                existsDepartment = true;
                break;
            }
        }

        if (!existsDepartment) throw new EntityNotFound("No department found with ID " + departmentId);

        Integer employeeId = employeeService.getNewEmployeeId();
        employeeService.createEmployee(employeeId, departmentId, name, phone, license);
        System.out.println("Registered employee " + employeeId + " to department " + departmentId);
    }

    /**
     * Retrieves all deliveries assigned to given Employee.
     *
     * @param employeeID unique identifier for Employee object
     * @return list containing all deliveries assigned to Employee
     */
    public List<Delivery> getDeliveriesForEmployee(Integer employeeID) {
        List<Employee> employees = employeeService.getEmployees();
        boolean existsEmployee = false;

        for (Employee employee: employees){
            if (employee.getEmployeeID() == employeeID){
                existsEmployee = true;
                break;
            }
        }

        if(!existsEmployee) throw new EntityNotFound("No employee found with ID " + employeeID);

        return employeeService.getDeliveriesForEmployee(employeeID);
    }

    /**
     * Validates whether a selected Delivery exists based on its unique identifier.
     *
     * @param deliveryID unique identifier for Delivery object
     * @return true if it exists; false otherwise
     */
    public boolean validateSelectedDelivery(Integer deliveryID) {
        return employeeService.getDelivery().stream().anyMatch(delivery -> Objects.equals(delivery.getDeliveryID(), deliveryID));
    }

    /**
     * Drops an assigned Delivery from Employee.
     *
     * @param employeeID unique identifier for Employee object
     * @param deliveryID unique identifier for Delivery object
     */
    public void dropDelivery(Integer employeeID, Integer deliveryID) {
        List<Employee> employees = employeeService.getEmployees();
        boolean existsEmployee = false;

        for (Employee employee: employees){
            if (employee.getEmployeeID() == employeeID){
                existsEmployee = true;
                break;
            }
        }

        if(!existsEmployee) throw new EntityNotFound("No employee found with ID " + employeeID);

        List<Delivery> deliveries = employeeService.getDelivery();
        Delivery assigendDelivery = null;
        boolean existsDelivery = false;

        for (Delivery delivery: deliveries){
            if (Objects.equals(delivery.getDeliveryID(), deliveryID)){
                existsDelivery = true;
                assigendDelivery = delivery;
                break;
            }
        }

        if(!existsDelivery) throw new EntityNotFound("No delivery found with ID " + deliveryID);

        if (!Objects.equals(assigendDelivery.getEmployeeID(), employeeID)){
            throw new BusinessLogicException("The chosen delivery is not assigned to the chosen employee!");
        }

        employeeService.dropDelivery(employeeID, deliveryID);
        System.out.println("Dropped Delivery with id " + deliveryID + " by Employee with id " + employeeID + " successfully");
    }

    /**
     * Assigns an Employee to an unassigned Delivery.
     *
     * @param employeeID unique identifier for Employee object
     * @param deliveryID unique identifier for Delivery object
     */
    public void assignEmployeeToUnassignedDelivery(Integer employeeID, Integer deliveryID) {
        List<Employee> employees = employeeService.getEmployees();
        boolean existsEmployee = false;

        for (Employee employee: employees){
            if (employee.getEmployeeID() == employeeID){
                existsEmployee = true;
                break;
            }
        }

        if(!existsEmployee) throw new EntityNotFound("No employee found with ID " + employeeID);

        List<Delivery> deliveries = employeeService.getDelivery();
        Delivery assigendDelivery = null;
        boolean existsDelivery = false;

        for (Delivery delivery: deliveries){
            if (Objects.equals(delivery.getDeliveryID(), deliveryID)){
                existsDelivery = true;
                assigendDelivery = delivery;
                break;
            }
        }

        if(!existsDelivery) throw new EntityNotFound("No delivery found with ID " + deliveryID);

        if (assigendDelivery.getEmployeeID() != null || assigendDelivery.getDeliveryPeronID() != null){
            throw new BusinessLogicException("The chosen delivery is already assigned to an employee!");
        }

        employeeService.pickDelivery(employeeID, deliveryID);
        System.out.println("Picked Delivery with id " + deliveryID + " by Employee with id " + employeeID + " successfully");
    }

    /**
     * @param
     * @return
     */

    public void getdeliveriesSortedByOrderDateTime() {
        List<Delivery> deliveries = employeeService.getDelivery();
        List<Delivery> deliveryList = employeeService.getSortedDeliveriesByOrderDateTime(deliveries);
        StringBuilder output = new StringBuilder("Deliveries suitable for Delivery Person to pick up:\n");
        if (deliveryList.isEmpty()) {
            output.append("No deliveries available for 'to be shipped' orders.\n");
        } else {
            deliveryList.forEach(delivery -> output.append(delivery.toString()).append("\n"));
        }

        System.out.println(output);
    }

    /**
     * Displays all available departments.
     */
    public void viewAllDepartments() {
        StringBuilder output = new StringBuilder("Available Departments:\n");
        employeeService.getDepartments().forEach(department -> output.append(department.toString()).append("\n"));
        System.out.println(output);
    }

    /**
     * Displays all deliveries in a formatted manner.
     */
    public void viewAllDeliveries() {
        StringBuilder output = new StringBuilder("All Deliveries:\n");
        employeeService.getDelivery().forEach(delivery -> output.append(delivery.toString()).append("\n"));
        System.out.println(output);
    }


}
