package org.lahutina.testassignment.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lahutina.testassignment.dto.UserFullNameDto;
import org.lahutina.testassignment.entity.User;
import org.lahutina.testassignment.exception.InvalidUserAgeException;
import org.lahutina.testassignment.exception.UserNotFoundException;
import org.lahutina.testassignment.repository.UserRepository;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setFirstName("Oksana");
        testUser.setLastName("Lahutina");
        testUser.setBirthDate(LocalDate.of(2000, 1, 1));
        testUser.setEmail("oksana@example.com");
        testUser.setAddress("123 Street, City");
        testUser.setPhoneNumber("+1234567890");
    }

    @Test
    void testCreate() {
        when(userRepository.save(any())).thenReturn(testUser);

        User createdUser = userService.create(testUser);

        assertEquals(testUser, createdUser);
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void testUpdateFullName() {
        User compareToUser = new User();
        compareToUser.setId(testUser.getId());
        compareToUser.setFirstName("Jane");
        compareToUser.setLastName("Doe");
        compareToUser.setBirthDate(testUser.getBirthDate());
        compareToUser.setEmail(testUser.getEmail());
        compareToUser.setAddress(testUser.getAddress());
        UserFullNameDto userFullNameDto = new UserFullNameDto("Jane", "Doe");

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any())).thenReturn(compareToUser);

        User updatedUser = userService.updateFullName(1L, userFullNameDto);

        assertEquals(compareToUser.getFirstName(), updatedUser.getFirstName());
        assertEquals(compareToUser.getLastName(), updatedUser.getLastName());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void testUpdateFullNameUserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.updateFullName(1L, new UserFullNameDto()));
    }

    @Test
    void testUpdate() {
        User newUser = new User();
        newUser.setId(testUser.getId());
        newUser.setFirstName("Jane");
        newUser.setLastName("Doe");
        newUser.setBirthDate(testUser.getBirthDate().plusDays(1));

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any())).thenReturn(newUser);

        User updatedUser = userService.update(1L, newUser);

        assertEquals(newUser.getFirstName(), updatedUser.getFirstName());
        assertEquals(newUser.getLastName(), updatedUser.getLastName());
        assertEquals(newUser.getBirthDate(), updatedUser.getBirthDate());
        verify(userRepository, times(1)).save(newUser);
    }

    @Test
    void testUpdateUserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.update(1L, testUser));
    }

    @Test
    void testUpdateInvalidUserAge() {
        testUser.setBirthDate(LocalDate.now().minusYears(2));
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        assertThrows(InvalidUserAgeException.class, () -> userService.update(1L, testUser));
    }

    @Test
    void testDelete() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        boolean result = userService.delete(1L);

        assertTrue(result);
        verify(userRepository, times(1)).delete(testUser);
    }

    @Test
    void testFindUsersByBirthDateRange() {
        when(userRepository.findUsersByBirthDateRange(LocalDate.of(2000, 1, 1),
                LocalDate.of(2005, 1, 1)))
                .thenReturn(Collections.singletonList(testUser));

        List<User> users = userService.findUsersByBirthDateRange("2000-01-01", "2005-01-01");

        assertEquals(1, users.size());
        assertEquals(testUser, users.get(0));
    }

    @Test
    void testFindById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        User foundUser = userService.findById(1L);

        assertEquals(testUser, foundUser);
    }

    @Test
    void testFindAll() {
        when(userRepository.findAll()).thenReturn(Collections.singletonList(testUser));

        List<User> users = userService.findAll();

        assertEquals(1, users.size());
        assertEquals(testUser, users.get(0));
    }
}