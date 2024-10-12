package net.kosa.mentopingserver.domain.post.service;

import lombok.RequiredArgsConstructor;
import net.kosa.mentopingserver.domain.post.repository.MentoringReviewRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MentoringReviewServiceImpl implements MentoringReviewService{

    private final MentoringReviewRepository mentoringReviewRepository;
}
