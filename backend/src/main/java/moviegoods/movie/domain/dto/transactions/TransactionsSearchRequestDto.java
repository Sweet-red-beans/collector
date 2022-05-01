package moviegoods.movie.domain.dto.transactions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionsSearchRequestDto {
    private Long user_id;
    private Boolean is_proceed;
    private String search_word;
    private String sort_criteria;
    private String search_criteria;
    private Long start;
    private Long end;

}
