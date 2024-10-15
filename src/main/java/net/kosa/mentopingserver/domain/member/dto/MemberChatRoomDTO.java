package net.kosa.mentopingserver.domain.member.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberChatRoomDTO {
    private Long id;
    private Long memberId;
    private String chatRoomId;
    private String chatRoomName;
    private Long otherParticipantId;
    private String createdAt;
    private String updatedAt;

}
