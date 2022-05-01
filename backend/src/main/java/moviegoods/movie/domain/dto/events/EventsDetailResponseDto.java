package moviegoods.movie.domain.dto.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import moviegoods.movie.domain.dto.mypage.MyPageLikeEvent;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventsDetailResponseDto {
    private Long event_id;
    private String cinema_name;
    private String title;
    private List<String> detail_image_url=new ArrayList<>();
    private String link_url;
    private Date start_date;
    private Date end_date;
    private Long like_count;
    private Boolean is_like;

}
