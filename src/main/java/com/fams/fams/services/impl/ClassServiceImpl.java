package com.fams.fams.services.impl;

import com.fams.fams.models.entities.Class;
import com.fams.fams.models.entities.User;
import com.fams.fams.models.exception.FamsApiException;
import com.fams.fams.models.payload.dto.ClassDto;
import com.fams.fams.models.payload.dto.UserDto;
import com.fams.fams.models.payload.responseModel.ClassDetailDto;
import com.fams.fams.repositories.ClassRepository;
import com.fams.fams.repositories.StudentClassRepository;
import com.fams.fams.repositories.UserClassRepository;
import com.fams.fams.services.ClassService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClassServiceImpl implements ClassService {
    private final StudentClassRepository studentClassRepository;
    private final ClassRepository classRepository;
    private final UserClassRepository userClassRepository;
    private final ModelMapper mapper;

    @Autowired
    public ClassServiceImpl(StudentClassRepository studentClassRepository, ClassRepository classRepository, UserClassRepository userClassRepository, ModelMapper mapper) {
        this.studentClassRepository = studentClassRepository;
        this.classRepository = classRepository;
        this.userClassRepository = userClassRepository;
        this.mapper = mapper;
    }

    @Override
    public Page<ClassDto> getClassesByStudentId(Long studentId, int page, int size) {
        PageRequest pageable = PageRequest.of(page - 1, size);
        Page<Class> classes = studentClassRepository.getClassesByStudentId(studentId, pageable);
        if(classes.isEmpty()){
            throw new FamsApiException(HttpStatus.NOT_FOUND, "Classes not found!");
        }else {
            return classes.map(this::mapToDto);
        }
    }

    @Override
    public ClassDetailDto getClassesDetailById(Long id) {
        String role_admin = "ROLE_ADMIN";
        Optional<Class> classes = Optional.ofNullable(classRepository.findById(id)
                .orElseThrow(() -> new FamsApiException(HttpStatus.NOT_FOUND, "Classes not found!")));
        List<User> users = userClassRepository.findAllByClasses_ClassId(classes.get().getClassId());
        List<User> admins = new ArrayList<>();
        List<User> trainers = new ArrayList<>();
        for(User u: users){
            if(u.getRole().getRoleName().equals(role_admin)){
                admins.add(u);
            }else{
                trainers.add(u);
            }
        }
        Optional<ClassDetailDto> classDetailDto = classes.map(this::mapToDetailDto);
        classDetailDto.get().setAdmins(admins.stream().map(this::mapToUserDto).toList());
        classDetailDto.get().setTrainers(trainers.stream().map(this::mapToUserDto).toList());
        return classDetailDto.get();
    }

    @Override
    public Page<ClassDto> getAllClass(int page, int size) {
        PageRequest pageable = PageRequest.of(page - 1, size);
        Page<Class> classes = classRepository.findAll(pageable);
        if(classes.isEmpty()){
            throw new FamsApiException(HttpStatus.NOT_FOUND, "Classes not found!");
        }else {
            return classes.map(this::mapToDto);
        }
    }

    private ClassDto mapToDto(Class classObj){
        return mapper.map(classObj, ClassDto.class);
    }
    private UserDto mapToUserDto(User user){
        return mapper.map(user, UserDto.class);
    }
    private ClassDetailDto mapToDetailDto(Class classObj){
        return mapper.map(classObj, ClassDetailDto.class);
    }
}
