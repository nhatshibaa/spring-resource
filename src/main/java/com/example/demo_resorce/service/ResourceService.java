package com.example.demo_resorce.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo_resorce.entity.Resource;
import com.example.demo_resorce.repository.ResourceServerRepository;
import com.example.demo_resorce.util.JwtUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class ResourceService {

    @Autowired
    private ResourceServerRepository resourceServerRepository;

    public Optional<Collection<Resource>> getResource(String token) {
        String tokenDecoded = token.replace("Bearer", "").trim();
        try {
//        log.info("tokenDecoded la " + tokenDecoded);
            DecodedJWT decodedJWT = JwtUtil.getDecodedJwt(tokenDecoded);
            log.info("token DecodedJWT la " + decodedJWT);

//            String scope = getScope(decodedJWT);
//        log.info("roles la " + decodedJWT.getClaim(JwtUtil.ROLE_CLAIM_KEY).asString());
            List<Resource> resourceList = resourceServerRepository.findByScope(decodedJWT.getClaim(JwtUtil.ROLE_CLAIM_KEY).asString());
            if (resourceList != null) {
                if (checkTokenIsExpire(decodedJWT)) {
                    return Optional.empty();
                }
            }
            return Optional.ofNullable(resourceList);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public String getScope(String token) {
        String tokenDecoded = token.replace("Bearer", "").trim();
        DecodedJWT decodedJWT = JwtUtil.getDecodedJwt(tokenDecoded);
        return decodedJWT.getClaim(JwtUtil.ROLE_CLAIM_KEY).asString();
    }

    public boolean checkTokenIsExpire(DecodedJWT decodedJWT) {
        Date date = new Date();

        log.info("thoi gian het han " + decodedJWT.getExpiresAt().getTime());
        if (date.getTime() > decodedJWT.getExpiresAt().getTime()) {
            log.info("so sanh hom nay " + date.getTime() + " > thoi gian het han " + decodedJWT.getExpiresAt().getTime());
            return true;
        }
        if (date.getTime() < decodedJWT.getExpiresAt().getTime()) {
            log.info("so sanh hom nay " + date.getTime() + " < thoi gian het han " + decodedJWT.getExpiresAt().getTime());
            return false;
        }
        return true;
    }
}
