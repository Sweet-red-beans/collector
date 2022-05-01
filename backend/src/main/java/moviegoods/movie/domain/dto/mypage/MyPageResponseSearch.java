package moviegoods.movie.domain.dto.mypage;


import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@RequiredArgsConstructor
public class MyPageResponseSearch {

    private List<MyPageComment> comment=new ArrayList<>();
    private List<MyPageContent> content=new ArrayList<>();
    private List<MyPageLikeEvent> likeEvent=new ArrayList<>();
    private MyPageUser user;
    private List<MyPageTransaction> likeTransaction=new ArrayList<>();
    private List<MyPageTransaction> writeTransaction=new ArrayList<>();




}
