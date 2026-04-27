package JesusDeciples.RankingQuiz.api.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberInfoResponseDto {
    private String name;
    private Integer point;

    public MemberInfoResponseDto(String name, Integer point) {
        this.name = name;
        this.point = point;
    }

    public static MemberInfoResponseDto of(String name, Integer point) {
        return new MemberInfoResponseDto(name, point);
    }
}
