package com.shyam.controllers;

import com.shyam.config.custom.MyUserDetails;
import com.shyam.dto.request.LoginRequest;
import com.shyam.dto.request.UserRequest;
import com.shyam.dto.response.UserResponse;
import com.shyam.entities.UserEntity;
import com.shyam.exceptions.UserExistsException;
import com.shyam.services.AuthService;
import com.shyam.services.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtService jwtService;
    private final ModelMapper modelMapper;
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        System.out.println(loginRequest);
        UserEntity userEntity = authService.loginUser(loginRequest);

        if(userEntity == null)
            return null;

        UserResponse userResponse = modelMapper.map(userEntity, UserResponse.class);
        userResponse.setToken(jwtService.generateJwtToken(new MyUserDetails(userEntity)));
        userResponse.setResponse("Login success");

        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(userResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody UserRequest userRequest) throws UserExistsException {
        UserEntity userEntity = authService.registerUser(userRequest);

        if(userEntity == null)
            throw new UserExistsException();

        UserResponse userResponse = modelMapper.map(userEntity, UserResponse.class);
        userResponse.setToken(jwtService.generateJwtToken(new MyUserDetails(userEntity)));
        userResponse.setResponse("register success");

        return ResponseEntity
                .status(HttpStatus.CREATED.value())
                .body(userResponse);
    }
}
