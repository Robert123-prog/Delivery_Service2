package service;

import model.*;
import repository.IRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserService {
    private final IRepository<Customer> customerIRepository;
    private final IRepository<Employee> employeeIRepository;
    private final IRepository<Delivery_Person> deliveryPersonIRepository;
    private final IRepository<Department> departmentIRepository;

    public UserService(IRepository<Customer> customerIRepository, IRepository<Employee> employeeIRepository, IRepository<Delivery_Person> deliveryPersonIRepository, IRepository<Department> departmentIRepository){
        this.customerIRepository = customerIRepository;
        this.employeeIRepository = employeeIRepository;
        this.deliveryPersonIRepository = deliveryPersonIRepository;
        this.departmentIRepository = departmentIRepository;

    }


    public List<String> getTransportationTypes(){
        List<String> enumNames = Stream.of(Transportation_Type.values())
                .map(Enum::name)
                .collect(Collectors.toList());

        return enumNames;
    }

    public List<Customer> getCustomers() {
        return customerIRepository.readAll();
    }

    public void deleteCustomer(Integer customerId) {
        Customer customer = customerIRepository.get(customerId);
        if (customer != null) {
            customerIRepository.delete(customerId);
        }
    }

    /**
     * Retrieves all employees from the repository
     *
     * @return List of all employees
     */
    public List<Employee> getEmployees() {
        return employeeIRepository.readAll();
    }

    public void unenrollEmployee(Integer employeeId) {
        Employee employee = employeeIRepository.get(employeeId);
        Department department = departmentIRepository.get(employee.getDepartmentID());
        if (department != null) {
            department.removeEmployee(employee);
        }
        employeeIRepository.delete(employeeId);

    }

    public List<Delivery_Person> getDeliveryPerson() {
        return deliveryPersonIRepository.readAll();
    }

    public void unenrollDeliveryPerson(Integer deliveryPersonId) {
        //Delivery_Person deliveryPerson = deliveryPersonIRepository.get(deliveryPersonId);
        deliveryPersonIRepository.delete(deliveryPersonId);
    }


}
