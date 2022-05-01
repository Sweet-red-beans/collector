package moviegoods.movie.domain.dto.main;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MainDailyCommunityResponseDto {
    private Long post_id;
    private String title;
    private String content;
    private Long views;
    private Long comments_num;
    private LocalDateTime written_date;

}
