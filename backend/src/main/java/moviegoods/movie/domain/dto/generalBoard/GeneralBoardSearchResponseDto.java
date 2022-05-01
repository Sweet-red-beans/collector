package moviegoods.movie.domain.dto.generalBoard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GeneralBoardSearchResponseDto {
    private Long post_id;
    private String title;
    private String nickname;
    private LocalDateTime written_date;
    private Long view;
}
