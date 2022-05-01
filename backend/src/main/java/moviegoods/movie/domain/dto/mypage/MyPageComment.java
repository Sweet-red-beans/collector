package moviegoods.movie.domain.dto.mypage;

import lombok.Data;

import java.time.LocalDateTime;


@Data
public class MyPageComment {
    Long  comment_id;
    String comment_content;
    LocalDateTime written_date ;
    String category;
    Long post_id;

    public MyPageComment(Long comment_id, String comment_content, LocalDateTime written_date,String category,Long post_id){
        this.comment_content=comment_content;
        this.comment_id=comment_id;
        this.written_date=written_date;
        this.category=category;
        this.post_id=post_id;
    }
}
