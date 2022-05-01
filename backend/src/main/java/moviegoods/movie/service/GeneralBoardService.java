package moviegoods.movie.service;

import com.google.firebase.auth.FirebaseAuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moviegoods.movie.domain.dto.booleanResult.ResultResponseDto;
import moviegoods.movie.domain.dto.comments.Comments;
import moviegoods.movie.domain.dto.generalBoard.*;
import moviegoods.movie.domain.entity.Comment.Comment;
import moviegoods.movie.domain.entity.Comment.CommentRepository;
import moviegoods.movie.domain.entity.Content_Detail.Content_Detail;
import moviegoods.movie.domain.entity.Post.Post;
import moviegoods.movie.domain.entity.Post.PostRepository;
import moviegoods.movie.domain.entity.User.UserStatus;
import moviegoods.movie.domain.entity.User.User;
import moviegoods.movie.domain.entity.User.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class GeneralBoardService {
    private final UserRepository userRepository;
    private final ContentDetailService contentDetailService;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final FireBaseService fireBaseService;
    private final EntityManager em;

    @Transactional(rollbackFor = Exception.class)
    public ResultResponseDto savePost(User loginUser, GeneralBoardWriteRequestDto requestDto) throws IOException, FirebaseAuthException {
        ResultResponseDto resultResponseDto = new ResultResponseDto();
        String title = requestDto.getTitle();

        String content = requestDto.getContent();
        MultipartFile image_url = requestDto.getImage_url();
        String firebaseUrl = "";

        if (loginUser == null) {
            resultResponseDto.setResult(false);
            return resultResponseDto;
        }
        Long user_id = loginUser.getUser_id();

        if(image_url!=null){
            String nameFile= UUID.randomUUID().toString();

            fireBaseService.uploadFiles(image_url,nameFile);
            firebaseUrl+="https://firebasestorage.googleapis.com/v0/b/stroagetest-f0778.appspot.com/o/"+nameFile+"?alt=media";
        }

        User user = userRepository.getById(user_id);
        Content_Detail content_detail = contentDetailService.saveContentDetail(content);
        Post saveEntity = Post.builder().category("자유").views(0L).user(user).content_detail(content_detail).title(title).image_url(firebaseUrl).build();

        postRepository.save(saveEntity);
        resultResponseDto.setResult(true);

        return resultResponseDto;

    }

    @Transactional(rollbackFor = Exception.class)
    public List<GeneralBoardSearchResponseDto> search(GeneralBoardSearchRequestDto requestDto) {

        List<GeneralBoardSearchResponseDto> searchList=new ArrayList<>();

        String search_word = requestDto.getSearch_word();
        String search_criteria = requestDto.getSearch_criteria(); // 제목 / 제목+내용 / 내용 / 작성자

        if (search_word == null) {
            search_word = "";
        }

        String searchJpql = "select p from post p join p.user u join p.content_detail c where p.category = '자유' ";

        String criteriaJpql = "";
        String criteriaJpql2 = "";
        log.info("search_criteria={}", search_criteria);
        if (search_criteria != "") {
            if (Objects.equals(search_criteria, "작성자")) {
                criteriaJpql = "u.nickname";
            }
            if (Objects.equals(search_criteria, "내용")) {
                criteriaJpql = "c.content";
            }
            if (Objects.equals(search_criteria, "제목")) {
                criteriaJpql = "p.title";
            }
            if (Objects.equals(search_criteria, "제목+내용")) {
                criteriaJpql = "c.content";
                criteriaJpql2 = "p.title";
            }
            if (criteriaJpql2 != "") {
                searchJpql += "and ("+criteriaJpql + " like '%" + search_word + "%' "+"OR "+criteriaJpql2 + " like '%" + search_word + "%') ";
            }
            else {
                searchJpql += "and "+criteriaJpql + " like '%" + search_word + "%' ";
            }

        }


        searchJpql += "order by c.written_date desc";
        log.info("searchJpql={}", searchJpql);

        List<Post> postList = em.createQuery(searchJpql, Post.class).getResultList();
        for (Post post : postList) {
            Long post_id = post.getPost_id();
            String title = post.getTitle();
            LocalDateTime written_date = post.getContent_detail().getWritten_date();
            String nickname = post.getUser().getNickname();
            Long view = post.getViews();

            searchList.add(new GeneralBoardSearchResponseDto(post_id,title,nickname,written_date,view));
        }
        return searchList;

    }

    @Transactional(rollbackFor = Exception.class)
    public GeneralBoardDetailResponseDto detailInfo(User loginUser, GeneralBoardDetailRequestDto requestDto){
        Long user_id = null;
        if (loginUser != null) {
            user_id = loginUser.getUser_id();
        }

        Long post_id = requestDto.getPost_id();
        Post post=postRepository.findById(post_id).get();
        String title = post.getTitle();
        LocalDateTime written_date = post.getContent_detail().getWritten_date();
        String content = post.getContent_detail().getContent();
        Long views = post.getViews();
        String nickname = post.getUser().getNickname();
        Long post_user_id = post.getUser().getUser_id();
        String image_url = post.getImage_url();
        Boolean is_mine = Boolean.FALSE;

        if (post_user_id == user_id) {
            is_mine = Boolean.TRUE;
        }

        List<Comments> searchCommentList = new ArrayList<>();

        String searchJpql = "select c from comment c join c.post p left join c.user u left join c.content_detail d where p.post_id=:post_id";
        List<Comment> commentList = em.createQuery(searchJpql, Comment.class).setParameter("post_id",post_id).getResultList();
        for (Comment comment : commentList) {
            Long search_user_id = comment.getUser().getUser_id();
            Long comment_id = comment.getComment_id();
            String comment_nickname = comment.getUser().getNickname();
            String comment_content = comment.getContent_detail().getContent();
            LocalDateTime comment_written_date = comment.getContent_detail().getWritten_date();
            UserStatus user_status = comment.getUser().getUser_status();

            Boolean comment_is_mine = Boolean.FALSE;
            if (search_user_id == user_id) {
                comment_is_mine = Boolean.TRUE;
            }

            searchCommentList.add(new Comments(comment_id,search_user_id,comment_nickname,comment_content,comment_is_mine,comment_written_date,user_status));
        }

        GeneralBoardDetailResponseDto responseDto = new GeneralBoardDetailResponseDto(post_id,title,written_date,content,views+1,nickname,image_url,is_mine,searchCommentList);
        post.setViews(post.getViews()+1);
        postRepository.save(post);

        return responseDto;

    }

    @Transactional(rollbackFor = Exception.class)
    public ResultResponseDto saveComment(User loginUser, GeneralBoardCommentRequestDto requestDto){
        ResultResponseDto resultResponseDto = new ResultResponseDto();
        if (loginUser == null) {
            resultResponseDto.setResult(false);
            return resultResponseDto;
        }

        String content = requestDto.getContent();
        Long post_id = requestDto.getPost_id();
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

    @Transactional(rollbackFor = Exception.class)
    public ResultResponseDto deleteDetail(User loginUser, GeneralBoardDetailDeleteRequestDto requestDto){
        ResultResponseDto resultResponseDto = new ResultResponseDto();
        Long user_id = null;
        if (loginUser != null) {
            user_id = loginUser.getUser_id();
        }

        Long post_id=requestDto.getPost_id();
        postRepository.deleteById(post_id);


        resultResponseDto.setResult(!postRepository.existsById(post_id));

        return resultResponseDto;
    }

    @Transactional(rollbackFor = Exception.class)
    public ResultResponseDto deleteComment(User loginUser, GeneralBoardCommentDeleteRequestDto requestDto){
        ResultResponseDto resultResponseDto = new ResultResponseDto();
        Long user_id = null;
        if (loginUser != null) {
            user_id = loginUser.getUser_id();
        }

        Long comment_id=requestDto.getComment_id();
        commentRepository.deleteById(comment_id);

        resultResponseDto.setResult(!commentRepository.existsById(comment_id));

        return resultResponseDto;
    }

}
