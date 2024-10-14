package net.kosa.mentopingserver.domain.member;

import net.kosa.mentopingserver.domain.member.entity.Member;
import net.kosa.mentopingserver.global.common.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    Optional<Member> findByNickname(String nickname);

    Optional<Member> findByEmailAndNickname(String email, String nickname);

    Optional<Member> findByOauthId(String oauthId);

    long countByRole(Role role);


}
