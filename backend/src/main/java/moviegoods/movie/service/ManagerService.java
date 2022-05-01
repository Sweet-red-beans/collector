package moviegoods.movie.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moviegoods.movie.domain.dto.booleanResult.ResultResponseDto;
import moviegoods.movie.domain.dto.manager.ManagerResponseDto;
import moviegoods.movie.domain.entity.Content_Detail.Content_Detail;
import moviegoods.movie.domain.entity.Report.Report;
import moviegoods.movie.domain.entity.Report.ReportRepository;
import moviegoods.movie.domain.entity.Transaction.Transaction;
import moviegoods.movie.domain.entity.User.User;
import moviegoods.movie.domain.entity.User.UserRepository;
import moviegoods.movie.domain.entity.User.UserStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

import static moviegoods.movie.domain.entity.User.UserStatus.정상;
import static moviegoods.movie.domain.entity.User.UserStatus.정지;

@Slf4j
@Service
@RequiredArgsConstructor
public class ManagerService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;

    public List<ManagerResponseDto> show() {
        List<ManagerResponseDto> reportsList = new ArrayList<>();
        List<Report> reports = reportRepository.findAll();
        for (Report report : reports) {
            Content_Detail content_detail = report.getContent_detail();
            String content = content_detail.getContent(); //신고내용
            LocalDateTime written_date = content_detail.getWritten_date(); //신고시간
            User user = report.getUser(); //신고한 사용자
            Long user_id = user.getUser_id(); //신고자 아이디
            String nickname = user.getNickname(); //신고자 닉네임

            Transaction transaction = report.getTransaction();
            User reportedUser = transaction.getUser(); //신고당한 사용자
            String reported_nickname = reportedUser.getNickname(); //신고당한 사용자 닉네임
            Long reported_user_id = reportedUser.getUser_id(); //신고당한 사용자 아이디
            String reported_content = transaction.getContent_detail().getContent(); //신고당한 내용

            Boolean is_complete = false;
            if(reportedUser.getUser_status() == 정지) {
                is_complete = true;
            }

            reportsList.add(new ManagerResponseDto(content, written_date, nickname, user_id, reported_user_id, reported_nickname, reported_content, is_complete));
        }

        Comp comp = new Comp();
        Collections.sort(reportsList, comp);

        return reportsList;
    }

    public ResultResponseDto approve(Long user_id) {
        Optional<User> user = userRepository.findById(user_id);
        ResultResponseDto result = new ResultResponseDto();
        UserStatus statusActive = 정상;
        UserStatus statusDeActive = 정지;
        if(user.isPresent()) {
            User findedUser = user.get();
            if(findedUser.getUser_status() == 정상) {
                findedUser.setUser_status(statusDeActive);
                userRepository.save(findedUser);
            }
            else if (findedUser.getUser_status() == 정지) {
                findedUser.setUser_status(statusActive);
                userRepository.save(findedUser);
            }
            result.setResult(true);
        }
        else {
            result.setResult(true);
        }
        return result;
    }

    private static class Comp implements Comparator<ManagerResponseDto> {

        @Override
        public int compare(ManagerResponseDto responseDto1, ManagerResponseDto responseDto2) {

            LocalDateTime date1 = responseDto1.getWritten_date();
            LocalDateTime date2 = responseDto2.getWritten_date();

            int result = date1.compareTo(date2);
            return result;
        }
    }
}