package com.fams.fams.repositories;

import com.fams.fams.models.entities.Module;
import com.fams.fams.models.entities.TrainingProgramModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TrainingProgramModuleRepository extends JpaRepository<TrainingProgramModule, Long> {
    @Query("select m\n" +
            "from TrainingProgramModule tm \n" +
            "inner join Module m\n" +
            "on tm.modules.moduleId = m.moduleId\n" +
            "where tm.trainingPrograms.programID= :programId")
    List<Module> findModuleByTrainingProgramsID(@Param("programId") Long programId);
    List<TrainingProgramModule> findByTrainingProgramsProgramID(Long trainingProgramId);
}
