package com.example.gpstrabalho.repository;

import com.example.gpstrabalho.entity.ImovelEntity;
import com.example.gpstrabalho.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImovelRepository extends JpaRepository<ImovelEntity, Integer> {
    List<ImovelEntity> findAllByApplicationUser(UserEntity userEntity);
}
