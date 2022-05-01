package moviegoods.movie.domain.dto.directMessage;


import lombok.Data;

@Data
public class DirectMessageRequestReport {

    Long transaction_id;
    Long user_id;
    String report_content;
}
