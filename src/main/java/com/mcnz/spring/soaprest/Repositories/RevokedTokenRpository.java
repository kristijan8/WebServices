package com.mcnz.spring.soaprest.Repositories;

import com.mcnz.spring.soaprest.Models.RevokedToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;

public interface RevokedTokenRpository extends JpaRepository<RevokedToken, String> {
    public void deleteAllByRevokedAtBefore(Instant cutoff);
}
