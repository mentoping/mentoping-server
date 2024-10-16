package net.kosa.mentopingserver.domain.mentor;

import net.kosa.mentopingserver.domain.mentor.dto.MentorRequestDto;
import net.kosa.mentopingserver.domain.mentor.dto.MentorResponseDto;

import java.util.List;

public interface MentorService {

    // 멘토 생성
    MentorResponseDto createMentor(MentorRequestDto mentorRequestDto);

    // 멘토 조회 (ID로 조회)
    MentorResponseDto getMentorById(Long id);

    // 모든 멘토 목록 조회
    List<MentorResponseDto> getAllMentors();

    // 멘토 정보 업데이트
    MentorResponseDto updateMentor(Long id, MentorRequestDto mentorRequestDto);

    // 멘토 삭제
    void deleteMentor(Long id);

    // 멤버 ID 로 멘토 검색
    MentorResponseDto getMentorByMemberId (Long memberId);
}
