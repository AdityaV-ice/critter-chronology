package com.udacity.jdnd.course3.critter.pet;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetRepository extends JpaRepository<Pet, Long> {

    // Used by PetController.getPetsByOwner
    List<Pet> findByOwnerId(Long ownerId);
}
