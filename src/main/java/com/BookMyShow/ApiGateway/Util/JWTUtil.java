package com.BookMyShow.ApiGateway.Util;

import io.jsonwebtoken.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
public class JWTUtil {

    public PublicKey getPublicKey(String filename) throws Exception {
        ClassPathResource resource = new ClassPathResource(filename);
        InputStream inputStream = resource.getInputStream();

        byte[] keyBytes = inputStream.readAllBytes();
        String publicKeyPEM = new String(keyBytes);
        publicKeyPEM = publicKeyPEM.replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", ""); // Remove all whitespaces
        byte[] decoded = Base64.getDecoder().decode(publicKeyPEM);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

    public  Claims parseJwt(String jwtToken, PublicKey PublicKey) {
        // You need to pass the public/private key depending on your JWT signing mechanism
        try {
        return Jwts.parser()
                .verifyWith(PublicKey)
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload();
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("Token is expired", e);
        } catch (UnsupportedJwtException e) {
            throw new RuntimeException("Unsupported JWT", e);
        } catch (MalformedJwtException e) {
            throw new RuntimeException("Malformed JWT", e);
        } catch (SignatureException e) {
            throw new RuntimeException("Invalid signature", e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Token is null or empty", e);
        }
    }
}
