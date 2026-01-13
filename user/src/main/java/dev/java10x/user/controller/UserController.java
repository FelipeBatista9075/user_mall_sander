package dev.java10x.user.controller;

import dev.java10x.user.dto.UserDto;
import dev.java10x.user.entities.UserModel;
import dev.java10x.user.producer.UserProducer;
import dev.java10x.user.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private UserProducer userProducer;


    public UserController(UserService userService, UserProducer userProducer) {
        this.userService = userService;
        this.userProducer = userProducer;
    }

    @PostMapping("/create")
    public ResponseEntity<UserModel> createUser(@RequestBody UserDto userDto) {
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(userDto, userModel);

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.saveAndPublish(userModel));
    }

    @GetMapping("list/users")
    public ResponseEntity<List<UserModel>> getAllUsers() {
        List<UserModel> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.DeleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filter")
    public ResponseEntity<List<UserModel>> filterUsersByName(@RequestParam String name) {
        List<UserModel> users = userService.filterUsersByName(name);
        return ResponseEntity.ok(users);
    }
    @PatchMapping("/update/{id}")
    public ResponseEntity<UserModel> patchUser(@PathVariable String id, @RequestBody UserModel partialUser) {
        UserModel updatedUser = userService.patchUser(id, partialUser);
        return ResponseEntity.ok(updatedUser);
    }
}
