package com.fams.fams.models.payload.responseModel;

import com.fams.fams.models.payload.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClassDetailDto {
    private Long classId;
    private String classCode;
    private String className;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate createdDate;
    private String createdBy;
    private LocalDate updatedDate;
    private String updatedBy;
    private Float duration;
    private String location;
    private String status;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate approvedDate;
    private String approvedBy;
    private String attendee;
    private String FSU;
    private List<UserDto> admins;
    private List<UserDto> trainers;
}
