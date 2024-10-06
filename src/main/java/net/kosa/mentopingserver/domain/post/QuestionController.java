package net.kosa.mentopingserver.domain.post;

import lombok.RequiredArgsConstructor;
import net.kosa.mentopingserver.domain.post.dto.QuestionRequestDto;
import net.kosa.mentopingserver.domain.post.dto.QuestionResponseDto;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/questions")
public class QuestionController {

    private final QuestionService questionService;


}
