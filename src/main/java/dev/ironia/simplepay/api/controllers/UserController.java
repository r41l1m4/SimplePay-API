package dev.ironia.simplepay.api.controllers;

import dev.ironia.simplepay.api.domain.user.User;
import dev.ironia.simplepay.api.dtos.UserDto;
import dev.ironia.simplepay.api.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private UserService userService;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserDto userDto) {
        User user = userService.createUser(userDto);
        return new ResponseEntity<>(
                user,
                HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(
                this.userService.getAllUsers(),
                HttpStatus.OK);
    }
}
