package JesusDeciples.RankingQuiz.api.config;

import JesusDeciples.RankingQuiz.api.entity.quiz.Quiz;
import JesusDeciples.RankingQuiz.api.entity.quizContent.MultipleChoiceQuizContent;
import JesusDeciples.RankingQuiz.api.entity.quizContent.QuizContentLinkReferenceTag;
import JesusDeciples.RankingQuiz.api.entity.quizContent.ReferenceTag;
import JesusDeciples.RankingQuiz.api.entity.quizContent.ShortAnswerQuizContent;
import JesusDeciples.RankingQuiz.api.enums.QuizCategory;
import JesusDeciples.RankingQuiz.api.repository.MultipleChoiceQuizContentRepository;
import JesusDeciples.RankingQuiz.api.repository.QuizRepository;
import JesusDeciples.RankingQuiz.api.repository.ReferenceTagRepository;
import JesusDeciples.RankingQuiz.api.repository.ShortAnswerQuizContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final MultipleChoiceQuizContentRepository multipleChoiceQuizContentRepository;
    private final ShortAnswerQuizContentRepository shortAnswerQuizContentRepository;
    private final ReferenceTagRepository referenceTagRepository;
    private final QuizRepository quizRepository;

    @Override
    public void run(ApplicationArguments args) {
        Tags tags = saveTags();
        initEngVocaQuizContents(tags);
        initBibleQuizContents(tags);
    }

    private Tags saveTags() {
        List<ReferenceTag> saved = referenceTagRepository.saveAll(List.of(
                new ReferenceTag("영단어"),
                new ReferenceTag("기초영어"),
                new ReferenceTag("고급영어"),
                new ReferenceTag("성경"),
                new ReferenceTag("신약"),
                new ReferenceTag("구약")
        ));
        return new Tags(
                saved.get(0), saved.get(1), saved.get(2),
                saved.get(3), saved.get(4), saved.get(5)
        );
    }

    private void initEngVocaQuizContents(Tags tags) {
        MultipleChoiceQuizContent apple = new MultipleChoiceQuizContent();
        apple.setStatement("'사과'를 영어로 무엇이라 하나요?");
        apple.setAnswer("apple");
        apple.setTimeLimit(8);
        apple.setCategory(QuizCategory.ENG_VOCA);
        apple.setOptions(List.of("apple", "banana", "grape", "peach"));
        apple.linkWithTags(linksOf(tags.engVoca, tags.basicEng));

        MultipleChoiceQuizContent school = new MultipleChoiceQuizContent();
        school.setStatement("'학교'를 영어로 무엇이라 하나요?");
        school.setAnswer("school");
        school.setTimeLimit(8);
        school.setCategory(QuizCategory.ENG_VOCA);
        school.setOptions(List.of("hospital", "school", "library", "museum"));
        school.linkWithTags(linksOf(tags.engVoca, tags.basicEng));

        MultipleChoiceQuizContent friend = new MultipleChoiceQuizContent();
        friend.setStatement("'친구'를 영어로 무엇이라 하나요?");
        friend.setAnswer("friend");
        friend.setTimeLimit(8);
        friend.setCategory(QuizCategory.ENG_VOCA);
        friend.setOptions(List.of("enemy", "stranger", "friend", "neighbor"));
        friend.linkWithTags(linksOf(tags.engVoca, tags.basicEng));

        MultipleChoiceQuizContent benevolent = new MultipleChoiceQuizContent();
        benevolent.setStatement("'benevolent'의 뜻으로 올바른 것은?");
        benevolent.setAnswer("자비로운");
        benevolent.setTimeLimit(8);
        benevolent.setCategory(QuizCategory.ENG_VOCA);
        benevolent.setOptions(List.of("자비로운", "악의적인", "무관심한", "냉혹한"));
        benevolent.linkWithTags(linksOf(tags.engVoca, tags.advEng));

        MultipleChoiceQuizContent ephemeral = new MultipleChoiceQuizContent();
        ephemeral.setStatement("'ephemeral'의 뜻으로 올바른 것은?");
        ephemeral.setAnswer("일시적인");
        ephemeral.setTimeLimit(8);
        ephemeral.setCategory(QuizCategory.ENG_VOCA);
        ephemeral.setOptions(List.of("영원한", "일시적인", "반복적인", "강렬한"));
        ephemeral.linkWithTags(linksOf(tags.engVoca, tags.advEng));

        List<MultipleChoiceQuizContent> saved = multipleChoiceQuizContentRepository.saveAll(
                List.of(apple, school, friend, benevolent, ephemeral)
        );
        saved.forEach(content -> quizRepository.save(new Quiz(content)));
    }

    private void initBibleQuizContents(Tags tags) {
        ShortAnswerQuizContent genesis = new ShortAnswerQuizContent();
        genesis.setStatement("성경의 첫 번째 책 이름은?");
        genesis.setAnswer("창세기");
        genesis.setTimeLimit(8);
        genesis.setCategory(QuizCategory.BIBLE);
        genesis.linkWithTags(linksOf(tags.bible, tags.oldTestament));

        ShortAnswerQuizContent moses = new ShortAnswerQuizContent();
        moses.setStatement("이스라엘 백성을 이집트에서 이끌어 낸 선지자의 이름은?");
        moses.setAnswer("모세");
        moses.setTimeLimit(8);
        moses.setCategory(QuizCategory.BIBLE);
        moses.linkWithTags(linksOf(tags.bible, tags.oldTestament));

        ShortAnswerQuizContent psalms = new ShortAnswerQuizContent();
        psalms.setStatement("성경에서 가장 많은 장(章)을 가진 책의 이름은?");
        psalms.setAnswer("시편");
        psalms.setTimeLimit(8);
        psalms.setCategory(QuizCategory.BIBLE);
        psalms.linkWithTags(linksOf(tags.bible, tags.oldTestament));

        ShortAnswerQuizContent lastSupper = new ShortAnswerQuizContent();
        lastSupper.setStatement("예수님이 십자가에 못 박히시기 전날 밤 제자들과 나눈 식사를 무엇이라고 하나요?");
        lastSupper.setAnswer("최후의 만찬");
        lastSupper.setTimeLimit(8);
        lastSupper.setCategory(QuizCategory.BIBLE);
        lastSupper.linkWithTags(linksOf(tags.bible, tags.newTestament));

        ShortAnswerQuizContent revelation = new ShortAnswerQuizContent();
        revelation.setStatement("신약성경의 마지막 책 이름은?");
        revelation.setAnswer("요한계시록");
        revelation.setTimeLimit(8);
        revelation.setCategory(QuizCategory.BIBLE);
        revelation.linkWithTags(linksOf(tags.bible, tags.newTestament));

        List<ShortAnswerQuizContent> saved = shortAnswerQuizContentRepository.saveAll(
                List.of(genesis, moses, psalms, lastSupper, revelation)
        );
        saved.forEach(content -> quizRepository.save(new Quiz(content)));
    }

    private List<QuizContentLinkReferenceTag> linksOf(ReferenceTag... referenceTags) {
        return java.util.Arrays.stream(referenceTags)
                .map(QuizContentLinkReferenceTag::new)
                .toList();
    }

    private record Tags(
            ReferenceTag engVoca,
            ReferenceTag basicEng,
            ReferenceTag advEng,
            ReferenceTag bible,
            ReferenceTag newTestament,
            ReferenceTag oldTestament
    ) {}
}
