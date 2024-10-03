package net.kosa.mentopingserver.domain.login;

import net.kosa.mentopingserver.domain.member.Member;
import net.kosa.mentopingserver.domain.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService{

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public Member login(String email, String password) {
        // 이메일로 사용자 검색
        Member member = memberRepository.findByEmail(email);

        // 사용자가 존재하고 비밀번호가 일치하면 사용자 객체 반환
        if (member != null && member.getPassword().equals(password)) {
            return member;
        }
        // 사용자가 없거나 비밀번호가 일치하지 않으면 null 반환
        return null;
    }
}
