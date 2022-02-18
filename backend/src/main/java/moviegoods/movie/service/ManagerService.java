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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

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
            String content = content_detail.getContent();
            LocalDateTime written_date = content_detail.getWritten_date();

            Transaction transaction = report.getTransaction();
            User user = transaction.getUser();
            String nickname = user.getNickname();
            Long user_id = user.getUser_id();

            reportsList.add(new ManagerResponseDto(content, written_date, nickname, user_id));
        }

        Comp comp = new Comp();
        Collections.sort(reportsList, comp);

        return reportsList;
    }

    public ResultResponseDto approve(Long user_id) {
        Optional<User> user = userRepository.findById(user_id);
        ResultResponseDto result = new ResultResponseDto();
        Byte statusActive = 1;
        Byte statusDeActive = 0;
        if(user.isPresent()) {
            User findedUser = user.get();
            if(findedUser.getStatus() == 1) {
                findedUser.setStatus(statusDeActive);
                userRepository.save(findedUser);
            }
            else if (findedUser.getStatus() == 0) {
                findedUser.setStatus(statusActive);
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