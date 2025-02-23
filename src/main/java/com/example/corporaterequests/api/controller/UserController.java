package com.example.corporaterequests.api.controller;


import com.example.corporaterequests.dto.request.UserRequest;
import com.example.corporaterequests.dto.response.UserResponse;
import com.example.corporaterequests.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    /**
     * user service to call db repository.
     */
    @Autowired
    private UserService userService;

    /**
     * create user api.
     * @param userRequest user request
     * @return user response
     */
    @PostMapping
    public ResponseEntity<UserResponse> createUser(
            @RequestBody final UserRequest userRequest
    ) {
        UserResponse response = userService.createUser(userRequest);
        return ResponseEntity.ok(response);
    }

    /**
     * get user by id.
     * @param id user id
     * @return user response
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable final Long id) {
        UserResponse response = userService.getUser(id);
        return ResponseEntity.ok(response);
    }

    /**
     * update user.
     * @param id user id
     * @param userRequest user request
     * @return user response
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable final Long id,
            @RequestBody final UserRequest userRequest
    ) {
        UserResponse response = userService.updateUser(id, userRequest);
        return ResponseEntity.ok(response);
    }

    /**
     * physical delete user.
     * @param id user id
     * @return void
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable final String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}
