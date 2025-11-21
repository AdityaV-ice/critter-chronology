package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.user.Customer;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
public class PetController {

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO){
        Pet pet = convertDTOToPet(petDTO);
        Pet saved = petService.savePet(pet, petDTO.getOwnerId());
        return convertPetToDTO(saved);
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        Pet pet = petService.getPet(petId);
        return convertPetToDTO(pet);
    }

    @GetMapping
    public List<PetDTO> getPets(){
        return petService.getPets()
                .stream()
                .map(this::convertPetToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId){
        return petService.getPetsByOwner(ownerId)
                .stream()
                .map(this::convertPetToDTO)
                .collect(Collectors.toList());
    }

    // ----------------- helpers -----------------

    private PetDTO convertPetToDTO(Pet pet) {
        if (pet == null) {
            return null;
        }
        PetDTO dto = new PetDTO();
        dto.setId(pet.getId());
        dto.setType(pet.getType());
        dto.setName(pet.getName());
        dto.setBirthDate(pet.getBirthDate());
        dto.setNotes(pet.getNotes());

        Customer owner = pet.getOwner();
        if (owner != null) {
            dto.setOwnerId(owner.getId());
        }

        return dto;
    }

    private Pet convertDTOToPet(PetDTO dto) {
        Pet pet = new Pet();
        pet.setId(dto.getId());
        pet.setType(dto.getType());
        pet.setName(dto.getName());
        pet.setBirthDate(dto.getBirthDate());
        pet.setNotes(dto.getNotes());
        // owner is set in PetService based on ownerId
        return pet;
    }
}
