package net.kosa.mentopingserver;

import net.kosa.mentopingserver.domain.login.CustomOAuth2User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping("/")
    public String home() {
        return "Welcome to Metain!";
    }

    @GetMapping("/health")
    public String healthCheck() {
        return "Server is up and running!";
    }

    @GetMapping("/version")
    public String version() {
        return "Metain version 1.0.0";
    }

    @GetMapping("/user-info")
    public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        if (customOAuth2User != null) {
            return ResponseEntity.ok(customOAuth2User);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
    }
}