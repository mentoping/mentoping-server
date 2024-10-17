package net.kosa.mentopingserver.domain.post.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import net.kosa.mentopingserver.domain.member.MemberRepository;
import net.kosa.mentopingserver.domain.member.entity.Member;
import net.kosa.mentopingserver.domain.post.dto.MentoringApplicationRequestDto;
import net.kosa.mentopingserver.domain.post.dto.MentoringApplicationResponseDto;
import net.kosa.mentopingserver.domain.post.entity.MentoringApplication;
import net.kosa.mentopingserver.domain.post.entity.Post;
import net.kosa.mentopingserver.domain.post.repository.MentoringApplicationRepository;
import net.kosa.mentopingserver.domain.post.repository.PostRepository;
import net.kosa.mentopingserver.global.common.enums.PostType;
import net.kosa.mentopingserver.global.common.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MentoringApplicationServiceImpl implements MentoringApplicationService {

    private final MentoringApplicationRepository mentoringApplicationRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public MentoringApplicationResponseDto applyForMentoring(Long mentoringId, MentoringApplicationRequestDto requestDto, Long memberId) {
        Post post = postRepository.findById(mentoringId)
                .orElseThrow(() -> new EntityNotFoundException("Mentoring post not found"));

        if (post.getPostType() != PostType.MENTORING) {
            throw new IllegalArgumentException("This post is not a mentoring post");
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));

        Optional<MentoringApplication> existingApplication = mentoringApplicationRepository
                .findByPostAndMemberAndStatus(post, member, Status.PENDING);

        if (existingApplication.isPresent()) {
            throw new IllegalStateException("You already have a pending application for this mentoring post");
        }

        MentoringApplication application = MentoringApplication.builder()
                .post(post)
                .member(member)
                .contact(requestDto.getContact())
                .email(requestDto.getEmail())
                .content(requestDto.getContent())
                .status(Status.PENDING)
                .build();

        MentoringApplication savedApplication = mentoringApplicationRepository.save(application);
        return convertToDto(savedApplication);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MentoringApplicationResponseDto> getApplicationsForMentoring(Long mentoringId, PageRequest pageRequest) {
        Page<MentoringApplication> applications = mentoringApplicationRepository.findByPostId(mentoringId, pageRequest);
        return applications.map(this::convertToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public MentoringApplicationResponseDto getApplicationById(Long mentoringId, Long applicationId) {
        MentoringApplication application = mentoringApplicationRepository.findByIdAndPostId(applicationId, mentoringId)
                .orElseThrow(() -> new EntityNotFoundException("Application not found"));
        return convertToDto(application);
    }

    @Override
    @Transactional
    public MentoringApplicationResponseDto updateApplication(Long mentoringId, Long applicationId, MentoringApplicationRequestDto requestDto) {
        MentoringApplication application = mentoringApplicationRepository.findByIdAndPostId(applicationId, mentoringId)
                .orElseThrow(() -> new EntityNotFoundException("Application not found"));

        MentoringApplication updatedApplication = application.toBuilder()
                .contact(requestDto.getContact())
                .email(requestDto.getEmail())
                .content(requestDto.getContent())
                .build();

        MentoringApplication savedApplication = mentoringApplicationRepository.save(updatedApplication);
        return convertToDto(savedApplication);
    }

    @Override
    @Transactional
    public MentoringApplicationResponseDto updateApplicationStatus(Long applicationId, Status newStatus) {
        MentoringApplication application = mentoringApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new EntityNotFoundException("Mentoring application not found"));

        if (application.getStatus() != Status.PENDING) {
            throw new IllegalStateException("Can only update status of pending applications");
        }

        if (newStatus == Status.PENDING) {
            throw new IllegalArgumentException("Cannot set status back to PENDING");
        }

        application = application.toBuilder()
                .status(newStatus)
                .build();

        MentoringApplication updatedApplication = mentoringApplicationRepository.save(application);
        return convertToDto(updatedApplication);
    }

    @Override
    @Transactional
    public void deleteApplication(Long mentoringId, Long applicationId) {
        MentoringApplication application = mentoringApplicationRepository.findByIdAndPostId(applicationId, mentoringId)
                .orElseThrow(() -> new EntityNotFoundException("Application not found"));
        mentoringApplicationRepository.delete(application);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MentoringApplicationResponseDto> getApplicationsByMemberId(Long memberId, PageRequest pageRequest) {
        Page<MentoringApplication> applications = mentoringApplicationRepository.findByMemberId(memberId, pageRequest);
        return applications.map(this::convertToDto);
    }

    @Override
    public boolean hasUserApplied(Long mentoringId, Long memberId) {
        return mentoringApplicationRepository.existsByPostIdAndMemberIdAndStatusWaiting(mentoringId, memberId);
    }

    private MentoringApplicationResponseDto convertToDto(MentoringApplication application) {
        return MentoringApplicationResponseDto.builder()
                .id(application.getId())
                .postId(application.getPost().getId())
                .postTitle(application.getPost().getTitle())
                .memberId(application.getMember().getId())
                .memberName(application.getMember().getName())
                .contact(application.getContact())
                .email(application.getEmail())
                .content(application.getContent())
                .status(application.getStatus())
                .createdAt(application.getCreatedAt())
                .updatedAt(application.getUpdatedAt())
                .build();
    }
}
