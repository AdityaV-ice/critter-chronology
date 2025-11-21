package com.udacity.jdnd.course3.critter.schedule;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findAllByPetsId(Long petId);

    List<Schedule> findAllByEmployeesId(Long employeeId);

    // Spring Data will follow nested property: pets.owner.id
    List<Schedule> findAllByPetsOwnerId(Long ownerId);
}
