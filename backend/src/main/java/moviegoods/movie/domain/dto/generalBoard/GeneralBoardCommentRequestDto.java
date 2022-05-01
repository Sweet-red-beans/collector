package moviegoods.movie.domain.dto.generalBoard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeneralBoardCommentRequestDto {
    String content;
    Long post_id;

}
