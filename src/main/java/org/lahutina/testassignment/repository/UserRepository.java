package org.lahutina.testassignment.repository;

import org.lahutina.testassignment.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface for User entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.birthDate BETWEEN :fromDate AND :toDate")
    List<User> findUsersByBirthDateRange(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);
}
