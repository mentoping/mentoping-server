package net.kosa.mentopingserver.domain.member.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoomCreationRequest {
    private Long senderId;
    private Long receiverId;
    private String chatRoomName;
    private String firebaseChatRoomId;
}
