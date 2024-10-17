package net.kosa.mentopingserver.domain.Inquiry;

import lombok.RequiredArgsConstructor;
import net.kosa.mentopingserver.domain.Inquiry.dto.InquiryRequestDto;
import net.kosa.mentopingserver.domain.Inquiry.dto.InquiryResponseDto;
import net.kosa.mentopingserver.domain.Inquiry.dto.UserInquiryRequestDto;
import net.kosa.mentopingserver.domain.member.MemberRepository;
import net.kosa.mentopingserver.domain.member.entity.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InquiryServiceImpl implements InquiryService {

    private final InquiryRepository inquiryRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public InquiryResponseDto createInquiry(UserInquiryRequestDto inquiryRequestDto) {
        Member member = memberRepository.findById(Long.parseLong(inquiryRequestDto.getUserId()))
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));

        Inquiry inquiry = Inquiry.builder()
                .member(member)
                .title(inquiryRequestDto.getSubject())
                .inquiryContent(inquiryRequestDto.getInquiryContent())
                .build();

        Inquiry savedInquiry = inquiryRepository.save(inquiry);
        return mapToResponseDto(savedInquiry);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InquiryResponseDto> getAllInquiries() {
        return inquiryRepository.findAll().stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public InquiryResponseDto getInquiryById(Long id) {
        Inquiry inquiry = inquiryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Inquiry not found"));
        return mapToResponseDto(inquiry);
    }

    @Override
    @Transactional
    public void deleteInquiry(Long id) {
        Inquiry inquiry = inquiryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Inquiry not found"));
        inquiryRepository.delete(inquiry);
    }

    @Override
    @Transactional
    public InquiryResponseDto updateInquiryAnswer(Long id, Map<String, String> answerContentMap) {
        String answerContent = answerContentMap.get("answerContent");

        Inquiry inquiry = inquiryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Inquiry not found"));

        inquiry = inquiry.toBuilder().answerContent(answerContent).build();

        Inquiry updatedInquiry = inquiryRepository.save(inquiry);
        return mapToResponseDto(updatedInquiry);
    }

    private InquiryResponseDto mapToResponseDto(Inquiry inquiry) {
        Member member = memberRepository.findById(inquiry.getMember().getId())
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        return InquiryResponseDto.builder()
                .id(inquiry.getId())
                .userId(String.valueOf(inquiry.getMember().getId()))
                .userName(member.getName()) // userName 추가
                .subject(inquiry.getTitle())
                .inquiryContent(inquiry.getInquiryContent())
                .answerContent(inquiry.getAnswerContent())
                .createdAt(inquiry.getCreatedAt())
                .build();
    }
}
