package org.lahutina.testassignment.service;

import org.lahutina.testassignment.dto.UserFullNameDto;
import org.lahutina.testassignment.entity.User;

import java.util.List;

/**
 * Service interface for managing users.
 */
public interface UserService {

    User create(User user);

    User updateFullName(Long id, UserFullNameDto userFullNameDto);

    User update(Long id, User user);

    boolean delete(Long id);

    User findById(Long id);

    List<User> findAll();

    List<User> findUsersByBirthDateRange(String fromDate, String toDate);

}
