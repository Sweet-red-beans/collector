package moviegoods.movie.service;


import com.google.firebase.auth.FirebaseAuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moviegoods.movie.domain.dto.mypage.*;
import moviegoods.movie.domain.entity.Transaction.Status;
import moviegoods.movie.domain.entity.User.User;
import moviegoods.movie.domain.entity.User.UserRepository;
import moviegoods.movie.domain.entity.User.UserStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static moviegoods.movie.domain.entity.User.UserStatus.탈퇴;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyPageService {

    private final EntityManager em;
    private final UserRepository userRepository;
    private final FireBaseService fireBaseService;

    public MyPageResponseSearch search(User loginUser,MyPageRequestSearch mprs){
        MyPageResponseSearch myPageResponseSearch=new MyPageResponseSearch();
        Long user_id = null;
        if (loginUser != null) {
            user_id = loginUser.getUser_id();
        }
        User user = userRepository.getById(user_id);
        log.info("user_id={}", user_id);
        log.info("user_profile={}", loginUser.getProfile_url());

        MyPageUser myPageUserDto=new MyPageUser();
        myPageUserDto.setNickname(user.getNickname());
        myPageUserDto.setProfile_url(user.getProfile_url());
        myPageUserDto.setReliability(user.getReliability());
        myPageResponseSearch.setUser(myPageUserDto);


        List<Object[]> row= em.createQuery("select c.comment_id , d.content, d.written_date,p.category,p.post_id from comment c join c.user u left join c.content_detail d left join c.post p where u.user_id =:user_id order by d.written_date DESC").setParameter("user_id",user_id).getResultList();

        //작성 댓글
        for (Object[] objects : row) {

            Long  comment_id=(Long)objects[0];
            String comment_content=(String)objects[1];
            LocalDateTime written_date=(LocalDateTime)objects[2];
            String category=(String)objects[3];
            Long post_id=(Long)objects[4];
            myPageResponseSearch.getComment().add(new MyPageComment(comment_id,comment_content,written_date,category,post_id));


        }
        //작성한  글
        List<Object[]> row1=  em.createQuery("select p.post_id, p.title, c.written_date , p.category, c.content from post p join p.content_detail c left join p.user u where u.user_id =:user_id  order by c.written_date DESC").setParameter("user_id",user_id).getResultList();


        for (Object[] objects : row1) {
            Long post_id=(Long)objects[0];
            String title=(String)objects[1];
            LocalDateTime written_date=(LocalDateTime)objects[2];
            String category=(String)objects[3];
            String content=(String)objects[4];

            myPageResponseSearch.getContent().add(new MyPageContent(post_id,title,written_date,category,content));
        }


        //작성한 대리구매글
        List<Object[]> row2=  em.createQuery("select t.transaction_id , c.content , c.written_date , u.nickname ,u.reliability,u.user_id,t.status, u.profile_url,u.user_status from transaction t join t.content_detail c left join t.user u where u.user_id =:user_id order by c.written_date DESC").setParameter("user_id",user_id).getResultList();


        for (Object[] objects : row2) {
            Boolean is_mine=false;
            Long transaction_id=(Long)objects[0];
            String content=(String)objects[1];
            LocalDateTime written_date=(LocalDateTime)objects[2];
            String nickname=(String)objects[3];
            Long reliability=(Long)objects[4];
            Long user_id2=(Long)objects[5];
            if(user_id==user_id2){
                is_mine=true;
            }

            Status status=(Status)objects[6];
            String profile_url=(String)objects[7];
            UserStatus user_status=(UserStatus)objects[8];

            myPageResponseSearch.getWriteTransaction().add(new MyPageTransaction(transaction_id,content,written_date,nickname,reliability,is_mine,status,profile_url,user_status));
        }

        //좋아요한 대리구매글
        List<Object[]> row3=  em.createQuery("select t.transaction_id ,u.nickname,u.reliability,u.user_id, u.profile_url, u.user_status from like_basket b join b.user u left join b.transaction t where u.user_id =:user_id order by t.transaction_id DESC").setParameter("user_id",user_id).getResultList();
        for (Object[] objects : row3) {
            Boolean is_mine=false;
            Long transaction_id=(Long)objects[0];
            String nickname=(String)objects[1];
            Long reliability=(Long)objects[2];
            Long user_id2=(Long)objects[3];
            String profile_url=(String)objects[4];
            UserStatus user_status=(UserStatus)objects[5];
            if(user_id==user_id2){
                is_mine=true;
            }
            List<Object[]> row4=  em.createQuery("select c.content, c.written_date ,t.status from transaction t join t.content_detail c where t.transaction_id =:transaction_id ").setParameter("transaction_id",transaction_id).getResultList();
            if(row4.size()!=0){
                Object[] result4=row4.get(0);
                String content=(String)result4[0];

                LocalDateTime written_date=(LocalDateTime)result4[1];
                Status status=(Status)result4[2];
                myPageResponseSearch.getLikeTransaction().add(new MyPageTransaction(transaction_id,content,written_date,nickname,reliability,is_mine,status,profile_url,user_status));
            }
        }

        //좋아요한 이벤트

        List<Object[]> row5= em.createQuery("select e.event_id, e.title, e.thumbnail_url, e.start_date, e.end_date from like_basket b join b.event e left join b.user u where u.user_id=:user_id order by b.like_basket_id DESC").setParameter("user_id",user_id).getResultList();
        for (Object[] objects : row5) {
            Long event_id=(Long)objects[0];
            String event_title=(String)objects[1];
            String thumbnail_url=(String)objects[2];
            Date start_date=(Date)objects[3];
            Date end_date=(Date)objects[4];
            myPageResponseSearch.getLikeEvent().add(new MyPageLikeEvent(event_id,event_title,thumbnail_url,start_date,end_date));
        }



        return myPageResponseSearch;

    }

    public String updateNickname(User loginUser,MyPageRequestNickname mprn){
        Long user_id = null;
        if (loginUser != null) {
            user_id = loginUser.getUser_id();
        }
        String nickname=mprn.getNickname();
        log.info("nickname={}", nickname);
        User user=userRepository.findById(user_id).get();
        user.setNickname(nickname);
        userRepository.save(user);
        return userRepository.findById(user_id).get().getNickname();
    }

    public void updateProfile(User loginUser,MyPageRequestProfile mprp) throws IOException, FirebaseAuthException {
        MultipartFile profile_image=mprp.getProfile_image();
        Long user_id = null;
        if (loginUser != null) {

            user_id = loginUser.getUser_id();
        }
        String firebaseUrl="";
        if(profile_image!=null){
            String nameFile= UUID.randomUUID().toString();
            fireBaseService.uploadFiles(profile_image,nameFile);
            firebaseUrl+="https://firebasestorage.googleapis.com/v0/b/stroagetest-f0778.appspot.com/o/"+nameFile+"?alt=media";

        }
        User user=userRepository.findById(user_id).get();
        user.setProfile_url(firebaseUrl);
        userRepository.save(user);

    }

    public Boolean withdrawal(User loginUser,MyPageRequestWithdrawal mrwd){
        Boolean check=false;
        Long user_id = null;
        if (loginUser != null) {

            user_id = loginUser.getUser_id();

        }
        String basicUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRlx2rvqRVwn6S5uXPkHl856CcYvV2z8bUMyw&usqp=CAU";
        User user=userRepository.findById(user_id).get();
        user.setUser_status(탈퇴);
        user.setEmail("0");
        user.setNickname("0");
        user.setPassword("0");
        user.setProfile_url(basicUrl);


        userRepository.save(user);



        if(userRepository.findById(user_id).get().getUser_status()== 탈퇴){
            check=true;
        }
        return check;
    }

    public Result nicknameDuplicate(MyPageRequestNicknameDuplicateDto mpnd){

        String nickname = mpnd.getNickname();
        User user = new User();

        if (nickname != null) {
            user = userRepository.findByNickname(nickname).orElse(null);
        }
        Result result = new Result();
        if (user == null) {
            result.setResult(false);
        }
        else {
            result.setResult(true);
        }

        return result;
    }



}