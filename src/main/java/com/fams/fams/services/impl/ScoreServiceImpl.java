package com.fams.fams.services.impl;

import com.fams.fams.models.entities.*;
import com.fams.fams.models.entities.Class;
import com.fams.fams.models.entities.Module;
import com.fams.fams.models.exception.FamsApiException;
import com.fams.fams.models.payload.dto.StudentScorePerClassDto;
import com.fams.fams.repositories.*;
import com.fams.fams.services.ScoreService;
import com.fams.fams.utils.Utils;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ScoreServiceImpl implements ScoreService {
    private final AssignmentRepository assignmentRepository;
    private final ScoreRepository scoreRepository;
    private final TrainingProgramRepository trainingProgramRepository;
    private final StudentRepository studentRepository;
    private final StudentClassRepository studentClassRepository;
    private final ClassRepository classRepository;
    private final TrainingProgramModuleRepository trainingProgramModuleRepository;
    private final StudentModuleRepository studentModuleRepository;

    @Autowired
    public ScoreServiceImpl(AssignmentRepository assignmentRepository, ScoreRepository scoreRepository, TrainingProgramRepository trainingProgramRepository, StudentRepository studentRepository, StudentClassRepository studentClassRepository, ClassRepository classRepository, TrainingProgramModuleRepository trainingProgramModuleRepository, StudentModuleRepository studentModuleRepository) {
        this.assignmentRepository = assignmentRepository;
        this.scoreRepository = scoreRepository;
        this.trainingProgramRepository = trainingProgramRepository;
        this.studentRepository = studentRepository;
        this.studentClassRepository = studentClassRepository;
        this.classRepository = classRepository;
        this.trainingProgramModuleRepository = trainingProgramModuleRepository;
        this.studentModuleRepository = studentModuleRepository;
    }

    @Override
    public void importScoreFromXlsx(InputStream excelFile, String option,Long classId) {

        try {
            Workbook workbook = new XSSFWorkbook(excelFile);
            Sheet sheet = workbook.getSheetAt(0);
            //
            TrainingProgram course = new TrainingProgram();
            Class thisClass = new Class();
            List<Assignment> listAssignment = new ArrayList<>();
            List<Long> listModuleId = new ArrayList<>();
            List<TrainingProgramModule> listModuleInThisCourse = new ArrayList<>();
            //
            for (Row row : sheet) {

                if (row.getRowNum() == 0) {
                    course = trainingProgramRepository.findByCode(row.getCell(1).getStringCellValue()).orElseThrow(() -> new FamsApiException(HttpStatus.NOT_FOUND, "Course (Training program is not found)"));
                    listModuleInThisCourse = trainingProgramModuleRepository.findByTrainingProgramsProgramID(course.getProgramID());
                    thisClass = classRepository.findById(classId).orElseThrow(() -> new FamsApiException(HttpStatus.NOT_FOUND, "Class with id: " + classId + " is not found!"));
                    Class finalThisClass = thisClass;
                    TrainingProgram finalCourse = course;
                    classRepository.findByClassCodeAndTrainingProgramsProgramID(thisClass.getClassCode(), course.getProgramID()).orElseThrow(() -> new FamsApiException(HttpStatus.NOT_FOUND, "Class " + finalThisClass.getClassCode() + " does not belong to training program " + finalCourse.getCode() + "!"));
                    if (listModuleInThisCourse.isEmpty()) {
                        throw new FamsApiException(HttpStatus.NOT_FOUND, "The course has no module!");
                    } else if (listModuleInThisCourse.size() != 2) {
                        StringBuilder listNameOfModule = Utils.getStringBuilder(listModuleInThisCourse);
                        throw new FamsApiException(HttpStatus.NOT_FOUND, "The module was given from file missing or out of this list: " + listNameOfModule);

                    }else{
                        listModuleId = listModuleInThisCourse.stream()
                                .map(module -> module.getModules().getModuleId())
                                .collect(Collectors.toList());
                        listAssignment = assignmentRepository.findAssignmentByModulesModuleIdIn(listModuleId);
                    }


                }
                if (row.getRowNum() != 0 && row.getRowNum() > 4) {
                    int i = 3;
                    try {

                        Optional<Student> student = studentRepository.findByStudentCode(row.getCell(1).getStringCellValue());
                        if (student.isEmpty()) {
                            throw new FamsApiException(HttpStatus.NOT_FOUND, "Student " + row.getCell(1).getStringCellValue() + " not found");
                        }
                        StudentClass studentInClass = studentClassRepository.findAllByStudentsAndClasses(classId, student.get().getStudentId());
                        if (studentInClass == null) {
                            throw new FamsApiException(HttpStatus.NOT_FOUND, "Student in row " + row.getRowNum() + " is not in class " + thisClass.getClassCode());
                        } else {

                            float HTMLQuiz,CSSQuiz,Quiz3,Quiz4,Quiz5,Quiz6,pe1,pe2,pe3,quizFinal,audit,peFinal,mock;
                            try{
                                //read score data
                                HTMLQuiz = (float) row.getCell(3).getNumericCellValue();
                                i = 4;
                                CSSQuiz = (float) row.getCell(4).getNumericCellValue();
                                i = 5;
                                Quiz3 = (float) row.getCell(5).getNumericCellValue();
                                i = 6;
                                Quiz4 = (float) row.getCell(6).getNumericCellValue();
                                i = 7;
                                Quiz5 = (float) row.getCell(7).getNumericCellValue();
                                i = 8;
                                Quiz6 = (float) row.getCell(8).getNumericCellValue();
                                i = 10;
                                pe1 = (float) row.getCell(10).getNumericCellValue();
                                i = 11;
                                pe2 = (float) row.getCell(11).getNumericCellValue();
                                i = 12;
                                pe3 = (float) row.getCell(12).getNumericCellValue();
                                i = 14;
                                quizFinal = (float) row.getCell(14).getNumericCellValue();
                                i = 15;
                                audit = (float) row.getCell(15).getNumericCellValue();
                                i = 16;
                                peFinal = (float) row.getCell(16).getNumericCellValue();
                                i = 21;
                                mock = (float) row.getCell(21).getNumericCellValue();
                            }catch (Exception e){
                                throw new FamsApiException(HttpStatus.BAD_REQUEST, "Missing data or error data at row " + (row.getRowNum() + 1) + ", cell " + i);
                            }

                            List<List<Float>> listScoreForReCalculateQuizAverage = new ArrayList<>();
                            List<List<Float>> listScoreForReCalculatePEAverage = new ArrayList<>();
                            List<List<Score>> saveScoreList = new ArrayList<>();
                            Map<String,Float> scoreForFinal = new HashMap<>();
                            List<Score> mergedList = new ArrayList<>();
                            List<Score> MockScoreList = new ArrayList<>();
                            // get the old score
                            List<Score> listOldScore = scoreRepository.findScoreByStudentIdAndModuleId(student.get().getStudentId(),listModuleId);


                            List<String> listOfAssignmentName =
                                    Arrays.asList(
                                            "HTML Quiz","CSS Quiz", "Quiz 3",
                                            "Quiz 4","Quiz 5","Quiz 6","Average","Practice Exam 1",
                                            "Practice Exam 2","Practice Exam 3","Quiz Final","Audit",
                                            "Practice Final","MOCK"
                                    );
                            Map<String, List<Score>> scoreMap = new HashMap<>();

                            for(String assignmentName: listOfAssignmentName){
                                scoreMap.put(assignmentName, new ArrayList<>());
                            }
                            for (Score score: listOldScore) {
                                String assignmentName = score.getAssignments().getAssignmentName();
                                if(scoreMap.containsKey(assignmentName)){
                                    scoreMap.get(assignmentName).add(score);
                                }
                            }

                            for(Assignment assignment : listAssignment){
                                switch (assignment.getAssignmentName()){
                                    case "HTML Quiz":
                                        List<Score> HTMLQuizScoreList =
                                                processingScoreBaseOnOption(
                                                        assignment,
                                                        student.get(),
                                                        HTMLQuiz,
                                                        option,
                                                        scoreMap.get(assignment.getAssignmentName()));
                                        saveScoreList.add(HTMLQuizScoreList);
                                        listScoreForReCalculateQuizAverage.add(getListScore(HTMLQuizScoreList));

                                        break;
                                    case "CSS Quiz":
                                        List<Score> CSSQuizScoreList =
                                                processingScoreBaseOnOption(
                                                        assignment,
                                                        student.get(),
                                                        CSSQuiz,
                                                        option,
                                                        scoreMap.get(assignment.getAssignmentName()));
                                        saveScoreList.add(CSSQuizScoreList);
                                        listScoreForReCalculateQuizAverage.add(getListScore(CSSQuizScoreList));
                                        break;
                                    case "Quiz 3":
                                        List<Score> Quiz3ScoreList =
                                                processingScoreBaseOnOption(
                                                        assignment,
                                                        student.get(),
                                                        Quiz3,
                                                        option,
                                                        scoreMap.get(assignment.getAssignmentName()));
                                        saveScoreList.add(Quiz3ScoreList);
                                        listScoreForReCalculateQuizAverage.add(getListScore(Quiz3ScoreList));
                                        break;
                                    case "Quiz 4":
                                        List<Score> Quiz4ScoreList =
                                                processingScoreBaseOnOption(
                                                        assignment,
                                                        student.get(),
                                                        Quiz4,
                                                        option,
                                                        scoreMap.get(assignment.getAssignmentName()));
                                        saveScoreList.add(Quiz4ScoreList);
                                        listScoreForReCalculateQuizAverage.add(getListScore(Quiz4ScoreList));
                                        break;
                                    case "Quiz 5":
                                        List<Score> Quiz5ScoreList =
                                                processingScoreBaseOnOption(
                                                        assignment,
                                                        student.get(),
                                                        Quiz5,
                                                        option,
                                                        scoreMap.get(assignment.getAssignmentName()));
                                        saveScoreList.add(Quiz5ScoreList);
                                        listScoreForReCalculateQuizAverage.add(getListScore(Quiz5ScoreList));
                                        break;
                                    case "Quiz 6":
                                        List<Score> Quiz6ScoreList =
                                                processingScoreBaseOnOption(
                                                        assignment,
                                                        student.get(),
                                                        Quiz6,
                                                        option,
                                                        scoreMap.get(assignment.getAssignmentName()));
                                        saveScoreList.add(Quiz6ScoreList);
                                        listScoreForReCalculateQuizAverage.add(getListScore(Quiz6ScoreList));
                                        break;
                                    case "Average":
                                        if(assignment.getAssignmentType().equalsIgnoreCase("Quiz")){

                                            float avgQuiz = Utils.calculateAverage(listScoreForReCalculateQuizAverage);
                                            Score avg = new Score();
                                            avg.setSubmissionDate(LocalDate.now());
                                            avg.setScore(avgQuiz);
                                            avg.setAssignments(assignment);
                                            avg.setStudents(student.get());
                                            mergedList.add(avg);
                                            scoreForFinal.put("Quiz Avg",avgQuiz);

                                        }else {

                                            float avgPE = Utils.calculateAverage(listScoreForReCalculatePEAverage);
                                            Score avg = new Score();
                                            avg.setSubmissionDate(LocalDate.now());
                                            avg.setScore(avgPE);
                                            avg.setAssignments(assignment);
                                            avg.setStudents(student.get());
                                            mergedList.add(avg);
                                            scoreForFinal.put("ASM Avg",avgPE);
                                        }
                                        break;
                                    case "Practice Exam 1":
                                        List<Score> PE1ScoreList =
                                                processingScoreBaseOnOption(
                                                        assignment,
                                                        student.get(),
                                                        pe1,
                                                        option,
                                                        scoreMap.get(assignment.getAssignmentName()));
                                        saveScoreList.add(PE1ScoreList);
                                        listScoreForReCalculatePEAverage.add(getListScore(PE1ScoreList));
                                        break;
                                    case "Practice Exam 2":
                                        List<Score> PE2ScoreList =
                                                processingScoreBaseOnOption(
                                                        assignment,
                                                        student.get(),
                                                        pe2,
                                                        option,
                                                        scoreMap.get(assignment.getAssignmentName()));
                                        saveScoreList.add(PE2ScoreList);
                                        listScoreForReCalculatePEAverage.add(getListScore(PE2ScoreList));
                                        break;
                                    case "Practice Exam 3":
                                        List<Score> PE3ScoreList =
                                                processingScoreBaseOnOption(
                                                        assignment,
                                                        student.get(),
                                                        pe3,
                                                        option,
                                                        scoreMap.get(assignment.getAssignmentName()));
                                        saveScoreList.add(PE3ScoreList);
                                        listScoreForReCalculatePEAverage.add(getListScore(PE3ScoreList));
                                        break;
                                    case "Quiz Final":
                                        List<Score> QuizFinalScoreList =
                                                processingScoreBaseOnOption(
                                                        assignment,
                                                        student.get(),
                                                        quizFinal,
                                                        option,
                                                        scoreMap.get(assignment.getAssignmentName()));
                                        saveScoreList.add(QuizFinalScoreList);
                                        scoreForFinal.put("Quiz Final",getAverage(QuizFinalScoreList));

                                        break;
                                    case "Audit":
                                        List<Score> AuditScoreList =
                                                processingScoreBaseOnOption(
                                                        assignment,
                                                        student.get(),
                                                        audit,
                                                        option,
                                                        scoreMap.get(assignment.getAssignmentName()));
                                        saveScoreList.add(AuditScoreList);

                                        scoreForFinal.put("Audit",getAverage(AuditScoreList));



                                        break;
                                    case "Practice Final":
                                        List<Score> PEFinalScoreList =
                                                processingScoreBaseOnOption(
                                                        assignment,
                                                        student.get(),
                                                        peFinal,
                                                        option,
                                                        scoreMap.get(assignment.getAssignmentName()));
                                        saveScoreList.add(PEFinalScoreList);
                                        scoreForFinal.put("PE Final",getAverage(PEFinalScoreList));
                                        break;
                                    case "MOCK":
                                        MockScoreList =
                                                processingScoreBaseOnOption(
                                                        assignment,
                                                        student.get(),
                                                        mock,
                                                        option,
                                                        scoreMap.get(assignment.getAssignmentName()));
                                        saveScoreList.add(MockScoreList);
                                        break;
                                }
                            }

                            mergedList.addAll(saveScoreList.stream()
                                    .flatMap(List::stream)
                                    .toList()) ;
                            scoreRepository.saveAll(mergedList);
                            //done score table
                            ////////////////////////
                            //student module table

                            List<StudentModule> listFinalResultForStudentModules = new ArrayList<>();

                            float finalScore =calculateFinalScore(scoreForFinal);
                            float finalMockScore =getAverage(MockScoreList);

                            for(TrainingProgramModule module: listModuleInThisCourse){

                                if(module.getModules().getModuleName().equalsIgnoreCase("FEE")){
                                    List<StudentModule> check = studentModuleRepository.findByStudentsStudentIdAndModulesModuleId(student.get().getStudentId(),module.getModules().getModuleId());
                                    if(check.isEmpty()){
                                        listFinalResultForStudentModules.add(setupResultForStudentEachModule(finalScore,student.get(),module.getModules(),new StudentModule()));
                                    }else{
                                        StudentModule oldStudentModule = check.get(0);

                                        listFinalResultForStudentModules.add(setupResultForStudentEachModule(finalScore,student.get(),module.getModules(),oldStudentModule));
                                    }
                                }else{
                                    List<StudentModule> check = studentModuleRepository.findByStudentsStudentIdAndModulesModuleId(student.get().getStudentId(),module.getModules().getModuleId());
                                    if(check.isEmpty()){
                                        listFinalResultForStudentModules.add(setupResultForStudentEachModule(finalMockScore,student.get(),module.getModules(),new StudentModule()));
                                    }else{
                                        StudentModule oldStudentModule = check.get(0);

                                        listFinalResultForStudentModules.add(setupResultForStudentEachModule(finalMockScore,student.get(),module.getModules(),oldStudentModule));
                                    }}
                            }
                            studentModuleRepository.saveAll(listFinalResultForStudentModules);

                            studentClassRepository.save(setupStudentClassResult(studentInClass,finalScore,finalMockScore));


                        }
                    } catch (FamsApiException e) {
                        throw new FamsApiException(e.getStatus(), e.getMessage());
                    }
                }
            }
            workbook.close();
        } catch (IOException exception) {
            throw new FamsApiException(HttpStatus.BAD_REQUEST, "Fail to import excel file!");

        }


    }

    private float getAverage(List<Score> scores){
        float sum=0;
        for(Score score:scores){
            sum+=score.getScore();
        }
        return sum/scores.size();
    }

    private StudentModule setupResultForStudentEachModule(float finalScore,Student student,Module module,StudentModule result){

        result.setId(new StudentModuleKey(student.getStudentId(),module.getModuleId()));
        result.setStudents(student);
        result.setModules(module);
        result.setStatus(finalScore > 6);
        result.setModuleScore(finalScore);
        if (finalScore >= 9) {
            result.setModuleLevel("A+");
            return result;
        } else if (finalScore >= 8) {
            result.setModuleLevel("A");
            return result;
        } else if (finalScore >= 7) {
            result.setModuleLevel("B");
            return result;
        } else if (finalScore >= 6) {
            result.setModuleLevel("C");
            return result;
        } else {
            result.setModuleLevel("D");
            return result;
        }
    }

    private StudentClass setupStudentClassResult(StudentClass result, float finalFeeScore,float finalMockScore){

        float finalScore =  finalFeeScore * 0.6f + finalMockScore * 0.4f;

        result.setResult(finalScore > 6);
        result.setFinalScore(finalScore);
        if (finalScore >= 9) {
            result.setGPALevel("A+");
            return result;
        } else if (finalScore >= 8) {
            result.setGPALevel("A");
            return result;
        } else if (finalScore >= 7) {
            result.setGPALevel("B");
            return result;
        } else if (finalScore >= 6) {
            result.setGPALevel("C");
            return result;
        } else {
            result.setGPALevel("D");
            return result;
        }

    }

    private List<Float> getListScore(List<Score> listScore){
        List<Float> listFloatScore = new ArrayList<>();
        for (Score score: listScore) {
            listFloatScore.add(score.getScore());
        }
        return listFloatScore;
    }

    private float calculateFinalScore(Map<String, Float> scoreForFinal) {
        // Fixed weights
        float quizAvgWeight = 10f;   // 10%
        float asmAvgWeight = 20f;    // 20%
        float quizFinalWeight = 15f; // 15%
        float auditWeight = 15f;     // 15%
        float peFinalWeight = 40f;   // 40%

        // Calculate weighted sum
        float weightedSum = quizAvgWeight * scoreForFinal.get("Quiz Avg")
                + asmAvgWeight * scoreForFinal.get("ASM Avg")
                + quizFinalWeight * scoreForFinal.get("Quiz Final")
                + auditWeight * scoreForFinal.get("Audit")
                + peFinalWeight * scoreForFinal.get("PE Final");

        // Calculate final score
        return weightedSum / 100;
    }

    private List<Score> processingScoreBaseOnOption(Assignment assignment,Student student,float newScore,String option,List<Score> listScore){

        Score score = new Score();
        score.setScore(newScore);
        score.setAssignments(assignment);
        score.setStudents(student);
        score.setSubmissionDate(LocalDate.now());

        switch (option){
            case "allow":

                listScore.add(score);
                break;

            case "skip":

                if(listScore.isEmpty()){
                    listScore.add(score);
                }
                break;

            case "replace":
                if(listScore.isEmpty()){
                    listScore.add(score);
                }else {
                    scoreRepository.deleteAll(listScore);
                    listScore.clear();
                    listScore.add(score);
                }
                break;
        }
        return listScore;

    }

    @Override
    public StudentScorePerClassDto getStudentScoreDetail(Long studentId) {
        StudentScorePerClassDto studentScorePerClassDto = new StudentScorePerClassDto();
        Student exsitingStudent = studentRepository.findById(studentId)
                .orElseThrow(()
                        -> new FamsApiException(HttpStatus.NOT_FOUND, "Cannot find student with ID = " + studentId));
        studentScorePerClassDto.setStudentID(exsitingStudent.getStudentId());
        studentScorePerClassDto.setFullName(exsitingStudent.getFullName());
        studentScorePerClassDto.setFaAccount(exsitingStudent.getFAAccount());

        List<Score> existingScoreStudent = scoreRepository.findByStudentsStudentId(studentId);
        if(!existingScoreStudent.isEmpty()) {

            List<Map<String, Float>> gradeList = new ArrayList<>();
            for(Score score: existingScoreStudent){
                Assignment tmpAssignment = assignmentRepository.findById(score.getAssignments().getAssignmentId())
                        .orElseThrow(() -> new FamsApiException(HttpStatus.NOT_FOUND, "Can not find Score"));
                Map<String, Float> tmpGrade = new HashMap<>();
                tmpGrade.put(tmpAssignment.getAssignmentName(), score.getScore());
                gradeList.add(tmpGrade);
            }
            studentScorePerClassDto.setGradeList(gradeList);
        }
        return studentScorePerClassDto;
    }

    @Override
    public List<StudentScorePerClassDto> getStudentScoreListByClass(Long classId) {
        List<StudentClass> studentClasses = studentClassRepository.findByClassesClassId(classId);
        if(studentClasses.isEmpty()){
            throw new FamsApiException(HttpStatus.NOT_FOUND, "Can not find Score!");
        }
        List<StudentScorePerClassDto> studentScoreList = new ArrayList<>();
        for(StudentClass sc : studentClasses){
            studentScoreList.add(this.getStudentScoreDetail(sc.getStudents().getStudentId()));
        }
        return studentScoreList;
    }

    @Override
    public HttpHeaders downloadScoreTemplate() {

        // Load the file from the resources directory
        Resource resource = new ClassPathResource("Mark Tracking Template.xlsx");

        // Set the content type and headers for the response
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFilename());
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);

        return headers;
    }

    @Override
    public Score updateScore(Score score) {
        try {
            Score existingScore = scoreRepository.findById(score.getScoreId())
                    .orElseThrow(() -> new IllegalArgumentException("Score not found"));

            // Update the existing score with new values
            existingScore.setScore(score.getScore());
            existingScore.setSubmissionDate(score.getSubmissionDate());

            return scoreRepository.save(existingScore);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to update score: " + ex.getMessage());
        }
    }


}
