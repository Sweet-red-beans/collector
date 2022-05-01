package moviegoods.movie.domain.dto.directMessage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import moviegoods.movie.domain.entity.User.UserStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DirectMessageListResponseDto {

    private Long chat_room_id;

    private Long not_mine_id;
    private String not_mine_nickname;
    private String not_mine_profile_url;
    private Long not_mine_reliability;
    private UserStatus not_mine_user_status;

    private Long transaction_id;
    private Boolean is_complete;

    private String recent_message;
    private LocalDateTime recent_message_date;
}