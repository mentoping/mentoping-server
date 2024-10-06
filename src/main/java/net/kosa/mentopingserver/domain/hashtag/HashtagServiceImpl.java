package net.kosa.mentopingserver.domain.hashtag;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HashtagServiceImpl implements HashtagService {

    private final HashtagRepository hashtagRepository;

    @Override
    @Transactional
    public Hashtag save(String hashtagStr) {
        Optional<Hashtag> optHashtag = hashtagRepository.findByName(hashtagStr);

        if(optHashtag.isPresent()) {
            return optHashtag.get();
        }

        Hashtag hashtag = Hashtag.builder()
                .name(hashtagStr)
                .build();

        return hashtagRepository.save(hashtag);
    }
}
