package moviegoods.movie.domain.dto.generalBoard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import moviegoods.movie.domain.dto.comments.Comments;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeneralBoardDetailResponseDto {
    private long post_id;
    private String title;
    private LocalDateTime written_date;
    private String content;
    private Long views;
    private String nickname;
    private String image_url ;
    private Boolean is_mine;

    List<Comments> comment=new ArrayList<>();

}
