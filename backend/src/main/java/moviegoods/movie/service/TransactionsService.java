package moviegoods.movie.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moviegoods.movie.domain.dto.booleanResult.ResultResponseDto;
import moviegoods.movie.domain.entity.Content_Detail.Content_Detail;
import moviegoods.movie.domain.entity.Like_Basket.LikeBasketRepository;
import moviegoods.movie.domain.entity.Like_Basket.Like_Basket;
import moviegoods.movie.domain.entity.Report.Report;
import moviegoods.movie.domain.entity.Report.ReportRepository;
import moviegoods.movie.domain.entity.Transaction.Status;
import moviegoods.movie.domain.entity.Transaction.Transaction;
import moviegoods.movie.domain.entity.Transaction.TransactionRepository;
import moviegoods.movie.domain.entity.User.User;
import moviegoods.movie.domain.entity.User.UserStatus;
import moviegoods.movie.domain.entity.User.UserRepository;
import moviegoods.movie.domain.dto.transactions.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static moviegoods.movie.domain.entity.Transaction.Status.*;

@RequiredArgsConstructor
@Service
@Slf4j
public class TransactionsService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final LikeBasketRepository likeBasketRepository;
    private final ReportRepository reportRepository;
    private final ContentDetailService contentDetailService;
    private final LikeBasketsService likeBasketsService;
    private final EntityManager em;

    @Transactional(rollbackFor = Exception.class)
    public ResultResponseDto write(User loginUser,TransactionsSaveRequestDto requestDto) {
        ResultResponseDto resultResponseDto = new ResultResponseDto();

        if (loginUser == null) {
            resultResponseDto.setResult(false);
            return resultResponseDto;
        }
        Long user_id = loginUser.getUser_id();

        String content = requestDto.getContent();
        Status status = 진행중;

        User user = userRepository.getById(user_id);
        Content_Detail content_detail = contentDetailService.saveContentDetail(content);
        Transaction saveEntity = Transaction.builder().user(user).content_detail(content_detail).status(status).build();

        transactionRepository.save(saveEntity);
        resultResponseDto.setResult(true);

        return resultResponseDto;
    }

    @Transactional(rollbackFor = Exception.class)
    public List<TransactionsSearchResponseDto> search(User loginUser, TransactionsSearchRequestDto requestDto) {
        Long user_id = null;
        if (loginUser != null) {
            user_id = loginUser.getUser_id();
        }

        List<TransactionsSearchResponseDto> searchList = new ArrayList<>();

        Boolean is_proceed = requestDto.getIs_proceed(); // 모집중(1) , 전체(0)
        String search_word = requestDto.getSearch_word(); // 검색어
        String search_criteria = requestDto.getSearch_criteria(); // 작성자/글내용
        Long start = requestDto.getStart();
        Long end = requestDto.getEnd();
        String linking_word = "where ";

        if (search_word == null) {
            search_word = "";
        }

        String searchJpql = "select t from transaction t join t.user u join t.content_detail c where ";
        Integer check = 0;

        // 작성자/글내용 (기본 값 줘야)
        String criteriaJpql = "";
        if (search_criteria != null) {
            check++;
            if (Objects.equals(search_criteria, "작성자")) {
                criteriaJpql = "u.nickname";
            }
            if (Objects.equals(search_criteria, "글내용")) {
                criteriaJpql= "c.content";
            }
            searchJpql += criteriaJpql + " like '%" + search_word + "%' ";
        }

        // 진행여부
        if (Objects.equals(is_proceed, true)) {
            if (check == 1) {
                linking_word = "and ";
            }
            log.info("check={},searchJpql={}", check, searchJpql);
            searchJpql += linking_word+ "t.status = '진행중' ";
        }

        // 정렬 기준(최신순) 기본 값 줘야
        searchJpql += "order by c.written_date desc";
        log.info("searchJpql={}", searchJpql);

        List<Transaction> transactionList = em.createQuery(searchJpql, Transaction.class).setFirstResult(start.intValue()).setMaxResults(end.intValue()).getResultList();
        for (Transaction transaction : transactionList) {
            Long search_user_id = transaction.getUser().getUser_id();
            Long reliability = transaction.getUser().getReliability();
            String content = transaction.getContent_detail().getContent();
            String status2 = String.valueOf(transaction.getStatus());
            UserStatus user_status = transaction.getUser().getUser_status();
            Long transaction_id = transaction.getTransaction_id();
            LocalDateTime written_date = transaction.getContent_detail().getWritten_date();
            String nickname = transaction.getUser().getNickname();
            String profile_url = transaction.getUser().getProfile_url();

            Boolean is_mine = Boolean.FALSE;
            if (search_user_id == user_id) {
                is_mine = Boolean.TRUE;
            }

            Boolean is_like = likeBasketsService.isLikeTransaction(user_id, transaction_id);

            searchList.add(new TransactionsSearchResponseDto(search_user_id, content, status2, user_status, transaction_id,
                    reliability, written_date, is_mine, is_like,nickname,profile_url));
        }
        return searchList;
    }

    @Transactional(rollbackFor = Exception.class)
    public ResultResponseDto changeStatus(User loginUser, TransactionsChangeStatusRequestDto requestDto) {
        ResultResponseDto resultResponseDto = new ResultResponseDto();
        String status = requestDto.getStatus();
        Long transaction_id = requestDto.getTransaction_id();

        Transaction transaction = transactionRepository.getById(transaction_id);

        if (transaction.getUser().getUser_id() != loginUser.getUser_id() || loginUser == null) {
            resultResponseDto.setResult(false);
            return resultResponseDto;
        }

        if (Objects.equals(status,"진행중")) {
            transaction.setStatus(마감);
        }
        if (Objects.equals(status, "마감")) {
            transaction.setStatus(진행중);
        }

        transactionRepository.save(transaction);
        resultResponseDto.setResult(true);

        return resultResponseDto;
    }

    @Transactional(rollbackFor = Exception.class)
    public ResultResponseDto delete(User loginUser, TransactionsDeleteRequestDto requestDto) {
        ResultResponseDto resultResponseDto = new ResultResponseDto();
        Long transaction_id = requestDto.getTransaction_id();

        Transaction transaction = transactionRepository.getById(transaction_id);

        if (transaction.getUser().getUser_id() != loginUser.getUser_id() || loginUser == null) {
            resultResponseDto.setResult(false);
            return resultResponseDto;
        }
        transactionRepository.delete(transaction);
        resultResponseDto.setResult(true);

        return resultResponseDto;
    }

    @Transactional(rollbackFor = Exception.class)
    public ResultResponseDto report(User loginUser, TransactionsReportRequestDto requestDto) {
        ResultResponseDto resultResponseDto = new ResultResponseDto();
        Long transaction_id = requestDto.getTransaction_id();
        String content = requestDto.getContent();

        Transaction transaction = transactionRepository.getById(transaction_id);
        Content_Detail content_detail = contentDetailService.saveContentDetail(content);

        if (transaction.getUser() != loginUser || loginUser == null) {
            resultResponseDto.setResult(false);
            return resultResponseDto;
        }
        Report saveEntity = Report.builder().user(loginUser).transaction(transaction).content_detail(content_detail).build();
        reportRepository.save(saveEntity);

        resultResponseDto.setResult(true);

        return resultResponseDto;

    }

    @Transactional(rollbackFor = Exception.class)
    public ResultResponseDto like(User loginUser, TransactionsLikeRequestDto requestDto){
        ResultResponseDto resultResponseDto = new ResultResponseDto();
        Long transaction_id = requestDto.getTransaction_id();

        Transaction transaction = transactionRepository.getById(transaction_id);

        if (loginUser == null) {
            resultResponseDto.setResult(false);
            return resultResponseDto;
        }

        Long user_id = loginUser.getUser_id();
        Boolean is_like = likeBasketsService.isLikeTransaction(user_id, transaction_id);

        Boolean result = false;
        if (Objects.equals(is_like,false)) {
            Like_Basket saveEntity=Like_Basket.builder().user(loginUser).transaction(transaction).build();
            likeBasketRepository.save(saveEntity);
            result = true;
        }
        if (Objects.equals(is_like, true)) {
            Long like_basket_id = likeBasketsService.selectLikeTransaction(user_id,transaction_id);
            result = likeBasketsService.deleteLike(like_basket_id,user_id);
        }

        resultResponseDto.setResult(result);

        return resultResponseDto;

    }

}
