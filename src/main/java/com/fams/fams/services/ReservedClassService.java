package com.fams.fams.services;

import com.fams.fams.models.entities.Class;
import com.fams.fams.models.entities.StudentClass;
import com.fams.fams.models.payload.dto.ReservedClassDto;
import com.fams.fams.models.payload.dto.StudentDetailsDto;
import com.fams.fams.models.payload.requestModel.ReservedClassAddNew;
import com.fams.fams.models.payload.responseModel.ReservedClassList;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReservedClassService {
    Page<ReservedClassList> getAllStudents(int page, int size);
    ReservedClassDto getReservationStudent(@Param("id") Long id);
    void addNewReservation(ReservedClassAddNew reservedClassAddNew);
    List<Class> findNewClassForReservedStudent(Long studentId, Long classId);
    void updateStatusDropOut(Long reservedClassId, Long studentId, Long classId);
    void addReservedStudentIntoNewClass(Long reservedClassId, Long classId, Long studentId);
}
