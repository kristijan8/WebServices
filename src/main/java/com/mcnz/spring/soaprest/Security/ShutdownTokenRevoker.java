package com.mcnz.spring.soaprest.Security;


import com.mcnz.spring.soaprest.Repositories.UserRepository;
import com.mcnz.spring.soaprest.Services.RevokedTokenService;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

@Component
public class ShutdownTokenRevoker {
    private final RevokedTokenService blacklistService;
    private final UserRepository userRepository;

    public ShutdownTokenRevoker(RevokedTokenService blacklistService,
                                UserRepository userRepository) {
        this.blacklistService = blacklistService;
        this.userRepository   = userRepository;
    }

    /**
     * This method runs when Spring is shutting down.
     * We read each user’s lastJti (if any) and revoke it.
     */
    @PreDestroy
    public void revokeAllActiveTokens() {
        userRepository.findAll().stream()
                .map(user -> user.getLastJti())
                .filter(lastJti -> lastJti != null && !lastJti.isBlank())
                .forEach(jti -> {
                    blacklistService.revoke(jti);
                });
    }

}
