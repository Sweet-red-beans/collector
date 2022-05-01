package moviegoods.movie.domain.dto.transactions;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionsSaveRequestDto {

    private String content;
    private Long user_id;


//    public Transaction toEntity() {
//        return Transaction.builder().user(user).content_detail(content_detail).build();
//    }
}
