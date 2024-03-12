package com.fams.fams.repositories;

import com.fams.fams.models.entities.User;
import com.fams.fams.models.entities.UserClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserClassRepository extends JpaRepository<UserClass, Long> {
    @Query("select u from UserClass uc inner join User u on uc.users.userId = u.userId where uc.classes.classId = :classId")
    List<User> findAllByClasses_ClassId(Long classId);
}
