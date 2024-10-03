package net.kosa.mentopingserver.domain.member;

import java.util.List;
import java.util.Optional;

public interface MemberService {
    List<Member> getAllMembers();
    Optional<Member> getMemberByEmail(String email);
}
