package server.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RequestDto {

    @NotNull(message = "사용자 정보가 null입니다")
    private Long userId;

    public RequestDto(String userId) {
        this.userId = Long.valueOf(userId);
    }
}
