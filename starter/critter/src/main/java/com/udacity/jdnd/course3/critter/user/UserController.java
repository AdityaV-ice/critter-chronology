package com.udacity.jdnd.course3.critter.user;

import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Users.
 *
 * Includes requests for both customers and employees.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private final CustomerService customerService;
    private final EmployeeService employeeService;

    public UserController(CustomerService customerService,
                          EmployeeService employeeService) {
        this.customerService = customerService;
        this.employeeService = employeeService;
    }

    // ---------------------------
    // CUSTOMER ENDPOINTS
    // ---------------------------

    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
        Customer customer = convertCustomerDTOToEntity(customerDTO);
        Customer saved = customerService.saveCustomer(customer);
        return convertCustomerToDTO(saved);
    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers(){
        return customerService.getAllCustomers()
                .stream()
                .map(this::convertCustomerToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId){
        Customer owner = customerService.getOwnerByPet(petId);
        return convertCustomerToDTO(owner);
    }

    // ---------------------------
    // EMPLOYEE ENDPOINTS
    // ---------------------------

    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        Employee employee = convertEmployeeDTOToEntity(employeeDTO);
        Employee saved = employeeService.saveEmployee(employee);
        return convertEmployeeToDTO(saved);
    }

    @PostMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        Employee emp = employeeService.getEmployee(employeeId);
        return convertEmployeeToDTO(emp);
    }

    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable,
                                @PathVariable long employeeId) {
        employeeService.setAvailability(daysAvailable, employeeId);
    }

    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeDTO) {
        List<Employee> employees =
                employeeService.findEmployeesForService(
                        employeeDTO.getSkills(),
                        employeeDTO.getDate().getDayOfWeek());

        return employees.stream()
                .map(this::convertEmployeeToDTO)
                .collect(Collectors.toList());
    }

    // ---------------------------
    // DTO CONVERTERS
    // ---------------------------

    private CustomerDTO convertCustomerToDTO(Customer customer) {
        if (customer == null) return null;

        CustomerDTO dto = new CustomerDTO();
        dto.setId(customer.getId());
        dto.setName(customer.getName());
        dto.setPhoneNumber(customer.getPhoneNumber());
        dto.setNotes(customer.getNotes());

        if (customer.getPets() != null) {
            dto.setPetIds(
                    customer.getPets()
                            .stream()
                            .map(p -> p.getId())
                            .collect(Collectors.toList())
            );
        }
        return dto;
    }

    private Customer convertCustomerDTOToEntity(CustomerDTO dto) {
        Customer customer = new Customer();
        customer.setId(dto.getId());
        customer.setName(dto.getName());
        customer.setPhoneNumber(dto.getPhoneNumber());
        customer.setNotes(dto.getNotes());
        return customer;
    }

    private EmployeeDTO convertEmployeeToDTO(Employee e) {
        if (e == null) return null;

        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(e.getId());
        dto.setName(e.getName());
        dto.setSkills(e.getSkills());
        dto.setDaysAvailable(e.getDaysAvailable());
        return dto;
    }

    private Employee convertEmployeeDTOToEntity(EmployeeDTO dto) {
        Employee e = new Employee();
        e.setId(dto.getId());
        e.setName(dto.getName());
        e.setSkills(dto.getSkills());
        e.setDaysAvailable(dto.getDaysAvailable());
        return e;
    }
}
