package net.kosa.mentopingserver.domain.member;


import lombok.RequiredArgsConstructor;
import net.kosa.mentopingserver.domain.member.dto.MemberDto;
import net.kosa.mentopingserver.domain.member.entity.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 모든 Member 정보 찾기
    @GetMapping
    public ResponseEntity<List<MemberDto>> getAllMembers() {
        List<MemberDto> members = memberService.getAllMembers();
        return ResponseEntity.ok(members);
    }

    // 특정 Member Id 로 찾기
    @PostMapping("/id")
    public ResponseEntity<MemberDto> getMemberById(@RequestBody Long id) {
        MemberDto member = memberService.getMemberById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 회원을 찾을 수 없습니다: " + id));
        return ResponseEntity.ok(member);
    }

    // 특정 Member 이메일로 찾기
    @PostMapping("/email")
    public ResponseEntity<MemberDto> getMemberByEmail(@RequestBody String email) {
        MemberDto member = memberService.getMemberByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일의 회원을 찾을 수 없습니다."));
        return ResponseEntity.ok(member);
    }



    // Member 생성
    @PostMapping
    public ResponseEntity<MemberDto> createMember(@RequestBody MemberDto memberDto) {
        MemberDto createdMember = memberService.createMember(memberDto);
        return ResponseEntity.ok(createdMember);
    }

    // Member 수정
    @PutMapping("/{id}")
    public ResponseEntity<MemberDto> updateMember(@PathVariable Long id, @RequestBody MemberDto memberDetails) {
        MemberDto updatedMember = memberService.updateMember(id, memberDetails);
        return ResponseEntity.ok(updatedMember);
    }

    // Member 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }
}