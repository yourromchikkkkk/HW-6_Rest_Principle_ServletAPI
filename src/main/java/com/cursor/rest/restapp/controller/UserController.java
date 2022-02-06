package com.cursor.rest.restapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.cursor.rest.restapp.model.User;

import java.net.URI;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

@RestController
public class UserController {

    List<User> users = new ArrayList<>();

    public UserController() {
        users.add(new User(1, "Roman", "Matsuiev", 19));
        users.add(new User(2, "Maksim", "Hamulyak", 18));
    }
    @GetMapping("/users")
    public List<User> getUsers() {
        return users;
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getById(@PathVariable("id") int id) {
        Optional<User> result = users.stream().filter(user -> user.getId() == id).findFirst();
        if (result.isPresent()) {
            return ResponseEntity.ok(result.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/users")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        Optional optionalUserName = users.stream().filter(temp -> Objects.equals(temp.getName(), user.getName())).findFirst();
        Optional optionalUserId = users.stream().filter(temp -> temp.getId() == user.getId()).findFirst();
        if (!optionalUserName.isPresent() && !optionalUserId.isPresent()) {
            users.add(user);
            return ResponseEntity.created(URI.create("/users")).build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@RequestBody User user, @PathVariable("id") int id) {
        if (users.stream().anyMatch(temp -> temp.getId() == id)) {
            User userToUpdate = users.stream().filter(temp -> temp.getId() == id).findFirst().get();
            userToUpdate.setAge(user.getAge());
            userToUpdate.setName(user.getName());
            userToUpdate.setSurname(user.getSurname());
        } else {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.accepted().build();
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable("id") int id) {
        if(users.stream().anyMatch(temp -> temp.getId() == id)) {
            users.remove(users.stream().filter(temp -> temp.getId() == id).findFirst().get());
            return ResponseEntity.accepted().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
