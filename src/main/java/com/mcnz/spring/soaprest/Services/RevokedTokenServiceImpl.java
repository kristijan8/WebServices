package com.mcnz.spring.soaprest.Services;

import com.mcnz.spring.soaprest.Models.RevokedToken;
import com.mcnz.spring.soaprest.Repositories.RevokedTokenRpository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RevokedTokenServiceImpl implements RevokedTokenService{
    private RevokedTokenRpository revokedTokenRpository;

    public RevokedTokenServiceImpl(RevokedTokenRpository revokedTokenRpository) {
        this.revokedTokenRpository = revokedTokenRpository;
    }

    @Override
    public void revoke(String jti) {
        if (!isRevoked(jti))
        {
            revokedTokenRpository.save(new RevokedToken(jti));
        }
    }

    @Override
    public boolean isRevoked(String jti) {
        return revokedTokenRpository.existsById(jti);
    }
}
