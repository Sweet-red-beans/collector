package moviegoods.movie.domain.dto.informationShare;


import lombok.Data;
import moviegoods.movie.domain.dto.comments.Comments;
import moviegoods.movie.domain.entity.User.UserStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class InformationShareResponseDetail {

    private long post_id;
    private String title;
    private LocalDateTime written_date;
    private String content;
    private Long views;
    private String nickname;
    private String image_url ;
    private String cinema_name;
    private String cinema_area;
    private String cinema_branch;
    private Boolean is_mine;
    private UserStatus user_status;
    List<Comments> comment=new ArrayList<>(); //댓글리스트

    /*
    private Long user_id;
    private String comment_nickname;
    private String comment_content;
    private LocalDateTime comment_written_date;
    */



}