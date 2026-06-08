package com.assignment.payflow.Service;

import com.assignment.payflow.DTOs.UserDto;
import com.assignment.payflow.Entity.Users;
import com.assignment.payflow.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserDto addUser(Users user) {
        Users savedUser = userRepository.save(user);
        UserDto userDto = new UserDto();
        userDto.setUserId(savedUser.getUserId());
        userDto.setUserName(savedUser.getUserName());
        return userDto;
    }

    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    public Users getUser(Long id, String upiId) {
        //return userRepository.findByFilter(id,upiId).orElse(null);
        return userRepository.findByFilter(id,upiId).get();
    }
}
