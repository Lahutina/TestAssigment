package org.lahutina.testassignment.service;

import lombok.RequiredArgsConstructor;
import org.lahutina.testassignment.dto.UserFullNameDto;
import org.lahutina.testassignment.entity.User;
import org.lahutina.testassignment.exception.InvalidUserAgeException;
import org.lahutina.testassignment.exception.UserNotFoundException;
import org.lahutina.testassignment.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of UserService.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Value("${user.min.age}")
    private int minimumAge;

    private final UserRepository userRepository;

    /**
     * Creates a new user.
     *
     * @param user The user to create/register
     * @return The created/registered user
     * @throws InvalidUserAgeException if the user's age is less than the minimum allowed age.
     */
    @Override
    public User create(User user) {
        logger.info("Creating user: {}", user);
        validateUserAge(user.getBirthDate());
        User createdUser = userRepository.save(user);
        logger.info("User created: {}", createdUser);
        return createdUser;
    }


    /**
     * Updates the full name of a user.
     *
     * @param id              The id of the user to update
     * @param userFullNameDto The new full name data fields
     * @return The updated user
     * @throws UserNotFoundException if the user with the provided ID is not found.
     */
    @Override
    public User updateFullName(Long id, UserFullNameDto userFullNameDto) {
        logger.info("Updating full name of user with ID {}: {}", id, userFullNameDto);
        User user = findById(id);
        if (userFullNameDto.getFirstName() != null && !userFullNameDto.getFirstName().isEmpty()) {
            user.setFirstName(userFullNameDto.getFirstName());
        }
        if (userFullNameDto.getLastName() != null && !userFullNameDto.getLastName().isEmpty()) {
            user.setLastName(userFullNameDto.getLastName());
        }
        User updatedUser = userRepository.save(user);
        logger.info("Full name updated: {}", updatedUser);
        return updatedUser;
    }

    /**
     * Updates a user's information.
     *
     * @param id   The id of the user to update
     * @param user The updated user information
     * @return The updated user
     * @throws UserNotFoundException if the user with the provided ID is not found.
     * @throws InvalidUserAgeException if the user's age is less than the minimum allowed age.
     */
    @Override
    public User update(Long id, User user) {
        logger.info("Updating user with ID {}: {}", id, user);
        validateUserAge(user.getBirthDate());
        User existingUser = findById(id);
        BeanUtils.copyProperties(user, existingUser, "id");
        User updatedUser = userRepository.save(existingUser);
        logger.info("User updated: {}", updatedUser);
        return updatedUser;
    }

    /**
     * Deletes a user.
     *
     * @param id The id of the user to delete
     * @return True if the user was deleted successfully
     * @throws UserNotFoundException if the user with the provided ID is not found.
     */
    @Override
    public boolean delete(Long id) {
        logger.info("Deleting user with ID {}", id);
        User user = findById(id);
        userRepository.delete(user);
        logger.info("User deleted successfully");
        return true;
    }

    /**
     * Finds users within the specified birthdate range.
     *
     * @param fromDate The start date of the birthdate range
     * @param toDate   The end date of the birthdate range
     * @return A list of users whose birthdays fall within the specified range
     */
    @Override
    public List<User> findUsersByBirthDateRange(String fromDate, String toDate) {
        logger.info("Retrieving users by birthdate range: {} to {}", fromDate, toDate);
        LocalDate from = LocalDate.parse(fromDate);
        LocalDate to = LocalDate.parse(toDate);
        return userRepository.findUsersByBirthDateRange(from, to);
    }

    /**
     * Finds a user by ID.
     *
     * @param id The id of the user to find
     * @return The found user
     * @throws UserNotFoundException If the user with the given ID is not found
     */
    @Override
    public User findById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            throw new UserNotFoundException("User not found with id: " + id);
        }
    }

    /**
     * Retrieves all users.
     *
     * @return List of all users
     */
    @Override
    public List<User> findAll() {
        logger.info("Retrieving all users");
        return userRepository.findAll();
    }

    private void validateUserAge(LocalDate birthDate) {
        if (birthDate == null) {
            throw new InvalidUserAgeException("Birthdate cannot be null");
        }
        if (calculateAge(birthDate) < minimumAge) {
            throw new InvalidUserAgeException("User must be at least " + minimumAge + " years old.");
        }
    }

    private int calculateAge(LocalDate birthDate) {
        return LocalDate.now().minusYears(birthDate.getYear()).getYear();
    }
}
