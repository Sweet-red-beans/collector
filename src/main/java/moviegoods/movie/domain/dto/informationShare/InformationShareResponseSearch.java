package moviegoods.movie.domain.dto.informationShare;

import lombok.Data;

import java.time.LocalDateTime;


@Data
public class InformationShareResponseSearch {

    private Long post_id;   //post
    private String title;  //post
    private String nickname; //user
    private LocalDateTime written_date;  //content-detail
    private Long view;  //post

}
