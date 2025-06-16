package com.mcnz.spring.soaprest.Services;

public interface RevokedTokenService {
    public void revoke(String jti);
    public boolean isRevoked(String jti);
}
