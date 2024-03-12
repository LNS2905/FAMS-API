package com.fams.fams.repositories;

import com.fams.fams.models.entities.ReservedClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReservedClassRepository extends JpaRepository<ReservedClass, Long> {
    @Query("select rc FROM ReservedClass rc WHERE rc.classes.classId = :classId and rc.students.studentId = :studentId")
    ReservedClass findByClassIdAndStudentId(Long classId, Long studentId);
    @Query("select rc FROM ReservedClass rc WHERE rc.classes.classId = :classId and rc.students.studentId = :studentId and rc.reservedclassid = :reservedId")
    ReservedClass findByClassIdAndStudentIdAndReservedClassId(Long reservedId, Long classId, Long studentId);
}
