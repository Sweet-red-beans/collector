package moviegoods.movie.domain.dto.informationShare;

import lombok.Data;

import java.time.LocalDateTime;


@Data
public class Comments {
    private Long comment_id;
    private Long user_id;
    private String comment_nickname;
    private String comment_content;
    private Boolean is_mine;
    private LocalDateTime comment_written_date;

    public Comments(Long user_id, String comment_nickname, String comment_content, LocalDateTime comment_written_date, Long comment_id){
        this.comment_id=comment_id;
        this.user_id=user_id;
        this.comment_nickname=comment_nickname;
        this.comment_content=comment_content;
        this.comment_written_date=comment_written_date;

    }
}