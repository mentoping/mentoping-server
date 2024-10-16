package net.kosa.mentopingserver.domain.mentor;

import lombok.RequiredArgsConstructor;
import net.kosa.mentopingserver.domain.answer.Answer;
import net.kosa.mentopingserver.domain.answer.dto.AnswerResponseDto;
import net.kosa.mentopingserver.domain.member.MemberRepository;
import net.kosa.mentopingserver.domain.member.entity.Member;
import net.kosa.mentopingserver.domain.mentor.dto.MentorRequestDto;
import net.kosa.mentopingserver.domain.mentor.dto.MentorResponseDto;
import net.kosa.mentopingserver.domain.mentor.entity.Mentor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MentorServiceImpl implements MentorService {

    private final MentorRepository mentorRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public MentorResponseDto createMentor(MentorRequestDto mentorRequestDto) {
        // 멤버 ID로 멤버 조회
        Member member = memberRepository.findById(mentorRequestDto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Member not found with id: " + mentorRequestDto.getMemberId()));

        Mentor mentor = Mentor.builder()
                .contact(mentorRequestDto.getContact())
                .content(mentorRequestDto.getIntroduction())
                .exp(mentorRequestDto.getExpertise())
                .category(mentorRequestDto.getCategory())
                .sns(mentorRequestDto.getSns())
                .mentorRank(mentorRequestDto.getMentorRank()) // MentorRank 설정
                .member(member)  // member 설정
                .build();

        Mentor savedMentor = mentorRepository.save(mentor);

        return convertToResponseDto(savedMentor);
    }

    @Override
    @Transactional(readOnly = true)
    public MentorResponseDto getMentorById(Long id) {
        Mentor mentor = mentorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Mentor not found with id: " + id));
        return convertToResponseDto(mentor);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MentorResponseDto> getAllMentors() {
        return mentorRepository.findAll().stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public MentorResponseDto updateMentor(Long id, MentorRequestDto mentorRequestDto) {
        Mentor mentor = mentorRepository.findByMemberId(id)
                .orElseThrow(() -> new IllegalArgumentException("Mentor not found with id: " + id));
        if (mentor == null) {
            throw new IllegalArgumentException("Mentor not found with memberId: " + id);
        }

        mentor = mentor.toBuilder()
                .contact(mentorRequestDto.getContact())
                .content(mentorRequestDto.getIntroduction())
                .exp(mentorRequestDto.getExpertise())
                .mentorRank(mentorRequestDto.getMentorRank()) // MentorRank 업데이트
                .build();

        Mentor updatedMentor = mentorRepository.save(mentor);

        return convertToResponseDto(updatedMentor);
    }

    @Override
    @Transactional
    public void deleteMentor(Long id) {
        Mentor mentor = mentorRepository.findByMemberId(id)
                .orElseThrow(() -> new IllegalArgumentException("Mentor not found with id: " + id));

        mentorRepository.delete(mentor);
    }

    @Override
    @Transactional(readOnly = true) // 데이터를 조회하는 경우에는 readOnly = true로 설정
    public MentorResponseDto getMentorByMemberId(Long memberId) {
        Mentor mentor = mentorRepository.findByMemberId(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Mentor not found with id: " + memberId));

        return convertToResponseDto(mentor);
    }



    private MentorResponseDto convertToResponseDto(Mentor mentor) {
        // AnswerResponseDto 변환
//        List<AnswerResponseDto> answers = mentor.getAnswers().stream()
//                .map(this::convertToAnswerResponseDto)
//                .collect(Collectors.toList());
//
//        List<AnswerResponseDto> selectedAnswers = mentor.getSelectedAnswers().stream()
//                .map(this::convertToAnswerResponseDto)
//                .collect(Collectors.toList());

        return MentorResponseDto.builder()
                .name(mentor.getMember().getName())  // member가 null이 아니므로 안전하게 호출 가능
                .introduction(mentor.getContent())
                .profileImage(null)  // 프로필 이미지 처리 필요
                .expertise(mentor.getExp())
                .category(mentor.getCategory())  // Category 필드 추가
                .contact(mentor.getContact())
                .sns(mentor.getSns())
                .mentorRank(mentor.getMentorRank())
//                .answer(answers)
//                .answerSelected(selectedAnswers)
                .build();
    }

//    // Answer -> AnswerResponseDto 변환 로직
//    private AnswerResponseDto convertToAnswerResponseDto(Answer answer) {
//        return AnswerResponseDto.builder()
//                .id(answer.getId())
//                .content(answer.getContent())
//                .createdAt(answer.getCreatedAt().toString())
//                .build();
//    }
}
