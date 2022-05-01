package moviegoods.movie.domain.dto.transactions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionsChangeStatusRequestDto {
    private Long user_id;
    private Long transaction_id;
    private String status;
}
