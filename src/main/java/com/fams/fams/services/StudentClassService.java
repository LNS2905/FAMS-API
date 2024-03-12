package com.fams.fams.services;

import com.fams.fams.models.payload.dto.StudentDetailsInClassDto;

import java.util.List;

public interface StudentClassService {
    void updateStatusStudents(List<Long> studentIds, Long classId, String attendingStatus);
    void updateStatusStudent(Long studentIds, Long classId, String attendingStatus);
    StudentDetailsInClassDto getStudentDetailsInClass(Long studentClassId);


}
