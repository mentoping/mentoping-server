package net.kosa.mentopingserver.global.jwt.util;

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

    @Setter
    private long clockSkew = 3000; // 3초의 스큐 허용

    public JWTUtil(@Value("${secret-key}") String secret) {

        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public String getOauthId(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("oauthId", String.class);
    }

    public String getRole(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }

    public String getTokenType(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("tokenType", String.class);
    }

    public Boolean isExpired(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

    public String createJwt(String oauthId, Role role, Long expiredMs) {

        return Jwts.builder()
                .claim("oauthId", oauthId)
                .claim("role", role)
                .claim("tokenType", "access")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }

    public String createRefreshToken(String oauthId, Long expiredMs) {
        return Jwts.builder()
                .claim("oauthId", oauthId)
                .claim("tokenType", "refresh")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }

    // 새로운 메소드: 리프레시 토큰을 사용하여 새 액세스 토큰 생성
    public String refreshAccessToken(String refreshToken) {
        if (isExpired(refreshToken)) {
            throw new RuntimeException("Refresh token is expired");
        }

        String oauthId = getOauthId(refreshToken);
        Role role = Role.valueOf(getRole(refreshToken));

        // 새 액세스 토큰 생성 (예: 1시간 유효)
        return createJwt(oauthId, role, 60 * 60 * 1000L);
    }
}