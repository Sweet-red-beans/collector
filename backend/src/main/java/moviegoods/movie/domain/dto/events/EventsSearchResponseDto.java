package moviegoods.movie.domain.dto.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventsSearchResponseDto {
    private String cinema_name;
    private Long event_id;
    private String thumbnail_url;
    private String title;
    private Date start_date;
    private Date end_date;
    private Boolean is_like;
    private Boolean is_end;


}
