package JesusDeciples.RankingQuiz.api.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class GuideMessageBundle {
    private final GuideMessage prepareMessage = new GuideMessage("준비하세요! 잠시 후 퀴즈를 시작합니다!");
    private final GuideMessage quizInProgressMessage = new GuideMessage("현재 퀴즈가 진행 중입니다. 잠시 기다린 뒤 다음 퀴즈에 참가해주세요.");
    private final GuideMessage quizEndedMessage = new GuideMessage("현재 퀴즈가 종료되었습니다. 잠시 기다린 뒤 다음 퀴즈에 참가해주세요.");
    private final GuideMessage gradingMessage = new GuideMessage("이전 퀴즈를 채점하고 있습니다. 잠시 기다린 뒤 다음 퀴즈에 참가해주세요.");
    private final GuideMessage resultsAnnouncementMessage = new GuideMessage("이전 퀴즈 결과를 발표하고 있습니다. 잠시 기다린 뒤 다음 퀴즈에 참가해주세요.");

    private final GuideMessage answerSubmittedMessage = new GuideMessage("답안이 정상적으로 제출되었습니다. 결과 발표 후 바로 다음 문제가 시작됩니다.");
    private final GuideMessage autoSubmissionMessage = new GuideMessage("Ranking Quiz는 자동으로 답안이 제출됩니다. 잠시만 기다려 주세요.");
    private final GuideMessage guide_message_late_sumbssion = new GuideMessage("제출 시간이 초과되어 답안을 무효 처리합니다.");
    private final GuideMessage guide_message_invalid_answer_data = new GuideMessage("서버에서 답안을 인식하지 못했습니다.");
    private final GuideMessage guide_message_quiz_id_mismatch = new GuideMessage("진행된 퀴즈와 답안의 퀴즈가 일치하지 않습니다. 답안을 무효처리합니다.");
    private final GuideMessage answerIssueGradingMessage = new GuideMessage("제출하신 답안에 문제가 있어서 답안을 무효 처리합니다. 현재 답안을 수거하고 있습니다.");
    private final GuideMessage answerIssueResultsMessage = new GuideMessage("제출하신 답안에 문제가 있어서 답안을 무효 처리합니다. 이전 퀴즈 결과를 발표하고 있습니다.");
    private final GuideMessage submissionTimeExceededResultsMessage = new GuideMessage("제출 시간이 초과되어 해당 답안을 무효 처리합니다. 이전 퀴즈 결과를 발표하고 있습니다.");
    private final GuideMessage invalidAnswerMessage = new GuideMessage("해당 답안은 이미 끝난 퀴즈의 답안입니다! 무효입니다! 다음 퀴즈가 곧 시작합니다!");

    private final GuideMessage guide_message_not_accept_answer_data = new GuideMessage("지금은 답안을 받지 않습니다.");

    private final GuideMessage notParticipatedMessage = new GuideMessage("퀴즈에 참가하지 않으셨어요. 다음 퀴즈에는 꼭 참가해보세요!");
    private final GuideMessage errorMessage = new GuideMessage("예기치 못한 에러가 발생했습니다. 신속히 조치를 취하겠습니다.");

    @Setter
    private GuideMessage winner_notification;
}
