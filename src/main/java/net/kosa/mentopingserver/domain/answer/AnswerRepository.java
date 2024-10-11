package net.kosa.mentopingserver.domain.answer;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    boolean existsByPostIdAndIsSelectedTrue(Long postId);
}
