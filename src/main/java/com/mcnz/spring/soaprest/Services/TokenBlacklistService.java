package com.mcnz.spring.soaprest.Services;

import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenBlacklistService {

    private final Set<String> blacklisted = ConcurrentHashMap.newKeySet();

    /** Mark this token ID as revoked */
    public void revoke(String jti) {
        blacklisted.add(jti);
    }

    /** True if this token ID has been revoked */
    public boolean isRevoked(String jti) {
        return blacklisted.contains(jti);
    }

}
