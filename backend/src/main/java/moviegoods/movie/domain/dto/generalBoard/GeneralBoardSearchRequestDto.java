package moviegoods.movie.domain.dto.generalBoard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GeneralBoardSearchRequestDto {
    String search_word;
    String search_criteria;
}
