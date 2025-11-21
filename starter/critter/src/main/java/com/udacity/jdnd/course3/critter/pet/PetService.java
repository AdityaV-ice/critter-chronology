package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.user.Customer;
import com.udacity.jdnd.course3.critter.user.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetService {

    private final PetRepository petRepository;
    private final CustomerRepository customerRepository;

    public PetService(PetRepository petRepository,
                      CustomerRepository customerRepository) {
        this.petRepository = petRepository;
        this.customerRepository = customerRepository;
    }

    public Pet savePet(Pet pet, long ownerId) {
        Customer owner = customerRepository.findById(ownerId).orElse(null);
        pet.setOwner(owner);
        Pet saved = petRepository.save(pet);

        if (owner != null) {
            // keep both sides of the relationship in sync
            List<Pet> pets = owner.getPets();
            if (pets == null) {
                pets = new java.util.ArrayList<>();
                owner.setPets(pets);
            }
            if (!pets.contains(saved)) {
                pets.add(saved);
            }
            customerRepository.save(owner);
        }

        return saved;
    }

    public Pet getPet(long petId) {
        return petRepository.findById(petId).orElse(null);
    }

    public List<Pet> getPets() {
        return petRepository.findAll();
    }

    public List<Pet> getPetsByOwner(long ownerId) {
        return petRepository.findByOwnerId(ownerId);
    }
}
