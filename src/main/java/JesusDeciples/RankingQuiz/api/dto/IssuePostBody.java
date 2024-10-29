package JesusDeciples.RankingQuiz.api.dto;

import lombok.Data;

import java.util.List;

@Data
public class IssuePostBody {
    private String title;
    private String body;
    private List<String> assignees;
    private List<String> labels;

    public IssuePostBody(String title, String body, List<String> assignees, List<String> labels) {
        this.title = title;
        this.body = body;
        this.assignees = assignees;
        this.labels = labels;
    }
}
