package com.fams.fams.services;

import com.fams.fams.models.payload.dto.ClassDto;
import com.fams.fams.models.payload.responseModel.ClassDetailDto;
import org.springframework.data.domain.Page;

public interface ClassService {
    Page<ClassDto> getClassesByStudentId(Long studentId, int page, int size);
    ClassDetailDto getClassesDetailById(Long id);
    Page<ClassDto> getAllClass(int page, int size);
}
