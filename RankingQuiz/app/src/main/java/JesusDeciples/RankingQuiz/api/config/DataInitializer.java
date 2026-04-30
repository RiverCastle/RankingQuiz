package JesusDeciples.RankingQuiz.api.config;

import JesusDeciples.RankingQuiz.api.entity.QuizCategory;
import JesusDeciples.RankingQuiz.api.entity.quiz.Quiz;
import JesusDeciples.RankingQuiz.api.entity.quizContent.MultipleChoiceQuizContent;
import JesusDeciples.RankingQuiz.api.entity.quizContent.QuizContentLinkReferenceTag;
import JesusDeciples.RankingQuiz.api.entity.quizContent.ReferenceTag;
import JesusDeciples.RankingQuiz.api.entity.quizContent.ShortAnswerQuizContent;
import JesusDeciples.RankingQuiz.api.repository.MultipleChoiceQuizContentRepository;
import JesusDeciples.RankingQuiz.api.repository.QuizCategoryRepository;
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
    private final QuizCategoryRepository quizCategoryRepository;

    @Override
    public void run(ApplicationArguments args) {
        if (quizCategoryRepository.count() == 0) {
            quizCategoryRepository.saveAll(List.of(
                    QuizCategory.of("ENG_VOCA", "단어 퀴즈", true, true),
                    QuizCategory.of("BIBLE", "성경 퀴즈", true, false),
                    QuizCategory.of("KOR_VOCA", "한글 퀴즈", true, true)
            ));
        }

        if (referenceTagRepository.count() > 0) return;
        Tags tags = saveTags();
        initEngVocaQuizContents(tags);
        initBibleQuizContents(tags);
        initKorVocaQuizContents(tags);
    }

    private Tags saveTags() {
        List<ReferenceTag> saved = referenceTagRepository.saveAll(List.of(
                new ReferenceTag("영단어"),
                new ReferenceTag("기초영어"),
                new ReferenceTag("고급영어"),
                new ReferenceTag("성경"),
                new ReferenceTag("신약"),
                new ReferenceTag("구약"),
                new ReferenceTag("한국어"),
                new ReferenceTag("사자성어"),
                new ReferenceTag("속담")
        ));
        return new Tags(
                saved.get(0), saved.get(1), saved.get(2),
                saved.get(3), saved.get(4), saved.get(5),
                saved.get(6), saved.get(7), saved.get(8)
        );
    }

    private void initEngVocaQuizContents(Tags tags) {
        MultipleChoiceQuizContent apple = new MultipleChoiceQuizContent();
        apple.setStatement("'사과'를 영어로 무엇이라 하나요?");
        apple.setAnswer("apple");
        apple.setTimeLimit(8);
        apple.setCategoryCode("ENG_VOCA");
        apple.setOptions(List.of("apple", "banana", "grape", "peach"));
        apple.linkWithTags(linksOf(tags.engVoca, tags.basicEng));

        MultipleChoiceQuizContent school = new MultipleChoiceQuizContent();
        school.setStatement("'학교'를 영어로 무엇이라 하나요?");
        school.setAnswer("school");
        school.setTimeLimit(8);
        school.setCategoryCode("ENG_VOCA");
        school.setOptions(List.of("hospital", "school", "library", "museum"));
        school.linkWithTags(linksOf(tags.engVoca, tags.basicEng));

        MultipleChoiceQuizContent friend = new MultipleChoiceQuizContent();
        friend.setStatement("'친구'를 영어로 무엇이라 하나요?");
        friend.setAnswer("friend");
        friend.setTimeLimit(8);
        friend.setCategoryCode("ENG_VOCA");
        friend.setOptions(List.of("enemy", "stranger", "friend", "neighbor"));
        friend.linkWithTags(linksOf(tags.engVoca, tags.basicEng));

        MultipleChoiceQuizContent benevolent = new MultipleChoiceQuizContent();
        benevolent.setStatement("'benevolent'의 뜻으로 올바른 것은?");
        benevolent.setAnswer("자비로운");
        benevolent.setTimeLimit(8);
        benevolent.setCategoryCode("ENG_VOCA");
        benevolent.setOptions(List.of("자비로운", "악의적인", "무관심한", "냉혹한"));
        benevolent.linkWithTags(linksOf(tags.engVoca, tags.advEng));

        MultipleChoiceQuizContent ephemeral = new MultipleChoiceQuizContent();
        ephemeral.setStatement("'ephemeral'의 뜻으로 올바른 것은?");
        ephemeral.setAnswer("일시적인");
        ephemeral.setTimeLimit(8);
        ephemeral.setCategoryCode("ENG_VOCA");
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
        genesis.setCategoryCode("BIBLE");
        genesis.linkWithTags(linksOf(tags.bible, tags.oldTestament));

        ShortAnswerQuizContent moses = new ShortAnswerQuizContent();
        moses.setStatement("이스라엘 백성을 이집트에서 이끌어 낸 선지자의 이름은?");
        moses.setAnswer("모세");
        moses.setTimeLimit(8);
        moses.setCategoryCode("BIBLE");
        moses.linkWithTags(linksOf(tags.bible, tags.oldTestament));

        ShortAnswerQuizContent psalms = new ShortAnswerQuizContent();
        psalms.setStatement("성경에서 가장 많은 장(章)을 가진 책의 이름은?");
        psalms.setAnswer("시편");
        psalms.setTimeLimit(8);
        psalms.setCategoryCode("BIBLE");
        psalms.linkWithTags(linksOf(tags.bible, tags.oldTestament));

        ShortAnswerQuizContent lastSupper = new ShortAnswerQuizContent();
        lastSupper.setStatement("예수님이 십자가에 못 박히시기 전날 밤 제자들과 나눈 식사를 무엇이라고 하나요?");
        lastSupper.setAnswer("최후의 만찬");
        lastSupper.setTimeLimit(8);
        lastSupper.setCategoryCode("BIBLE");
        lastSupper.linkWithTags(linksOf(tags.bible, tags.newTestament));

        ShortAnswerQuizContent revelation = new ShortAnswerQuizContent();
        revelation.setStatement("신약성경의 마지막 책 이름은?");
        revelation.setAnswer("요한계시록");
        revelation.setTimeLimit(8);
        revelation.setCategoryCode("BIBLE");
        revelation.linkWithTags(linksOf(tags.bible, tags.newTestament));

        List<ShortAnswerQuizContent> saved = shortAnswerQuizContentRepository.saveAll(
                List.of(genesis, moses, psalms, lastSupper, revelation)
        );
        saved.forEach(content -> quizRepository.save(new Quiz(content)));
    }

    private void initKorVocaQuizContents(Tags tags) {
        ShortAnswerQuizContent geumsugang = new ShortAnswerQuizContent();
        geumsugang.setStatement("'비가 온 뒤에 땅이 굳는다'와 같은 뜻의 사자성어는?");
        geumsugang.setAnswer("우후죽순");
        geumsugang.setTimeLimit(10);
        geumsugang.setCategoryCode("KOR_VOCA");
        geumsugang.linkWithTags(linksOf(tags.korean, tags.idiom));

        ShortAnswerQuizContent eomucheong = new ShortAnswerQuizContent();
        eomucheong.setStatement("'사공이 많으면 배가 산으로 간다'와 같은 의미의 사자성어는?");
        eomucheong.setAnswer("군웅할거");
        eomucheong.setTimeLimit(10);
        eomucheong.setCategoryCode("KOR_VOCA");
        eomucheong.linkWithTags(linksOf(tags.korean, tags.idiom));

        ShortAnswerQuizContent seokgoshin = new ShortAnswerQuizContent();
        seokgoshin.setStatement("'옛것을 익히고 새것을 안다'는 뜻의 사자성어는?");
        seokgoshin.setAnswer("온고지신");
        seokgoshin.setTimeLimit(10);
        seokgoshin.setCategoryCode("KOR_VOCA");
        seokgoshin.linkWithTags(linksOf(tags.korean, tags.idiom));

        MultipleChoiceQuizContent bakjwi = new MultipleChoiceQuizContent();
        bakjwi.setStatement("'낮말은 새가 듣고 밤말은 쥐가 듣는다'의 의미로 알맞은 것은?");
        bakjwi.setAnswer("말은 항상 조심해야 한다");
        bakjwi.setTimeLimit(10);
        bakjwi.setCategoryCode("KOR_VOCA");
        bakjwi.setOptions(List.of("말은 항상 조심해야 한다", "새와 쥐는 말을 잘 듣는다", "낮과 밤은 다르다", "동물도 언어를 이해한다"));
        bakjwi.linkWithTags(linksOf(tags.korean, tags.proverb));

        MultipleChoiceQuizContent gaemi = new MultipleChoiceQuizContent();
        gaemi.setStatement("'가는 말이 고와야 오는 말이 곱다'의 의미로 알맞은 것은?");
        gaemi.setAnswer("내가 먼저 잘해야 상대도 잘한다");
        gaemi.setTimeLimit(10);
        gaemi.setCategoryCode("KOR_VOCA");
        gaemi.setOptions(List.of("내가 먼저 잘해야 상대도 잘한다", "말을 예쁘게 발음해야 한다", "여행은 천천히 가야 한다", "말보다 행동이 중요하다"));
        gaemi.linkWithTags(linksOf(tags.korean, tags.proverb));

        List<ShortAnswerQuizContent> savedShort = shortAnswerQuizContentRepository.saveAll(
                List.of(geumsugang, eomucheong, seokgoshin)
        );
        List<MultipleChoiceQuizContent> savedMultiple = multipleChoiceQuizContentRepository.saveAll(
                List.of(bakjwi, gaemi)
        );
        savedShort.forEach(content -> quizRepository.save(new Quiz(content)));
        savedMultiple.forEach(content -> quizRepository.save(new Quiz(content)));
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
            ReferenceTag oldTestament,
            ReferenceTag korean,
            ReferenceTag idiom,
            ReferenceTag proverb
    ) {}
}
