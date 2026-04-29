package JesusDeciples.RankingQuiz.api.service;

import JesusDeciples.RankingQuiz.api.dto.QuizType;
import JesusDeciples.RankingQuiz.api.dto.request.QuizContentCreateDto;
import JesusDeciples.RankingQuiz.api.dto.response.BulkImportResultDto;
import JesusDeciples.RankingQuiz.api.dto.response.BulkImportRowErrorDto;
import JesusDeciples.RankingQuiz.api.enums.QuizCategory;
import JesusDeciples.RankingQuiz.api.facade.QuizContentCreateFacade;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BulkImportService {

    private final QuizContentCreateFacade quizContentCreateFacade;
    private final ObjectMapper objectMapper;

    public BulkImportResultDto importFromTsv(String rawText, QuizCategory category) {
        if (rawText == null || rawText.isEmpty()) {
            return new BulkImportResultDto(false, 0,
                    List.of(new BulkImportRowErrorDto(0, "입력 데이터가 비어 있습니다.")));
        }

        String[] lines = rawText.split("\r?\n");
        List<BulkImportRowErrorDto> errors = new ArrayList<>();
        List<QuizContentCreateDto> validDtos = new ArrayList<>();

        for (int i = 0; i < lines.length; i++) {
            int rowNumber = i + 1;
            String line = lines[i];

            if (line.trim().isEmpty()) continue;

            String[] columns = line.split("\t", -1);
            if (columns.length != 3) {
                errors.add(new BulkImportRowErrorDto(rowNumber,
                        "컬럼 수가 올바르지 않습니다. 탭으로 구분된 3개의 컬럼(문제, 정답, 선택지)이 필요합니다. (실제: " + columns.length + "개)"));
                continue;
            }

            String question = columns[0].trim();
            String answer = columns[1].trim();
            String choicesRaw = columns[2].trim();

            if (question.isEmpty()) {
                errors.add(new BulkImportRowErrorDto(rowNumber, "문제 내용이 비어 있습니다."));
                continue;
            }
            if (answer.isEmpty()) {
                errors.add(new BulkImportRowErrorDto(rowNumber, "정답이 비어 있습니다."));
                continue;
            }

            List<String> choices;
            try {
                choices = objectMapper.readValue(choicesRaw, new TypeReference<List<String>>() {});
            } catch (Exception e) {
                errors.add(new BulkImportRowErrorDto(rowNumber,
                        "JSON 형식이 올바르지 않습니다 (따옴표 및 대괄호 확인 요망): " + e.getMessage()));
                continue;
            }

            if (choices.size() < 2) {
                errors.add(new BulkImportRowErrorDto(rowNumber,
                        "선택지는 최소 2개 이상이어야 합니다. (현재: " + choices.size() + "개)"));
                continue;
            }

            if (choices.size() % 2 != 0) {
                errors.add(new BulkImportRowErrorDto(rowNumber,
                        "선택지 개수는 짝수여야 합니다. (현재: " + choices.size() + "개)"));
                continue;
            }

            if (!choices.contains(answer)) {
                errors.add(new BulkImportRowErrorDto(rowNumber,
                        "정답이 선택지 목록에 존재하지 않습니다. 정답: '" + answer + "'"));
                continue;
            }

            QuizContentCreateDto dto = new QuizContentCreateDto();
            dto.setStatement(question);
            dto.setAnswer(answer);
            dto.setQuizType(QuizType.MULTIPLE_CHOICE);
            dto.setCategory(category);
            dto.setMultipleOptions(choices);
            dto.setTags(Collections.emptyList());
            validDtos.add(dto);
        }

        if (!errors.isEmpty()) {
            return new BulkImportResultDto(false, 0, errors);
        }

        for (QuizContentCreateDto dto : validDtos) {
            quizContentCreateFacade.createQuizContent(dto);
        }

        return new BulkImportResultDto(true, validDtos.size(), Collections.emptyList());
    }
}
