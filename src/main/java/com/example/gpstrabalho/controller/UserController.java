package com.example.gpstrabalho.controller;


import com.example.gpstrabalho.dto.LoginDto;
import com.example.gpstrabalho.dto.UserDto;
import com.example.gpstrabalho.entity.ImovelEntity;
import com.example.gpstrabalho.entity.UserEntity;
import com.example.gpstrabalho.repository.ImovelRepository;
import com.example.gpstrabalho.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("ap1/v1/user")
public class UserController {

    private final UserRepository userRepository;
    private final ImovelRepository imovelRepository;
    private final ModelMapper modelMapper;

    @PostMapping("/create")
    public ResponseEntity<UserEntity> createUser(@RequestBody UserDto userDto) {
        try {
            final var userEntity = modelMapper.map(userDto, UserEntity.class);
            final var save = userRepository.save(userEntity);
            return ResponseEntity.ok().body(save);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<UserEntity> login(@RequestBody LoginDto loginDto) {
        try {
            final var user = userRepository.findByEmailAndPassword(loginDto.getEmail(), loginDto.getPassword());
            return user.map(userEntity -> ResponseEntity.status(HttpStatus.OK).body(userEntity)).orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/findUserByEmail")
    public ResponseEntity<UserEntity> findUserByEmail(String email) {
        try {
            final var user = userRepository.findByEmail(email);
            return user.map(userEntity -> ResponseEntity.status(HttpStatus.OK).body(userEntity)).orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/findAllImoveis")
    public ResponseEntity<?> findAllImoveis(@RequestBody UserDto userDto) {
        final var user = userRepository.findByEmailAndPassword(userDto.getEmail(), userDto.getPassword());

        if (user.isPresent()) {
            final var all = imovelRepository.findAllByApplicationUser(user.get());
            return ResponseEntity.status(HttpStatus.OK).body(all);
        } else {
            final var message = Map.of("message", "User with ID {} not found.");
            return ResponseEntity.status(HttpStatus.OK).body(message);
        }
    }

}
