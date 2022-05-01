package moviegoods.movie.domain.dto.mypage;


import lombok.Data;

import java.util.Date;

@Data
public class MyPageLikeEvent {

    // 좋아요한 이벤트
    private Long event_id;
    private String title;
    private String thumbnail_url;
    private Date end_date;
    private Date start_date;

    public MyPageLikeEvent(Long event_id, String title, String thumbnail_url,Date start_date,Date end_date){
        this.event_id=event_id;
        this.title=title;
        this.thumbnail_url=thumbnail_url;
        this.start_date=start_date;
        this.end_date=end_date;
    }
}