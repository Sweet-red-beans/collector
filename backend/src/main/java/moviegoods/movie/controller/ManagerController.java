package moviegoods.movie.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moviegoods.movie.domain.dto.booleanResult.ResultResponseDto;
import moviegoods.movie.domain.dto.manager.ManagerRequestDto;
import moviegoods.movie.domain.dto.manager.ManagerResponseDto;
import moviegoods.movie.service.ManagerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/manager/report")
public class ManagerController {

    private final ManagerService managerService;

    @GetMapping
    public ResponseEntity<Map<String, List<ManagerResponseDto>>> show() {
        List<ManagerResponseDto> reportsList = managerService.show();
        Map<String, List<ManagerResponseDto>> reportJson = new HashMap<>();
        reportJson.put("reports", reportsList);

        ResponseEntity<Map<String, List<ManagerResponseDto>>> result = new ResponseEntity<>(reportJson, HttpStatus.OK);
        return result;
    }

    @PatchMapping
    public ResultResponseDto approve(@RequestBody ManagerRequestDto requestDto) {
        ResultResponseDto result = managerService.approve(requestDto.getUser_id());
        return result;
    }
}
