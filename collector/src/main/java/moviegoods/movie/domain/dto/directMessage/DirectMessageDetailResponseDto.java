package moviegoods.movie.domain.dto.directMessage;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DirectMessageDetailResponseDto {

    private String message_content;
    private LocalDateTime written_date;
    private String image_url;
    private Long user_id;
    private String nickname;
    private String profile_url;
    private Long reliability;
}
