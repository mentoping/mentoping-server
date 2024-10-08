package net.kosa.mentopingserver.domain.login;


import net.kosa.mentopingserver.domain.member.Member;
import net.kosa.mentopingserver.domain.member.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/login")
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
