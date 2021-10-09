package com.example.gpstrabalho.controller;


import com.example.gpstrabalho.dto.ImovelDto;
import com.example.gpstrabalho.dto.UpdateImovelDto;
import com.example.gpstrabalho.entity.ImovelEntity;
import com.example.gpstrabalho.repository.ImovelRepository;
import com.example.gpstrabalho.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("ap1/v1/imovel")
public class ImovelController {

    private final ImovelRepository imovelRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @PostMapping("/create")
    public ResponseEntity<?> createImovel(@RequestBody ImovelDto imovelDto) {
        try {
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            var imovelEntity = modelMapper.map(imovelDto, ImovelEntity.class);

            final var userID = imovelDto.getApplicationUserId();
            final var userEntity = userRepository.findById(userID);

            if (userEntity.isEmpty()) {
                final var message = Map.of("Message", "Unable to find user with ID " + imovelDto.getApplicationUserId());
                return ResponseEntity.badRequest().body(message);
            }

            imovelEntity.setApplicationUser(userEntity.get());
            final var save = imovelRepository.save(imovelEntity);
            return ResponseEntity.ok().body(save);

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/findall")
    public ResponseEntity<List<ImovelEntity>> findAll() {
        try {
            final var collect = new ArrayList<>(imovelRepository.findAll());
            return ResponseEntity.status(HttpStatus.OK).body(collect);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody UpdateImovelDto updateImovelDto) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.typeMap(UpdateImovelDto.class, ImovelEntity.class)
                .addMapping(UpdateImovelDto::getApplicationUserId, (d, v) -> d.getApplicationUser().setId((Integer) v));
        try {

            final var imovelId = updateImovelDto.getId();
            final var byId = imovelRepository.findById(imovelId);
            if (byId.isEmpty()) {
                final var message = Map.of("Message", "Unable to find imovel with ID " + updateImovelDto.getId());
                return ResponseEntity.badRequest().body(message);
            }

            var imovelEntity = modelMapper.map(updateImovelDto, ImovelEntity.class);
            final var save = imovelRepository.save(imovelEntity);
            return ResponseEntity.ok().body(save);

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.typeMap(UpdateImovelDto.class, ImovelEntity.class)
                .addMapping(UpdateImovelDto::getApplicationUserId, (d, v) -> d.getApplicationUser().setId((Integer) v));
        try {
            imovelRepository.deleteById(id);
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
