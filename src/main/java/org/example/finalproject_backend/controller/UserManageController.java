package org.example.finalproject_backend.controller;

import lombok.RequiredArgsConstructor;
import org.example.finalproject_backend.security.CustomUserDetails;
import org.example.finalproject_backend.dto.UserDto;
import org.example.finalproject_backend.entity.User;
import org.example.finalproject_backend.mapper.UserMapper;
import org.example.finalproject_backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserManageController {
    private final UserService userService;
    private final UserMapper userMapper;

    @PreAuthorize("hasRole('ADMIN')")
    @CrossOrigin(origins = "http://localhost:3000")
    @DeleteMapping("/deleteUserAsAdmin")
    public ResponseEntity<String> deleteUserAsAdmin(Long userId) {
        try {
            userService.deleteUserAsAdmin(userId);
            return new ResponseEntity<>("user deleted", HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PreAuthorize("hasRole('USER')")
    @CrossOrigin(origins = "http://localhost:3000")
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteOwnAccount() {
        Long userId = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        userService.deleteUserAsTheUser();
        return ResponseEntity.ok("Account deleted successfully");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/getAllUsersAsAdmin")
    public ResponseEntity<List<UserDto>> getUserDto() {
        List<UserDto> userDtoList = new ArrayList<>();
        List<User> userList = userService.getUsers();
        for (User user:userList) {
            userDtoList.add(userMapper.userEntityToDto(user));
        }
        return new ResponseEntity<>(userDtoList, HttpStatus.OK);
    }

}
