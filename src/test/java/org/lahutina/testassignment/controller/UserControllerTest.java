package org.lahutina.testassignment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.Priority;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lahutina.testassignment.dto.UserFullNameDto;
import org.lahutina.testassignment.entity.User;
import org.lahutina.testassignment.exception.InvalidUserAgeException;
import org.lahutina.testassignment.exception.UserNotFoundException;
import org.lahutina.testassignment.service.UserService;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
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
    @Priority(1)
    void testCreateUser() throws Exception {
        when(userService.create(any(User.class))).thenReturn(testUser);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(testUser)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("Oksana"))
                .andExpect(jsonPath("$.lastName").value("Lahutina"))
                .andExpect(jsonPath("$.birthDate").value("2000-01-01"))
                .andExpect(jsonPath("$.email").value("oksana@example.com"))
                .andExpect(jsonPath("$.address").value("123 Street, City"))
                .andExpect(jsonPath("$.phoneNumber").value("+1234567890"));
    }

    @Test
    @Priority(2)
    void testReadUser() throws Exception {
        Long userId = 1L;
        when(userService.findById(userId)).thenReturn(testUser);

        mockMvc.perform(get("/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("Oksana"))
                .andExpect(jsonPath("$.lastName").value("Lahutina"))
                .andExpect(jsonPath("$.birthDate").value("2000-01-01"))
                .andExpect(jsonPath("$.email").value("oksana@example.com"))
                .andExpect(jsonPath("$.address").value("123 Street, City"))
                .andExpect(jsonPath("$.phoneNumber").value("+1234567890"));
    }

    @Test
    @Priority(3)
    void testUpdateFullName() throws Exception {
        Long userId = 1L;
        UserFullNameDto userFullNameDto = new UserFullNameDto();
        userFullNameDto.setFirstName("Ada");
        userFullNameDto.setLastName("Lavleys");

        User updatedUser = new User(userId, "oksana@example.com", "Ada", "Lavleys",
                LocalDate.of(2000, 1, 1), "123 Street, City", "+1234567890");

        when(userService.updateFullName(userId, userFullNameDto)).thenReturn(updatedUser);

        mockMvc.perform(patch("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\": \"Ada\", \"lastName\": \"Lavleys\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("Ada"))
                .andExpect(jsonPath("$.lastName").value("Lavleys"))
                .andExpect(jsonPath("$.email").value("oksana@example.com"))
                .andExpect(jsonPath("$.birthDate").value("2000-01-01"))
                .andExpect(jsonPath("$.address").value("123 Street, City"))
                .andExpect(jsonPath("$.phoneNumber").value("+1234567890"));
    }

    @Test
    @Priority(4)
    void testUpdateUser() throws Exception {
        Long userId = 1L;
        when(userService.update(eq(userId), any(User.class))).thenReturn(testUser);

        mockMvc.perform(put("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\": \"Oksana\", \"lastName\": \"Lahutina\", \"birthDate\": \"2000-01-01\", \"email\": \"oksana@example.com\", \"address\": \"123 Street, City\", \"phoneNumber\": \"+1234567890\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("Oksana"))
                .andExpect(jsonPath("$.lastName").value("Lahutina"))
                .andExpect(jsonPath("$.birthDate").value("2000-01-01"))
                .andExpect(jsonPath("$.email").value("oksana@example.com"))
                .andExpect(jsonPath("$.address").value("123 Street, City"))
                .andExpect(jsonPath("$.phoneNumber").value("+1234567890"));
    }

    @Test
    @Priority(5)
    void testDeleteUser() throws Exception {
        Long userId = 1L;
        when(userService.delete(userId)).thenReturn(true);

        mockMvc.perform(delete("/users/{id}", userId))
                .andExpect(status().isNoContent());
    }

    @Test
    @Priority(6)
    void testSearchUsersByBirthDateRange() throws Exception {
        String fromDate = "2022-01-01";
        String toDate = "2022-12-31";

        List<User> users = Arrays.asList(new User(), new User()); // Example users

        when(userService.findUsersByBirthDateRange(fromDate, toDate)).thenReturn(users);

        mockMvc.perform(get("/users/search")
                        .param("from", fromDate)
                        .param("to", toDate))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2)); // Assuming two users are returned
    }

    @Test
    @Priority(7)
    void testInvalidCreateUser() throws Exception {
        testUser.setBirthDate(LocalDate.of(2023, 1, 1));

        when(userService.create(testUser)).thenThrow(InvalidUserAgeException.class);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(testUser)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Priority(8)
    void testUserNotFound() throws Exception {
        Long nonExistingUserId = 100L;
        when(userService.findById(nonExistingUserId)).thenThrow(UserNotFoundException.class);

        mockMvc.perform(get("/users/{id}", nonExistingUserId))
                .andExpect(status().isNotFound());
    }

    @Test
    @Priority(9)
    void testReadAllUsers() throws Exception {
        List<User> userList = Arrays.asList(new User(), new User());
        when(userService.findAll()).thenReturn(userList);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());
    }


    @Test
    @Priority(10)
    void testValidationConstraints() throws Exception {
        User invalidUser = new User();
        invalidUser.setEmail("");
        invalidUser.setFirstName("");
        invalidUser.setLastName("");
        invalidUser.setBirthDate(LocalDate.now().plusDays(1));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(invalidUser)))
                .andExpect(status().isBadRequest());
    }

    private String asJsonString(Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}