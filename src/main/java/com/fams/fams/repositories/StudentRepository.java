package com.fams.fams.repositories;

import com.fams.fams.models.entities.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByStudentCode(String studentCode);
    List<Student> findByEmail(String email);
    @Query("select s from Student s where s.status not like 'Disable'")
    Page<Student> findAllByOrderByStudentId(Pageable pageable);
}

