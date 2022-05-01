package moviegoods.movie.domain.dto.informationShare;



import lombok.Data;

import java.time.LocalDateTime;


@Data
public class InformationShareDetailDTO {
    Long user_id;
    String nickname;
    Long views;
    String title;
    String image_url;

    LocalDateTime written_date;
    String content;
    String name;
    String area;
    String branch;

    public InformationShareDetailDTO(Long user_id, String nickname, Long views, String title, String image_url,
                                     LocalDateTime written_date, String content, String name, String area, String branch){
        this.user_id=user_id;
        this.nickname=nickname;
        this.views=views;
        this.title=title;
        this.image_url=image_url;
        this.written_date=written_date;
        this.content=content;
        this.name=name;
        this.area=area;
        this.branch=branch;
    }


}
