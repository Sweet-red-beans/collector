package moviegoods.movie.domain.dto.mypage;


import lombok.Data;
import moviegoods.movie.domain.entity.Transaction.Status;
import moviegoods.movie.domain.entity.User.UserStatus;

import java.time.LocalDateTime;

@Data
public class MyPageTransaction {

    Long transaction_id;
    String content;
    LocalDateTime written_date;
    String nickname;
    Long reliability;
    Boolean is_mine;
    Status status;
    UserStatus user_status;
    String profile_url;



    public MyPageTransaction(Long transaction_id, String content, LocalDateTime written_date,String nickname,Long reliability,Boolean is_mine,Status status,String profile_url,UserStatus user_status){
        this.transaction_id=transaction_id;
        this.content=content;
        this.written_date=written_date;
        this.nickname=nickname;
        this.reliability=reliability;
        this.is_mine=is_mine;
        this.status=status;
        this.profile_url=profile_url;
        this.user_status=user_status;

    }
}