package net.kosa.mentopingserver.global.jwt.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.Setter;
import net.kosa.mentopingserver.global.common.enums.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTUtil {

    private final SecretKey secretKey;
    private static final long TOKEN_VALIDITY = 60 * 60 * 60 * 1000L; // 60시간 (밀리초 단위)

    public JWTUtil(@Value("${spring.jwt.secret}") String secret) {
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public String getOauthId(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("oauthId", String.class);
    }

    public String getRole(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }

    public Boolean isExpired(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

    public String createJwt(String oauthId, Role role, Long expiredMs) {
        return Jwts.builder()
                .claim("oauthId", oauthId)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }

    public String refreshToken(String expiredToken) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(expiredToken)
                    .getPayload();

            String oauthId = claims.get("oauthId", String.class);
            String roleString = claims.get("role", String.class);

            Role role;
            try {
                role = Role.valueOf(roleString.toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid role: " + roleString);
                return null;
            }

            // TOKEN_VALIDITY를 사용하여 새 토큰 생성
            return createJwt(oauthId, role, TOKEN_VALIDITY);
        } catch (Exception e) {
            return null;
        }
    }

}