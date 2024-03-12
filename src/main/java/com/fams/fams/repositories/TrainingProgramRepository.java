package com.fams.fams.repositories;

import com.fams.fams.models.entities.TrainingProgram;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrainingProgramRepository extends JpaRepository<TrainingProgram,Long> {

    Optional<TrainingProgram> findByCode(String code);

}
