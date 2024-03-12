package com.fams.fams.repositories;

import com.fams.fams.models.entities.Class;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ClassRepository extends JpaRepository<Class, Long> {

    Optional<Class> findByClassCode(String code);

    Optional<Class> findByClassCodeAndTrainingProgramsProgramID(String classCode, Long programId);

    Optional<Class> findById(Long id);

    @Query("SELECT c FROM Class c")
    Page<Class> findAllClassesPaging(Pageable pageable);


}
