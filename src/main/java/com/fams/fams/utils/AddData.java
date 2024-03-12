package com.fams.fams.utils;

import com.fams.fams.models.entities.*;
import com.fams.fams.models.entities.Class;
import com.fams.fams.models.entities.Module;
import com.fams.fams.repositories.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
@Transactional
public class AddData {

    // private final TrainingProgramRepository trainingProgramRepository;
    // private final ModuleRepository moduleRepository;
    // private final StudentRepository studentRepository;
    // private final TrainingProgramModuleRepository trainingProgramModuleRepository;
    // private final ClassRepository classRepository;
    // private final StudentClassRepository studentClassRepository;
    // private final UserRepository userRepository;
    // private final RoleRepository roleRepository;
    // private final AssignmentRepository assignmentRepository;
    // private final EmailTemplateRepository emailTemplateRepository;
    // private final UserClassRepository userClassRepository;

    // @Autowired
    // public AddData(TrainingProgramRepository trainingProgramRepository, ModuleRepository moduleRepository,
    //         StudentRepository studentRepository, TrainingProgramModuleRepository trainingProgramModuleRepository,
    //         ClassRepository classRepository, StudentClassRepository studentClassRepository,
    //         UserRepository userRepository, RoleRepository roleRepository, AssignmentRepository assignmentRepository,
    //         EmailTemplateRepository emailTemplateRepository, UserClassRepository userClassRepository) {
    //     this.trainingProgramRepository = trainingProgramRepository;
    //     this.moduleRepository = moduleRepository;
    //     this.studentRepository = studentRepository;
    //     this.trainingProgramModuleRepository = trainingProgramModuleRepository;
    //     this.classRepository = classRepository;
    //     this.studentClassRepository = studentClassRepository;
    //     this.userRepository = userRepository;
    //     this.roleRepository = roleRepository;
    //     this.assignmentRepository = assignmentRepository;
    //     this.emailTemplateRepository = emailTemplateRepository;
    //     this.userClassRepository = userClassRepository;
    // }

    // @PostConstruct
    // public void importData() {
    //     Role role1 = new Role();
    //     role1.setRoleName("ROLE_ADMIN");
    //     roleRepository.save(role1);

    //     Role role2 = new Role();
    //     role2.setRoleName("ROLE_TRAINER");
    //     roleRepository.save(role2);

    //     User user1 = new User();
    //     user1.setFullName("Nguyễn Đức Sơn");
    //     user1.setEmail("nguyenducson22915@gmail.com");
    //     user1.setDOB(LocalDate.of(1990, 5, 15));
    //     user1.setAddress("123 Main St, City, Country");
    //     user1.setGender("Male");
    //     user1.setPhone("1234567890");
    //     user1.setUserName("admin");
    //     user1.setPassword("$2a$10$IXDGgm..gMBfSu6LnAAr0e.l1IGMqdEuhxfQxaJa3zP2JaB9Al/fO");
    //     user1.setRole(role1);
    //     userRepository.save(user1);

    //     User user2 = new User();
    //     user2.setFullName("Ngọc Cường");
    //     user2.setEmail("nguyenducson29155@gmail.com");
    //     user2.setDOB(LocalDate.of(1985, 10, 25));
    //     user2.setAddress("456 Elm St, Town, Country");
    //     user2.setGender("Female");
    //     user2.setPhone("9876543210");
    //     user2.setUserName("admin1");
    //     user2.setPassword("$2a$10$RUaMxQo3nuA.Tc0YybonVeU.xHt3OYwNSopRoTkhCZmik/K5NOTne");
    //     user2.setRole(role1);
    //     userRepository.save(user2);

    //     User user3 = new User();
    //     user3.setFullName("Lê Cường");
    //     user3.setEmail("nguyenducson291555@gmail.com");
    //     user3.setDOB(LocalDate.of(1985, 10, 25));
    //     user3.setAddress("456 Elm St, Town, Country");
    //     user3.setGender("Female");
    //     user3.setPhone("9876543210");
    //     user3.setUserName("admin2");
    //     user3.setPassword("$2a$10$RUaMxQo3nuA.Tc0YybonVeU.xHt3OYwNSopRoTkhCZmik/K5NOTne");
    //     user3.setRole(role1);
    //     userRepository.save(user3);

    //     User user4 = new User();
    //     user4.setFullName("Nguyễn Cường");
    //     user4.setEmail("henrynguyen1203@gmail.com");
    //     user4.setDOB(LocalDate.of(1985, 10, 25));
    //     user4.setAddress("456 Elm St, Town, Country");
    //     user4.setGender("Female");
    //     user4.setPhone("9876543210");
    //     user4.setUserName("trainer");
    //     user4.setPassword("$2a$10$RUaMxQo3nuA.Tc0YybonVeU.xHt3OYwNSopRoTkhCZmik/K5NOTne");
    //     user4.setRole(role2);
    //     userRepository.save(user4);

    //     User user5 = new User();
    //     user5.setFullName("Nguyễn Ngọc");
    //     user5.setEmail("sonndse171782@fpt.edu.vn");
    //     user5.setDOB(LocalDate.of(1985, 10, 25));
    //     user5.setAddress("456 Elm St, Town, Country");
    //     user5.setGender("Female");
    //     user5.setPhone("9876543210");
    //     user5.setUserName("trainer1");
    //     user5.setPassword("$2a$10$RUaMxQo3nuA.Tc0YybonVeU.xHt3OYwNSopRoTkhCZmik/K5NOTne");
    //     user5.setRole(role2);
    //     userRepository.save(user5);

    //     User user6 = new User();
    //     user6.setFullName("Lê Ngọc");
    //     user6.setEmail("nguyenducson2915@gmail.com");
    //     user6.setDOB(LocalDate.of(1985, 10, 25));
    //     user6.setAddress("456 Elm St, Town, Country");
    //     user6.setGender("Female");
    //     user6.setPhone("9876543210");
    //     user6.setUserName("trainer2");
    //     user6.setPassword("$2a$10$RUaMxQo3nuA.Tc0YybonVeU.xHt3OYwNSopRoTkhCZmik/K5NOTne");
    //     user6.setRole(role2);
    //     userRepository.save(user6);

    //     User user7 = new User();
    //     user7.setFullName("Nguyễn Sơn");
    //     user7.setEmail("henrynguyen11203@gmail.com");
    //     user7.setDOB(LocalDate.of(1985, 10, 25));
    //     user7.setAddress("456 Elm St, Town, Country");
    //     user7.setGender("Female");
    //     user7.setPhone("9876543210");
    //     user7.setUserName("trainer3");
    //     user7.setPassword("$2a$10$RUaMxQo3nuA.Tc0YybonVeU.xHt3OYwNSopRoTkhCZmik/K5NOTne");
    //     user7.setRole(role2);
    //     userRepository.save(user7);

    //     User user8 = new User();
    //     user8.setFullName("Đức Sơn");
    //     user8.setEmail("henrynguyen12203@gmail.com");
    //     user8.setDOB(LocalDate.of(1985, 10, 25));
    //     user8.setAddress("456 Elm St, Town, Country");
    //     user8.setGender("Female");
    //     user8.setPhone("9876543210");
    //     user8.setUserName("trainer4");
    //     user8.setPassword("$2a$10$RUaMxQo3nuA.Tc0YybonVeU.xHt3OYwNSopRoTkhCZmik/K5NOTne");
    //     user8.setRole(role2);
    //     userRepository.save(user8);

    //     User user9 = new User();
    //     user9.setFullName("Tiến Dũng");
    //     user9.setEmail("henrynguyen12003@gmail.com");
    //     user9.setDOB(LocalDate.of(1985, 10, 25));
    //     user9.setAddress("456 Elm St, Town, Country");
    //     user9.setGender("Female");
    //     user9.setPhone("9876543210");
    //     user9.setUserName("trainer5");
    //     user9.setPassword("$2a$10$RUaMxQo3nuA.Tc0YybonVeU.xHt3OYwNSopRoTkhCZmik/K5NOTne");
    //     user9.setRole(role2);
    //     userRepository.save(user9);

    //     Module module1 = new Module();
    //     module1.setModuleName("FEE");
    //     module1.setCreatedDate(LocalDate.now());
    //     module1.setCreatedBy("admin");
    //     module1.setUpdatedDate(LocalDate.now());
    //     module1.setUpdatedBy("admin");
    //     moduleRepository.save(module1);

    //     Module module2 = new Module();
    //     module2.setModuleName("MOCK");
    //     module2.setCreatedDate(LocalDate.now());
    //     module2.setCreatedBy("admin");
    //     module2.setUpdatedDate(LocalDate.now());
    //     module2.setUpdatedBy("admin");
    //     moduleRepository.save(module2);

    //     TrainingProgram trainingProgram1 = new TrainingProgram();
    //     trainingProgram1.setName("Chương trình A");
    //     trainingProgram1.setStatus("Active");
    //     trainingProgram1.setCode("A001");
    //     trainingProgram1.setCreateDate(LocalDate.now());
    //     trainingProgram1.setDuration(1.0f); // Giả sử mỗi module học trong 1 tháng
    //     trainingProgram1.setCreatedBy("admin");
    //     trainingProgram1.setUpdateDate(LocalDate.now());
    //     trainingProgram1.setUpdatedBy("admin");
    //     trainingProgramRepository.save(trainingProgram1);

    //     TrainingProgram trainingProgram2 = new TrainingProgram();
    //     trainingProgram2.setName("Chương trình B");
    //     trainingProgram2.setStatus("Active");
    //     trainingProgram2.setCode("B001");
    //     trainingProgram2.setCreateDate(LocalDate.now());
    //     trainingProgram2.setDuration(1.0f); // Giả sử mỗi module học trong 1 tháng
    //     trainingProgram2.setCreatedBy("admin");
    //     trainingProgram2.setUpdateDate(LocalDate.now());
    //     trainingProgram2.setUpdatedBy("admin");
    //     trainingProgramRepository.save(trainingProgram2);

    //     TrainingProgram trainingProgram3 = new TrainingProgram();
    //     trainingProgram3.setName("Chương trình A");
    //     trainingProgram3.setStatus("Active");
    //     trainingProgram3.setCode("BE_JAVA_SPRING");
    //     trainingProgram3.setCreateDate(LocalDate.now());
    //     trainingProgram3.setDuration(1.0f); // Giả sử mỗi module học trong 1 tháng
    //     trainingProgram3.setCreatedBy("admin");
    //     trainingProgram3.setUpdateDate(LocalDate.now());
    //     trainingProgram3.setUpdatedBy("admin");
    //     trainingProgramRepository.save(trainingProgram3);

    //     Student student1 = new Student();
    //     student1.setStudentCode("SV001");
    //     student1.setFullName("Nguyễn Văn A");
    //     student1.setDOB(LocalDate.of(1995, 5, 10));
    //     student1.setGender("Nam");
    //     student1.setPhone("0123456789");
    //     student1.setEmail("student1@example.com");
    //     student1.setSchool("Trường A");
    //     student1.setMajor("Ngành A");
    //     student1.setGraduatedDate(LocalDate.of(2020, 6, 15));
    //     student1.setGPA(3.8f);
    //     student1.setAddress("Địa chỉ A");
    //     student1.setFAAccount("fa_account_1");
    //     student1.setType("Loại A");
    //     student1.setStatus("Hoạt động");
    //     student1.setRECer("RECer A");
    //     student1.setJoinedDate(LocalDate.now());
    //     student1.setArea("Khu vực A");
    //     studentRepository.save(student1);

    //     // Sinh viên 2
    //     Student student2 = new Student();
    //     student2.setStudentCode("SV002");
    //     student2.setFullName("Trần Thị B");
    //     student2.setDOB(LocalDate.of(1996, 7, 20));
    //     student2.setGender("Nữ");
    //     student2.setPhone("0987654321");
    //     student2.setEmail("student2@example.com");
    //     student2.setSchool("Trường B");
    //     student2.setMajor("Ngành B");
    //     student2.setGraduatedDate(LocalDate.of(2019, 5, 20));
    //     student2.setGPA(3.6f);
    //     student2.setAddress("Địa chỉ B");
    //     student2.setFAAccount("fa_account_2");
    //     student2.setType("Loại B");
    //     student2.setStatus("Hoạt động");
    //     student2.setRECer("RECer B");
    //     student2.setJoinedDate(LocalDate.now());
    //     student2.setArea("Khu vực B");
    //     studentRepository.save(student2);

    //     Class class1 = new Class();
    //     class1.setClassCode("ABC123");
    //     class1.setClassName("Mathematics");
    //     class1.setStartTime(LocalTime.now());
    //     class1.setEndTime(LocalTime.now().plusHours(2));
    //     class1.setStartDate(LocalDate.now().minusDays(10));
    //     class1.setEndDate(LocalDate.now().plusMonths(3));
    //     class1.setCreatedDate(LocalDate.now().minusMonths(1));
    //     class1.setCreatedBy(user1.getFullName());
    //     class1.setUpdatedDate(LocalDate.now());
    //     class1.setUpdatedBy(user4.getFullName());
    //     class1.setApprovedDate(LocalDate.now());
    //     class1.setApprovedBy(user5.getFullName());
    //     class1.setAttendee("Fresher");
    //     class1.setFSU("FHM");
    //     class1.setDuration(35f);
    //     class1.setLocation("Room A");
    //     class1.setStatus("Active");
    //     class1.setTrainingPrograms(trainingProgram1);
    //     classRepository.save(class1);

    //     Class class2 = new Class();
    //     class2.setClassCode("DEF456");
    //     class2.setClassName("English");
    //     class2.setStartTime(LocalTime.now());
    //     class2.setEndTime(LocalTime.now().plusHours(2));
    //     class2.setStartDate(LocalDate.now().minusMonths(1));
    //     class2.setEndDate(LocalDate.now().plusMonths(2));
    //     class2.setCreatedDate(LocalDate.now().minusMonths(1));
    //     class2.setCreatedBy(user2.getFullName());
    //     class2.setUpdatedDate(LocalDate.now());
    //     class2.setUpdatedBy(user6.getFullName());
    //     class2.setApprovedDate(LocalDate.now());
    //     class2.setApprovedBy(user7.getFullName());
    //     class2.setAttendee("Intern");
    //     class2.setFSU("FHM");
    //     class2.setDuration(20f);
    //     class2.setLocation("Room B");
    //     class2.setStatus("Active");
    //     class2.setTrainingPrograms(trainingProgram1);
    //     classRepository.save(class2);

    //     Class class3 = new Class();
    //     class3.setClassCode("DEF4567");
    //     class3.setClassName("BE");
    //     class3.setStartTime(LocalTime.now());
    //     class3.setEndTime(LocalTime.now().plusHours(2));
    //     class3.setStartDate(LocalDate.now().minusDays(15));
    //     class3.setEndDate(LocalDate.now().plusMonths(2));
    //     class3.setCreatedDate(LocalDate.now().minusMonths(2));
    //     class3.setCreatedBy(user3.getFullName());
    //     class3.setUpdatedDate(LocalDate.now());
    //     class3.setUpdatedBy(user8.getFullName());
    //     class3.setApprovedDate(LocalDate.now());
    //     class3.setApprovedBy(user9.getFullName());
    //     class3.setAttendee("Online fee-fresher");
    //     class3.setFSU("FHM");
    //     class3.setDuration(25f);
    //     class3.setLocation("Room B");
    //     class3.setStatus("Active");
    //     class3.setTrainingPrograms(trainingProgram3);
    //     classRepository.save(class3);

    //     TrainingProgramModule trainingProgramModule1 = new TrainingProgramModule();
    //     trainingProgramModule1.setModules(module1);
    //     trainingProgramModule1.setTrainingPrograms(trainingProgram1);
    //     trainingProgramModuleRepository.save(trainingProgramModule1);

    //     TrainingProgramModule trainingProgramModule2 = new TrainingProgramModule();
    //     trainingProgramModule2.setModules(module2);
    //     trainingProgramModule2.setTrainingPrograms(trainingProgram1);
    //     trainingProgramModuleRepository.save(trainingProgramModule2);

    //     TrainingProgramModule trainingProgramModule3 = new TrainingProgramModule();
    //     trainingProgramModule3.setModules(module1);
    //     trainingProgramModule3.setTrainingPrograms(trainingProgram2);
    //     trainingProgramModuleRepository.save(trainingProgramModule3);

    //     TrainingProgramModule trainingProgramModule4 = new TrainingProgramModule();
    //     trainingProgramModule4.setModules(module2);
    //     trainingProgramModule4.setTrainingPrograms(trainingProgram2);
    //     trainingProgramModuleRepository.save(trainingProgramModule4);

    //     TrainingProgramModule trainingProgramModule5 = new TrainingProgramModule();
    //     trainingProgramModule5.setModules(module1);
    //     trainingProgramModule5.setTrainingPrograms(trainingProgram3);
    //     trainingProgramModuleRepository.save(trainingProgramModule5);

    //     TrainingProgramModule trainingProgramModule6 = new TrainingProgramModule();
    //     trainingProgramModule6.setModules(module2);
    //     trainingProgramModule6.setTrainingPrograms(trainingProgram3);
    //     trainingProgramModuleRepository.save(trainingProgramModule6);

    //     StudentClass entity1 = new StudentClass();
    //     entity1.setStudents(student1);
    //     entity1.setClasses(class1);
    //     entity1.setAttendingStatus("InClass");
    //     entity1.setResult(true);
    //     entity1.setFinalScore(8.5f);
    //     entity1.setGPALevel("A");
    //     entity1.setCertificationStatus("Certified");
    //     entity1.setCertificationDate(LocalDate.now());
    //     entity1.setMethod("Online");
    //     studentClassRepository.save(entity1);

    //     StudentClass entity2 = new StudentClass();
    //     entity2.setStudents(student1);
    //     entity2.setClasses(class2);
    //     entity2.setAttendingStatus("InClass");
    //     entity2.setResult(false);
    //     entity2.setFinalScore(6.0f);
    //     entity2.setGPALevel("C");
    //     entity2.setCertificationStatus("Uncertified");
    //     entity2.setCertificationDate(LocalDate.now());
    //     entity2.setMethod("In-person");
    //     studentClassRepository.save(entity2);

    //     StudentClass entity3 = new StudentClass();
    //     entity3.setStudents(student2);
    //     entity3.setClasses(class1);
    //     entity3.setAttendingStatus("InClass");
    //     entity3.setResult(true);
    //     entity3.setFinalScore(8.5f);
    //     entity3.setGPALevel("A");
    //     entity3.setCertificationStatus("Certified");
    //     entity3.setCertificationDate(LocalDate.now());
    //     entity3.setMethod("Online");
    //     studentClassRepository.save(entity3);

    //     UserClass userClass1 = new UserClass();
    //     userClass1.setClasses(class1);
    //     userClass1.setUsers(user1);
    //     userClassRepository.save(userClass1);

    //     UserClass userClass2 = new UserClass();
    //     userClass2.setClasses(class1);
    //     userClass2.setUsers(user4);
    //     userClassRepository.save(userClass2);

    //     UserClass userClass3 = new UserClass();
    //     userClass3.setClasses(class1);
    //     userClass3.setUsers(user5);
    //     userClassRepository.save(userClass3);

    //     UserClass userClass4 = new UserClass();
    //     userClass4.setClasses(class2);
    //     userClass4.setUsers(user2);
    //     userClassRepository.save(userClass4);

    //     UserClass userClass5 = new UserClass();
    //     userClass5.setClasses(class2);
    //     userClass5.setUsers(user6);
    //     userClassRepository.save(userClass5);

    //     UserClass userClass6 = new UserClass();
    //     userClass6.setClasses(class2);
    //     userClass6.setUsers(user7);
    //     userClassRepository.save(userClass6);

    //     UserClass userClass7 = new UserClass();
    //     userClass7.setClasses(class3);
    //     userClass7.setUsers(user3);
    //     userClassRepository.save(userClass7);

    //     UserClass userClass8 = new UserClass();
    //     userClass8.setClasses(class3);
    //     userClass8.setUsers(user8);
    //     userClassRepository.save(userClass8);

    //     UserClass userClass9 = new UserClass();
    //     userClass9.setClasses(class3);
    //     userClass9.setUsers(user9);
    //     userClassRepository.save(userClass9);

    //     Assignment assignment1 = new Assignment();
    //     assignment1.setAssignmentName("Quiz 3");
    //     assignment1.setAssignmentType("Quiz");
    //     assignment1.setCreatedBy("DoNVC");
    //     assignment1.setCreatedDate(LocalDate.of(2024, 1, 20)); // Note: Month is 0-based
    //     assignment1.setDescription("Random description");
    //     assignment1.setDueDate(LocalDate.of(2024, 1, 25)); // Note: Month is 0-based
    //     assignment1.setUpdatedBy("DoNVC");
    //     assignment1.setUpdatedDate(LocalDate.of(2024, 1, 20)); // Note: Month is 0-based
    //     assignment1.setModules(module1);
    //     assignmentRepository.save(assignment1);

    //     Assignment assignment2 = new Assignment();
    //     assignment2.setAssignmentName("Quiz 4");
    //     assignment2.setAssignmentType("Quiz");
    //     assignment2.setCreatedBy("DoNVC");
    //     assignment2.setCreatedDate(LocalDate.of(2024, 1, 21)); // Note: Month is 0-based
    //     assignment2.setDescription("Random description");
    //     assignment2.setDueDate(LocalDate.of(2024, 1, 26)); // Note: Month is 0-based
    //     assignment2.setUpdatedBy("DoNVC");
    //     assignment2.setUpdatedDate(LocalDate.of(2024, 1, 21)); // Note: Month is 0-based
    //     assignment2.setModules(module1);
    //     assignmentRepository.save(assignment2);

    //     Assignment assignment3 = new Assignment();
    //     assignment3.setAssignmentName("Quiz 5");
    //     assignment3.setAssignmentType("Quiz");
    //     assignment3.setCreatedBy("DoNVC");
    //     assignment3.setCreatedDate(LocalDate.of(2024, 1, 22)); // Note: Month is 0-based
    //     assignment3.setDescription("Random description");
    //     assignment3.setDueDate(LocalDate.of(2024, 1, 27)); // Note: Month is 0-based
    //     assignment3.setUpdatedBy("DoNVC");
    //     assignment3.setUpdatedDate(LocalDate.of(2024, 1, 22)); // Note: Month is 0-based
    //     assignment3.setModules(module1);
    //     assignmentRepository.save(assignment3);

    //     Assignment assignment4 = new Assignment();
    //     assignment4.setAssignmentName("Quiz 6");
    //     assignment4.setAssignmentType("Quiz");
    //     assignment4.setCreatedBy("DoNVC");
    //     assignment4.setCreatedDate(LocalDate.of(2024, 1, 23)); // Note: Month is 0-based
    //     assignment4.setDescription("Random description");
    //     assignment4.setDueDate(LocalDate.of(2024, 1, 28)); // Note: Month is 0-based
    //     assignment4.setUpdatedBy("DoNVC");
    //     assignment4.setUpdatedDate(LocalDate.of(2024, 1, 23)); // Note: Month is 0-based
    //     assignment4.setModules(module1);
    //     assignmentRepository.save(assignment4);

    //     Assignment assignment5 = new Assignment();
    //     assignment5.setAssignmentName("Average");
    //     assignment5.setAssignmentType("Quiz");
    //     assignment5.setCreatedBy("DoNVC");
    //     assignment5.setCreatedDate(LocalDate.of(2024, 1, 24)); // Note: Month is 0-based
    //     assignment5.setDescription("Random description");
    //     assignment5.setDueDate(LocalDate.of(2024, 1, 29)); // Note: Month is 0-based
    //     assignment5.setUpdatedBy("DoNVC");
    //     assignment5.setUpdatedDate(LocalDate.of(2024, 1, 24)); // Note: Month is 0-based
    //     assignment5.setModules(module1);
    //     assignmentRepository.save(assignment5);

    //     Assignment assignment6 = new Assignment();
    //     assignment6.setAssignmentName("Practice Exam 1");
    //     assignment6.setAssignmentType("ASM");
    //     assignment6.setCreatedBy("DoNVC");
    //     assignment6.setCreatedDate(LocalDate.of(2024, 1, 20)); // Note: Month is 0-based
    //     assignment6.setDescription("Random description");
    //     assignment6.setDueDate(LocalDate.of(2024, 1, 25)); // Note: Month is 0-based
    //     assignment6.setUpdatedBy("DoNVC");
    //     assignment6.setUpdatedDate(LocalDate.of(2024, 1, 20)); // Note: Month is 0-based
    //     assignment6.setModules(module1);
    //     assignmentRepository.save(assignment6);

    //     Assignment assignment7 = new Assignment();
    //     assignment7.setAssignmentName("Practice Exam 2");
    //     assignment7.setAssignmentType("ASM");
    //     assignment7.setCreatedBy("DoNVC");
    //     assignment7.setCreatedDate(LocalDate.of(2024, 1, 21)); // Note: Month is 0-based
    //     assignment7.setDescription("Random description");
    //     assignment7.setDueDate(LocalDate.of(2024, 1, 26)); // Note: Month is 0-based
    //     assignment7.setUpdatedBy("DoNVC");
    //     assignment7.setUpdatedDate(LocalDate.of(2024, 1, 21)); // Note: Month is 0-based
    //     assignment7.setModules(module1);
    //     assignmentRepository.save(assignment7);

    //     Assignment assignment8 = new Assignment();
    //     assignment8.setAssignmentName("Practice Exam 3");
    //     assignment8.setAssignmentType("ASM");
    //     assignment8.setCreatedBy("DoNVC");
    //     assignment8.setCreatedDate(LocalDate.of(2024, 1, 22)); // Note: Month is 0-based
    //     assignment8.setDescription("Random description");
    //     assignment8.setDueDate(LocalDate.of(2024, 1, 27)); // Note: Month is 0-based
    //     assignment8.setUpdatedBy("DoNVC");
    //     assignment8.setUpdatedDate(LocalDate.of(2024, 1, 22)); // Note: Month is 0-based
    //     assignment8.setModules(module1);
    //     assignmentRepository.save(assignment8);

    //     Assignment assignment9 = new Assignment();
    //     assignment9.setAssignmentName("Average");
    //     assignment9.setAssignmentType("ASM");
    //     assignment9.setCreatedBy("DoNVC");
    //     assignment9.setCreatedDate(LocalDate.of(2024, 1, 23)); // Note: Month is 0-based
    //     assignment9.setDescription("Random description");
    //     assignment9.setDueDate(LocalDate.of(2024, 1, 27)); // Note: Month is 0-based
    //     assignment9.setUpdatedBy("DoNVC");
    //     assignment9.setUpdatedDate(LocalDate.of(2024, 1, 23)); // Note: Month is 0-based
    //     assignment9.setModules(module1);
    //     assignmentRepository.save(assignment9);

    //     Assignment assignment10 = new Assignment();
    //     assignment10.setAssignmentName("Quiz Final");
    //     assignment10.setAssignmentType("Quiz Final");
    //     assignment10.setCreatedBy("DoNVC");
    //     assignment10.setCreatedDate(LocalDate.of(2024, 1, 21)); // Note: Month is 0-based
    //     assignment10.setDescription("Random description");
    //     assignment10.setDueDate(LocalDate.of(2024, 1, 26)); // Note: Month is 0-based
    //     assignment10.setUpdatedBy("DoNVC");
    //     assignment10.setUpdatedDate(LocalDate.of(2024, 1, 21)); // Note: Month is 0-based
    //     assignment10.setModules(module1);
    //     assignmentRepository.save(assignment10);

    //     Assignment assignment11 = new Assignment();
    //     assignment11.setAssignmentName("Audit");
    //     assignment11.setAssignmentType("Audit");
    //     assignment11.setCreatedBy("DoNVC");
    //     assignment11.setCreatedDate(LocalDate.of(2024, 1, 20)); // Note: Month is 0-based
    //     assignment11.setDescription("Random description");
    //     assignment11.setDueDate(LocalDate.of(2024, 1, 25)); // Note: Month is 0-based
    //     assignment11.setUpdatedBy("DoNVC");
    //     assignment11.setUpdatedDate(LocalDate.of(2024, 1, 20)); // Note: Month is 0-based
    //     assignment11.setModules(module1);
    //     assignmentRepository.save(assignment11);

    //     Assignment assignment12 = new Assignment();
    //     assignment12.setAssignmentName("Practice Final");
    //     assignment12.setAssignmentType("Practice Final");
    //     assignment12.setCreatedBy("DoNVC");
    //     assignment12.setCreatedDate(LocalDate.of(2024, 1, 22)); // Note: Month is 0-based
    //     assignment12.setDescription("Random description");
    //     assignment12.setDueDate(LocalDate.of(2024, 1, 27)); // Note: Month is 0-based
    //     assignment12.setUpdatedBy("DoNVC");
    //     assignment12.setUpdatedDate(LocalDate.of(2024, 1, 22)); // Note: Month is 0-based
    //     assignment12.setModules(module1);
    //     assignmentRepository.save(assignment12);

    //     Assignment assignment13 = new Assignment();
    //     assignment13.setAssignmentName("MOCK");
    //     assignment13.setAssignmentType("MOCK");
    //     assignment13.setCreatedBy("DoNVC");
    //     assignment13.setCreatedDate(LocalDate.of(2024, 1, 22)); // Note: Month is 0-based
    //     assignment13.setDescription("Random description");
    //     assignment13.setDueDate(LocalDate.of(2024, 1, 27)); // Note: Month is 0-based
    //     assignment13.setUpdatedBy("DoNVC");
    //     assignment13.setUpdatedDate(LocalDate.of(2024, 1, 22)); // Note: Month is 0-based
    //     assignment13.setModules(module2);
    //     assignmentRepository.save(assignment13);

    //     EmailTemplate emailTemplate1 = new EmailTemplate();
    //     emailTemplate1.setType("Remind Trainer Input Student's Score");
    //     emailTemplate1.setName("Remind Trainer Input Student's Score");
    //     emailTemplate1.setDescription(
    //             "We are approaching the end of the assessment period, and the most important task is to update grades for our students. To ensure accuracy and fairness in this process, it is essential to enter grades promptly and thoroughly.\nBelow are the steps to follow:\n. Log in to our student management system.\n. Access the grade entry section or the corresponding class grade sheet.\n. Review the list of students and enter grades for each student as required.\n. Double-check each grade before saving to ensure accuracy.\n. If you encounter any difficulties during this process, please do not hesitate to contact the technical support department.");
    //     emailTemplate1.setCreatedDate(LocalDate.now());
    //     emailTemplate1.setCreatedBy("Admin");
    //     emailTemplate1.setUpdatedDate(LocalDate.now());
    //     emailTemplate1.setUpdatedBy("Admin");
    //     emailTemplate1.setStatus(true);
    //     emailTemplateRepository.save(emailTemplate1);

    //     EmailTemplate emailTemplate2 = new EmailTemplate();
    //     emailTemplate2.setType("Inform Tet's Holiday");
    //     emailTemplate2.setName("Inform Tet's Holiday break");
    //     emailTemplate2.setDescription(
    //             "We would like to inform you about the upcoming holiday break in celebration of Tet.\n" +
    //                     "Please see the details below:\n- Holiday period: from 1-1-2024 to 5-1-2024.\n" +
    //                     "Any special activities or events during the break will be announced separately.\n" +
    //                     "We hope that each of you will utilize this break to rest, relax, and enjoy memorable moments with your families and friends.\n"
    //                     +
    //                     "Thank you for your understanding and cooperation. Wishing you all a joyful and peaceful holiday break!\n");
    //     emailTemplate2.setCreatedDate(LocalDate.now());
    //     emailTemplate2.setCreatedBy("Admin");
    //     emailTemplate2.setUpdatedDate(LocalDate.now());
    //     emailTemplate2.setUpdatedBy("Admin");
    //     emailTemplate2.setStatus(true);
    //     emailTemplateRepository.save(emailTemplate2);
    // }
}
