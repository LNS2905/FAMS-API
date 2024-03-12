package com.fams.fams.repositories;

import com.fams.fams.models.entities.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface ScoreRepository extends JpaRepository<Score,Long> {
    List<Score> findByAssignmentsAssignmentIdAndStudentsStudentId(Long assignmentId,Long studentId);
    @Query(value = "SELECT s FROM Score s " +
            "JOIN Assignment a ON s.assignments.assignmentId = a.assignmentId " +
            "JOIN Module m ON a.modules.moduleId = m.moduleId " +
            "WHERE s.students.studentId = :studentId AND m.moduleId IN :moduleIds")
    List<Score> findScoreByStudentIdAndModuleId(@Param("studentId") Long studentId, @Param("moduleIds") List<Long> moduleIds);
    List<Score> findByStudentsStudentId(Long studentId);
    Score findByScoreId(Long id);
}
