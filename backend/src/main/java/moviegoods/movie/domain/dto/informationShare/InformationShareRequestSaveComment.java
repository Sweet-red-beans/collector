package moviegoods.movie.domain.dto.informationShare;


import lombok.Data;

@Data
public class InformationShareRequestSaveComment {
    Long post_id;
    Long user_id;
    String content;
}
