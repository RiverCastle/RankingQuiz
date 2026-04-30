package JesusDeciples.RankingQuiz.api.facade;


import JesusDeciples.RankingQuiz.api.dto.QuizType;
import JesusDeciples.RankingQuiz.api.dto.request.QuizContentCreateDto;
import JesusDeciples.RankingQuiz.api.entity.quizContent.QuizContent;
import JesusDeciples.RankingQuiz.api.repository.QuizContentRepository;
import JesusDeciples.RankingQuiz.api.repository.ShortAnswerQuizContentRepository;
import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
@EnableAsync
class QuizContentCreateFacadeTest {
    @Autowired
    private QuizContentCreateFacade facade;
    @Autowired
    private QuizContentRepository qRepository;
    @Autowired
    private ShortAnswerQuizContentRepository sRepository;

    @Test
    @DisplayName("퀴즈 생성 테스트")
    void testCreateQuizContent() throws InterruptedException {
        int initialCount = qRepository.findAll().size();

        QuizContentCreateDto dto = new QuizContentCreateDto();
        String sampleData = "test";
        List<String> sampleTags = new ArrayList<>();
        sampleTags.add(sampleData);

        dto.setStatement(sampleData);
        dto.setAnswer(sampleData);
        dto.setQuizType(QuizType.SHORT_ANSWER_WRITING);
        dto.setCategoryCode("ENG_VOCA");
        dto.setTags(sampleTags);

        facade.createQuizContent(dto);
        Thread.sleep(1000);

        List<QuizContent> entities = qRepository.findAll();
        assertEquals(initialCount + 1, entities.size());
    }

    @Test
    @DisplayName("퀴즈 생성 배열 테스트")
    void testCreateQuizContents() throws InterruptedException {
        int initialCount = qRepository.findAll().size();
        int size = 30;
        QuizContentCreateDto[] dtoArray = new QuizContentCreateDto[size * 2];

        String sampleData = "test";
        List<String> sampleTags = new ArrayList<>();
        sampleTags.add(sampleData);

        for (int i = 0; i < size; i++) {
            QuizContentCreateDto dto = new QuizContentCreateDto();
            if (i != 4) dto.setStatement(sampleData + " " + i);
            if (i != 14) dto.setAnswer(sampleData + i);
            dto.setQuizType(QuizType.SHORT_ANSWER_WRITING);
            dto.setCategoryCode("ENG_VOCA");
            dto.setTags(sampleTags);
            dtoArray[i] = dto;
        }

        for (int i = size; i < size * 2; i++) {
            QuizContentCreateDto dto = new QuizContentCreateDto();
            List<String> options = new ArrayList<>();
            options.add("1");
            options.add("2");
            options.add("3");
            options.add("4");
            options.add(sampleData + i);
            if (i != 44) dto.setStatement(sampleData + " " + i);
            if (i != 54) dto.setAnswer(sampleData + i);
            if (i != 34) dto.setMultipleOptions(options);
            dto.setQuizType(QuizType.MULTIPLE_CHOICE);
            dto.setCategoryCode("BIBLE");
            dto.setTags(sampleTags);
            dtoArray[i] = dto;
        }

        for (QuizContentCreateDto dto : dtoArray) {
            facade.createQuizContent(dto);
        }

        Thread.sleep(6000);

        List<QuizContent> results = qRepository.findAll();
        assertEquals(initialCount + size * 2 - 5, results.size());
    }
}
