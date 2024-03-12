package com.fams.fams.services;


import com.fams.fams.models.entities.Score;
import com.fams.fams.models.payload.dto.StudentScorePerClassDto;
import org.springframework.http.HttpHeaders;

import java.io.InputStream;
import java.util.List;

public interface ScoreService {
    void importScoreFromXlsx(InputStream excelFile,String option,Long classId);
    StudentScorePerClassDto getStudentScoreDetail(Long studentId);
    List<StudentScorePerClassDto> getStudentScoreListByClass(Long classId);
    HttpHeaders downloadScoreTemplate();
    Score updateScore(Score score);
}
