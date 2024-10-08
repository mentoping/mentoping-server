package net.kosa.mentopingserver.domain.login;

import net.kosa.mentopingserver.domain.member.Member;
import net.kosa.mentopingserver.domain.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginServiceImpl implements LoginService{

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public Member login(String email, String password) {
        // 이메일로 사용자 검색
        Optional<Member> loggedInMember = memberRepository.findByEmail(email);

        if (loggedInMember.isPresent()) {
            // 로그인 성공 처리

            Member member = loggedInMember.get();
            return member;
            // ...
        }
        // 사용자가 없거나 비밀번호가 일치하지 않으면 null 반환
        return null;
    }
}
