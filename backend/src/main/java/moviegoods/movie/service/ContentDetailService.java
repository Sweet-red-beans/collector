package moviegoods.movie.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moviegoods.movie.domain.entity.Content_Detail.Content_Detail;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class ContentDetailService {
    public Content_Detail saveContentDetail(String content) {
        Content_Detail content_detail = new Content_Detail();

        content_detail.setContent(content);
        content_detail.setWritten_date(LocalDateTime.now());

        return content_detail;
    }
}