package com.fams.fams.repositories;

import com.fams.fams.models.entities.Student;
import com.fams.fams.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserNameOrEmail(String userName, String email);
    @Query("select u from User u where u.userId = :id")
    User findWithId(Long id);

}
