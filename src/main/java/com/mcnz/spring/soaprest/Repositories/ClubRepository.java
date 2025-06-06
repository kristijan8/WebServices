package com.mcnz.spring.soaprest.Repositories;

import com.mcnz.spring.soaprest.Models.Club;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClubRepository extends JpaRepository<Club, Long> {
    Optional<Club> findByTitle(String title);
}
