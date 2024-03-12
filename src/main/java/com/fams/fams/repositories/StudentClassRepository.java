package com.fams.fams.repositories;

import com.fams.fams.models.entities.Class;
import com.fams.fams.models.entities.Student;
import com.fams.fams.models.entities.StudentClass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentClassRepository extends JpaRepository<StudentClass, Long> {
    @Query("SELECT c FROM StudentClass sc JOIN Class c ON sc.classes.classId = c.classId WHERE sc.students.studentId = :studentId")
    Page<Class> getClassesByStudentId(@Param("studentId") Long studentId, Pageable pageable);
    @Query("SELECT s FROM Student s INNER JOIN StudentClass sc on s.studentId = sc.students.studentId INNER JOIN Class c on c.classId = sc.classes.classId WHERE c.classId = :classId")
    Page<Student> getStudentsByClassId(@Param("classId") String keyword, Pageable pageable);
    @Query("SELECT sc FROM Student s INNER JOIN StudentClass sc on s.studentId = sc.students.studentId INNER JOIN Class c on c.classId = sc.classes.classId WHERE c.classId = :classId")
    List<StudentClass> getStudentClassByClassId(@Param("classId") Long classId);
    @Query("SELECT sc FROM Student s INNER JOIN StudentClass sc on s.studentId = sc.students.studentId INNER JOIN Class c on c.classId = sc.classes.classId WHERE c.classId = :classId and s.studentId = :studentId")
    StudentClass findAllByStudentsAndClasses(Long classId, Long studentId);
    List<StudentClass> findByClassesClassId(Long classId);

    List<StudentClass> getAllByStudents_StudentIdOrderByAttendingStatusDesc(Long studentId);
}
