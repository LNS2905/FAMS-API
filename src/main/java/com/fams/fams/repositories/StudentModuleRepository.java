package com.fams.fams.repositories;

import com.fams.fams.models.entities.StudentModule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentModuleRepository extends JpaRepository<StudentModule,Long> {
    List<StudentModule> findByStudentsStudentIdAndModulesModuleId(Long studentId, Long moduleId);


}
