package org.lahutina.testassignment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.lahutina.testassignment.dto.UserFullNameDto;
import org.lahutina.testassignment.entity.User;
import org.lahutina.testassignment.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class to handle requests related to users.
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<User> create(@Valid @RequestBody User user) {
        User createdUser = userService.create(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> read(@PathVariable Long id) {
        User user = userService.findById(id);
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<User> updateFullName(@PathVariable Long id, @RequestBody UserFullNameDto userFullNameDto) {
        User updatedUser = userService.updateFullName(id, userFullNameDto);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable Long id, @Valid @RequestBody User user) {
        User updatedUser = userService.update(id, user);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = userService.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsersByBirthDateRange(@RequestParam("from") String fromDate, @RequestParam("to") String toDate) {
        List<User> users = userService.findUsersByBirthDateRange(fromDate, toDate);
        return ResponseEntity.ok(users);
    }

    @GetMapping
    public ResponseEntity<List<User>> readAll() {
        return ResponseEntity.ok(userService.findAll());
    }
}
