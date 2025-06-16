package com.mcnz.spring.soaprest.Models;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "revoked_tokens")
public class RevokedToken {
    @Id
    @Column(length = 36)            // UUID length
    private String jti;

    @Column(name = "revoked_at", nullable = false)
    private Instant revokedAt = Instant.now();

    public RevokedToken(String jti) {
        this.jti = jti;
    }

}
