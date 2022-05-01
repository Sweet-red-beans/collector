package moviegoods.movie.service;


import com.google.firebase.auth.FirebaseAuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moviegoods.movie.domain.dto.booleanResult.ResultResponseDto;
import moviegoods.movie.domain.dto.comments.Comments;
import moviegoods.movie.domain.dto.informationShare.*;
import moviegoods.movie.domain.entity.Cinema.Cinema;
import moviegoods.movie.domain.entity.Cinema.CinemaRepository;
import moviegoods.movie.domain.entity.Comment.Comment;
import moviegoods.movie.domain.entity.Comment.CommentRepository;
import moviegoods.movie.domain.entity.Content_Detail.ContentDetailRepository;
import moviegoods.movie.domain.entity.Content_Detail.Content_Detail;
import moviegoods.movie.domain.entity.Post.Post;
import moviegoods.movie.domain.entity.Post.PostRepository;
import moviegoods.movie.domain.entity.User.UserStatus;
import moviegoods.movie.domain.entity.User.User;
import moviegoods.movie.domain.entity.User.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class InformationShareService {

    private final CinemaRepository cinemaRepository;
    private final UserRepository userRepository;
    private final ContentDetailRepository contentDetailRepository;
    private final ContentDetailService contentDetailService;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final FireBaseService fireBaseService;
    private final EntityManager em;

    public List<Post> savePost(InformationShareRequestWrite isrw) throws IOException, FirebaseAuthException {

        Long user_id=isrw.getUser_id();
        String title=isrw.getTitle();
        String cinema_name=isrw.getCinema_name();
        String cinema_area=isrw.getCinema_area();
        String cinema_branch=isrw.getCinema_branch();
        String content=isrw.getContent();
        MultipartFile image_url=isrw.getImage_url();
        String firebaseUrl="";
        log.info("imageurl={}",image_url);
        if(image_url!=null){
            String nameFile= UUID.randomUUID().toString();
            //log.info("imageurl={}",image_url);

            fireBaseService.uploadFiles(image_url,nameFile);
            firebaseUrl+="https://firebasestorage.googleapis.com/v0/b/stroagetest-f0778.appspot.com/o/"+nameFile+"?alt=media";
            //log.info("firebaseurl={}",firebaseUrl);
        }


        Optional<User> OptionalUser=userRepository.findById(user_id);
        if(OptionalUser.isPresent()){
            User user=OptionalUser.get();
            Cinema cinema=new Cinema();
            cinema.setName(cinema_name);
            cinema.setBranch(cinema_branch);
            cinema.setArea(cinema_area);
            Content_Detail content_detail=saveContent_detail(content);
            Post post=new Post();
            post.setImage_url(firebaseUrl);
            post.setTitle(title);
            post.setViews(0L);
            post.setCategory("정보공유");
            user.getPosts().add(post);
            post.setUser(user);
            content_detail.setPost(post);
            post.setContent_detail(content_detail);
            cinema.setPost(post);
            post.setCinema(cinema);


            postRepository.save(post);

            List<Post> postList=postRepository.findAll();


            return postList;
        }
        return new ArrayList<>();



    }

    public Content_Detail saveContent_detail(String content){

        Content_Detail content_detail=new Content_Detail();

        content_detail.setContent(content);
        content_detail.setWritten_date(LocalDateTime.now());

        return content_detail;
    }

    public void saveUser(User user){

        userRepository.save(user);

    }



    public InformationShareResponseSearch makeInformationShareResponseSearch(String nickname, String title, Long views, Long post_id, LocalDateTime written_date, UserStatus status){
        InformationShareResponseSearch informationShareResponseSearch=new InformationShareResponseSearch();
        informationShareResponseSearch.setNickname(nickname);
        informationShareResponseSearch.setPost_id(post_id);
        informationShareResponseSearch.setTitle(title);
        informationShareResponseSearch.setView(views);
        informationShareResponseSearch.setWritten_date(written_date);
        informationShareResponseSearch.setUser_status(status);
        return informationShareResponseSearch;
    }

    public List<InformationShareResponseSearch> Search(InformationShareRequestSearch isrs) {

        List<InformationShareResponseSearch> searchList=new ArrayList<>();

        String area = isrs.getCinema_area();
        String branch = isrs.getCinema_branch();
        String name = isrs.getCinema_name();
        String search_word = isrs.getSearch_word();
        String sort_name = isrs.getSort_name(); // 제목 / 제목+내용 / 내용 / 작성자
        String searchJpql1 = "select p From post p join p.content_detail c ";
        String searchWord = "where p.category='정보공유' ";
        String orderLatest ="order by c.written_date DESC";
        String categoryInformationShare=" and p.category='정보공유' ";
        if ((area == null) && (branch == null) && (name == null)) { // 영화관 필터가 없을때
            if (search_word.equals("")) {   //검색어가 없으면

            }
            if (!search_word.equals("")) {  //검색어가 있으면
                if (sort_name.equals("제목")) {
                    searchWord += "and p.title like '%" + search_word + "%'";
                } else if (sort_name.equals("제목+내용")) {
                    searchWord += "and (c.content like '%" + search_word + "%') " + "OR (p.title like '%" + search_word + "%')";

                } else if (sort_name.equals("내용")) {
                    searchWord += "and c.content like '%" + search_word + "%'";
                } else if (sort_name.equals("작성자")) {

                    String searchJpql2="select p From post p join p.user u where u.nickname like '%"+search_word+"%'"+categoryInformationShare;

                    List<Post> postList=em.createQuery(searchJpql2,Post.class).getResultList();
                    for (Post post : postList) {
                        Long content_detail_id=post.getContent_detail().getContent_detail_id();
                        Content_Detail content_detail=contentDetailRepository.findById(content_detail_id).get();

                        String nickname=post.getUser().getNickname();
                        UserStatus status=userRepository.findByNickname(nickname).get().getUser_status();
                        String title=post.getTitle();
                        Long views=post.getViews();
                        Long post_id=post.getPost_id();
                        LocalDateTime written_date=content_detail.getWritten_date();
                        searchList.add(makeInformationShareResponseSearch(nickname,title,views,post_id,written_date,status));



                    }
                    Collections.reverse(searchList);
                    return searchList;
                }
            }
            searchJpql1+=searchWord;

            searchJpql1+=orderLatest;

            List<Post> postList=em.createQuery(searchJpql1,Post.class).getResultList();
            for (Post post : postList) {
                Long user_id=post.getUser().getUser_id();
                User user=userRepository.findById(user_id).get();
                String nickname=user.getNickname();
                UserStatus status=userRepository.findByNickname(nickname).get().getUser_status();
                String title=post.getTitle();
                Long views=post.getViews();
                Long post_id=post.getPost_id();
                LocalDateTime written_date=post.getContent_detail().getWritten_date();
                searchList.add(makeInformationShareResponseSearch(nickname,title,views,post_id,written_date,status));

            }
            return searchList;
        }
        else{ //영화관 필터가 있을때


            Boolean check=false;
            if(search_word.equals("")){  //검색어가 없으면
                check=false;
                String filterJpql="select p from post p join p.cinema c where ";
                if(area!=null){
                    filterJpql+="c.area= '"+area+"'";
                    check=true;
                }
                if(branch!=null){
                    if(check==true){
                        filterJpql+="AND c.branch= '"+branch+"'";
                    }
                    else{
                        filterJpql+="c.branch= '"+branch+"'";

                    }
                    check=true;
                }
                if(name!=null){
                    if(check==true){
                        filterJpql+="AND c.name= '"+name+"'";
                    }
                    else{
                        filterJpql+="c.name= '"+name+"'";
                    }
                }

                filterJpql+=categoryInformationShare;


                List<Post> postList=em.createQuery(filterJpql,Post.class).getResultList();
                for (Post post : postList) {
                    String nickname=userRepository.findById(post.getUser().getUser_id()).get().getNickname();
                    UserStatus status=userRepository.findByNickname(nickname).get().getUser_status();
                    String title=post.getTitle();
                    Long views=post.getViews();
                    Long post_id=post.getPost_id();
                    LocalDateTime written_date=contentDetailRepository.findById(post.getContent_detail().getContent_detail_id()).get().getWritten_date();
                    searchList.add(makeInformationShareResponseSearch(nickname,title,views,post_id,written_date,status));

                }
                Collections.reverse(searchList);
                return searchList;

            }
            //영화관 필터 & 검색어가 있으면
            check=false;
            String filterJpql1="select p from post p join p.cinema c left join p.content_detail d left join p.user u where ";
            if(area!=null){
                filterJpql1+="c.area= '"+area+"'";
                check=true;
            }
            if(branch!=null){
                if(check==true){
                    filterJpql1+="AND c.branch= '"+branch+"'";
                }
                else{
                    filterJpql1+="c.branch= '"+branch+"'";

                }
                check=true;
            }
            if(name!=null){
                if(check==true){
                    filterJpql1+="AND c.name= '"+name+"'";
                }
                else{
                    filterJpql1+="c.name= '"+name+"'";
                }
            }
            filterJpql1+=categoryInformationShare;
            filterJpql1+="AND ";
            if (sort_name.equals("제목")) {

                filterJpql1+= " p.title like '%" + search_word + "%'";
            } else if (sort_name.equals("제목+내용")) {
                filterJpql1 += " ( (d.content like '%" + search_word + "%') " + "OR (p.title like '%" + search_word + "%') )";

            } else if (sort_name.equals("내용")) {
                filterJpql1 += " d.content like '%" + search_word + "%'";
            }else if (sort_name.equals("작성자")) {

                filterJpql1+="u.nickname like '%"+search_word+"%'";

            }
            List<Post> postList=em.createQuery(filterJpql1,Post.class).getResultList();
            for (Post post : postList) {
                Long content_detail_id=post.getContent_detail().getContent_detail_id();
                Content_Detail content_detail=contentDetailRepository.findById(content_detail_id).get();

                String nickname=post.getUser().getNickname();
                UserStatus status=userRepository.findByNickname(nickname).get().getUser_status();
                String title=post.getTitle();
                Long views=post.getViews();
                Long post_id=post.getPost_id();
                LocalDateTime written_date=content_detail.getWritten_date();
                searchList.add(makeInformationShareResponseSearch(nickname,title,views,post_id,written_date,status));



            }
            Collections.reverse(searchList);
            return searchList;

        }
    }


    //상세조회
    public InformationShareResponseDetail detailInfo(User loginUser, InformationShareRequestDetail isrd){
        Long user_id = null;
        if (loginUser != null) {
            user_id = loginUser.getUser_id();
        }
        Long post_id=isrd.getPost_id();
        User user=loginUser;
        Post post=postRepository.findById(post_id).get();
        List<Object[]> row=em.createQuery("select u.user_id, u.nickname, p.views,p.title,p.image_url,d.written_date," +
                " d.content,c.name,c.area,c.branch from post p join p.content_detail d left join p.cinema c left join p.user u where p.post_id=:post_id ").setParameter("post_id",post_id).getResultList();
        Object[] result=row.get(0);
        InformationShareDetailDTO ifsd= new InformationShareDetailDTO((Long)result[0],(String)result[1],(Long)result[2],(String)result[3],(String)result[4],(LocalDateTime)result[5],(String)result[6],(String)result[7],(String)result[8],(String)result[9]);

        InformationShareResponseDetail informationShareResponseDetail=new InformationShareResponseDetail();
        String nickname=(String)result[1];
        informationShareResponseDetail.setUser_status(userRepository.findByNickname(nickname).get().getUser_status());
        List<Object[]> row2=em.createQuery("select  u.user_id, u.nickname, d.content, d.written_date ,c.comment_id from comment c join c.post p left join c.user u left join c.content_detail d where p.post_id=:post_id").setParameter("post_id",post_id).getResultList();
        List<Comments> result2=new ArrayList<>();

        for (Object[] objects : row2) {
            Boolean check=false;
            String nickname2=(String)objects[1];
            UserStatus status2=userRepository.findByNickname(nickname2).get().getUser_status();
            Comments comment=new Comments((Long)objects[0],(String)objects[1],(String)objects[2],(LocalDateTime)objects[3],(Long)objects[4],status2);
            if(user_id==(Long)objects[0]){
                check=true;
            }
            comment.setIs_mine(check);
            result2.add(comment);
        }
        Collections.reverse(result2);

        if(ifsd.getUser_id()==user_id){
            informationShareResponseDetail.setIs_mine(true);
        }else{
            informationShareResponseDetail.setIs_mine(false);
        }
        informationShareResponseDetail.setCinema_area(ifsd.getArea());
        informationShareResponseDetail.setCinema_branch(ifsd.getBranch());
        informationShareResponseDetail.setCinema_name(ifsd.getName());
        informationShareResponseDetail.setContent(ifsd.getContent());
        informationShareResponseDetail.setImage_url(ifsd.getImage_url());
        informationShareResponseDetail.setNickname(ifsd.getNickname());
        informationShareResponseDetail.setPost_id(post_id);
        informationShareResponseDetail.setViews(ifsd.getViews()+1);
        informationShareResponseDetail.setWritten_date(ifsd.getWritten_date());
        informationShareResponseDetail.setTitle(ifsd.getTitle());

        informationShareResponseDetail.setComment(result2);


        post.setViews(post.getViews()+1);
        postRepository.save(post);

        return informationShareResponseDetail;

    }

    public ResultResponseDto saveComment(User loginUser, InformationShareRequestSaveComment isrsc){
        ResultResponseDto resultResponseDto = new ResultResponseDto();
        if (loginUser == null) {
            resultResponseDto.setResult(false);
            return resultResponseDto;
        }

        Long user_id = loginUser.getUser_id();

        String content = isrsc.getContent();
        Long post_id = isrsc.getPost_id();
        Post post = postRepository.getById(post_id);
        Content_Detail content_detail = contentDetailService.saveContentDetail(content);
        Comment saveEntity = Comment.builder().post(post).user(loginUser).content_detail(content_detail).build();


        commentRepository.save(saveEntity);
        if(post.getViews()>0){
            post.setViews(post.getViews()-1);
            postRepository.save(post);
        }

        resultResponseDto.setResult(true);

        return resultResponseDto;

    }

    public Boolean deleteDetail(User loginUser, InformationShareRequestDeleteDetail isrdd){
        Long user_id = null;
        if (loginUser != null) {
            user_id = loginUser.getUser_id();
        }
        Long post_id=isrdd.getPost_id();


        postRepository.deleteById(post_id);

        Boolean result=postRepository.existsById(post_id);
        return result;
    }

    public Boolean deleteComment(User loginUser, InformationShareRequestDeleteComment isrdc){
        Long user_id = null;
        if (loginUser != null) {
            user_id = loginUser.getUser_id();
        }
        Long comment_id=isrdc.getComment_id();


        commentRepository.deleteById(comment_id);
        Boolean result=commentRepository.existsById(comment_id);
        return result;
    }

}