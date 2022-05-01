package moviegoods.movie.domain.dto.informationShare;


import lombok.Data;

@Data
public class InformationShareRequestDeleteComment {

    private Long user_id;
    private Long comment_id;
}
