package com.mcnz.spring.soaprest.Controllers;

import com.mcnz.spring.soaprest.Dto.AuthResponseDTO;
import com.mcnz.spring.soaprest.Dto.LoginDto;
import com.mcnz.spring.soaprest.Dto.RegisterDto;
import com.mcnz.spring.soaprest.Models.Role;
import com.mcnz.spring.soaprest.Models.UserEntity;
import com.mcnz.spring.soaprest.Repositories.RoleRepository;
import com.mcnz.spring.soaprest.Repositories.UserRepository;
import com.mcnz.spring.soaprest.Security.JWTGenerator;
import com.mcnz.spring.soaprest.Services.TokenBlacklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/rest/auth")
public class AuthController {


    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JWTGenerator jwtGenerator;
    private TokenBlacklistService tokenBlacklistService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
                          UserRepository userRepository,
                          RoleRepository roleRepository,
                          PasswordEncoder passwordEncoder,
                          JWTGenerator jwtGenerator,
                          TokenBlacklistService tokenBlacklistService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator = jwtGenerator;
        this.tokenBlacklistService = tokenBlacklistService;
    }


    @PostMapping("login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtGenerator.generateToken(authentication);

        return new ResponseEntity<>(new AuthResponseDTO(token), HttpStatus.OK);
    }



    @PostMapping("register")
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto) {
        if (userRepository.existsByUsername(registerDto.getUsername())) {
            return ResponseEntity.badRequest().body("Username already exists");
        }
        UserEntity user = new UserEntity();
        user.setUsername(registerDto.getUsername());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        Role roles = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Role not found"));
        user.setRoles(Collections.singletonList(roles));

        userRepository.save(user);
        return new ResponseEntity<>("User registered successfully", HttpStatus.OK);

    }

    @PostMapping("logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String jti   = jwtGenerator.getJtiFromToken(token);
            tokenBlacklistService.revoke(jti);
        }
        return ResponseEntity.ok().build();
    }



}
