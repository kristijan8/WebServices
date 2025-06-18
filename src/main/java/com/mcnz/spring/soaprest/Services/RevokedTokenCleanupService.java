package com.mcnz.spring.soaprest.Services;

import com.mcnz.spring.soaprest.Repositories.RevokedTokenRpository;
import com.mcnz.spring.soaprest.Security.SecurityConstants;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class RevokedTokenCleanupService {

    private final RevokedTokenRpository revokedTokenRepository;

    public RevokedTokenCleanupService(RevokedTokenRpository revokedTokenRepository) {
        this.revokedTokenRepository = revokedTokenRepository;
    }

    /**
     * Runs every 6 hours and deletes any revoked-token entries
     * older than the max JWT lifetime.
     */
    @Scheduled(fixedRateString = "${token.blacklist.cleanup-ms:21600000}")
    @Transactional
    public void purgeOldRevokedTokens() {
        Instant cutoff = Instant.now().minusMillis(SecurityConstants.JWT_Expiration);
        revokedTokenRepository.deleteAllByRevokedAtBefore(cutoff);
    }
}
