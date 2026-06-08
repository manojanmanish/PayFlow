package com.assignment.payflow.Controller;

import com.assignment.payflow.DTOs.UserDto;
import com.assignment.payflow.Entity.Users;
import com.assignment.payflow.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> addUserDetails(@RequestBody Users user){
        return new ResponseEntity<>(userService.addUser(user), HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<Users>> displayAllUserDetails(){
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<?> displayUserDetails(@RequestParam(required = false) Long userId,
                                                    @RequestParam(required = false) String upiId){
        if (userId == null && upiId == null) {
            return ResponseEntity
                    .badRequest()
                    .body("At least one of userId or upiId must be provided");
        }
        return new ResponseEntity<>(userService.getUser(userId,upiId), HttpStatus.OK);
    }
}
