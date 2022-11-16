package com.bitsco.vks.sso.security;

import com.bitsco.vks.common.response.Response;
import com.bitsco.vks.common.response.Token;
import com.bitsco.vks.sso.cache.CacheService;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(TokenProvider.class);
    @Autowired
    CacheService cacheService;
    @Value("${app.auth.tokenExpirationMsec}")
    private long tokenExpirationMsec;
    @Value("${app.auth.tokenSecret}")
    private String tokenSecret;

    public String createTokenOauth2(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + tokenExpirationMsec);

        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, tokenSecret)
                .compact();
    }

    public Token createToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Date createdAt = new Date();
        Date expiredAt = new Date(createdAt.getTime() + tokenExpirationMsec);
        Token token = new Token(userPrincipal.getUsername(),
                Jwts.builder()
                        .setSubject(userPrincipal.getUsername())
                        .setIssuedAt(new Date())
                        .setExpiration(expiredAt)
                        .signWith(SignatureAlgorithm.HS512, tokenSecret)
                        .compact(),
                createdAt,
                expiredAt);
        cacheService.addToken2RedisCache(token);
        return token;
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(tokenSecret)
                .parseClaimsJws(token)
                .getBody().getSubject();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(tokenSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
        }
        return false;
    }

    public Response checkToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(tokenSecret).parseClaimsJws(authToken);
            return Response.SUCCESS;
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature", ex);
            return Response.INVALID_JWT_SIGNATURE;
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token", ex);
            return Response.INVALID_JWT_TOKEN;
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token", ex);
            return Response.EXPIRED_JWT_TOKEN;
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token", ex);
            return Response.UNSUPPORTED_JWT_TOKEN;
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.", ex);
            return Response.MISSING_PARAM;
        }
    }

}
