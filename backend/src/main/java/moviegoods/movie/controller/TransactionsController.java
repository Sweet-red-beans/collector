package moviegoods.movie.controller;


import lombok.RequiredArgsConstructor;
import moviegoods.movie.domain.argumentresolver.Login;
import moviegoods.movie.domain.dto.booleanResult.ResultResponseDto;
import moviegoods.movie.domain.entity.User.User;
import moviegoods.movie.service.TransactionsService;
import moviegoods.movie.domain.dto.transactions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.text.ParseException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transactions")
public class TransactionsController {

    private final TransactionsService transactionsService;

    @PostMapping("/write")
    public ResponseEntity<ResultResponseDto> write(@Login User loginUser, @RequestBody TransactionsSaveRequestDto requestDto) {
        ResultResponseDto resultResponseDto = transactionsService.write(loginUser,requestDto);
        ResponseEntity<ResultResponseDto> result;
        result = new ResponseEntity<>(resultResponseDto, HttpStatus.OK);

        if (!resultResponseDto.isResult()) {
            result = new ResponseEntity<>(resultResponseDto, HttpStatus.UNAUTHORIZED);
        }

        return result;
    }

    @GetMapping("/search")
    public ResponseEntity<List<TransactionsSearchResponseDto>>  search(@Login User loginUser, @ModelAttribute TransactionsSearchRequestDto requestDto) {
        List<TransactionsSearchResponseDto> list = transactionsService.search(loginUser, requestDto);
        ResponseEntity<List<TransactionsSearchResponseDto>> result = new ResponseEntity<>(list, HttpStatus.OK);

        return result;
    }

    @PostMapping("/change-status")
    public ResultResponseDto changeStatus(@Login User loginUser, @RequestBody TransactionsChangeStatusRequestDto requestDto) {
        ResultResponseDto resultResponseDto = transactionsService.changeStatus(loginUser, requestDto);

        return resultResponseDto;
    }

    @DeleteMapping
    public ResultResponseDto delete(@Login User loginUser, @RequestBody TransactionsDeleteRequestDto requestDto) {
        ResultResponseDto resultResponseDto = transactionsService.delete(loginUser, requestDto);

        return resultResponseDto;
    }

    @PostMapping("/report")
    public ResponseEntity<ResultResponseDto> report(@Login User loginUser, @RequestBody TransactionsReportRequestDto requestDto) {
        ResultResponseDto resultResponseDto = transactionsService.report(loginUser, requestDto);
        ResponseEntity<ResultResponseDto> result;
        result = new ResponseEntity<>(resultResponseDto, HttpStatus.OK);

        if (!resultResponseDto.isResult()) {
            result = new ResponseEntity<>(resultResponseDto, HttpStatus.UNAUTHORIZED);
        }

        return result;
    }

    @PostMapping("/like")
    public ResponseEntity<ResultResponseDto> like(@Login User loginUser, @RequestBody TransactionsLikeRequestDto requestDto) throws ParseException {
        ResultResponseDto resultResponseDto = transactionsService.like(loginUser, requestDto);
        ResponseEntity<ResultResponseDto> result;
        result = new ResponseEntity<>(resultResponseDto, HttpStatus.OK);

        if (!resultResponseDto.isResult()) {
            result = new ResponseEntity<>(resultResponseDto, HttpStatus.UNAUTHORIZED);
        }

        return result;
    }
}
