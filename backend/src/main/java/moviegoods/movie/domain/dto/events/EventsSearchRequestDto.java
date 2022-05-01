package moviegoods.movie.domain.dto.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventsSearchRequestDto {
    private String cinema_name;
    private String search_word;
    private String sort_criteria;
    private Boolean is_end;
    private Long user_id;
}
