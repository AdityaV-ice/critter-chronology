package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final PetRepository petRepository;

    public CustomerService(CustomerRepository customerRepository,
                           PetRepository petRepository) {
        this.customerRepository = customerRepository;
        this.petRepository = petRepository;
    }

    // -------------------------
    // BASIC CRUD
    // -------------------------

    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomer(long customerId) {
        return customerRepository.findById(customerId).orElse(null);
    }

    // -------------------------
    // RELATIONSHIPS
    // -------------------------

    /** Get owner for a given pet id */
    public Customer getOwnerByPet(long petId) {
        return petRepository.findById(petId)
                .map(Pet::getOwner)
                .orElse(null);
    }

    /** Add a pet to the customer's pet list and save */
    public void addPetToCustomer(long customerId, Pet pet) {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer != null) {
            List<Pet> pets = customer.getPets();
            if (pets == null) {
                pets = new ArrayList<>();
                customer.setPets(pets);
            }
            pets.add(pet);
            customerRepository.save(customer);
        }
    }
}
