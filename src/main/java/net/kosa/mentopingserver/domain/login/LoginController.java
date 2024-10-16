package net.kosa.mentopingserver.domain.login;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import net.kosa.mentopingserver.domain.member.entity.Member;
import net.kosa.mentopingserver.global.config.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Member member = loginService.login(loginRequest.getEmail(), loginRequest.getPassword());

        if (member != null) {
            return ResponseEntity.ok(member); // 인증 성공 시 사용자 정보 반환
        } else {
            return ResponseEntity.status(401).body("Invalid email or password"); // 인증 실패 시 401 상태 반환
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        // 클라이언트의 쿠키에서 토큰 삭제
        Cookie cookie = new Cookie("Authorization", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok().body("Logged out successfully");
    }

    @GetMapping("/check-user")
    public ResponseEntity<?> checkCurrentUser(@CurrentUser(required = false) Long memberId) {
        if (memberId != null) {
            return ResponseEntity.ok("현재 로그인한 사용자의 ID: " + memberId);
        } else {
            return ResponseEntity.ok("로그인한 사용자가 없습니다.");
        }
    }
}



// 로그인 요청을 처리하기 위한 데이터 클래스
class LoginRequest {
    private String email;
    private String password;

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
