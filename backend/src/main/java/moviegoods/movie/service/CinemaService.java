package moviegoods.movie.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moviegoods.movie.domain.entity.Cinema.Cinema;
import moviegoods.movie.domain.entity.Content_Detail.Content_Detail;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class CinemaService {
    public Cinema saveCinemaDetail(String cinema_name, String cinema_area, String cinema_branch) {
        Cinema cinema = new Cinema();

        cinema.setName(cinema_name);
        cinema.setArea(cinema_area);
        cinema.setBranch(cinema_branch);

        return cinema;
    }
}
