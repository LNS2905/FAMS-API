package com.fams.fams.repositories;

import com.fams.fams.models.entities.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface AssignmentRepository extends JpaRepository<Assignment,Long> {

    List<Assignment> findAssignmentByModulesModuleIdIn(Collection<Long> moduleIds);

}
