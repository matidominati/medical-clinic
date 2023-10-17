package com.github.matidominati.medicalclinic.repository;

import com.github.matidominati.medicalclinic.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);

}
