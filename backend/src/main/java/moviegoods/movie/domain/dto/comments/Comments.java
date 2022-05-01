package moviegoods.movie.domain.dto.comments;

import lombok.AllArgsConstructor;
import lombok.Data;
import moviegoods.movie.domain.entity.User.UserStatus;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
public class Comments {
    private Long comment_id;
    private Long user_id;
    private String comment_nickname;
    private String comment_content;
    private Boolean is_mine;
    private LocalDateTime comment_written_date;
    private UserStatus user_status;

    public Comments(Long user_id, String comment_nickname, String comment_content, LocalDateTime comment_written_date, Long comment_id, UserStatus status){
        this.comment_id=comment_id;
        this.user_id=user_id;
        this.comment_nickname=comment_nickname;
        this.comment_content=comment_content;
        this.comment_written_date=comment_written_date;
        this.user_status=status;

    }
}