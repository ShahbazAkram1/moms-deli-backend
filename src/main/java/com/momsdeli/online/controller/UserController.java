package com.momsdeli.online.controller;

import com.momsdeli.online.dto.StatusDto;
import com.momsdeli.online.exception.UserException;
import com.momsdeli.online.model.Role;
import com.momsdeli.online.model.RoleName;
import com.momsdeli.online.model.User;
import com.momsdeli.online.repository.RoleRepository;
import com.momsdeli.online.request.LoginRequest;
import com.momsdeli.online.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;


@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class UserController {

    private final UserService userService;


    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    private final RoleRepository roleRepository;

    public UserController(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder, RoleRepository roleRepository) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.roleRepository = roleRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<StatusDto> loginAPi(@RequestBody LoginRequest loginRequest){
        StatusDto statusDto = new StatusDto();
        statusDto.setCode(200L);
        statusDto.setData(this.userService.generateToken(loginRequest));
       return  ResponseEntity.status(HttpStatus.OK).body(statusDto);
    }



    @PostMapping("/register")
    public ResponseEntity<?> registerApi(@RequestBody User user){

        Role role = new Role();
        role.setName(RoleName.ROLE_CUSTOMER);


        roleRepository.save(role);
        HashSet<Role> hashSet = new HashSet<>();
        hashSet.add(role);
        user.setRoles(hashSet);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));


        return  ResponseEntity.status(HttpStatus.OK).body(this.userService.createUser(user));
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) throws UserException {
        User user = userService.findUserById(userId);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
