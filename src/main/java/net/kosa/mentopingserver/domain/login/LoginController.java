package net.kosa.mentopingserver.domain.login;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.kosa.mentopingserver.domain.member.entity.Member;
import net.kosa.mentopingserver.global.config.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {

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
@Setter
@Getter
class LoginRequest {
    // Getters and Setters
    private String email;
    private String password;

}
