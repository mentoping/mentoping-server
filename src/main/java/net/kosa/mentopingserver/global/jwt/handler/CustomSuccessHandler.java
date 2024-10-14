package net.kosa.mentopingserver.global.jwt.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import net.kosa.mentopingserver.domain.login.CustomOAuth2User;
import net.kosa.mentopingserver.global.common.enums.Role;
import net.kosa.mentopingserver.global.jwt.util.JWTUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        //OAuth2User
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();
        System.out.println("handling");

        String oauthId = customUserDetails.getOauthId();
        Role role = extractRole(authentication);

        String accessToken = jwtUtil.createJwt(oauthId, role, 60 * 60L);
        String refreshToken = jwtUtil.createRefreshToken(oauthId, 14 * 24 * 60 * 60L);

        response.addCookie(createCookie("Authorization", accessToken, 60 * 60));
        response.addCookie(createCookie("Refresh-Token", refreshToken, 14 * 24 * 60 * 60));

        response.sendRedirect("http://localhost:3000/");
    }

    private Role extractRole(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .map(Role::valueOf)
                .orElseThrow(() -> new IllegalStateException("No authority found"));
    }

    private Cookie createCookie(String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        //cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        return cookie;
    }

}
