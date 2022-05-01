package moviegoods.movie.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moviegoods.movie.domain.dto.main.MainDailyCommunityRequestDto;
import moviegoods.movie.domain.dto.main.MainDailyCommunityResponseDto;
import moviegoods.movie.domain.dto.main.MainEventLimitResponseDto;
import moviegoods.movie.domain.entity.Event.Event;
import moviegoods.movie.domain.entity.Post.Post;
import moviegoods.movie.domain.entity.User.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class MainService {
    private final EntityManager em;
    private final LikeBasketsService likeBasketsService;

    @Transactional(rollbackFor = Exception.class)
    public List<MainEventLimitResponseDto> eventLimit(User loginUser) {
        List<String> cinemaNameList = new ArrayList<String>();
        cinemaNameList.add("CGV");
        cinemaNameList.add("메가박스");
        cinemaNameList.add("롯데시네마");
        cinemaNameList.add("씨네큐");
        List<MainEventLimitResponseDto> searchList = new ArrayList<>();

        for (String cinema_name : cinemaNameList) {
            String searchJpql = "select e from event e join e.cinema c where c.name = '" + cinema_name + "' and e.end_date > STR_TO_DATE(NOW(), '%Y-%m-%d') ";
            List<Event> eventList = em.createQuery(searchJpql, Event.class).setMaxResults(6).getResultList();
            for (Event event : eventList) {
                Long event_id = event.getEvent_id();
                String search_cinema_name = event.getCinema().getName();
                String title = event.getTitle();
                String thumbnail_url = event.getThumbnail_url();
                Date start_date = event.getStart_date();
                Date end_date = event.getEnd_date();
                Long user_id = null;
                if (loginUser != null) {
                    user_id = loginUser.getUser_id();
                }
                Boolean is_like = likeBasketsService.isLikeEvent(user_id, event_id);

                searchList.add(new MainEventLimitResponseDto(search_cinema_name, event_id, thumbnail_url, title, start_date, end_date, is_like));
            }

        }
        return searchList;
    }

    @Transactional(rollbackFor = Exception.class)
    public List<MainDailyCommunityResponseDto> dailyCommunity(MainDailyCommunityRequestDto requestDto) {
        List<MainDailyCommunityResponseDto> searchList = new ArrayList<>();

        String community_category = requestDto.getCommunity_category();
        LocalDateTime startDatetime = LocalDateTime.of(LocalDate.now(), LocalTime.of(0,0,0)); //오늘 00:00:00
        LocalDateTime endDatetime = LocalDateTime.of(LocalDate.now(), LocalTime.of(23,59,59)); //오늘 23:59:59

        String searchJpql = "select p from post p join p.content_detail c where p.category = '"+community_category+"' and c.written_date between '"+startDatetime+"' and '"+endDatetime+ "' order by p.views DESC";

        List<Post> postList = em.createQuery(searchJpql, Post.class).setMaxResults(5).getResultList();
        for (Post post : postList) {
            Long post_id = post.getPost_id();
            String title = post.getTitle();
            String content = post.getContent_detail().getContent();
            Long views = post.getViews();
            Long comments_num = Long.valueOf(post.getComments().size());
            LocalDateTime written_date = post.getContent_detail().getWritten_date();

            searchList.add(new MainDailyCommunityResponseDto(post_id, title,content,views,comments_num,written_date));
        }
        return searchList;
    }
}