package moviegoods.movie.domain.dto.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventsDetailRequestDto {
    private Long user_id;
    private Long event_id;
}
