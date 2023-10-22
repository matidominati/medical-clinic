package com.github.matidominati.medicalclinic.repository;

import com.github.matidominati.medicalclinic.model.entity.UserData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserData,Long> {
    Optional<UserData> findByEmail(String email);
    Optional<UserData> findByUsername(String username);

}
